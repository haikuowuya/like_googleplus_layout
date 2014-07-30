package com.roboo.like.google.async;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class WifiAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<ScanResult>>
{
 
	private Context mContext;

	public WifiAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;

	}

	public LinkedList<ScanResult> loadInBackground()
	{
		LinkedList<ScanResult> data = getWifiInfo();
		mEndTime = System.currentTimeMillis();
		if (mEndTime - mStartTime < THREAD_LEAST_DURATION_TIME)
		{
			try
			{
				Thread.sleep(THREAD_LEAST_DURATION_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	@SuppressLint("NewApi")
	private LinkedList<ScanResult> getWifiInfo()
	{
		WifiManager mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mDebug = false;
		if (mDebug)
		{
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			System.out.println(wifiInfo.getSSID() + " " + wifiInfo.getIpAddress() + " " + wifiInfo.getLinkSpeed() + " " + wifiInfo.getMacAddress() + " " + wifiInfo.getNetworkId() + " " + wifiInfo.getRssi() + " " + wifiInfo.getBSSID());
		}
		List<ScanResult> data = mWifiManager.getScanResults();
		LinkedList<ScanResult> wifis = null;
		if (null != data)
		{
			wifis = new LinkedList<ScanResult>();
			for (int i = 0; i < data.size(); i++)
			{
				ScanResult scanResult = data.get(i);
				mDebug = false;
				if (mDebug)
				{
					System.out.println("无线网络名称  = " + scanResult.SSID + " capabilities = " + scanResult.capabilities + " frequency = " + scanResult.frequency + " level = " + scanResult.level + " timestamp = " + scanResult.timestamp + " describeContents = " + scanResult.describeContents()
						+ " BSSID = " + scanResult.BSSID);
				}
				 
				wifis.add(scanResult);
			}
		}
		List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();
		if (null != networks)
		{
			for (WifiConfiguration configuration : networks)
			{
				if (mDebug)
				{
					System.out.println("configuration = " + configuration);
				}
			}
		}
		HashMap<String, String> hashMap = getConfigWifiInfos();
		LinkedList<ScanResult> tmpList = new LinkedList<ScanResult>();
		for (ScanResult scanResult : wifis)
		{
			scanResult.capabilities = "WIFI密码没有配置过";
			for (String key : hashMap.keySet())
			{
				String ssid = scanResult.SSID;
				mDebug = true;
				if (mDebug)
				{
					System.out.println("scanResult.SSID = " + ssid + " string = " + key + " password = " + hashMap.get(key));
				}
				boolean flag = ssid.equals(key);
				if (flag)
				{
					tmpList.add(scanResult);
					scanResult.capabilities = hashMap.get(key);
					break;
				}
			}
		}
		handleWifi(wifis ,tmpList);
		return wifis;
	}

	private void handleWifi(LinkedList<ScanResult> wifis,LinkedList<ScanResult> tmpList)
	{
		wifis.removeAll(tmpList);
		wifis.addAll(0, tmpList);
		tmpList.clear();
		 for(ScanResult scanResult :wifis)
		 {
			 if(TextUtils.isEmpty(scanResult.SSID))
			 {
				 tmpList.add(scanResult);
			 }
		 }
		 if(tmpList.size() > 0)
		 {
			 wifis.removeAll(tmpList);
		 }
		
	}

	/**
	 * 获取设备中已经配置过的wifi信息
	 */
	private HashMap<String, String> getConfigWifiInfos()
	{
		HashMap<String, String> data = new HashMap<String, String>();
		String newFilePath = "/mnt/sdcard/wifi.conf";
		File file = new File(newFilePath);
		if (!file.exists())
		{
			String originFilePath = "/data/misc/wifi/wpa_supplicant.conf";
			// System.out.println(" runRootCommand = " + runRootCommand(" chmod 777 " + originFilePath));
			System.out.println(" runRootCommand = " + runRootCommand(" cp " + originFilePath + "  " + newFilePath));
		}
		if (file.exists() && file.canRead())
		{
			try
			{
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len = -1;
				StringBuffer stringBuffer = new StringBuffer();
				while ((len = fileInputStream.read(buffer)) != -1)
				{
					stringBuffer.append(new String(buffer, 0, len, HTTP.UTF_8));
				}
				mDebug = false;
				if(mDebug)
				{
				 System.out.println("stringBuffer = " + stringBuffer);
				}
				if (stringBuffer.toString().contains("network="))
				{
					stringBuffer = stringBuffer.delete(0, stringBuffer.toString().indexOf("network="));
					String[] tmp = stringBuffer.toString().split("network=");
					for (String str : tmp)
					{
						if (str.contains("{") && str.contains("}"))
						{
							str = str.replace("{", "");
							str = str.replace("}", "");
						}
						str = str.trim();
						if (str.contains("\n"))
						{
							String[] tmp1 = str.split("\n");
							// System.out.println("tmp1 = " + tmp1.length);
							if (tmp1.length == 4)// 有密码
							{
								String ssid = tmp1[0];
								String password = tmp1[1];
								if (ssid.contains("="))
								{
									ssid = ssid.split("=")[1];
									ssid = ssid.substring(1, ssid.length() - 1);
								}
								if (password.contains("="))
								{
									password = password.split("=")[1];
									password = password.substring(1, password.length() - 1);
								}
								// System.out.println("ssid = " + ssid + " password = " + password);
								data.put(ssid, password);
							}
						}
					}
				}
				fileInputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		file.delete();
		return data;
	}

	/***
	 * 执行Root 命令
	 * 
	 * @param command
	 *            要执行的命令[chmod 777 /data/misc/wifi/wpa_supplicant.conf]
	 * @return true 命令成功执行 或者 false 命令执行失败
	 */
	public static boolean runRootCommand(String command)
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{}
		}
		return true;
	}
}
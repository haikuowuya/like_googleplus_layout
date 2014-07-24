package com.roboo.like.google.fragments;

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
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.WIFIAdapter;

@SuppressLint("NewApi")
public class WIFIFragment extends BaseWithProgressFragment
{
	private ListView mListView;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressBar.setVisibility(View.GONE);
			mListView.setAdapter(getAdapter());
		};
	};

	public static WIFIFragment newInstance()
	{
		WIFIFragment fragment = new WIFIFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_wifi, null);// TODO
		mListView = (ListView) view.findViewById(R.id.dlv_list);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	public void setListener()
	{
		mListView.setOnItemClickListener(getOnItemClickListener());
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

			}
		};
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000L);
	}

	private ListAdapter getAdapter()
	{
		LinkedList<ScanResult> data = getWifiInfo();
		return new WIFIAdapter(getActivity(), data);
		// return ArrayAdapter.createFromResource(getActivity(), R.array.actionbar_navigation_ithome_arrays, android.R.layout.simple_list_item_1);
	}

	@SuppressLint("NewApi")
	private LinkedList<ScanResult> getWifiInfo()
	{
		WifiManager mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		// WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		// System.out.println(wifiInfo.getSSID() + " " + wifiInfo.getIpAddress() + " " + wifiInfo.getLinkSpeed() + " " + wifiInfo.getMacAddress() + " " + wifiInfo.getNetworkId() + " " + wifiInfo.getRssi() + " " + wifiInfo.getBSSID());
		List<ScanResult> data = mWifiManager.getScanResults();
		LinkedList<ScanResult> wifis = null;
		if (null != data)
		{
			wifis = new LinkedList<ScanResult>();
			for (int i = 0; i < data.size(); i++)
			{
				ScanResult scanResult = data.get(i);
				System.out.println("无线网络名称  = " + scanResult.SSID + " capabilities = " + scanResult.capabilities + " frequency = " + scanResult.frequency + " level = " + scanResult.level + " timestamp = " + scanResult.timestamp + " describeContents = " + scanResult.describeContents() + " BSSID = "
					+ scanResult.BSSID);
				wifis.add(scanResult);
			}
		}
		List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();
		if (null != networks)
		{
			for (WifiConfiguration configuration : networks)
			{
//				System.out.println("configuration = " + configuration);
			}
		}
		HashMap<String, String> hashMap = getConfigWifiInfos();
		for(String string :hashMap.keySet())
		{
			for(ScanResult scanResult : wifis)
			{
				System.out.println("scanResult.SSID = " + scanResult.SSID + " string = " + string);
				if(scanResult.SSID.equals(string))
				{
					
				}
			}
		}
		return wifis;
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
			System.out.println(" runRootCommand = " + runRootCommand(" chmod 777 " + originFilePath));
			System.out.println(" runRootCommand = " + runRootCommand(" cp " + originFilePath + "  " + newFilePath));
		}
		else
		{
			if (file.canRead())
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
					// System.out.println("stringBuffer = " + stringBuffer);
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
									}
									if (password.contains("="))
									{
										password = password.split("=")[1];
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
//			file.delete();
		}
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

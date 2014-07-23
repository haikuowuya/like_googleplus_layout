package com.roboo.like.google.fragments;

import java.util.LinkedList;
import java.util.List;

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
				System.out.println(configuration);
			}
		}
		return wifis;
	}
}

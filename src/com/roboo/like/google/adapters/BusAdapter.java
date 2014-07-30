package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.BusItem;

public class BusAdapter extends BaseAdapter
{
	private LinkedList<BusItem> mData;
	private Activity mActivity;

	public BusAdapter(Activity mActivity, LinkedList<BusItem> data)
	{
		this.mData = data;
		this.mActivity = mActivity;
	}

	@Override
	public int getCount()
	{
		return null == mData ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null == mData ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		BusItem item = mData.get(position);
		convertView = LayoutInflater.from(mActivity).inflate(R.layout.wifi_list_item, null);
		TextView tvSSID = ViewHolder.getView(convertView, R.id.tv_ssid);
		TextView tvCapabilities = ViewHolder.getView(convertView, R.id.tv_capabilities);
		tvSSID.setText(item.busNo);
		tvCapabilities.setText(item.busName);
		return convertView;
	}

}

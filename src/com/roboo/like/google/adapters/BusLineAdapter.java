package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.roboo.like.google.R;
import com.roboo.like.google.models.BusLineItem;

public class BusLineAdapter extends BaseAdapter
{
	private LinkedList<BusLineItem> mData;
	private Activity mActivity;

	public BusLineAdapter(Activity mActivity, LinkedList<BusLineItem> data)
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
		final BusLineItem item = mData.get(position);
		convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_bus_line_item, null);// TODO
		TextView tvStationName = ViewHolder.getView(convertView, R.id.tv_station_name);
		TextView tvStationMark = ViewHolder.getView(convertView, R.id.tv_station_mark);
		TextView tvIncomingBusNo = ViewHolder.getView(convertView, R.id.tv_incoming_bus_no);
		TextView tvIncomingBusTime = ViewHolder.getView(convertView, R.id.tv_incoming_bus_time);
		tvStationName.setText(item.stationName);
		tvStationMark.setText(item.stationMark);
		if (!TextUtils.isEmpty(item.incomingBusNo))
		{
			tvIncomingBusNo.setVisibility(View.VISIBLE);
			tvIncomingBusNo.setText(item.incomingBusNo);
		}
		if (!TextUtils.isEmpty(item.incomingBusTime))
		{
			tvIncomingBusTime.setVisibility(View.VISIBLE);
			tvIncomingBusTime.setText(item.incomingBusTime);
		}
		if (!TextUtils.isEmpty(item.stationName) && item.stationName.length()>9)
		{
			tvStationName.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Toast.makeText(mActivity, item.stationName, Toast.LENGTH_SHORT).show();
				}
			});
			 
		}
		
		return convertView;
	}
}

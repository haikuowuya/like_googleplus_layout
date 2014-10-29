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

import com.roboo.like.google.BusLineActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.fragments.BusLineFragment2;
import com.roboo.like.google.models.BusStationItem;

public class BusStationAdapter2 extends BaseAdapter
{
	private LinkedList<BusStationItem> mData;
	private Activity mActivity;

	public BusStationAdapter2(Activity mActivity,
		LinkedList<BusStationItem> data)
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
		final BusStationItem item = mData.get(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mActivity).inflate(
				R.layout.list_bus_station_item2, null);// TODO
		}
		TextView tvBusNo = ViewHolder.getView(convertView, R.id.tv_bus_no);
		TextView tvBusLicensePlate = ViewHolder.getView(convertView,
			R.id.tv_bus_license_plate);
		TextView tvBusUpdateTime = ViewHolder.getView(convertView,
			R.id.tv_bus_update_time);
		TextView tvBusStopSpacing = ViewHolder.getView(convertView,
			R.id.tv_bus_stop_spacing);
		tvBusNo.setText(item.busNo + "路");
		tvBusLicensePlate.setText(item.busLicensePlate);
		tvBusUpdateTime.setText(item.busUpdateTime);
		tvBusStopSpacing.setText(item.busStopSpacing);
		if (TextUtils.isEmpty(item.busUpdateTime))
		{
			tvBusUpdateTime.setText("无");
		}

		if (!TextUtils.isEmpty(item.busStopSpacing))
		{
			if (TextUtils.isDigitsOnly(item.busStopSpacing)
				|| "进站".equals(item.busStopSpacing))
			{
				tvBusStopSpacing.setTextColor(0xFFFF0000);
			}
			if ("无车".equals(item.busStopSpacing))
			{
				tvBusStopSpacing.setTextColor(0xFF0000FF);
			}
		}
//		convertView.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				if (mActivity instanceof BusLineActivity)
//				{
//					BusLineActivity busLineActivity = (BusLineActivity) mActivity;
//					busLineActivity
//						.beginTransaction()
//						.replace(
//							R.id.frame_container,
//							BusLineFragment2.newInstance(item.stationUrl,
//								item.busNo)).commit();
//					busLineActivity.customActionBar(item.busNo);
//				}
//			}
//		});
		return convertView;
	}
}

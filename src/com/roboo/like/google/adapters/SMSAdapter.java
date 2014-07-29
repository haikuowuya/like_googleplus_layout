package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.SmsItem;

public class SMSAdapter extends BaseAdapter
{
	private LinkedList<SmsItem> mData;
	private Activity mActivity;

	public SMSAdapter(Activity mActivity, LinkedList<SmsItem> data)
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
		SmsItem item = mData.get(position);
		if (null != item)
		{
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.sms_list_item, null);
			TextView tvAddress = ViewHolder.getView(convertView, R.id.tv_address);
			TextView tvBody = ViewHolder.getView(convertView, R.id.tv_body);
			tvAddress.setText(item.address);
			tvBody.setText(item.body);
		}
		return convertView;
	}

}

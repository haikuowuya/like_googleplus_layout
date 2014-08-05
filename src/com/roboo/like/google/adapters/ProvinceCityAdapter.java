package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.CityItem;

public class ProvinceCityAdapter extends BaseAdapter
{
	private LinkedList<CityItem> mData;
	private Activity mActivity;

	public ProvinceCityAdapter(Activity mActivity, LinkedList<CityItem> data)
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
		CityItem item = mData.get(position);
		if (null != item)
		{
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_city_item, null);// TODO
			TextView textView = (TextView) convertView.findViewById(R.id.tv_city_name);
			if(TextUtils.isEmpty(item.cName))
			{
			textView.setText(item.pName);
			}
			else
			{
				textView.setText(item.cName);
				
			}
		}
		return convertView;
	}

}

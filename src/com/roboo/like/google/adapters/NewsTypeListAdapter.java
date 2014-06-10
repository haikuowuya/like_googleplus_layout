package com.roboo.like.google.adapters;

import java.util.LinkedList;

import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsTypeItem;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsTypeListAdapter extends BaseAdapter
{
	private LinkedList<NewsTypeItem> mData;
	private Activity activity;

	public NewsTypeListAdapter(LinkedList<NewsTypeItem> mData, Activity activity)
	{
		super();
		this.mData = mData;
		this.activity = activity;
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
		convertView = LayoutInflater.from(activity).inflate(android.R.layout.simple_list_item_1, null);
		TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
		textView.setText(mData.get(position).name);
		return convertView;
	}

}

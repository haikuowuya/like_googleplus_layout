package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.SubNewsTypeItem;

public class NewsTypeListAdapter extends BaseAdapter implements StickyHeadersAdapter,SectionIndexer
{
	private LinkedList<SubNewsTypeItem> mData;
	private Activity activity;
	private LayoutInflater mInflater;

	public NewsTypeListAdapter(LinkedList<SubNewsTypeItem> mData, Activity activity)
	{
		super();
		this.mData = mData;
		this.activity = activity;
		mInflater = LayoutInflater.from(activity);
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
		TextView textView =  ViewHolder.getView(convertView, android.R.id.text1);
		textView.setText(mData.get(position).name);
		return convertView;
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

	@Override
	public int getPositionForSection(int section)
	{
		return 0;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return 0;
	}

	@Override
	public long getHeaderId(int position)
	{
		return 0;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		textView.setText(""+(1+position));
		return convertView;
	}

}

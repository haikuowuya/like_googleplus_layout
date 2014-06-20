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
import com.roboo.like.google.models.StartNewsTypeItem;

public class StartNewsTypeListAdapter extends BaseAdapter implements StickyHeadersAdapter,SectionIndexer
{
	private LinkedList<StartNewsTypeItem> mData;
	private Activity activity;
	private LayoutInflater mInflater;

	public StartNewsTypeListAdapter(LinkedList<StartNewsTypeItem> mData, Activity activity)
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
		convertView = LayoutInflater.from(activity).inflate(R.layout.list_news_type_item, null);//TODO
		TextView textView =  ViewHolder.getView(convertView, R.id.tv_title);
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

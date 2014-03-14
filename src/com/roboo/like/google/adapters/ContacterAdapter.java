package com.roboo.like.google.adapters;

import java.util.LinkedList;

import com.roboo.like.google.R;
import com.roboo.like.google.models.ContacterItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContacterAdapter extends BaseAdapter
{
	private LinkedList<ContacterItem> data;
	private Context context;

	public ContacterAdapter(Context context,LinkedList<ContacterItem> data)
	{
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return null == data ? 0 : data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null == data ? null : data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.contacter_list_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		textView.setText(data.get(position).name);
		return convertView;
	}

}

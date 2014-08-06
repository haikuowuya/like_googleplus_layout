package com.roboo.like.google.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.CityItem;

public class ProvCityAdapter extends BaseAdapter implements StickyHeadersAdapter, SectionIndexer
{
	private static final boolean IS_CUSTOM_FAST_SCROLL_LABEL = false;
	private Context context;
	private LinkedList<CityItem> mData;

	private LayoutInflater mInflater;
	/** 用于记录兩條新聞日期不相同时，该比较字符串所在List集合的索引位置，在生成HeaderId时进行获取 */
	private LinkedList<Integer> mSectionIndex = new LinkedList<Integer>();
	private HashMap<String, Integer> mCityInProv = new HashMap<String, Integer>();

	public ProvCityAdapter(Context context, LinkedList<CityItem> data, LinkedList<Integer> sectionIndex)
	{
		super();
		this.context = context;
		this.mData = data;
		mInflater = LayoutInflater.from(context);
		this.mSectionIndex = sectionIndex;
	}

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

	public View getView(int position, View convertView, ViewGroup parent)// TODO
	{
		CityItem item = mData.get(position);
		if (null != item)
		{
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.list_city_item, null);
			}
			TextView tvCityName = ViewHolder.getView(convertView, R.id.tv_city_name);
			tvCityName.setText(item.cName);
		}
		return convertView;
	}

	public long getHeaderId(int position)
	{
		return mData.get(position).headerId;
	}

	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		CityItem item = mData.get(position);
		if (null != item)
		{
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
			}
			TextView textView = ViewHolder.getView(convertView, R.id.tv_text);
			TextView textView1 = ViewHolder.getView(convertView, R.id.tv_today_news_count);
			textView.setText(item.pName);
			int count = null == mCityInProv.get(item.pName) ? 0 : mCityInProv.get(item.pName).intValue();
			textView1.setText("总共 " + count + " 个城市");
		}
		return convertView;
	}

	@Override
	public Object[] getSections()
	{
		String[] sections = new String[mSectionIndex.size()];
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			sections[i] = getFastScrollLabel(mData.get(mSectionIndex.get(i)).pName);
		}
		return sections;
	}

	/** 获取快速滑动时显示的文字 */
	private String getFastScrollLabel(String pName)
	{
		if (IS_CUSTOM_FAST_SCROLL_LABEL)
		{
			return pName;
		}
		else
		{
			return pName;
		}
	}

	public int getPositionForSection(int section)
	{
		if (section >= mSectionIndex.size())
		{
			section = mSectionIndex.size() - 1;
		}
		else if (section < 0)
		{
			section = 0;
		}
		if (section < mSectionIndex.size() && mSectionIndex.size() > 0)
		{
			return mSectionIndex.get(section);
		}
		return 0;
	}

	public int getSectionForPosition(int position)
	{
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			if (position < mSectionIndex.get(i))
			{
				return i - 1;
			}
		}
		return mSectionIndex.size() - 1;
	}

	public void setSectionIndex(LinkedList<Integer> sectionIndex)
	{
		mSectionIndex = sectionIndex;
		String currentPName = mData.get(0).pName;
		int count = 0;
		for (int i = 0; i < mData.size(); i++)
		{
			CityItem item = (CityItem) mData.get(i);
			if (currentPName.equals(item.pName))
			{
				count++;
			}
			else
			{
				mCityInProv.put(currentPName, count);
				currentPName = item.pName;
				count = 1;
			}
		}
		mCityInProv.put(currentPName, count);// 最后一个日期
	}

}

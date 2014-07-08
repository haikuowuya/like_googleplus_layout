package com.roboo.like.google.adapters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.ContacterItem;
import com.roboo.like.google.utils.PinYinUtils;

public class ContacterAdapter extends BaseAdapter implements StickyHeadersAdapter, SectionIndexer
{
	private LinkedList<ContacterItem> mData;
	private Activity mActivity;
	private LayoutInflater mInflater;
	/**用于记录两个字符串大写首字母不相同时，该比较字符串所在List集合的索引位置*/
	private LinkedList<Integer> mSectionIndex =new LinkedList<Integer>();

	public ContacterAdapter(Activity activity, LinkedList<ContacterItem> data)
	{
		mActivity = activity;
		mInflater = LayoutInflater.from(mActivity);
		mData = generateHeaderId(data);
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
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(mActivity).inflate(R.layout.contacter_list_item, null);
		final CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.ctv_text);
		ContacterItem item = mData.get(position);
		textView.setText(item.name +"[ " + item.phone + " ]");
		textView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
			  	textView.setChecked(!textView.isChecked());
			}
		});
		return convertView;
	}

	@Override
	public Object[] getSections()
	{
		String[] sections = new String[mSectionIndex.size()];
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			sections[i] = getFristLetter(mData.get(mSectionIndex.get(i)).name);
		}
		return sections;
	}

	@Override
	public int getPositionForSection(int section)
	{
		if (section >= mSectionIndex.size())
		{
			section = mSectionIndex.size()-1;
		}
		else if (section < 0)
		{
			section = 0;
		}
		return mSectionIndex.get(section);
	}

	@Override
	public int getSectionForPosition(int position)
	{
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			if (position < mSectionIndex.get(i))
			{
				return i - 1;
			}
		}
		return mSectionIndex.size()- 1;
	}

	@Override
	public long getHeaderId(int position)
	{
		return mData.get(position).headerId;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		textView.setText(getFristLetter(mData.get(position).name));
		return convertView;
	}

	private LinkedList<ContacterItem> generateHeaderId(LinkedList<ContacterItem> data)
	{
		Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
		int mHeaderId = 1;
		LinkedList<ContacterItem> hasHeaderIdList = new LinkedList<ContacterItem>();
		for (int i = 0; i < data.size(); i++)
		{
			ContacterItem item = data.get(i);
			// 按照首字母进行生成HeaderId
			String firstLetter = getFristLetter(item.name);
			if (!mHeaderIdMap.containsKey(firstLetter))
			{
				item.headerId = mHeaderId;
				mHeaderIdMap.put(firstLetter, mHeaderId);
				mHeaderId++;
				mSectionIndex.add(i);
			}
			else
			{
				item.headerId = mHeaderIdMap.get(firstLetter);
			}
			hasHeaderIdList.add(item);
		}
		return hasHeaderIdList;
	}
	/**获取中文字符串的大写首字母*/
	private String getFristLetter(String str)
	{
		 HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		 defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		 defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		return PinYinUtils.getPinYinHeadChar(str, defaultFormat).substring(0, 1);
	}
}

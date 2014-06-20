package com.roboo.like.google.utils;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.models.StartNewsTypeItem;

public class DataUtils
{
	public static LinkedList<NewsTypeItem> handleNewsType(Context context)
	{
		String[] arrays = context.getResources().getStringArray(R.array.actionbar_navigation_ithome_arrays);
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_CSDN)
		{
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_csdn_arrays);
		}
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_PHONEKR)
		{
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_phonekr_arrays);
			
		}
		LinkedList<NewsTypeItem> data = new LinkedList<NewsTypeItem>();
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length > 1)
			{
				NewsTypeItem item = new NewsTypeItem();
				item.name = tmp[0];
				item.url = tmp[1];
				data.add(item);
			}
		}
		return data;
	}
	public static LinkedList<StartNewsTypeItem> handleStartNewsType(Context context)
	{
		String[] arrays = context.getResources().getStringArray(R.array.start_news_type_arrays);
		LinkedList<StartNewsTypeItem> data = new LinkedList<StartNewsTypeItem>();
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length > 1)
			{
				StartNewsTypeItem item = new StartNewsTypeItem();
				item.name = tmp[0];
				item.typeInt = Integer.parseInt(tmp[1]);
				data.add(item);
			}
		}
		return data;
	}
}

package com.roboo.like.google.news.utils;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.models.StartNewsTypeItem;

public class NewsTypeDataUtils
{
	public static LinkedList<NewsTypeItem> handleNewsType(Context context)
	{
		String[] arrays = context.getResources().getStringArray(R.array.actionbar_navigation_ithome_arrays);
		switch (GoogleApplication.mCurrentType)
		{
		case GoogleApplication.TYPE_CSDN:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_csdn_arrays);
			break;
		case GoogleApplication.TYPE_PHONEKR:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_phonekr_arrays);
			break;
		case GoogleApplication.TYPE_EOE:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_eoe_arrays);
			break;
		case GoogleApplication.TYPE_GEEKPARK:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_geekpark_arrays);;
			break;
		case GoogleApplication.TYPE_199IT:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_it199_arrays);
			break;
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
			if (tmp.length > 2)
			{
				StartNewsTypeItem item = new StartNewsTypeItem();
				item.name = tmp[0];
				item.typeInt = Integer.parseInt(tmp[1]);
				item.src = tmp[2];
				data.add(item);
			}
		}
		return data;
	}
}

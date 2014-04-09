package com.roboo.like.google.utils;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsTypeItem;

public class DataUtils
{
	public static LinkedList<NewsTypeItem> handleNewsType(Context context)
	{
		String[] arrays = context.getResources().getStringArray(R.array.actionbar_navigation_list_text_arrays);
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
}

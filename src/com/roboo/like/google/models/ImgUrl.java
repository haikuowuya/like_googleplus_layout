package com.roboo.like.google.models;

import java.util.LinkedList;

import com.droidux.trial.da;
import com.roboo.like.google.R;

import android.content.Context;

public class ImgUrl
{
	public String imgUrl;
	public String note;
	/***
	 * @param context
	 * @return null 或者 LinkedList<ImgUrl>对象
	 */
	public static LinkedList<ImgUrl> getImgUrls(Context context)
	{
		LinkedList<ImgUrl> data = new LinkedList<ImgUrl>();
		String[] arrays = context.getResources().getStringArray(R.array.start_news_type_arrays);
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length == 2)
			{
				ImgUrl item = new ImgUrl();
				item.imgUrl = tmp[0];
				item.note = tmp[1];
				data.add(item);
			}
		}
		if (data.size() == 0)
		{
			data = null;
		}
		return data;
	}
}

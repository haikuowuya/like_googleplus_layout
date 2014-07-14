package com.roboo.like.google.models;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.R;
import com.roboo.like.google.utils.MD5Utils;

public class ImgUrl
{
	public String imgUrl;
	public String note;
	public String md5 ;
	/***
	 * @param context
	 * @return null 或者 LinkedList<ImgUrl>对象
	 */
	public static LinkedList<ImgUrl> getImgUrls(Context context)
	{
		LinkedList<ImgUrl> data = new LinkedList<ImgUrl>();
		String[] arrays = context.getResources().getStringArray(R.array.img_arrays);
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length == 2)
			{
				ImgUrl item = new ImgUrl();
				item.imgUrl = tmp[0];
				item.note = tmp[1];
				item.md5 = MD5Utils.generate(item.imgUrl+item.note);
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

package com.roboo.like.google.models;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.R;
import com.roboo.like.google.utils.MD5Utils;

public class NewsTypeItem
{
	public String name;
	public int id;
	/** 根据name和img生成 */
	public String md5;
	public String addTime = System.currentTimeMillis() + "";
	public String note;
	public String desc ;
	public String img;
	/**是否订阅的标志*/
	public boolean flag;
	/**是否收藏的标志*/
	public boolean fav;

	/***
	 * 将arrays.xml中的新闻类型处理成LinkedList 对象
	 * 
	 * @param context
	 *            上下文对象
	 * @return null 或者 LinkedList<NewsTypeItem> 对象
	 */
	public static LinkedList<NewsTypeItem> getNewsTypeItems(Context context)
	{
		String[] arrays = context.getResources().getStringArray(R.array.start_news_type_arrays);
		LinkedList<NewsTypeItem> data = new LinkedList<NewsTypeItem>();
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length > 3)
			{
				NewsTypeItem item = new NewsTypeItem();
				item.name = tmp[0];
				item.id = Integer.parseInt(tmp[1]);
				item.note = item.desc = tmp[2];
				item.img = tmp[3];
				item.md5 = MD5Utils.generate(item.name + item.img);
				item.flag = true;
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

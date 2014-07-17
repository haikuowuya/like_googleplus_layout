package com.roboo.like.google.models;

import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;

public class SubNewsTypeItem
{
	public String name;
	public String url;

	public static LinkedList<SubNewsTypeItem> handleNewsType(Context context)
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
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_geekpark_arrays);
			;
			break;
		case GoogleApplication.TYPE_199IT:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_it199_arrays);
			break;
		case GoogleApplication.TYPE_36KR:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_36kr_arrays);
			break;
		case GoogleApplication.TYPE_HUXIU:
			arrays = context.getResources().getStringArray(R.array.actionbar_navigation_huxiu_arrays);
			break;
		case GoogleApplication.TYPE_CHUANYI_DABAN:
			arrays =  context.getResources().getStringArray(R.array.actionbar_navigation_cydb_arrays);
			break;
		}
		LinkedList<SubNewsTypeItem> data = new LinkedList<SubNewsTypeItem>();
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length > 1)
			{
				SubNewsTypeItem item = new SubNewsTypeItem();
				item.name = tmp[0];
				item.url = tmp[1];
				data.add(item);
			}
		}
		return data;
	}
}

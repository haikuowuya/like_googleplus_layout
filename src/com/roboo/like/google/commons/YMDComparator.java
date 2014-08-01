package com.roboo.like.google.commons;

import java.util.Comparator;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;

public class YMDComparator implements Comparator<NewsItem>
{
	public int compare(NewsItem o1, NewsItem o2)
	{
		int result = 0;
		if (null != o1 && null != o2)
		{
			result =  o2.getTime().compareTo(o1.getTime());
			if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_199IT)
			{
				result = -result;
			}
			if(o2.getTime().contains("第") && o2.getTime().contains("页"))
			{
				result = o1.getTime().compareTo(o2.getTime());
			}
		}
		return result;
	}
}

package com.roboo.like.google.utils;

import java.io.IOException;
import java.util.LinkedList;

import android.annotation.SuppressLint;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.news.utils.CSDNNewsUtils;
import com.roboo.like.google.news.utils.ITHomeUtils;
import com.roboo.like.google.news.utils.PhoneKRNewsUtils;

@SuppressLint("SimpleDateFormat")
public class NewsListUtils
{
	
	public static LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws IOException
	{
		if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_ITHOME)
		{
			return ITHomeUtils.getITHomeNewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_CSDN)
		{
			return CSDNNewsUtils.getCSDNNewsList(baseUrl, pageNo);
		}
		else if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_PHONEKR)
		{
			 return PhoneKRNewsUtils.getPhoneKRNewsList(baseUrl, pageNo);
		}
		else
		{
			return ITHomeUtils.getITHomeNewsList(baseUrl, pageNo);
		}
	}
}

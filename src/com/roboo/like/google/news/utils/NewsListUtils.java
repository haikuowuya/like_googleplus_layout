package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.util.LinkedList;

import android.annotation.SuppressLint;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.news.list.utils.CSDNNewsUtils;
import com.roboo.like.google.news.list.utils.EOENewsUtils;
import com.roboo.like.google.news.list.utils.GeekParkNewsUtils;
import com.roboo.like.google.news.list.utils.ITHomeNewsUtils;
import com.roboo.like.google.news.list.utils.PhoneKRNewsUtils;

@SuppressLint("SimpleDateFormat")
public class NewsListUtils
{

	public static LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_ITHOME)
		{
			return ITHomeNewsUtils.getITHomeNewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_CSDN)
		{
			return CSDNNewsUtils.getCSDNNewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_PHONEKR)
		{
			return PhoneKRNewsUtils.getPhoneKRNewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_EOE)
		{
			return EOENewsUtils.getEOENewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_GEEKPARK)
		{
			return GeekParkNewsUtils.getGeekParkNewsList(baseUrl, pageNo);
		}

		else
		{
			return ITHomeNewsUtils.getITHomeNewsList(baseUrl, pageNo);
		}
	}
}

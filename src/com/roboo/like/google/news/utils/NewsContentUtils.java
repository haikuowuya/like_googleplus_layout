package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.news.content.utils.CSDNNewsContentUtils;
import com.roboo.like.google.news.content.utils.EOENewsContentUtils;
import com.roboo.like.google.news.content.utils.ITHomeNewsContentUtils;
import com.roboo.like.google.news.content.utils.PhoneKRNewsContentUtils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class NewsContentUtils
{
	
	public static LinkedList<String> getNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		if (GoogleApplication.mCurrentType==GoogleApplication.TYPE_ITHOME)
		{
			data = ITHomeNewsContentUtils.getITHomeNewsDataList(newsUrl);
		}
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_CSDN)
		{
			data = CSDNNewsContentUtils.getCsdnNewsDataList(newsUrl);
		}
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_PHONEKR)
		{
			data = PhoneKRNewsContentUtils.getPhoneKRNewsDataList(newsUrl);
		}
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_EOE)
		{
			data = EOENewsContentUtils.getEoeNewsDataList(newsUrl);
		}
		
		return data;

	}

}

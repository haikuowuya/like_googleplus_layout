package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.util.LinkedList;

import com.droidux.trial.da;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.news.content.utils.CSDNNewsContentUtils;
import com.roboo.like.google.news.content.utils.EOENewsContentUtils;
import com.roboo.like.google.news.content.utils.GeekParkNewsContentUtils;
import com.roboo.like.google.news.content.utils.IT199NewsContentUtils;
import com.roboo.like.google.news.content.utils.ITHomeNewsContentUtils;
import com.roboo.like.google.news.content.utils.PhoneKRNewsContentUtils;

public class NewsContentUtils
{
	public static LinkedList<String> getNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		switch (GoogleApplication.mCurrentType)
		{
		case GoogleApplication.TYPE_ITHOME:
			data = ITHomeNewsContentUtils.getITHomeNewsDataList(newsUrl);
			break;
		case GoogleApplication.TYPE_CSDN:
			data = CSDNNewsContentUtils.getCsdnNewsDataList(newsUrl);
			break;
		case GoogleApplication.TYPE_PHONEKR:
			data = PhoneKRNewsContentUtils.getPhoneKRNewsDataList(newsUrl);
			break;
		case GoogleApplication.TYPE_EOE:
			data = EOENewsContentUtils.getEoeNewsDataList(newsUrl);
			break;
		case GoogleApplication.TYPE_GEEKPARK:
			data = GeekParkNewsContentUtils.getGeekParkNewsDataList(newsUrl);
			break;
		case GoogleApplication.TYPE_199IT:
			data = IT199NewsContentUtils.getIT199NewsDataList(newsUrl);
		default:
			break;
		}
		return data;
	}
}

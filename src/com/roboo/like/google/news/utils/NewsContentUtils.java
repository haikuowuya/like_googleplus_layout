package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.util.LinkedList;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.news.content.utils.BaseNewsContentUtils;
import com.roboo.like.google.news.content.utils.CSDNNewsContentUtils;
import com.roboo.like.google.news.content.utils.CYDBNewsContentUtils;
import com.roboo.like.google.news.content.utils.EOENewsContentUtils;
import com.roboo.like.google.news.content.utils.GeekParkNewsContentUtils;
import com.roboo.like.google.news.content.utils.HiApkNewsContentUtils;
import com.roboo.like.google.news.content.utils.HuXiuNewsContentUtils;
import com.roboo.like.google.news.content.utils.IT199NewsContentUtils;
import com.roboo.like.google.news.content.utils.ITHomeNewsContentUtils;
import com.roboo.like.google.news.content.utils.KR36NewsContentUtils;
import com.roboo.like.google.news.content.utils.PhoneKRNewsContentUtils;
import com.roboo.like.google.news.content.utils.WLJDNewsContentUtils;
import com.roboo.like.google.news.content.utils.XCFNewsContentUtils;

public class NewsContentUtils
{
	public static LinkedList<String> getNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		BaseNewsContentUtils baseNewsContentUtils = null;
		switch (GoogleApplication.mCurrentType)
		{
		case GoogleApplication.TYPE_ITHOME:
			baseNewsContentUtils = new ITHomeNewsContentUtils();
			break;
		case GoogleApplication.TYPE_CSDN:
			baseNewsContentUtils = new CSDNNewsContentUtils();
			break;
		case GoogleApplication.TYPE_PHONEKR:
			baseNewsContentUtils = new PhoneKRNewsContentUtils();
			break;
		case GoogleApplication.TYPE_EOE:
			baseNewsContentUtils = new EOENewsContentUtils();
			break;
		case GoogleApplication.TYPE_GEEKPARK:
			baseNewsContentUtils = new GeekParkNewsContentUtils();
			break;
		case GoogleApplication.TYPE_199IT:
			baseNewsContentUtils = new IT199NewsContentUtils();
			break;
		case GoogleApplication.TYPE_36KR:
			baseNewsContentUtils = new KR36NewsContentUtils();
			break;
		case GoogleApplication.TYPE_HUXIU:
			baseNewsContentUtils = new HuXiuNewsContentUtils();
			break;
		case GoogleApplication.TYPE_CHUANYI_DABAN:
			baseNewsContentUtils = new CYDBNewsContentUtils();
			break;
		case GoogleApplication.TYPE_WLJD:
		baseNewsContentUtils = new WLJDNewsContentUtils(); 
		case GoogleApplication.TYPE_HIAPK:
			baseNewsContentUtils = new HiApkNewsContentUtils(); 
			break;
		case GoogleApplication.TYPE_XCF:
			baseNewsContentUtils = new XCFNewsContentUtils();
		default:
			break;
		}
		if (null != baseNewsContentUtils)
		{
			data = baseNewsContentUtils.getNewsContentDataList(newsUrl);
		}
		return data;
	}
}

package com.roboo.like.google.news.utils;

import java.util.LinkedList;

import android.annotation.SuppressLint;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.news.list.utils.BaseNewsListUtils;
import com.roboo.like.google.news.list.utils.CSDNNewsListUtils;
import com.roboo.like.google.news.list.utils.CYDBNewsListUtils;
import com.roboo.like.google.news.list.utils.EOENewsListUtils;
import com.roboo.like.google.news.list.utils.GeekParkNewsListUtils;
import com.roboo.like.google.news.list.utils.HiApkNewsListUtils;
import com.roboo.like.google.news.list.utils.HuXiuNewsListUtils;
import com.roboo.like.google.news.list.utils.IT199NewsListUtils;
import com.roboo.like.google.news.list.utils.ITHomeNewsListUtils;
import com.roboo.like.google.news.list.utils.KR36NewsListUtils;
import com.roboo.like.google.news.list.utils.PhoneKRNewsListUtils;
import com.roboo.like.google.news.list.utils.WLJDNewsListUtils;
import com.roboo.like.google.news.list.utils.XCFNewsListUtils;

@SuppressLint("SimpleDateFormat")
public class NewsListUtils
{
	public static LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		BaseNewsListUtils baseNewsListUtils  = null;
		System.out.println(" NewsListUtils :: GoogleApplication.mCurrentType = " + GoogleApplication.mCurrentType);
		switch (GoogleApplication.mCurrentType)
		{
		case GoogleApplication.TYPE_ITHOME:
			baseNewsListUtils = new ITHomeNewsListUtils();
			break;
		case GoogleApplication.TYPE_CSDN:
			baseNewsListUtils = new CSDNNewsListUtils();
			break;
		case GoogleApplication.TYPE_PHONEKR:
			baseNewsListUtils = new PhoneKRNewsListUtils();
			break;
		case GoogleApplication.TYPE_EOE:
			baseNewsListUtils = new  EOENewsListUtils();
			break;
		case GoogleApplication.TYPE_GEEKPARK:
			baseNewsListUtils = new  GeekParkNewsListUtils();
			break;
		case GoogleApplication.TYPE_199IT:
			baseNewsListUtils = new IT199NewsListUtils();
			break;
		case GoogleApplication.TYPE_36KR:
		 baseNewsListUtils = new KR36NewsListUtils();
			break;
		case GoogleApplication.TYPE_HUXIU:
			baseNewsListUtils = new HuXiuNewsListUtils();
			break;
		case GoogleApplication.TYPE_CHUANYI_DABAN:
			baseNewsListUtils = new CYDBNewsListUtils();
			break;
		case GoogleApplication.TYPE_WLJD:
			baseNewsListUtils = new WLJDNewsListUtils();
			break;
		case GoogleApplication.TYPE_HIAPK:
			baseNewsListUtils = new HiApkNewsListUtils();
			break;
		case GoogleApplication.TYPE_XCF:
			baseNewsListUtils = new XCFNewsListUtils();
			break;
		default:
			baseNewsListUtils = new ITHomeNewsListUtils();
			break;
		}
		data = baseNewsListUtils.getNewsList(baseUrl, pageNo);
		return data;
	}
}

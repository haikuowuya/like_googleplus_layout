package com.roboo.like.google.news.utils;

import java.util.LinkedList;

import android.annotation.SuppressLint;

import com.droidux.trial.da;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.news.list.utils.CSDNNewsUtils;
import com.roboo.like.google.news.list.utils.CYDBNewsUtils;
import com.roboo.like.google.news.list.utils.EOENewsUtils;
import com.roboo.like.google.news.list.utils.GeekParkNewsUtils;
import com.roboo.like.google.news.list.utils.HiApkNewsUtils;
import com.roboo.like.google.news.list.utils.HuXiuNewsUtils;
import com.roboo.like.google.news.list.utils.IT199NewsUtils;
import com.roboo.like.google.news.list.utils.ITHomeNewsUtils;
import com.roboo.like.google.news.list.utils.KR36NewsUtils;
import com.roboo.like.google.news.list.utils.PhoneKRNewsUtils;
import com.roboo.like.google.news.list.utils.WLJDNewsUtils;

@SuppressLint("SimpleDateFormat")
public class NewsListUtils
{

	public static LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		System.out.println(" NewsListUtils :: GoogleApplication.mCurrentType = " + GoogleApplication.mCurrentType);
		switch (GoogleApplication.mCurrentType)
		{
		case GoogleApplication.TYPE_ITHOME:
			data = ITHomeNewsUtils.getITHomeNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_CSDN:
			data = CSDNNewsUtils.getCSDNNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_PHONEKR:
			data = PhoneKRNewsUtils.getPhoneKRNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_EOE:
			data = EOENewsUtils.getEOENewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_GEEKPARK:
			data = GeekParkNewsUtils.getGeekParkNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_199IT:
			data = IT199NewsUtils.getIT199NewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_36KR:
			data = KR36NewsUtils.get36KRNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_HUXIU:
			data = HuXiuNewsUtils.getHuXiuNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_CHUANYI_DABAN:
			data = CYDBNewsUtils.getCydbNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_WLJD:
			data = WLJDNewsUtils.getWljdNewsList(baseUrl, pageNo);
			break;
		case GoogleApplication.TYPE_HIAPK:
			data = HiApkNewsUtils.getHiApkNewsList(baseUrl, pageNo);
			break;
		default:
			data = ITHomeNewsUtils.getITHomeNewsList(baseUrl, pageNo);
			break;
		}
		return data;
	}
}

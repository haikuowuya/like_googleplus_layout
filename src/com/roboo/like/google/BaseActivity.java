package com.roboo.like.google;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;

import com.roboo.like.google.commons.CrashException;
import com.roboo.like.google.dao.impl.ImgUrlDaoImpl;
import com.roboo.like.google.dao.impl.NewsTypeItemDaoImpl;
import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.ImgUrl;
import com.roboo.like.google.models.NewsTypeItem;

public class BaseActivity extends FragmentActivity
{
	public static final String PREF_FAST_SCROLL="fast_scroll";
	private static final String PREF_FIRST_INSERT_IMG_URL = "insert_img_url";
	private static final String PREF_FIRST_INSERT_NEWS_TYPE = "insert_news_type";

	protected SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		GoogleApplication application = (GoogleApplication) getApplication();
		application.recordActivity(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashException.getInstance(this));
		mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
	}

	protected void onResume()
	{
		super.onResume();
		JPushInterface.onResume(this);
		if (!mPreferences.contains(PREF_FIRST_INSERT_IMG_URL) || !mPreferences.getBoolean(PREF_FIRST_INSERT_IMG_URL, false))
		{
			insertImgUrls();
		}
		if (!mPreferences.contains(PREF_FIRST_INSERT_NEWS_TYPE) || !mPreferences.getBoolean(PREF_FIRST_INSERT_NEWS_TYPE, false))
		{
			insertNewsType();
		}
	}
	private void insertNewsType()
	{
		LinkedList<NewsTypeItem> typeItems = NewsTypeItem.getNewsTypeItems(this);
		NewsTypeItemDaoImpl newsTypeItemDao = new NewsTypeItemDaoImpl(new DBHelper(this));
		if(null != typeItems)
		{
			boolean returnFlag = newsTypeItemDao.insertNewsTypeItems(typeItems);
			 mPreferences.edit().putBoolean(PREF_FIRST_INSERT_NEWS_TYPE, returnFlag).commit();
			if (returnFlag)
			{
				System.out.println("插入 新闻类型 成功");
			}
			else
			{
				System.out.println("插入 新闻类型  失败");
			}
		}
	}

	private void insertImgUrls()
	{
		LinkedList<ImgUrl> imgUrls = ImgUrl.getImgUrls(this);
		if (null != imgUrls)
		{
			ImgUrlDaoImpl imgUrlDao = new ImgUrlDaoImpl(new DBHelper(this));
			boolean returnFlag = imgUrlDao.insertImgUrls(imgUrls);
			 mPreferences.edit().putBoolean(PREF_FIRST_INSERT_IMG_URL, returnFlag).commit();
			if (returnFlag)
			{
				System.out.println("插入图片Base URL 成功");
			}
			else
			{
				System.out.println("插入图片Base URL 失败");
			}
		}
	}

	protected void onPause()
	{
		super.onPause();
		JPushInterface.onPause(this);
	}

	public boolean isImg(String str)
	{
		ImgUrlDaoImpl imgUrlDao = new ImgUrlDaoImpl(new DBHelper(this));
		List<String> imgList = imgUrlDao.getImgUrls();
		boolean flag = false;
		for (String string : imgList)
		{
			if (str.startsWith(string))
			{
				flag = true;
				break;
			}
			else if (str.toLowerCase(Locale.getDefault()).endsWith(".png") || str.toLowerCase(Locale.getDefault()).endsWith(".jpg") || str.toLowerCase(Locale.getDefault()).endsWith("gif"))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}

	protected FragmentTransaction beginTransaction()
	{
		int enter = R.anim.base_slide_right_in;
		int exit = R.anim.base_slide_right_out;
		int popEnter = R.anim.base_slide_right_in;
		int popExit = R.anim.base_slide_right_out;

		enter = exit = popEnter = popExit = 0;
		return getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit);
	}
}

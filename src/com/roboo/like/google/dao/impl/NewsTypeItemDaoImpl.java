package com.roboo.like.google.dao.impl;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.droidux.trial.cu;
import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.NewsTypeItem;

/**
 * 新闻类型的操作
 * 
 * @author bo.li 2014-7-14 下午1:05:13
 */
public class NewsTypeItemDaoImpl
{
	private static final boolean DEBUG_FLAG = true;
	private static final String TABLE_NEWS_TYPE_LIST = "news_type_list";
	private static final String NEWS_TYPE_MD5 = "news_type_md5";
	private static final String NEWS_TYPE_NAME = "news_type_name";
	private static final String NEWS_TYPE_ID = "news_type_id";
	private static final String NEWS_TYPE_IMG = "news_type_img";
	private static final String NEWS_TYPE_DESC = "news_type_desc";
	private static final String NEWS_TYPE_FLAG = "news_type_flag";
	private static final String NEWS_TYPE_FAV = "news_type_fav";
	private static final String NEWS_TYPE_ADD_TIME = "news_type_add_time";
	private static final String NEWS_TYPE_NOTE = "news_type_note";
	private DBHelper mHelper;

	public NewsTypeItemDaoImpl(DBHelper mHelper)
	{
		super();
		this.mHelper = mHelper;
	}

	/***
	 * 向数据库中插入已有的可以进行查看的新闻类型
	 * 
	 * @param items
	 *            新闻类型列表
	 * @return ture 插入成功 或者 false 插入失败
	 */
	public boolean insertNewsTypeItems(LinkedList<NewsTypeItem> items)
	{
		boolean flag = false;
		int insertCount = 0;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		if (items != null)
		{
			for (NewsTypeItem item : items)
			{
				if (!isNewsTypeItemExist(item.md5))
				{
					ContentValues values = new ContentValues();
					values.put(NEWS_TYPE_MD5, item.md5);
					values.put(NEWS_TYPE_NAME, item.name);
					values.put(NEWS_TYPE_ADD_TIME, item.addTime);
					values.put(NEWS_TYPE_FLAG, item.flag);
					values.put(NEWS_TYPE_FAV, item.fav);
					values.put(NEWS_TYPE_ID, item.id);
					values.put(NEWS_TYPE_IMG, item.img);
					values.put(NEWS_TYPE_NOTE, item.note);
					values.put(NEWS_TYPE_DESC, item.desc);
					db.insert(TABLE_NEWS_TYPE_LIST, null, values);
					insertCount++;
				}
				else
				{
					if (DEBUG_FLAG)
					{
						System.out.println("要添加的 新闻类型 已经存在于数据库中了……");
					}
				}
			}
		}
		flag = insertCount == items.size();
		return flag;
	}

	/**
	 * 根据新闻类型的md5判断要插入的新闻类型是否已经存在于数据库中……
	 * 
	 * @param md5
	 *            新闻类型的md5 根据 name 和 img生成
	 * @return true 要插入的新闻类型已经存在于数据库中了 或者 false 数据库中还不存在
	 */
	private boolean isNewsTypeItemExist(String md5)
	{
		boolean flag = false;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NEWS_TYPE_LIST, null, NEWS_TYPE_MD5 + " = ?", new String[] { md5 }, null, null, null, null);
		if (null != cursor)
		{
			flag = 1 == cursor.getCount();
		}
		return flag;
	}

	/**
	 * 获取已经插入数据库中的新闻类型列表
	 * 
	 * @return null 或者 LinkedList<NewsTypeItem> 对象
	 */
	public  LinkedList<NewsTypeItem> getNewsTypeItems()
	{
		LinkedList<NewsTypeItem> data = null;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NEWS_TYPE_LIST, new String[] { NEWS_TYPE_MD5, NEWS_TYPE_ID, NEWS_TYPE_NAME, NEWS_TYPE_IMG, NEWS_TYPE_FLAG, NEWS_TYPE_FAV }, null, null, null, null, NEWS_TYPE_ID + " ASC", null);
		if (null != cursor)
		{
			data = new LinkedList<NewsTypeItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				NewsTypeItem item = new NewsTypeItem();
				String md5 = cursor.getString(0);
				int id = cursor.getInt(1);
				String name = cursor.getString(2);
				String img = cursor.getString(3);
				boolean flag = "1".equals(cursor.getString(4));
				boolean fav = "1".equals(cursor.getString(5));
				item.md5 = md5;
				item.id = id;
				item.img = img;
				item.flag = flag;
				item.name = name;
				item.fav = fav;
				data.add(item);
			}
			cursor.close();
			if (data.size() == 0)
			{
				data = null;
			}
		}
		db.close();
		return data;
	}

	/***
	 * 根据给定的标志参数来获取相应的新闻类型集合
	 * 
	 * @param flag
	 *            true 订阅过的 或者 false 没有订阅过的
	 * @return null 或者 LinkedList<NewsTypeItem> 对象
	 */
	public LinkedList<NewsTypeItem> getNewsTypeItems(boolean flag)
	{
		LinkedList<NewsTypeItem> data = getNewsTypeItems();
		LinkedList<NewsTypeItem> tmp = new LinkedList<NewsTypeItem>();
		for (NewsTypeItem item : data)
		{
			if (item.flag == flag)
			{
				tmp.add(item);
			}
		}
		if (tmp.size() == 0)
		{
			tmp = null;
		}
		return tmp;
	}

	/***
	 * 根据md5来改变新闻类型的订阅标志
	 * 
	 * @param md5
	 *            新闻类型的md5
	 * @param flag
	 *            true 添加订阅 或者 false 取消订阅
	 * @return true 表示操作成功
	 */
	public boolean updateFlag(String md5, boolean flag)
	{
		int rowId = -1;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NEWS_TYPE_FLAG, flag);
		rowId = db.update(TABLE_NEWS_TYPE_LIST, values, NEWS_TYPE_MD5 + " = ?", new String[] { md5 });
		db.close();
		return rowId > -1;
	}

	/***
	 * 根据md5来改变新闻类型的收藏标志
	 * 
	 * @param md5
	 *            新闻类型的md5
	 * @param flag
	 *            true 收藏 或者 false 取消收藏
	 * @return true 表示操作成功
	 */
	public boolean updateFav(String md5, boolean fav)
	{
		int rowId = -1;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NEWS_TYPE_FAV, fav);
		rowId = db.update(TABLE_NEWS_TYPE_LIST, values, NEWS_TYPE_MD5 + " = ?", new String[] { md5 });
		db.close();
		return rowId > -1;
	}

	/***
	 * 根据md5来判断新闻类型是否收藏
	 * 
	 * @param md5
	 *            新闻类型的md5
	 * @return true 表示已经收藏成功
	 */
	public boolean getFav(String md5)
	{
		boolean fav = false;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NEWS_TYPE_LIST, new String[] { NEWS_TYPE_FAV }, NEWS_TYPE_MD5 + " = ?", new String[] { md5 }, null, null, null);
		if (null != cursor && cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			fav = "1".equals(cursor.getString(0));
			cursor.close();
		}
		db.close();
		return fav;
	}
}

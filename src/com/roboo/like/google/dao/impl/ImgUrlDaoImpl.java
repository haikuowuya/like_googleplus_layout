package com.roboo.like.google.dao.impl;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.ImgUrl;
/***
 *  图片url的操作
 * @author bo.li
 *
 * 2014-7-14 下午1:04:47
 */
public class ImgUrlDaoImpl
{
	private static final boolean DEBUG_FLAG = true;
	private static final String NEWS_IMG = "news_img";
	private static final String NEWS_IMG_MD5 = "news_img_md5";
	private static final String NEWS_IMG_NOTE = "news_img_note";
	private static final String TABLE_NEWS_IMG_LIST = "news_img_list";
	private DBHelper mHelper;

	public ImgUrlDaoImpl(DBHelper mHelper)
	{
		super();
		this.mHelper = mHelper;
	}
	/***
	 * 向数据库表中插入图片url前缀
	 * @param imgs 新闻内容中出现图片url的前缀
	 * @return ture 添加成功 或者 false  添加失败
	 */
	public boolean insertImgUrls(LinkedList<ImgUrl> imgs)
	{
		boolean flag = false;
		int insertCount = 0;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for (ImgUrl imgUrl : imgs)
		{
			if (!isImgUrlExist(imgUrl.md5))
			{
				ContentValues values = new ContentValues();
				values.put(NEWS_IMG, imgUrl.imgUrl);
				values.put(NEWS_IMG_NOTE, imgUrl.note);
				values.put(NEWS_IMG_MD5, imgUrl.md5);
				db.insert(TABLE_NEWS_IMG_LIST, null, values);
				insertCount++;
			}
			else
			{
				if (DEBUG_FLAG)
				{
					System.out.println("要添加的Img URL 已经存在于数据库中了……");
				}
			}
		}
		flag = insertCount == imgs.size();
		return flag;
	}

	/**
	 * 获取数据库中所有是图片url的集合封装到一个LinkedList对象中
	 * 
	 * @return null 或者 一个LinkedList对象(size() >0)
	 */
	public LinkedList<String> getImgUrls()
	{
		LinkedList<String> data = null;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NEWS_IMG_LIST, new String[] { NEWS_IMG_MD5 }, null, null, null, null, null, null);
		if (null != cursor)
		{
			data = new LinkedList<String>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				String str = cursor.getString(0);
				if (!TextUtils.isEmpty(str))
				{
					data.add(str);
				}
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
	 * 判断要添加的ImgUrl是否已经存在于数据库中了， 防止重复添加
	 * 
	 * @param md5
	 *            ： ImgUrl对象的md5，根据对象url和note生成的
	 * @return true 已经添加到数据库了， false 没有添加到数据库中
	 */
	private boolean isImgUrlExist(String md5)
	{
		boolean flag = false;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NEWS_IMG_LIST, null, NEWS_IMG_MD5 + " = ?", new String[] { md5 }, null, null, null, null);
		if (null != cursor)
		{
			flag = 1 == cursor.getCount();
		}
		return flag;
	}
}

package com.roboo.like.google.dao.impl;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.ImgUrl;

public class ImgUrlImpl
{
	private static final String NEWS_IMG = "news_img";
	private static final String NEWS_IMG_NOTE = "news_img_note";
	private static final String TABLE_NEWS_IMG_LIST = "news_img_list";
	private DBHelper mHelper;

	private ImgUrlImpl(DBHelper mHelper)
	{
		super();
		this.mHelper = mHelper;
	}

	public boolean insertImgUrls(LinkedList<ImgUrl> imgs)
	{
		boolean flag = false;
		int insertCount = 0;
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for (ImgUrl imgUrl : imgs)
		{
			ContentValues values = new ContentValues();
			values.put(NEWS_IMG, imgUrl.imgUrl);
			values.put(NEWS_IMG_NOTE, imgUrl.note);
			db.insert(TABLE_NEWS_IMG_LIST, null, values);
			insertCount++;
		}
		flag = insertCount == imgs.size();
		return flag;

	}
}

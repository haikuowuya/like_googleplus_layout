package com.roboo.like.google.databases;

import java.io.File;
import java.util.LinkedList;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.utils.DBJSonUtils;

public class DBHelper extends SQLiteOpenHelper
{
	private Context mContext;

	private DBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
		this.mContext = context;
	}

	public DBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		this.mContext = context;
	}

	public DBHelper(Context context, CursorFactory factory)
	{
		super(context, GoogleApplication.DB_NAME, factory, GoogleApplication.DB_VERSION);
		this.mContext = context;
	}

	public DBHelper(Context context)
	{
		super(context, GoogleApplication.DB_NAME, null, GoogleApplication.DB_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		LinkedList<String> data = DBJSonUtils.getSQList(mContext);
		if (null != data)
		{
			for (String sql : data)
			{
				db.execSQL(sql);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		String path = db.getPath();
		File file = new File(path);
		if (file.exists() && file.delete())
		{
			onCreate(db);
			System.out.println("删除数据文件成功");
		}
	}

}

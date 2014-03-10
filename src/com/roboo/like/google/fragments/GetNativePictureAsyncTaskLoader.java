package com.roboo.like.google.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import com.roboo.like.google.models.PictureItem;

public class GetNativePictureAsyncTaskLoader extends AsyncTaskLoader<LinkedList<PictureItem>>
{
	private Context mContext;

	public GetNativePictureAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		forceLoad();
	}

	@Override
	public LinkedList<PictureItem> loadInBackground()
	{
		LinkedList<PictureItem> items = null;
		mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = mContext.getContentResolver();
		Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_ADDED);
		if (mCursor != null && mCursor.getCount() > 0)
		{
			items = new LinkedList<PictureItem>();
		}
		while (mCursor.moveToNext())
		{
			// 获取图片的路径
			String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
			// 获取图片的添加到系统的毫秒数
			long time = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
			PictureItem item = new PictureItem(path, parseTimeToYMD(time));
			System.out.println("item = " + item);
			items.add(item);
		}
		mCursor.close();
		return items;
	}

	public String parseTimeToYMD(long time)
	{
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(new Date(time * 1000L));
	}
}
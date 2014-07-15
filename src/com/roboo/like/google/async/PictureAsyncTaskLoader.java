package com.roboo.like.google.async;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.roboo.like.google.models.PictureItem;

public class PictureAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<PictureItem>>
{
	private Context mContext;

	public PictureAsyncTaskLoader(Context context)
	{
		super(context);
		mContext = context;

	}

	public LinkedList<PictureItem> loadInBackground()
	{
		LinkedList<PictureItem> items = null;
		if (Build.VERSION.SDK_INT > 18)
		{
			items = getPictureInKK();
		}
		else
		{
			items = getPictureBeforeKK();
		}
		return items;
	}

	private LinkedList<PictureItem> getPictureInKK()
	{
		 LinkedList<PictureItem> items = null;
		// String[] paths = new String[] { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() };
		// String[] mimeTypes = new String[] { "image/*" };
		mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = mContext.getContentResolver();
		Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_ADDED);
		if (mCursor != null && mCursor.getCount() > 0)
		{
			items = new LinkedList<PictureItem>();
			mCursor.moveToFirst();
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
		}
		return items;
	}

	/**
	 * 获取SD卡中的图片， 在奇巧[19]之前使用此方法， API19后限制了系统应用才有权限使用广播通知系统扫描SD卡
	 */
	private LinkedList<PictureItem> getPictureBeforeKK()
	{
		LinkedList<PictureItem> items = null;
		mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = mContext.getContentResolver();
		Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_ADDED);
		if (mCursor != null && mCursor.getCount() > 0)
		{
			items = new LinkedList<PictureItem>();
			mCursor.moveToFirst();
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
		}
		return items;
	}

	@SuppressLint("SimpleDateFormat")
	public String parseTimeToYMD(long time)
	{
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(new Date(time * 1000L));
	}
}
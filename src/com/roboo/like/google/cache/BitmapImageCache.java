package com.roboo.like.google.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapImageCache implements ImageCache
{
	// 获取应用程序的最大内存
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(maxMemory / 8)
	{
		protected int sizeOf(String key, Bitmap bitmap)
		{
			return bitmap.getRowBytes() * bitmap.getHeight();
		};
	};

	public Bitmap getBitmap(String url)
	{
		return mLruCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap)
	{
		mLruCache.put(url, bitmap);
	}

}

package com.roboo.like.google.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils
{
	public static  Bitmap getBitmap(String path)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 设置缩放比例
		options.inSampleSize = computeScale(options, 300, 300);
		// 设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	public static  Bitmap getBitmap(String path,int width ,int height)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 设置缩放比例
		options.inSampleSize = computeScale(options, width, height);
		// 设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	public static  Bitmap getBitmap(InputStream inputStream,int width ,int height)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream);
		// 设置缩放比例
		options.inSampleSize = computeScale(options, width, height);
		// 设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(inputStream, null, options);
	}

	/**
	 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
	 * 
	 * @param options
	 * @param width
	 * @param height
	 */
	private static  int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight)
	{
		int inSampleSize = 1;
		if (viewWidth == 0 || viewWidth == 0)
		{
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;

		// 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
		if (bitmapWidth > viewWidth || bitmapHeight > viewWidth)
		{
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

			// 为了保证图片不缩放变形，我们取宽高比例最小的那个
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}

}

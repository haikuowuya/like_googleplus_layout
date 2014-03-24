package com.roboo.like.google.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

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

	public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels)
	{
		// 创建一个和原始图片一样大小位图
		Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		// 创建带有位图roundConcerImage的画布
		Canvas canvas = new Canvas(roundConcerImage);
		// 创建画笔
		Paint paint = new Paint();
		// 创建一个和原始图片一样大小的矩形
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		// 去锯齿
		paint.setAntiAlias(true);
		// 画一个和原始图片一样大小的圆角矩形
		canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
		// 设置相交模式
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// 把图片画到矩形去
		canvas.drawBitmap(bitmap, null, rect, paint);
		return roundConcerImage;
	}
}

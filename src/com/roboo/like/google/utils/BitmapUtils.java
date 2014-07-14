package com.roboo.like.google.utils;

import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class BitmapUtils
{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 300;

	/***
	 * 通过给定图片文件路径获取Bitmap对象,Bitmap大小采用默认值300*300
	 * 
	 * @param path
	 *            ：图片文件的完整路径
	 * @return null 或者 Bitmap对象
	 */
	public static Bitmap getBitmap(String path)
	{
		File file = new File(path);
		if (file.exists())
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 设置为true,表示解析Bitmap对象，该对象不占内存
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(path, options);//
			// 设置缩放比例
			options.inSampleSize = computeScale(options, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			// 设置为false,解析Bitmap对象加入到内存中
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);// 使用BitmapFactory.decodeStream方法会更好一点。

		}
		return null;
	}

	/**
	 * 通过给定图片文件路径获取Bitmap对象
	 * 
	 * @param path
	 *            图片文件的完整路径
	 * @param width
	 *            ：Bitmap对象宽度
	 * @param height
	 *            ：Bitmap对象高度
	 * @return null 或者 Bitmap对象
	 */
	public static Bitmap getBitmap(String path, int width, int height)
	{
		File file = new File(path);
		if (file.exists())
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
		return null;
	}

	public static Bitmap getBitmap(InputStream inputStream, int width, int height)
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
	private static int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight)
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

	/***
	 * 根据给定的Bitmap对象，获取一个带有圆角的Bitmap对象
	 * 
	 * @param bitmap
	 * @param roundPixels
	 * @return
	 */
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

	public static Bitmap createReflectedBitmap(Bitmap srcBitmap)
	{
		if (null == srcBitmap)
		{
			return null;
		}

		final int REFLECTION_GAP = 4;
		int srcWidth = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		int reflectionWidth = srcBitmap.getWidth();
		int reflectionHeight = srcBitmap.getHeight() / 2;
		if (0 == srcWidth || srcHeight == 0)
		{
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		try
		{
			// The reflection bitmap, width is same with original's, height is half of original's.
			Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0, srcHeight / 2, srcWidth, srcHeight / 2, matrix, false);

			if (null == reflectionBitmap)
			{
				return null;
			}
			// Create the bitmap which contains original and reflection bitmap.
			Bitmap bitmapWithReflection = Bitmap.createBitmap(reflectionWidth, srcHeight + reflectionHeight + REFLECTION_GAP, Config.RGB_565);

			if (null == bitmapWithReflection)
			{
				return null;
			}

			// Prepare the canvas to draw stuff.
			Canvas canvas = new Canvas(bitmapWithReflection);

			// Draw the original bitmap.
			canvas.drawBitmap(srcBitmap, 0, 0, null);

			// Draw the reflection bitmap.
			canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			LinearGradient shader = new LinearGradient(0, srcHeight, 0, bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));

			// Draw the linear shader.
			canvas.drawRect(0, srcHeight, srcWidth, bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);

			return bitmapWithReflection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Load bitmap file from sd card.
	 * 
	 * @param strPath
	 *            The bitmap file path.
	 * @return The Bitmap object, the returned value may be null.
	 */
	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		if (null == drawable)
		{
			return null;
		}

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();

		return drawableToBitmap(drawable, width, height);
	}

	/**
	 * Load bitmap file from sd card.
	 * 
	 * @param strPath
	 *            The bitmap file path.
	 * @return The Bitmap object, the returned value may be null.
	 */
	public static Bitmap drawableToBitmap(Drawable drawable, int width, int height)
	{
		if (null == drawable || width <= 0 || height <= 0)
		{
			return null;
		}
		Config config = (drawable.getOpacity() != PixelFormat.OPAQUE) ? Config.ARGB_8888 : Config.RGB_565;
		Bitmap bitmap = null;

		try
		{
			bitmap = Bitmap.createBitmap(width, height, config);
			if (null != bitmap)
			{
				Canvas canvas = new Canvas(bitmap);
				drawable.setBounds(0, 0, width, height);
				drawable.draw(canvas);
			}
		}
		catch (OutOfMemoryError e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}

	/** 将已有的bitmap处理为带有圆角 */
	public Bitmap processImage(Bitmap bitmap)
	{
		int RADIUS_FACTOR = 2;//圆角的缩放因子
		Bitmap bmp;

		bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		float radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / RADIUS_FACTOR;
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawRoundRect(rect, radius, radius, paint);
		return bmp;
	}
}

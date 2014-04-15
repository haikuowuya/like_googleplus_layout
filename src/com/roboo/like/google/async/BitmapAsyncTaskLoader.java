package com.roboo.like.google.async;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.roboo.like.google.GoogleApplication;

public class BitmapAsyncTaskLoader extends BaseAsyncTaskLoader<Bitmap>
{
	private Context mContext;
	private Uri mDataUri;

	public BitmapAsyncTaskLoader(Context context, Uri dataUri)
	{
		super(context);
		this.mContext = context;
		this.mDataUri = dataUri;
	}

	public Bitmap loadInBackground()
	{
		Bitmap bitmap = null;
		if (null != mDataUri)
		{
			ContentResolver resolver = mContext.getContentResolver();
			String[] columns = { MediaStore.Images.Media.DATA };
			Cursor cursor = resolver.query(mDataUri, columns, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(columns[0]);
			String imgPath = cursor.getString(columnIndex);
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("imgPath = " + imgPath);
			}
			// File file = new File(imgPath);
			// mDataUri = Uri.fromFile(handleFile(file));//TODO 将图片处理成大小符合要求的文件

			try
			{
				bitmap = getBitmap(imgPath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		mEndTime = System.currentTimeMillis();
		if (mEndTime - mStartTime < THREAD_LEAST_DURATION_TIME)
		{
			try
			{
				Thread.sleep(THREAD_LEAST_DURATION_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	protected Bitmap getBitmap(String imagePath) throws IOException
	{
		ExifInterface exifInterface = new ExifInterface(imagePath);
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		String datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
		String gpsLongitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
		String iso = exifInterface.getAttribute(ExifInterface.TAG_ISO);
		String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
		System.out.println("datetime = " + datetime + " gpsLongitude =  " + gpsLongitude + " iso = " + iso + " exposureTime = " + exposureTime + " orientation = " + orientation + " \n exifInterface = " + exifInterface);

		int degree = 0;
		switch (orientation)
		{
		case ExifInterface.ORIENTATION_ROTATE_90:
		case ExifInterface.ORIENTATION_UNDEFINED:
			degree = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		}
		System.out.println("拍照后的图片被旋转了  " + degree + " 度");
		Bitmap bitmap = null;
		DisplayMetrics dMetrics = mContext.getResources().getDisplayMetrics();
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		int widthSample = (int) (imageWidth / dMetrics.widthPixels);
		int heightSample = (int) (imageHeight / dMetrics.heightPixels);
		options.inSampleSize = widthSample < heightSample ? heightSample : widthSample;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap = makeWatermarkBitmap(bitmap, "海阔无涯");
		File file = new File(new File(imagePath).getParentFile(), System.currentTimeMillis() + ".png");
		saveHandledBitmap(bitmap, file);

		return bitmap;
	}

	/** 将添加水印后的Bitmap保存到文件中去 */
	private void saveHandledBitmap(Bitmap bitmap, File file)
	{
		try
		{
			if (bitmap.compress(CompressFormat.PNG, 50, new FileOutputStream(file)))
			{
				System.out.println("保存图片成功  :: "+ file.getPath());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/** 处理拍照/选择的文件 */
	protected File handleFile(File file)
	{
		DisplayMetrics dMetrics = mContext.getResources().getDisplayMetrics();
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		System.out.println("  imageWidth = " + imageWidth + " imageHeight = " + imageHeight);
		int widthSample = (int) (imageWidth / (dMetrics.density * 90));
		int heightSample = (int) (imageHeight / (dMetrics.density * 90));
		System.out.println("widthSample = " + widthSample + " heightSample = " + heightSample);
		options.inSampleSize = widthSample < heightSample ? heightSample : widthSample;
		options.inJustDecodeBounds = false;
		Bitmap mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		System.out.println("newBitmap.size = " + mBitmap.getRowBytes() * mBitmap.getHeight());
		File handleFile = new File(file.getParentFile(), "upload.png");
		try
		{
			if (mBitmap.compress(CompressFormat.PNG, 50, new FileOutputStream(handleFile)))
			{
				System.out.println("保存图片成功");
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return handleFile;
	}

	private Bitmap makeWatermarkBitmap(Bitmap bitmap, String watermark)
	{
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);
		// drawWatermark(bitmapWidth, bitmapHeight, canvas, watermark);
		drawWaterMask(watermark, canvas);
		canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		canvas.restore();// 存储
		return newBitmap;
	}

	/** 在图片最下方添加文字 */
	protected void drawWatermark(int bitmapWidth, int bitmapHeight, Canvas canvas, String watermark)
	{
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(Color.RED);
		textPaint.setTypeface(font);
		int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, mContext.getResources().getDisplayMetrics());
		textPaint.setTextSize(textSize);
		// 这里是自动换行的
		// StaticLayout layout = new StaticLayout(watermark, textPaint,
		// bitmap.getWidth(), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
		// layout.draw(canvas);
		canvas.drawText(watermark, (bitmapWidth - textSize * watermark.length()) / 2, bitmapHeight - 50, textPaint); // 文字就加左上角算了
	}

	/** 在图片对角线方向上添加文字 */
	private void drawWaterMask(String text, Canvas canvas)
	{
		int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, mContext.getResources().getDisplayMetrics());
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		Path path = new Path();
		path.moveTo(0, height);
		path.lineTo(width, 0);
		path.close();

		Paint paint = new Paint();
		paint.setColor(0x88ff0000);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		paint.setDither(true);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		int length = (int) Math.sqrt(width * width + height * height);
		int hOffset = (length - (bounds.right - bounds.left)) / 2;
		canvas.drawTextOnPath(text, path, hOffset, textSize / 2, paint);
	}

}
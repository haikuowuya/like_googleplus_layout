package com.roboo.like.google.async;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.roboo.like.google.utils.BitmapUtils;
import com.roboo.like.google.views.ZoomImageView;

public class BitmapAsyncTask extends AsyncTask<String, Integer, Bitmap>
{
	private View mView;
	private int mImageWidth;
	private int mImageHeight;

	public BitmapAsyncTask(View mView)
	{
		this.mView = mView;
	}

	public BitmapAsyncTask(View mView, int mImageWidth, int mImageHeight)
	{
		super();
		this.mView = mView;
		this.mImageWidth = mImageWidth;
		this.mImageHeight = mImageHeight;
	}

	@Override
	protected Bitmap doInBackground(String... params)
	{
		String url = params[0];
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			if (connection.getResponseCode() == HttpStatus.SC_OK)
			{
				InputStream inputStream = connection.getInputStream();
				saveImgFile(inputStream);
				return BitmapUtils.getBitmap(inputStream, mImageWidth, mImageHeight);
			}
		}
		catch (MalformedURLException e)
		{

			e.printStackTrace();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}

		return null;
	}

	private void saveImgFile(InputStream inputStream)
	{

	}

	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		if (null != bitmap)
		{
			if (mView instanceof ZoomImageView)
			{
				((ZoomImageView) mView).setImageBitmap(bitmap);

			}
			else if (mView instanceof ImageView)
			{
				((ImageView) mView).setImageBitmap(bitmap);
			}
		}
	}

}

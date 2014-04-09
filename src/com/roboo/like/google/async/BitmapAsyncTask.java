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

public class BitmapAsyncTask extends AsyncTask<String, Integer, Bitmap>
{
	private ImageView mImageView;
	private int mImageWidth;
	private int mImageHeight;

	public BitmapAsyncTask(ImageView imageView)
	{
		this.mImageView = imageView;
	}

	public BitmapAsyncTask(ImageView imageView, int mImageWidth, int mImageHeight)
	{
		super();
		this.mImageView = imageView;
		this.mImageWidth = mImageWidth;
		this.mImageHeight = mImageHeight;
	}

	@Override
	protected Bitmap doInBackground(String... params)
	{
		String imagePath = params[0];
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(imagePath).openConnection();
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
			mImageView.setImageBitmap(bitmap);
		}
	}

}

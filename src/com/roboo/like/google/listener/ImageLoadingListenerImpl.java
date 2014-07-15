package com.roboo.like.google.listener;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.roboo.like.google.R;

public class ImageLoadingListenerImpl implements ImageLoadingListener
{
	protected static final boolean DEBUG = true;
	@Override
	public void onLoadingStarted(String imageUri, View view)
	{
		 ImageView imageView  =(ImageView) view;
		 imageView.setImageResource(R.drawable.ic_default_image);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason)
	{
		 ImageView imageView  =(ImageView) view;
		 imageView.setImageResource(R.drawable.ic_default_image);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap bitmap)
	{
		 ImageView imageView  =(ImageView) view;
		 int height = bitmap.getHeight();
		 int width = bitmap.getWidth();
		 System.out.println("height = " + height + " width = " + width);
		 imageView.setImageBitmap(bitmap);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view)
	{
		 ImageView imageView  =(ImageView) view;
		 imageView.setImageResource(R.drawable.ic_default_image);
	}

}

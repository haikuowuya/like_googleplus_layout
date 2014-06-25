package com.roboo.like.google;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.roboo.like.google.commons.CrashException;

public class BaseActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		GoogleApplication application = (GoogleApplication) getApplication();
		application.recordActivity(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashException.getInstance(this));
		super.onCreate(savedInstanceState);
	}

	public boolean isImg(String str)
	{
		 List<String> imgList =  Arrays.asList(getResources().getStringArray(R.array.img_arrays));
		boolean flag = false;
		for (String string : imgList)
		{
			 if(str.startsWith(string))
			 {
				 flag = true;
				 break;
			 }
			 else if(str.toLowerCase(Locale.getDefault()).endsWith(".png")||str.toLowerCase(Locale.getDefault()).endsWith(".jpg")||str.toLowerCase(Locale.getDefault()).endsWith("gif"))
			 {
				 flag = true;
				 break;
			 }
		}
		return flag;
	}
}

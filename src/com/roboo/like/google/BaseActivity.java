package com.roboo.like.google;

import com.roboo.like.google.commons.CrashException;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public  class BaseActivity extends FragmentActivity  
{
	 @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		GoogleApplication application = (GoogleApplication) getApplication();
		application.recordActivity(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashException.getInstance(this));
		super.onCreate(savedInstanceState);
	}
}

package com.roboo.like.google;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;

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

	protected void onResume()
	{
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause()
	{
		super.onPause();
		JPushInterface.onPause(this);

	}

	public boolean isImg(String str)
	{
		List<String> imgList = Arrays.asList(getResources().getStringArray(R.array.img_arrays));
		boolean flag = false;
		for (String string : imgList)
		{
			if (str.startsWith(string))
			{
				flag = true;
				break;
			}
			else if (str.toLowerCase(Locale.getDefault()).endsWith(".png") || str.toLowerCase(Locale.getDefault()).endsWith(".jpg") || str.toLowerCase(Locale.getDefault()).endsWith("gif"))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}

	protected FragmentTransaction beginTransaction()
	{
		int enter = R.anim.base_slide_right_in;
		int exit = R.anim.base_slide_right_out;
		int popEnter = R.anim.base_slide_right_in;
		int popExit = R.anim.base_slide_right_out;
		
		enter = exit = popEnter = popExit = 0;
		return getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit);
	}
}

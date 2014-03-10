package com.roboo.like.google;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public  abstract class BaseActivity extends FragmentActivity
{
	protected ActionBar mActionBar;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		mActionBar = getActionBar();
	}
	public abstract void initView();
}

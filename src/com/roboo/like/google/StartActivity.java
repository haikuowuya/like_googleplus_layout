package com.roboo.like.google;

import javax.crypto.Mac;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.roboo.like.google.fragments.StartFragment;

public class StartActivity extends BaseLayoutActivity
{
	public static void actionStart(Activity activity)
	{
		Intent intent = new Intent(activity, StartActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, StartFragment.newInstance()).commit();
		}
		customActionBar();
		initView();

	}

	public void initView()
	{}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setCustomView(R.layout.actionbar_custom_view);
	 
	}

}	  

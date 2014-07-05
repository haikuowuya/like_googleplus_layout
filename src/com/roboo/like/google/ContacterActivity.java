package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.roboo.like.google.fragments.ContacterFragment;

public class ContacterActivity extends BaseLayoutActivity
{
	public static void actionContacter( Activity activity)
	{
		 Intent intent = new Intent(activity, ContacterActivity.class);
		 activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		customActionBar();
		setContentView(R.layout.activity_contacter);//TODO
		if(getSupportFragmentManager().findFragmentById(R.id.frame_container) ==null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, ContacterFragment.newInstance()).commit();
		}
	}

	@Override
	public void initView()
	{

	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("联系人");
		mActionBar.setLogo(R.drawable.ic_abs_friend_up);
	}

	
}

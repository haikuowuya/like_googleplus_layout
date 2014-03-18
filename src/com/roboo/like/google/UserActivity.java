package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.fragments.UserFragment;

/** 个人信息界面 */
public class UserActivity extends BaseActivity
{
 
	public static void actionUser(Activity activity)
	{
		Intent intent = new Intent(activity,UserActivity.class);
		activity.startActivity(intent);
	}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initView();
		customActionBar();
		if(getSupportFragmentManager().findFragmentById(R.id.frame_container) ==null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, UserFragment.newInstance()).commit();
		}
		 
	}
	 
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initView()
	{
	 
		 
	}
	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("个人信息");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}
}

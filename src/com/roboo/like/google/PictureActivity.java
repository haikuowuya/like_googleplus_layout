package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.roboo.like.google.fragments.PictureFragment;

/** 图片界面 */
public class PictureActivity extends BaseActivity
{
	private ProgressBar mProgressBar;

	/** 跳转到图片界面 */
	public static void actionPicture(Activity activity)
	{
		Intent intent = new Intent(activity, PictureActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		initView();
		customActionBar();
		
		if(getSupportFragmentManager().findFragmentById(R.id.frame_container) ==null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, PictureFragment.newInstance()).commit();
		}
		
	}
	 
	

	public void initView()
	{
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("照片");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

}

package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.roboo.like.google.fragments.CameraFragment;

/** 拍照界面 */
public class CameraActivity extends BaseActivity
{
	/** 跳转到拍照处理界面 */
	public static void actionCamera(Activity activity, Uri data)
	{
		Intent intent = new Intent(activity, CameraActivity.class);
		intent.setData(data);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, CameraFragment.newInstance(getIntent().getData())).commit();
		}
	}
	public void initView()
	{

	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("拍照");
		mActionBar.setLogo(R.drawable.ic_abs_camera_up);
	}

}

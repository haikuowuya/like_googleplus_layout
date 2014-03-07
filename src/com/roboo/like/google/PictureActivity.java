package com.roboo.like.google;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/** 图片界面 */
public class PictureActivity extends BaseActivity
{
	/** 跳转到图片界面 */
	public static void actionPicture(Activity activity)
	{
		Intent intent = new Intent(activity, PictureActivity.class);
		activity.startActivity(intent);

	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		customActionBar();
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("照片");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

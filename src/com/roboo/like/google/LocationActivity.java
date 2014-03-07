package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**我的位置*/
public class LocationActivity extends BaseActivity
{
	/** 跳转到我的位置界面 */
	public static void actionLocation(Activity activity)
	{
		Intent intent = new Intent(activity, LocationActivity.class);
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
		mActionBar.setTitle("我的位置");
		mActionBar.setLogo(R.drawable.ic_abs_location_up);
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

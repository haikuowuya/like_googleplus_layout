package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/** 心情界面 */
public class MoodActivity extends BaseActivity
{
	/** 跳转到心情界面 */
	public static void actionMood(Activity activity)
	{
		Intent intent = new Intent(activity, MoodActivity.class);
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
		mActionBar.setTitle("心情");
		mActionBar.setLogo(R.drawable.ic_abs_mood_up);
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

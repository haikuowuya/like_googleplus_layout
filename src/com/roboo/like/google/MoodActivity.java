package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/** 心情界面 */
public class MoodActivity extends BaseActivity
{
	private Button mBtnOpen;
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
		setContentView(R.layout.activity_mood);
		initView();
		setListener();
		
	}

	private void setListener()
	{
		 OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		 mBtnOpen.setOnClickListener(onClickListenerImpl);
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

	public void initView()
	{
		mBtnOpen = (Button) findViewById(R.id.btn_open);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("心情");
		mActionBar.setLogo(R.drawable.ic_abs_mood_up);
	}

	private class OnClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("kugou://start.weixin?filename=ee"));
			startActivity(intent);
		}
		
	}
}

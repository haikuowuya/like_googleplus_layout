package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.roboo.like.google.fragments.CommentFragment;
import com.roboo.like.google.fragments.NewsFragment;
import com.roboo.like.google.models.NewsItem;

/** 新闻详情 */
public class CommentActivity extends BaseLayoutActivity
{
	public static final String EXTRA_NEWS_ID = "news_id";

	public static void actionComment(Activity activity, String newsId)
	{
		Intent intent = new Intent(activity, CommentActivity.class);
		intent.putExtra(EXTRA_NEWS_ID, newsId);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		initView();

		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, CommentFragment.newInstance(getIntent().getStringExtra(EXTRA_NEWS_ID))).commit();
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

}

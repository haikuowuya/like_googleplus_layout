package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.fragments.NewsFragment;
import com.roboo.like.google.models.NewsItem;

/** 新闻详情 */
public class NewsActivity extends BaseActivity
{
	public static final String EXTRA_NEWS = "news";
	private NewsItem mItem;
	public static void actionNews(Activity activity, NewsItem item)
	{
		Intent intent = new Intent(activity, NewsActivity.class);
		intent.putExtra(EXTRA_NEWS, item);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			mItem = (NewsItem) getIntent().getSerializableExtra(EXTRA_NEWS);
			if(null != mItem)
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, NewsFragment.newInstance(mItem)).commit();
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
		mActionBar.setTitle("新闻详情");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}
}

package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.roboo.like.google.fragments.NewsFragment;
import com.roboo.like.google.models.NewsItem;

/** 新闻详情 */
public class DidiActivity extends BaseActivity
{
	public static final String EXTRA_NEWS = "news";
	private static final String URL = "http://pay.xiaojukeji.com/api/v2/webapp";
	private WebView mWebView;

	public static void actionDidi(Activity activity)
	{
		Intent intent = new Intent(activity, DidiActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_didi);
		customActionBar();
		initView();
		mWebView.loadUrl(URL);
		mWebView.cancelLongPress();
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
		mWebView =  (WebView) findViewById(R.id.wv_webview);
	}


	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("嘀嘀打车");
		mActionBar.setLogo(R.drawable.ic_abs_didi_up);
	}
}

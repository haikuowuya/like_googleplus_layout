package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;

/** 新闻详情 */
public class DidiActivity extends BaseActivity
{

	private static final String URL = "http://pay.xiaojukeji.com/api/v2/webapp?city=%E5%8C%97%E4%BA%AC&maptype=wgs84&fromlat=39.98096907577634&fromlng=116.30000865410719&fromaddr=%E9%93%B6%E7%A7%91%E5%A4%A7%E5%8E%A6&toaddr=%E8%A5%BF%E4%BA%8C%E6%97%97&toshop=%E5%BE%97%E5%AE%9E%E5%A4%A7%E5%8E%A6&channel=1224&d=130002030203";
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
		mWebView.setOnLongClickListener(new OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				return true;
			}
		});
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
		mWebView = (WebView) findViewById(R.id.wv_webview);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("嘀嘀打车");
		mActionBar.setLogo(R.drawable.ic_abs_didi_up);
	}
}

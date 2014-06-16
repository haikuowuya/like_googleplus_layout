package com.roboo.like.google;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;

/** 新闻详情 */
@SuppressLint("SetJavaScriptEnabled")
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
		initWebView();
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

	private void initWebView()
	{
		mWebView.clearHistory();
		mWebView.requestFocus();
		mWebView.setClickable(true);
		mWebView.getSettings().setJavaScriptEnabled(true);// 可用JS
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		 
		mWebView.setInitialScale(100);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setGeolocationEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setDatabaseEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.cancelLongPress();
		mWebView.getSettings().setSavePassword(false);// 设置不需要 弹出“是否保存密码” 对话框
		mWebView.setWebViewClient(getWebViewClient());
		
	}

	private WebViewClient getWebViewClient()
	{
		return new WebViewClient()
		{
			
		};
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

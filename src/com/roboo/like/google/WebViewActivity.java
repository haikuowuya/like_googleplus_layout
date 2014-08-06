package com.roboo.like.google;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/** 新闻详情 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends BaseLayoutActivity
{
	private static final String EXTRA_URL = "url";
	private static final String EXTRA_TITLE = "title";
	private static final String URL = "http://pay.xiaojukeji.com/api/v2/webapp?city=%E5%8C%97%E4%BA%AC&maptype=wgs84&fromlat=39.98096907577634&fromlng=116.30000865410719&fromaddr=%E9%93%B6%E7%A7%91%E5%A4%A7%E5%8E%A6&toaddr=%E8%A5%BF%E4%BA%8C%E6%97%97&toshop=%E5%BE%97%E5%AE%9E%E5%A4%A7%E5%8E%A6&channel=1224&d=130002030203";
	/** WebView要加载的URL */
	private String mUrl;
	/** ActionBar标题 */
	private String mTitle;

	private WebView mWebView;
	/** WebView加载进度条 */
	private ProgressBar mProgressBar;
	/** 后退 */
	private ImageButton mIBtnBrowserBack;
	/** 前进 */
	private ImageButton mIBtnBrowserForward;
	/** 刷新 or 取消 */
	private ImageButton mIBtnBrowserRefreshCancle;

	public static void actionWebView(Activity activity)
	{
		Intent intent = new Intent(activity, WebViewActivity.class);
		activity.startActivity(intent);
	}

	public static void actionWebView(Activity activity, String url)
	{
		Intent intent = new Intent(activity, WebViewActivity.class);
		intent.putExtra(EXTRA_URL, url);
		activity.startActivity(intent);
	}

	public static void actionWebView(Activity activity, String url, String title)
	{
		Intent intent = new Intent(activity, WebViewActivity.class);
		intent.putExtra(EXTRA_URL, url);
		intent.putExtra(EXTRA_TITLE, title);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);// TODO
		initView();
		initWebView();
		mUrl = getIntent().getStringExtra(EXTRA_URL);
		mTitle = getIntent().getStringExtra(EXTRA_TITLE);
		customActionBar();
		if (TextUtils.isEmpty(mUrl))
		{
			mUrl = URL;
		}
		mWebView.loadUrl(mUrl);
		setListener();
	}

	private void setListener()
	{
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		mIBtnBrowserBack.setOnClickListener(onClickListenerImpl);
		mIBtnBrowserForward.setOnClickListener(onClickListenerImpl);
		mIBtnBrowserRefreshCancle.setOnClickListener(onClickListenerImpl);
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
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setDomStorageEnabled(true);
//		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.setScrollBarStyle(0);
		mWebView.cancelLongPress();
		mWebView.getSettings().setSavePassword(false);// 设置不需要 弹出“是否保存密码” 对话框
		mWebView.setWebViewClient(getWebViewClient());
		mWebView.setWebChromeClient(getWebChromeClient());
	}

	private WebChromeClient getWebChromeClient()
	{
		return new WebChromeClient()
		{
			
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100)
				{
					mProgressBar.setVisibility(View.GONE);
					mProgressBar.setProgress(0);
					mIBtnBrowserRefreshCancle.setImageDrawable(getResources().getDrawable(R.drawable.browser_refresh_selector));
					if (mWebView.canGoForward())
					{
						mIBtnBrowserForward.setEnabled(true);
					}
					else
					{
						mIBtnBrowserForward.setEnabled(false);
					}
				}
				else
				{
					if (mProgressBar.getVisibility() == View.GONE)
					{
						mProgressBar.setVisibility(View.VISIBLE);
					}
					if (mProgressBar.getProgress() <= newProgress)
					{
						mProgressBar.setProgress(newProgress);
					}
					mIBtnBrowserRefreshCancle.setImageDrawable(getResources().getDrawable(R.drawable.browser_cancle_selector));
				}
			}

		};
	}

	private WebViewClient getWebViewClient()
	{
		return new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				 mProgressBar.setProgress(0);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return super.shouldOverrideUrlLoading(view, url);
			}
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
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		mWebView = (WebView) findViewById(R.id.wv_webview);
		mIBtnBrowserBack = (ImageButton) findViewById(R.id.ibtn_browser_back);
		mIBtnBrowserForward = (ImageButton) findViewById(R.id.ibtn_browser_forward);
		mIBtnBrowserRefreshCancle = (ImageButton) findViewById(R.id.ibtn_browser_refresh_or_cancle);
		mIBtnBrowserForward.setEnabled(false);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		if (TextUtils.isEmpty(mTitle))
		{
			mTitle = "嘀嘀打车";
		}
		mActionBar.setTitle(mTitle);
		mActionBar.setLogo(R.drawable.ic_abs_didi_up);
	}

	@Override
	public void onBackPressed()
	{
		goBack();
	}

	private void goBack()
	{
		if (mWebView.canGoBack())
		{
			mWebView.goBack();
			mIBtnBrowserForward.setEnabled(true);
		}
		else
		{
			mIBtnBrowserForward.setEnabled(false);
			finish();
		}
	}

	/** 刷新 or 取消 */
	public void refreshOrCancle()
	{
		StateListDrawable stateListDrawable = (StateListDrawable) mIBtnBrowserRefreshCancle.getDrawable();
		BitmapDrawable currentBitmapDrawable = (BitmapDrawable) stateListDrawable.getCurrent();
		BitmapDrawable refreshBitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_browser_refresh_pressed);
		BitmapDrawable cancleBitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_browser_cancle_pressed);
		System.out.println( currentBitmapDrawable.getBitmap() + " "+refreshBitmapDrawable.getBitmap() + " " + cancleBitmapDrawable.getBitmap());
		if (currentBitmapDrawable.getBitmap() == refreshBitmapDrawable.getBitmap())// 当前是刷新按钮
		{
			mWebView.reload();
		}
		else if (currentBitmapDrawable.getBitmap() == cancleBitmapDrawable.getBitmap())// 当前是停止按钮
		{
			mWebView.stopLoading();
		}
	}

	class OnClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.ibtn_browser_back:// 返回
				goBack();
				break;
			case R.id.ibtn_browser_forward:// 前进
				mWebView.goForward();
				break;
			case R.id.ibtn_browser_refresh_or_cancle:// 刷新or取消
				refreshOrCancle();
				break;
			default:
				break;
			}
		}

	}

}

package com.roboo.like.google;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.roboo.like.google.listener.SwipeBackListener;
import com.roboo.like.google.views.SwipeBackFrameLayout;
import com.roboo.like.google.views.SwipeBackFrameLayout.SwipeListener;
import com.roboo.like.google.views.helper.SwipeBackActivityHelper;

public abstract class BaseLayoutActivity extends BaseActivity implements SwipeBackListener
{
	public static final String PREF_LOACTION_CITY="city";
	public static final String PREF_APP_FIRST_START="first_start";
	public static final String PREF_LOACTION_ADDRESS="address";
	public static final String PREF_LOACTION_LATITUDE="latitude";
	public static final String PREF_LOACTION_LONGITUDE="longitude";

	public static final double SUZHOU_LONGITUDE = 120.676459;
	public static final double SUZHOU_LATITUDE = 31.300916;
	public static final String DEFAULT_CITY="苏州市";
	public static final String DEFAULT_ADDRESS="国际科技园";
	
	/** 滑动关闭Activity的帮助工具类对象 */
	protected SwipeBackActivityHelper mActivityHelper;
	/** ActionBar对象 */
	protected ActionBar mActionBar;
	/** 主要的窗口容器 */
	private FrameLayout mContainer;

	public abstract void initView();

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (isActionBarEnable())
		{
			mActionBar = getActionBar();
		}
		mActivityHelper = new SwipeBackActivityHelper(this);
		
		mActivityHelper.onActivityCreate();
		super.setContentView(R.layout.activity_base);// TODO
		mContainer = (FrameLayout) findViewById(R.id.frame_container);

	}

	public void setContentView(int layoutResID)
	{
		View childView = LayoutInflater.from(this).inflate(layoutResID, null);
		mContainer.addView(childView);

	}

	public void setContentView(View view)
	{
		mContainer.addView(view);
	}

	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mActivityHelper.onPostCreate();

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

	public SwipeBackFrameLayout getSwipeBackLayout()
	{
		return mActivityHelper.getSwipeBackLayout();
	}

	private boolean isActionBarEnable()
	{
		return !getWindow().hasFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void setSwipeBackEnable(boolean enable)
	{
		getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackFrameLayout.EDGE_RIGHT);
		getSwipeBackLayout().setEnableGesture(enable);
		getSwipeBackLayout().addSwipeListener(new SwipeListenerImpl());

	}

	@Override
	public void scrollToFinishActivity()
	{
		getSwipeBackLayout().scrollToFinishActivity();

	}

	@Override
	public void scrollToNextActivity()
	{

	}

	private class SwipeListenerImpl implements SwipeListener
	{
		public void onScrollStateChange(int state, float scrollPercent)
		{
			System.out.println("onScrollStateChange");
		}

		@Override
		public void onEdgeTouch(int edgeFlag)
		{
			System.out.println("onEdgeTouch");
		}

		@Override
		public void onScrollOverThreshold()
		{
			System.out.println("onScrollOverThreshold");
		}

	}
}

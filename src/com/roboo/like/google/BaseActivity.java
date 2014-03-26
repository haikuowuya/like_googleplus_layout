package com.roboo.like.google;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.roboo.like.google.listener.SwipeBackListener;
import com.roboo.like.google.views.SwipeBackFrameLayout;
import com.roboo.like.google.views.helper.SwipeBackActivityHelper;

public abstract class BaseActivity extends FragmentActivity implements SwipeBackListener
{
	/**IT之家的图片地址前缀*/
	public  static final String PREFIX_IMG_URL = "http://img.ithome.com";
	protected SwipeBackActivityHelper mActivityHelper;
	protected ActionBar mActionBar;
	private FrameLayout mContainer;

	public abstract void initView();

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(isActionBarEnable())
		{
			mActionBar = getActionBar();
		}
		mActivityHelper = new SwipeBackActivityHelper(this);
		mActivityHelper.onActivityCreate();
		super.setContentView(R.layout.activity_base);
		mContainer = (FrameLayout) findViewById(R.id.frame_container);
	}

	public void setContentView(int layoutResID)
	{
		View childView = LayoutInflater.from(this).inflate(layoutResID, null);
		mContainer.addView(childView);
	}

	@Override
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

	@Override
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
		getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackFrameLayout.EDGE_LEFT);
		getSwipeBackLayout().setEnableGesture(enable);

	}

	@Override
	public void scrollToFinishActivity()
	{
		getSwipeBackLayout().scrollToFinishActivity();
	}

	@Override
	public void overridePendingTransition(int enterAnim, int exitAnim)
	{
		// enterAnim = R.anim.base_slide_bottom_in;
		// exitAnim = R.anim.base_slide_right_out;
		super.overridePendingTransition(enterAnim, exitAnim);
	}
}

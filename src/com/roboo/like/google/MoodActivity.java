package com.roboo.like.google;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

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
		ActionBar mActionBar = getActionBar();
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

class CustomLinearLayout extends LinearLayout
{

	private static final String TAG = "CustomView";

	private Scroller mScroller;
	private GestureDetector mGestureDetector;

	public CustomLinearLayout(Context context)
	{
		this(context, null);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		Interpolator interpolator = new BounceInterpolator();
		setClickable(true);
		setLongClickable(true);
		mScroller = new Scroller(context ,interpolator);
		mGestureDetector = new GestureDetector(context, new CustomGestureListener());
	}

	// 调用此方法滚动到目标位置
	public void smoothScrollTo(int fx, int fy)
	{
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	// 调用此方法设置滚动的相对偏移
	public void smoothScrollBy(int dx, int dy)
	{
		// 设置mScroller的滚动偏移量
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
		invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	}

	@Override
	public void computeScroll()
	{
		// 先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset())
		{
			// 这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:
			Log.i(TAG, " ScorllY = " + getScrollY()+" ScrollX = " + getScrollX());
			smoothScrollTo(0, 0);
			break;
		default:
			return mGestureDetector.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	class CustomGestureListener implements GestureDetector.OnGestureListener
	{
		public boolean onDown(MotionEvent e)
		{
			return true;
		}

		public void onShowPress(MotionEvent e)
		{

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			int disY = (int) ((distanceY - 0.5) / 2);
			int disX = (int) ((distanceX - 0.5) / 2);
			Log.i(TAG, "disX =" + disX + " disY = " + disY);
			if ((Math.abs(disX) - Math.abs(disY)) >= 0)
			{
				smoothScrollBy(disX, 0);
			}
			else
			{
				smoothScrollBy(0, disY);
			}

			return false;
		}

		@Override
		public void onLongPress(MotionEvent e)
		{

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			return false;
		}

	}
}

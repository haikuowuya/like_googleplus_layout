package com.roboo.like.google;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

/** 心情界面 */
public class MoodActivity extends BaseActivity
{
	private Button mBtnOpen;
	private CustomLinearLayout mCustomLinearLayout;

	/** 跳转到心情界面 */
	public static void actionMood(Activity activity)
	{
		Intent intent = new Intent(activity, MoodActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood);//TODO
		customActionBar();
		initView();
		setListener();
	}

	private void setListener()
	{
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		mBtnOpen.setOnClickListener(onClickListenerImpl);
		mCustomLinearLayout.setOnFlingListener(new OnFlingListener()
		{
			public void onRightFling()
			{
//				Toast.makeText(getApplicationContext(), "右", Toast.LENGTH_SHORT).show();
				ContacterActivity.actionContacter(MoodActivity.this);
			}

			@Override
			public void onLeftFling()
			{
//				Toast.makeText(getApplicationContext(), "左", Toast.LENGTH_SHORT).show();
				MoodActivity.this.finish()	;
			}
		});
	}

	@Override
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
		mCustomLinearLayout = (CustomLinearLayout) findViewById(R.id.custom_linear_container);
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
	private int mTriggerThreshold;
	private static final String TAG = "CustomView";
	private OnFlingListener mOnFlingListener;
	private Scroller mScroller;
	private GestureDetector mGestureDetector;
	public CustomLinearLayout(Context context)
	{
		this(context, null);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setClickable(true);
		setLongClickable(true);
		init();
	}

	private void init()
	{
		Interpolator interpolator = new BounceInterpolator();
		mScroller = new Scroller(getContext(), interpolator);
		mGestureDetector = new GestureDetector(getContext(), new CustomGestureListener());
		mTriggerThreshold = (int) (100 * getResources().getDisplayMetrics().density);
		/*http://blog.csdn.net/a859522265/article/details/8889024*/
		setWillNotDraw(false);//清除ViewGroup不绘制自身的标志 ，导致onDraw()方法的调用， 或者设置background同理。
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawARGB(88, 255, 22, 22);
//		System.out.println("onDraw");
		super.onDraw(canvas);
		
	}
	@Override
	protected void dispatchDraw(Canvas canvas)//在绘制自己之后并且在绘制子View之前调用，
	{
//		System.out.println("dispatchDraw");
		super.dispatchDraw(canvas);
	}
	public void setOnFlingListener(OnFlingListener onFlingListener)
	{
		this.mOnFlingListener = onFlingListener;
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
		if (null != mOnFlingListener)
		{
			if (dx > mTriggerThreshold)
			{
				mOnFlingListener.onLeftFling();
			}
			else if(dx < -mTriggerThreshold)
			{
				mOnFlingListener.onRightFling();
			}
		}
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
			Log.i(TAG, " ScorllY = " + getScrollY() + " ScrollX = " + getScrollX());
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
//class CustomLinearLayout extends LinearLayout
//{
//	private int mTriggerThreshold;
//	private static final String TAG = "CustomView";
//	private OnFlingListener mOnFlingListener;
//	private Scroller mScroller;
//	private GestureDetector mGestureDetector;
//	
//	public CustomLinearLayout(Context context)
//	{
//		this(context, null);
//	}
//	
//	public CustomLinearLayout(Context context, AttributeSet attrs)
//	{
//		super(context, attrs);
//		setClickable(true);
//		setLongClickable(true);
//		init();
//		
//	}
//	
//	private void init()
//	{
//		Interpolator interpolator = new BounceInterpolator();
//		mScroller = new Scroller(getContext(), interpolator);
//		
//		mGestureDetector = new GestureDetector(getContext(), new CustomGestureListener());
//		mTriggerThreshold = (int) (100 * getResources().getDisplayMetrics().density);
//	}
//	
//	public void setOnFlingListener(OnFlingListener onFlingListener)
//	{
//		this.mOnFlingListener = onFlingListener;
//	}
//	
//	// 调用此方法滚动到目标位置
//	public void smoothScrollTo(int fx, int fy)
//	{
//		int dx = fx - mScroller.getFinalX();
//		int dy = fy - mScroller.getFinalY();
//		smoothScrollBy(dx, dy);
//	}
//	
//	// 调用此方法设置滚动的相对偏移
//	public void smoothScrollBy(int dx, int dy)
//	{
//		if (null != mOnFlingListener)
//		{
//			if (dx > mTriggerThreshold)
//			{
//				mOnFlingListener.onLeftFling();
//			}
//			else if(dx < -mTriggerThreshold)
//			{
//				mOnFlingListener.onRightFling();
//			}
//		}
//		// 设置mScroller的滚动偏移量
//		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
//		invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
//	}
//	
//	@Override
//	public void computeScroll()
//	{
//		// 先判断mScroller滚动是否完成
//		if (mScroller.computeScrollOffset())
//		{
//			// 这里调用View的scrollTo()完成实际的滚动
//			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//			// 必须调用该方法，否则不一定能看到滚动效果
//			postInvalidate();
//		}
//		super.computeScroll();
//	}
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent event)
//	{
//		switch (event.getAction())
//		{
//		case MotionEvent.ACTION_UP:
//			Log.i(TAG, " ScorllY = " + getScrollY() + " ScrollX = " + getScrollX());
//			smoothScrollTo(0, 0);
//			break;
//		default:
//			return mGestureDetector.onTouchEvent(event);
//		}
//		return super.onTouchEvent(event);
//	}
//	
//	class CustomGestureListener implements GestureDetector.OnGestureListener
//	{
//		public boolean onDown(MotionEvent e)
//		{
//			return true;
//		}
//		
//		public void onShowPress(MotionEvent e)
//		{
//			
//		}
//		
//		@Override
//		public boolean onSingleTapUp(MotionEvent e)
//		{
//			return false;
//		}
//		
//		@Override
//		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
//		{
//			int disY = (int) ((distanceY - 0.5) / 2);
//			int disX = (int) ((distanceX - 0.5) / 2);
//			Log.i(TAG, "disX =" + disX + " disY = " + disY);
//			if ((Math.abs(disX) - Math.abs(disY)) >= 0)
//			{
//				smoothScrollBy(disX, 0);
//			}
//			else
//			{
//				smoothScrollBy(0, disY);
//			}
//			
//			return false;
//		}
//		
//		@Override
//		public void onLongPress(MotionEvent e)
//		{
//			
//		}
//		
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
//		{
//			return false;
//		}
//		
//	}
//}

interface OnFlingListener
{
	public void onLeftFling();

	public void onRightFling();
}

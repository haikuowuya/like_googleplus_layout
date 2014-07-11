package com.roboo.like.google;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Paint.Cap;
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
		setContentView(R.layout.activity_mood);// TODO
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
				// Toast.makeText(getApplicationContext(), "右", Toast.LENGTH_SHORT).show();
				ContacterActivity.actionContacter(MoodActivity.this);
			}

			@Override
			public void onLeftFling()
			{
				// Toast.makeText(getApplicationContext(), "左", Toast.LENGTH_SHORT).show();
				MoodActivity.this.finish();
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
	private static final int CLIP_HEIGHT = 100;
	private OnFlingListener mOnFlingListener;
	private Scroller mScroller;
	private GestureDetector mGestureDetector;
	private Bitmap mCanvasBitmap;
	private Paint mCanvasPaint;
	private Region mCanvasRegion;
	public boolean status = HIDE;// 显示还是隐藏的状态，最开始为HIDE
	public static final boolean SHOW = true;// 显示图片
	public static final boolean HIDE = false;// 隐藏图片

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
		mCanvasBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image2);
		mCanvasRegion = new Region();
		mCanvasPaint = new Paint();
		/* http://blog.csdn.net/a859522265/article/details/8889024 */
		setWillNotDraw(false);// 清除ViewGroup不绘制自身的标志 ，导致onDraw()方法的调用， 或者设置background同理。
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		System.out.println("widthMeasureSpec = " + widthMeasureSpec + " heightMeasureSpec = " + heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		System.out.println("widthMode = " + widthMode + " heightMode = " + heightMode);
		System.out.println("AT_MOST = " + MeasureSpec.AT_MOST + " EXACTLY = " + MeasureSpec.EXACTLY + " UNSPECIFIED = " + MeasureSpec.UNSPECIFIED);
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
		System.out.println("measuredWidth = " + measuredWidth + " measuredHeight = " + measuredHeight);
		// int childWidthMeasureSpec = makeChildWidthMeasureSpec();
		// int childHeightMeasureSpec = makeChildHeightMeasureSpec();
		// measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);// 如果调用这句话，子view不会出现，除非调用measureChildren方法。
	}

	protected int makeChildHeightMeasureSpec()
	{
		int height = 100;
		return MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

	}

	protected int makeChildWidthMeasureSpec()
	{
		int width = 800;
		return MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		System.out.println("changed = " + changed + " l = " + l + " t = " + t + " r = " + r + " b = " + b);
		int childCount = getChildCount();
		System.out.println("childCount = " + childCount);
		for (int i = 0; i < childCount; i++)
		{
			View childView = getChildAt(i);
			// 此处应该计算子view的宽度和高度
			int childMeasureWidth = childView.getMeasuredWidth();
			int childMeasureHeight = childView.getMeasuredHeight();
			int width = childView.getWidth();
			int height = childView.getHeight();
			System.out.println("childMeasureWidth = " + childMeasureWidth + " childMeasureHeight = " + childMeasureHeight + "子View布局前计算的  width = " + width + " height = " + height);
			childView.layout(l, t, childMeasureWidth, childMeasureHeight);
			width = childView.getWidth();
			height = childView.getHeight();
			System.out.println(" 子View布局后计算的  width = " + width + " height = " + height);
		}
		// super.onLayout(changed, l, childCount, r, b);//如果没有调用父方法， 会导致子view没有布局而显示不出来， 除非调用子view的childView.layout(l, t, r, b)方法。
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawARGB(88, 255, 22, 22);
		mCanvasPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
		// canvas.saveLayerAlpha(0, 0, 800, 800, 66, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
		canvas.drawBitmap(mCanvasBitmap, 55, 150, mCanvasPaint);
		drawSomthing(canvas);
	}

	private void drawSomthing(Canvas canvas)
	{
		canvas.save();
		Paint paint = new Paint();
		float top = 60 * getResources().getDisplayMetrics().density;
		float left = 100;
		RectF rect = new RectF(left, top + 500, left + 500, top + 500 + 500);
		paint.setColor(Color.RED);
		// canvas.drawRect(left, top, left + 400, top + 400, paint);
		// paint.setColor(Color.BLUE);
		// canvas.drawRoundRect(rect , 40, 40, paint);

		rect.right = rect.left + 870;
		canvas.drawRect(rect, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(20);
		DashPathEffect pathEffect = new DashPathEffect(new float[] { 4, 30, 8, 40 }, 1);
		paint.setPathEffect(pathEffect);
		canvas.drawLine(left, top+40, left+600, top+40, paint);

		canvas.restore();
	}

	@Override
	protected void dispatchDraw(Canvas canvas)// 在绘制自己之后并且在绘制子View之前调用，
	{
		// canvas.translate(100, 0);
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
			else if (dx < -mTriggerThreshold)
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

// class CustomLinearLayout extends LinearLayout
// {
// private int mTriggerThreshold;
// private static final String TAG = "CustomView";
// private OnFlingListener mOnFlingListener;
// private Scroller mScroller;
// private GestureDetector mGestureDetector;
//
// public CustomLinearLayout(Context context)
// {
// this(context, null);
// }
//
// public CustomLinearLayout(Context context, AttributeSet attrs)
// {
// super(context, attrs);
// setClickable(true);
// setLongClickable(true);
// init();
//
// }
//
// private void init()
// {
// Interpolator interpolator = new BounceInterpolator();
// mScroller = new Scroller(getContext(), interpolator);
//
// mGestureDetector = new GestureDetector(getContext(), new CustomGestureListener());
// mTriggerThreshold = (int) (100 * getResources().getDisplayMetrics().density);
// }
//
// public void setOnFlingListener(OnFlingListener onFlingListener)
// {
// this.mOnFlingListener = onFlingListener;
// }
//
// // 调用此方法滚动到目标位置
// public void smoothScrollTo(int fx, int fy)
// {
// int dx = fx - mScroller.getFinalX();
// int dy = fy - mScroller.getFinalY();
// smoothScrollBy(dx, dy);
// }
//
// // 调用此方法设置滚动的相对偏移
// public void smoothScrollBy(int dx, int dy)
// {
// if (null != mOnFlingListener)
// {
// if (dx > mTriggerThreshold)
// {
// mOnFlingListener.onLeftFling();
// }
// else if(dx < -mTriggerThreshold)
// {
// mOnFlingListener.onRightFling();
// }
// }
// // 设置mScroller的滚动偏移量
// mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
// invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
// }
//
// @Override
// public void computeScroll()
// {
// // 先判断mScroller滚动是否完成
// if (mScroller.computeScrollOffset())
// {
// // 这里调用View的scrollTo()完成实际的滚动
// scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
// // 必须调用该方法，否则不一定能看到滚动效果
// postInvalidate();
// }
// super.computeScroll();
// }
//
// @Override
// public boolean onTouchEvent(MotionEvent event)
// {
// switch (event.getAction())
// {
// case MotionEvent.ACTION_UP:
// Log.i(TAG, " ScorllY = " + getScrollY() + " ScrollX = " + getScrollX());
// smoothScrollTo(0, 0);
// break;
// default:
// return mGestureDetector.onTouchEvent(event);
// }
// return super.onTouchEvent(event);
// }
//
// class CustomGestureListener implements GestureDetector.OnGestureListener
// {
// public boolean onDown(MotionEvent e)
// {
// return true;
// }
//
// public void onShowPress(MotionEvent e)
// {
//
// }
//
// @Override
// public boolean onSingleTapUp(MotionEvent e)
// {
// return false;
// }
//
// @Override
// public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
// {
// int disY = (int) ((distanceY - 0.5) / 2);
// int disX = (int) ((distanceX - 0.5) / 2);
// Log.i(TAG, "disX =" + disX + " disY = " + disY);
// if ((Math.abs(disX) - Math.abs(disY)) >= 0)
// {
// smoothScrollBy(disX, 0);
// }
// else
// {
// smoothScrollBy(0, disY);
// }
//
// return false;
// }
//
// @Override
// public void onLongPress(MotionEvent e)
// {
//
// }
//
// @Override
// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
// {
// return false;
// }
//
// }
// }

interface OnFlingListener
{
	public void onLeftFling();

	public void onRightFling();
}

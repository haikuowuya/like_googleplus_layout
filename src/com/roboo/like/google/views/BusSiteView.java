package com.roboo.like.google.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.widget.LinearLayout;

public class BusSiteView extends LinearLayout
{
	private Paint mCirclePaint;
	private Paint mLinePaint;
	private Paint mTextPaint;
	private float mRadius = 6;// DP
	private float mLineWidth = 1.5f;// DP
	private float mTextSize = 18;// SP;
	private int mViewWidth = 48;// DP;
	private int mTopMargin = mViewWidth * 3 / 2;// DP
	private boolean mIsEnd;
	private boolean mIsStart;
	private String mText;
	private int mPosition;
	private VerticalTextView mVerticalTextView;

	public BusSiteView(Context context)
	{
		this(context, null, false, false);
	}

	public BusSiteView(Context context, String text, boolean isStart,
		boolean isEnd)
	{
		super(context);
		mIsEnd = isEnd;
		mIsStart = isStart;
		mText = text;
		init();
	}

	private void init()
	{
		setWillNotDraw(false);

		mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
			mTextSize, getResources().getDisplayMetrics());
		mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			mRadius, getResources().getDisplayMetrics());
		mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			mLineWidth, getResources().getDisplayMetrics());
		mViewWidth = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, mViewWidth, getResources()
				.getDisplayMetrics());
		mTopMargin = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, mTopMargin, getResources()
				.getDisplayMetrics());
		mCirclePaint = new Paint();
		mCirclePaint.setColor(0xFF4CB649);
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(0xFF00DDFF);
		mLinePaint.setColor(0xFF4CB649);
		mTextPaint.setTextSize(mTextSize);
		mVerticalTextView = new VerticalTextView(getContext());
		LinearLayout.LayoutParams params = new LayoutParams(mViewWidth,
			LayoutParams.MATCH_PARENT);
		params.topMargin = mTopMargin;
		addView(mVerticalTextView, params);
		setBackgroundColor(0xFFDDDDDD);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int halfWidth = getWidth() / 2;
		int startX = halfWidth;
		int startY = mTopMargin / 2;
		int endX = getWidth();
		mVerticalTextView.setPadding(0, startY, 0, 0);
		canvas.drawCircle(startX, startY, mRadius, mCirclePaint);
		drawLine(canvas, startX, startY, endX);
		// drawText(canvas, startY ,halfWidth);
		drawCurrentIndex(canvas, startY, halfWidth);
	}

	private void drawLine(Canvas canvas, int startX, int startY, int endX)
	{
		if (!mIsStart)
		{
			startX = 0;
		}
		if (!mIsStart && mIsEnd)
		{
			endX /= 2;
		}
		canvas.drawRect(startX, startY - mLineWidth / 2, endX, startY
			+ mLineWidth / 2, mLinePaint);
	}

	private void drawText(Canvas canvas, int startY, int halfWidth)
	{
		char[] chars = mText.toCharArray();
		String position = mPosition + "";
		if (mPosition < 10)
		{
			position = " " + position;
		}
		canvas.drawText(position, halfWidth - mTextSize / 2, startY + mTextSize
			/ 2 + mRadius * 2, mTextPaint);
		for (int i = 0; i < chars.length; i++)
		{
			canvas.drawText(chars[i] + "", halfWidth - mTextSize / 2,
				getHeight() / 7 + mTextSize * 2 + mRadius * 2 + mTextSize * i,
				mTextPaint);
		}
	}

	private void drawCurrentIndex(Canvas canvas, int startY, int halfWidth)
	{
		String position = mPosition + "";
		if (mPosition < 10)
		{
			position = " " + position;
		}
		canvas.drawText(position, halfWidth - mTextSize / 2, startY + mTextSize
			 + mRadius * 2, mTextPaint);

	}

	public String getText()
	{
		return mText;
	}

	public void setText(String text)
	{
		mText = text;
		mVerticalTextView.setText(mText);
	}

	public void setPosition(int position)
	{
		mPosition = position;
	}

	public void setIsEnd(boolean isEnd)
	{
		this.mIsEnd = isEnd;
	}

	public void setIsStart(boolean isStart)
	{
		this.mIsStart = isStart;
	}

	public void setTextPaintColor(int color)
	{
		this.mTextPaint.setColor(color);
	}

	public VerticalTextView getVerticalTextView()
	{
		return mVerticalTextView;
	}

	
	
}

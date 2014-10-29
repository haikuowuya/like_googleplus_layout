package com.roboo.like.google.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class VerticalTextView extends TextView
{
	private Rect text_bounds = new Rect();
	private Paint mTextPaint;
	private float mTextSize = 18;// SP;

	// private int mViewWidth = 48;//DP;

	public VerticalTextView(Context context)
	{
		this(context, null);
	}

	public VerticalTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
			mTextSize, getResources().getDisplayMetrics());
		// mViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		// mViewWidth, getResources().getDisplayMetrics());
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(0xFF00DDFF);
		mTextPaint.setTextSize(mTextSize);
//		setBackgroundColor(0xFF222222);
	}

	private int measureWidth(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY)
		{
			result = specSize;
		}
		else
		{
			result = text_bounds.height() + getPaddingTop()
				+ getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY)
		{
			result = specSize;
		}
		else
		{
			result = text_bounds.width() + getPaddingLeft() + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		drawText(canvas);
		canvas.restore();
	}

	private void drawText(Canvas canvas)
	{
		char[] chars = getText().toString().toCharArray();

		for (int i = 0; i < chars.length; i++)
		{
			canvas.drawText(chars[i] + "", getWidth() / 2 - mTextSize / 2,
				mTextSize * (1 + i), mTextPaint);
		}
	}
	@Override
	public void setTextColor(int color)
	{
		mTextPaint.setColor(color);
		super.setTextColor(color);
	}
}
package com.roboo.like.google.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.roboo.like.google.R;
import com.roboo.like.google.infinite.InfiniteViewPager;

public class HeaderView extends LinearLayout//CustomLinearLayout
{
	private InfiniteViewPager mInfiniteViewPager;
	private CirclePageIndicator mIndicator;

	public HeaderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public HeaderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public HeaderView(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		View view = inflate(context, R.layout.listview_header_view, null);
		mInfiniteViewPager = (InfiniteViewPager) view.findViewById(R.id.vp_viewpager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.cpi_indicator);
		float scaleFactor = (float) (context.getResources().getDisplayMetrics().widthPixels / 640.0);
		int height = (int) (320 * scaleFactor);
		android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		addView(view, params);
	}

	public InfiniteViewPager getViewPager()
	{
		return mInfiniteViewPager;
	}

	public CirclePageIndicator getIndicator()
	{
		return mIndicator;
	}
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		boolean isHandle = super.onInterceptTouchEvent(event);
		if (isHandle)
		{
			getParent().requestDisallowInterceptTouchEvent(true);
		}
//		 return false;
		return true;//满足下拉刷新
	}
}

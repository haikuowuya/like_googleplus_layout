package com.roboo.like.google.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.roboo.like.google.R;
import com.roboo.like.google.infinite.InfiniteViewPager;

public class HeaderView extends LinearLayout
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
		float scaleFactor =  (float) (context.getResources().getDisplayMetrics().widthPixels/640.0);
		int height = (int) (320*scaleFactor);
		android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);;
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

	private void test(Context context)
	{
		ImageView imageView = new ImageView(context);
		int height = 160;
		android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.ic_test);
		imageView.setLayoutParams(params);
		addView(imageView);
	}

}

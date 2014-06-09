package com.roboo.like.google.views;

import com.roboo.like.google.R;
import com.roboo.like.google.infinite.InfiniteViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class HeaderView extends LinearLayout
{
	private InfiniteViewPager mInfiniteViewPager;
	public HeaderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public HeaderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public  HeaderView(Context context)
	{
		super(context);
		InfiniteViewPager viewPager = new InfiniteViewPager(getContext());
		viewPager.setId(R.id.vp_viewpager);
		int height =160;
		android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,height );;
		viewPager.setBackgroundResource(R.drawable.ic_launcher);
		addView(viewPager, params );
		
	}

	
	public InfiniteViewPager getViewPager()
	{
		return mInfiniteViewPager;
	}

	private void test(Context context)
	{
		ImageView imageView = new ImageView(context);
		int height =160;
		android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,height );
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.ic_test);
		imageView.setLayoutParams(params);
		addView(imageView);
	}
	 
	 

}

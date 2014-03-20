package com.roboo.like.google.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.roboo.like.google.adapters.InfinitePagerAdapter;

public class InfiniteViewPager extends ViewPager
{  
	
	
	public InfiniteViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public InfiniteViewPager(Context context)
	{
		super(context);
	}

	public void setAdapter(PagerAdapter adapter)
	{
		super.setAdapter(adapter);
	}

	@Override
	public void setCurrentItem(int item)
	{
		if (getAdapter() instanceof InfinitePagerAdapter)
		{
			InfinitePagerAdapter adapter = (InfinitePagerAdapter) getAdapter();
			item = item % adapter.getRealCount()+100*adapter.getRealCount();
		}
		super.setCurrentItem(item);
	}

}

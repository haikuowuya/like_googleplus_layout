package com.roboo.like.google.adapters;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class InfinitePagerAdapter extends PagerAdapter
{
	private PagerAdapter adapter;
	public InfinitePagerAdapter(PagerAdapter adapter)
	{
		this.adapter = adapter;
	}

	public int getCount()
	{
		return Integer.MAX_VALUE;
	}

	public int getRealCount()
	{
		return adapter.getCount();
	}
	public Object instantiateItem(ViewGroup container, int position)
	{
		int virtualPosition = position % getRealCount();
		return adapter.instantiateItem(container, virtualPosition);
	}
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		int virtualPosition = position % getRealCount();
		adapter.destroyItem(container, virtualPosition, object);
	}

	public void finishUpdate(ViewGroup container)
	{
		adapter.finishUpdate(container);
	}

	public boolean isViewFromObject(View view, Object object)
	{
		return adapter.isViewFromObject(view, object);
	}

	public void restoreState(Parcelable bundle, ClassLoader classLoader)
	{
		adapter.restoreState(bundle, classLoader);
	}

	public Parcelable saveState()
	{
		return adapter.saveState();
	}

	public void startUpdate(ViewGroup container)
	{
		adapter.startUpdate(container);
	}
}

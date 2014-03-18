package com.roboo.like.google.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.roboo.like.google.fragments.ImageFragment;

public class ImageFragmentAdapter extends FragmentPagerAdapter
{

	public ImageFragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int position)
	{
		return ImageFragment.newInstance();
	}

	@Override
	public int getCount()
	{
		return 4;
	}

}

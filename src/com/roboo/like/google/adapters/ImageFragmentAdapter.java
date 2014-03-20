package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.roboo.like.google.fragments.ImageFragment;

public class ImageFragmentAdapter extends FragmentPagerAdapter
{
	private LinkedList<Object> data;
	public ImageFragmentAdapter(FragmentManager fm, LinkedList<Object> data)
	{
		super(fm);
		this.data = data;
	}
 
	public Fragment getItem(int position)
	{
		int virtualPostion = position %data.size();
		return ImageFragment.newInstance(data.get(virtualPostion));
	}
 
	public int getCount()
	{
		return 100; 
	}
	public int getReadlCount()
	{
		return data.size();
	}

}

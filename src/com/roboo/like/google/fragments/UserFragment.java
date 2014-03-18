package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roboo.like.google.R;
import com.roboo.like.google.views.CirclePageIndicator;
import com.roboo.like.google.views.StickyGridHeadersGridView;

public class UserFragment extends BaseFragment  
{
	 private ViewPager mViewPager;
	 private CirclePageIndicator mIndicator;

	public static UserFragment newInstance()
	{
		UserFragment fragment = new UserFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_user, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_pager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.cpi_indicator);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
 
	}

 
}

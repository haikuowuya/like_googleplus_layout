package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.ImageFragmentAdapter;
import com.roboo.like.google.views.CirclePageIndicator;

public class UserFragment extends BaseFragment
{
	private ViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private PagerAdapter mAdapter;
	private int mPosition = 0;
	private ImageView mImageView;
	private final Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mImageView.getDrawable().setLevel(msg.what%6);
		};
	};
	private Runnable mSwapRunnable = new Runnable()
	{
		public void run()
		{
			mViewPager.setCurrentItem(mPosition % mViewPager.getAdapter().getCount(), true);
			mPosition++;
			mHandler.postDelayed(mSwapRunnable, 1000L);
			mHandler.sendEmptyMessage(mPosition);
		}
	};

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
		mImageView = (ImageView) view.findViewById(R.id.iv_image);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.cpi_indicator);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		LinkedList<Object> data = new LinkedList<Object>();
		data.add(new Object());
		data.add(new Object());
		data.add(new Object());
		data.add(new Object());
		mAdapter = new ImageFragmentAdapter(getFragmentManager(), data);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager);
		setListener();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mHandler.postDelayed(mSwapRunnable, 1000L);
	}

	@Override
	public void onDetach()
	{
		mHandler.removeCallbacks(mSwapRunnable);
		super.onDetach();
	}

	private void setListener()
	{
		mViewPager.setOnPageChangeListener(new OnPageChangeListenerImpl());
	}

	private class OnPageChangeListenerImpl implements OnPageChangeListener
	{

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{}

		public void onPageSelected(int position)
		{
			mPosition = position;
			mIndicator.setCurrentItem(mPosition % 4);
		}

		public void onPageScrollStateChanged(int state)
		{}

	}

}

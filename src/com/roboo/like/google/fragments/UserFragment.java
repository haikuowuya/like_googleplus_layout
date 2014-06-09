package com.roboo.like.google.fragments;

import java.util.LinkedList;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.ImageFragmentAdapter;
import com.roboo.like.google.progressbutton.ProcessButton;
import com.roboo.like.google.progressbutton.ProgressGenerator;
import com.roboo.like.google.progressbutton.ProgressGenerator.OnCompleteListener;
import com.roboo.like.google.views.CirclePageIndicator;
import com.roboo.like.google.views.NumberProgressBar;

public class UserFragment extends BaseFragment
{
	private ViewPager mViewPager;
	private NumberProgressBar mNumberProgressBar;
	private ProcessButton mProcessButton;
	private CirclePageIndicator mIndicator;
	private PagerAdapter mAdapter;
	private int mPosition = 0;
	private int mProgress;
	private ImageView mImageView;
	private final Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mImageView.getDrawable().setLevel(msg.what % 6);
			if (mNumberProgressBar.getProgress() >= 100)
			{
				mNumberProgressBar.setProgress(1);
			}
			mNumberProgressBar.incrementProgressBy(msg.what % 6);
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
		View view = inflater.inflate(R.layout.fragment_user, null);// TODO
		mViewPager = (ViewPager) view.findViewById(R.id.vp_viewpager);
		mImageView = (ImageView) view.findViewById(R.id.iv_image);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.cpi_indicator);
		mNumberProgressBar = (NumberProgressBar) view.findViewById(R.id.npb_progress);
		mProcessButton = (ProcessButton) view.findViewById(R.id.btnSend);
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
		mProcessButton.setOnClickListener(getOnClickListener());
	}

	private OnClickListener getOnClickListener()
	{
		return new OnClickListener()
		{
			public void onClick(View v)
			{
				new ProgressGenerator(getOnCompleteListener()).start(mProcessButton);
			}
		};
	}

	private OnCompleteListener<Object> getOnCompleteListener()
	{
		return new OnCompleteListener<Object>()
		{
			public Object onComplete()
			{
				mProgress = 0;
				Toast.makeText(getActivity(), "KKK", Toast.LENGTH_LONG).show();
				return null;
			}
			@Override
			public Object doInBackgroundProcess()
			{
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						mProgress += 10;
						mProcessButton.setProgress(mProgress);
						if (mProgress < 100)
						{
							handler.postDelayed(this, generateDelay());
						}
						else
						{
							onComplete();
						}
					}
				}, generateDelay());
				return null;
			}
		 
		private int generateDelay()
		{
			return new Random().nextInt(1000);
		}
		};
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

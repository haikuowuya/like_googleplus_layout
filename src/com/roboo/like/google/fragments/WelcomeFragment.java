package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.roboo.like.google.R;
import com.roboo.like.google.StartActivity;
import com.roboo.like.google.infinite.FixedSpeedScroller;
import com.roboo.like.google.infinite.ViewPagerEx;
import com.roboo.like.google.infinite.ViewPagerEx.OnPageChangeListener;
import com.roboo.like.google.infinite.ViewPagerEx.PageTransformer;
import com.roboo.like.google.views.CirclePageIndicator;

public class WelcomeFragment extends BaseFragment
{
	private static final int MAX_COUNT = 4;
	private static final int[] IMAGES = new int[] { R.drawable.ic_image, R.drawable.ic_image1, R.drawable.ic_image2, R.drawable.ic_image3 };
	private ViewPagerEx mViewPager;

	private CirclePageIndicator mIndicator;

	public static WelcomeFragment newInstance()
	{
		WelcomeFragment fragment = new WelcomeFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_welcome, null);// TODO
		initView(view);
		return view;
	}

	private void initView(View view)
	{
		mViewPager = (ViewPagerEx) view.findViewById(R.id.vp_viewpager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.cpi_indicator);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

		super.onActivityCreated(savedInstanceState);
		mViewPager.setAdapter(new SubPagerAdapter());
		mIndicator.setViewPager(mViewPager);
		mViewPager.setOnPageChangeListener(getOnPageChangeListener());
		FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(getActivity(), new BounceInterpolator());
		mViewPager.setFixedScroller(fixedSpeedScroller);
		mViewPager.setPageTransformer(true, getTransformer());
	}

	private OnPageChangeListener getOnPageChangeListener()
	{
		return new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				mIndicator.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		};
	}

	class SubPagerAdapter extends PagerAdapter
	{

		public int getCount()
		{
			return MAX_COUNT;
		}

		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = new ImageView(getActivity());
			RelativeLayout relativeLayout = new RelativeLayout(getActivity());
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(IMAGES[position]);
			relativeLayout.addView(imageView, relativeParams);
			if (position > 2)
			{
				Button button = new Button(getActivity());
				button.setTextColor(getResources().getColor(R.color.sky_blue_color));
				button.setText("开启浏览");
				button.setGravity(Gravity.CENTER);
				int buttonHeight = (int) (48 * getActivity().getResources().getDisplayMetrics().density);
				int buttonWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels - getActivity().getResources().getDisplayMetrics().density * 100);
				relativeParams = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
				relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

				relativeParams.bottomMargin = buttonHeight/2;
				relativeLayout.addView(button, relativeParams);
				button.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						StartActivity activity = (StartActivity) getActivity();
						activity.splashScreenEnd();
					}
				});
			}
			container.addView(relativeLayout, params);
			return relativeLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

	}

	private PageTransformer getTransformer()
	{
		PageTransformer pageTransformer = new PageTransformer()
		{
			public void transformPage(View page, float position)
			{
				ViewHelper.setTranslationX(page, position < 0 ? 0f : -page.getWidth() * position);
			}
		};

		return pageTransformer;
	}
}

package com.roboo.like.google;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.MenuItem;

import com.roboo.like.google.adapters.ImagePagerAdapter;
import com.roboo.like.google.infinite.InfinitePagerAdapter;
import com.roboo.like.google.infinite.InfiniteViewPager;
import com.roboo.like.google.infinite.ViewPagerEx.OnPageChangeListener;
import com.roboo.like.google.views.CirclePageIndicator;

/** 图片详情界面 */
public class PictureDetailActivity extends BaseActivity
{
	/** 是否自动切换图片 */
	private static final boolean IS_AUTO_SWITCH_PIC = false;
	private int mCurrentPosition = 0;
	private String mImagePath;
	private InfiniteViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private static final String EXTRA_IMAGE_PATH = "image_path";
	private static final String EXTRA_IMAGE_LIST = "image_list";
	private static final String EXTRA_IMAGE_POSITION = "image_postion";
	private ArrayList<String> mImageUrls = new ArrayList<String>();
	private Handler mHandler = new Handler();
	private Runnable mSwapRunnable = new Runnable()
	{
		public void run()
		{
			mCurrentPosition = (mCurrentPosition + 1) % mImageUrls.size();
			mViewPager.setCurrentItem(mCurrentPosition);
			mIndicator.setCurrentItem(mCurrentPosition);
			mHandler.postDelayed(mSwapRunnable, 2000L);
		}
	};

	/** 跳转到图片详情界面 */
	public static void actionPictureDetail(Activity activity, String imagePath)
	{
		Intent intent = new Intent(activity, PictureDetailActivity.class);
		intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
		activity.startActivity(intent);
	}

	/** 跳转到图片详情界面 */
	public static void actionPictureDetail(Activity activity, ArrayList<String> imageUrls, int position)
	{
		Intent intent = new Intent(activity, PictureDetailActivity.class);
		intent.putStringArrayListExtra(EXTRA_IMAGE_LIST, imageUrls);
		intent.putExtra(EXTRA_IMAGE_POSITION, position);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_detail);// TODO
		initView();
		customActionBar();
		mCurrentPosition = getIntent().getIntExtra(EXTRA_IMAGE_POSITION, 0);
		mImagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
		if (!TextUtils.isEmpty(mImagePath))
		{
			mImageUrls.add(mImagePath);
		}
		else
		{
			mImageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_LIST);
		}
		mViewPager.setAdapter(getPagerAdapter());
		mIndicator.setViewPager(mViewPager);
		mIndicator.setCurrentItem(mCurrentPosition);
		mViewPager.setCurrentItem(mCurrentPosition);
		mViewPager.setOnPageChangeListener(new OnPageChangeListenerImpl());
	}

	private PagerAdapter getPagerAdapter()
	{
	return	new ImagePagerAdapter(this, mImageUrls);
//		return new InfinitePagerAdapter(new ImagePagerAdapter(this, mImageUrls));
	}

	protected void onPause()
	{
		super.onPause();
		mHandler.removeCallbacks(mSwapRunnable);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mImageUrls.size() > 0)
		{
			if (IS_AUTO_SWITCH_PIC)
			{
				mHandler.postDelayed(mSwapRunnable, 2000L);
			}
		}
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initView()
	{
		mViewPager = (InfiniteViewPager) findViewById(R.id.vp_viewpager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.cpi_indicator);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("照片详情");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

	private class OnPageChangeListenerImpl implements OnPageChangeListener
	{
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{}

		public void onPageSelected(int position)
		{
			mCurrentPosition = position;
			mIndicator.setCurrentItem(position);

		}

		public void onPageScrollStateChanged(int state)
		{}

	}
}

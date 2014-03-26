package com.roboo.like.google.fragments;

import java.util.LinkedList;
import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.roboo.like.google.BaseActivity;
import com.roboo.like.google.PictureDetailActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.async.NewsContentAsyncTaskLoader;
import com.roboo.like.google.models.NewsItem;

public class NewsFragment extends BaseFragment implements LoaderCallbacks<LinkedList<String>>
{
	private static final String ARG_NEWS = "news";
	private static final int[] COLORS_COLLECTION = new int[] { R.color.red_color, R.color.sky_blue_color, R.color.hotpink_color, R.color.lightseagreen_color, R.color.orangered_color, R.color.turquoise_color };
	private NewsItem mItem;
	/** 显示获取新闻内容进度条 */
	private ProgressBar mProgressBar;
	/** 线性布局用于存放新闻内容 */
	private LinearLayout mLinearContainer;
	/** 显示新闻标题 */
	private TextView mTvTitle;
	private Random mRandom = new Random();
	/** 异步图片加载器 */
	private ImageLoader mImageLoader;
	/**ViewGroup中添加View时的动画*/
	private LayoutTransition mTransitioner;
	public static NewsFragment newInstance(NewsItem item)
	{
		NewsFragment fragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_NEWS, item);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_news, null);
		mLinearContainer = (LinearLayout) view.findViewById(R.id.linear_container);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title);
		 
		 Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "custom.ttf");
		addProgressBar();
		mTvTitle.setTypeface(typeface);
		return view;
	}

	private void addProgressBar()
	{
		mProgressBar = new ProgressBar(getActivity());
		FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(Window.ID_ANDROID_CONTENT);

		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		frameLayout.addView(mProgressBar, params);
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		setListener();
		getActivity().getSupportLoaderManager().initLoader(0, getArguments(), this);
	}

	private void setListener()
	{

	}

	public Loader<LinkedList<String>> onCreateLoader(int id, Bundle args)
	{
		mItem = (NewsItem) args.getSerializable(ARG_NEWS);
		return new NewsContentAsyncTaskLoader(getActivity(), mItem.getUrl());
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<String>> loader, LinkedList<String> data)
	{
		if (null != data)
		{
			setLinearContainerAnimation();
			int ltrb = (int) (10 * getActivity().getResources().getDisplayMetrics().density);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.bottomMargin = ltrb;
			for (String str : data)
			{
				if (str.startsWith(BaseActivity.PREFIX_IMG_URL))
				{
					ImageView imageView = new ImageView(getActivity());
					imageView.setLayoutParams(params);
					imageView.setBackgroundResource(R.drawable.list_item_selector);
					DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image)
							.showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
					mImageLoader.displayImage(str, imageView, options);
					imageView.setPadding(ltrb, ltrb, ltrb, ltrb);
					mLinearContainer.addView(imageView);
					imageView.setOnClickListener(new OnClickListenerImpl(str));
				}
				else
				{
					params.leftMargin = ltrb;
					params.rightMargin = ltrb;
					TextView textView = new TextView(getActivity());
					textView.setText(str);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					textView.setLayoutParams(params);
					textView.setPadding(ltrb, ltrb, ltrb, ltrb);
					textView.setBackgroundResource(R.drawable.list_item_selector);
					mLinearContainer.addView(textView);
				}
			}
		}
		int nextIndex = mRandom.nextInt(COLORS_COLLECTION.length);
		mTvTitle.setBackgroundColor(getResources().getColor(COLORS_COLLECTION[nextIndex]));
		mTvTitle.setText(mItem.getTitle());
		mTvTitle.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<LinkedList<String>> loader)
	{

	}

	private class OnClickListenerImpl implements OnClickListener
	{

		private String imagePath;

		public OnClickListenerImpl(String imagePath)
		{
			this.imagePath = imagePath;
		}

		@Override
		public void onClick(View v)
		{
			PictureDetailActivity.actionPictureDetail(getActivity(), imagePath);
		}

	}
	private void setLinearContainerAnimation()
	{
		mTransitioner = new LayoutTransition();
 
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 100);
        mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 100);
        setupCustomAnimations();
		mLinearContainer.setLayoutTransition(mTransitioner);

	}
	/**定制ViewGroup添加View时显示的动画*/
	private void setupCustomAnimations()
	{
		// Changing while Adding
		PropertyValuesHolder pvhLeft =
			PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop =
			PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight =
			PropertyValuesHolder.ofInt("right", 0, 1);
		PropertyValuesHolder pvhBottom =
			PropertyValuesHolder.ofInt("bottom", 0, 1);
		PropertyValuesHolder pvhScaleX =
			PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY =
			PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
			this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).
			setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation =
			PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(
			this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).
			setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
		changeOut.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).
			setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).
			setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}

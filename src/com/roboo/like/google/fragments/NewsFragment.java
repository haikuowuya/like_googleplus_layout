package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
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

	private NewsItem mItem;
	/** 显示获取新闻内容进度条 */
	private ProgressBar mProgressBar;
	/** 线性布局用于存放新闻内容 */
	private LinearLayout mLinearContainer;
	private ImageLoader mImageLoader;

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
		addProgressBar();
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
			int ltrb = (int) (10*getActivity().getResources().getDisplayMetrics().density);
			LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);				 
			params.bottomMargin = ltrb;
			for (String str : data)
			{
				if (str.startsWith(BaseActivity.PREFIX_IMG_URL))
				{
					ImageView imageView = new ImageView(getActivity());
					imageView.setLayoutParams(params);
					imageView.setBackgroundResource(R.drawable.list_item_selector);
					DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory()
							.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
					mImageLoader.displayImage(str, imageView, options);
					mLinearContainer.addView(imageView);
					imageView.setOnClickListener(new OnClickListenerImpl(str));
				}
				else
				{
					TextView textView = new TextView(getActivity());
					textView.setText(str);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					textView.setLayoutParams(params);
					textView.setPadding(ltrb, ltrb, ltrb, ltrb);
					textView.setBackgroundResource(R.drawable.list_item_default);
					mLinearContainer.addView(textView);
				}
			}
		}
		mProgressBar.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<LinkedList<String>> loader)
	{

	}
	private class OnClickListenerImpl implements OnClickListener
	{

		private String imagePath ;
		
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

}

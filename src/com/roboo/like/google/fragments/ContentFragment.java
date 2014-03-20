package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.roboo.like.google.LocationActivity;
import com.roboo.like.google.MoodActivity;
import com.roboo.like.google.PictureActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.NewsListViewAdapter;
import com.roboo.like.google.async.NewsAsyncTaskLoader;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.views.helper.PoppyListViewHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper.OnRefreshListener;

public class ContentFragment extends BaseFragment implements LoaderCallbacks<LinkedList<NewsItem>>
{
	/**
	 * IT之家之ANDROID之家
	 */
	public static final String IT_ANDROID = "http://it.ithome.com/category/10_";
	private static final String ARG_CURRENT_PAGENO ="current_pageno";
	private static final String ARG_NEWS_URL="new_url";
	/** 获取的是当前第几页的新闻数据 */
	private int mCurrentPageNo = 1;
	private ListView mListView;
	private PoppyListViewHelper mPoppyListViewHelper;
	private PullToRefreshHelper mPullToRefreshAttacher;
	private View mPoppyView;
	private Button mBtnPicture;
	private Button mBtnLocation;
	private Button mBtnMood;
	private Button mBtnText;
	private NewsListViewAdapter mAdapter;
	
	public static ContentFragment newInstance()
	{
		ContentFragment fragment = new ContentFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_content, null);
		mListView = (ListView) view.findViewById(R.id.lv_list);
		mPoppyListViewHelper = new PoppyListViewHelper(getActivity());
		mPullToRefreshAttacher = PullToRefreshHelper.get(getActivity());
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		 
		mPoppyView = mPoppyListViewHelper.createPoppyViewOnListView(R.id.lv_list, R.layout.poppyview);
		mBtnPicture = (Button) mPoppyView.findViewById(R.id.btn_picture);
		mBtnLocation = (Button) mPoppyView.findViewById(R.id.btn_location);
		mBtnMood = (Button) mPoppyView.findViewById(R.id.btn_mood);
		mBtnText = (Button) mPoppyView.findViewById(R.id.btn_text);
		mPullToRefreshAttacher.addRefreshableView(mListView, new OnRefreshListener()
		{
			public void onRefreshStarted(View view)
			{
				Bundle bundle = new Bundle();
				bundle.putString(ARG_NEWS_URL, IT_ANDROID);
				bundle.putInt(ARG_CURRENT_PAGENO, mCurrentPageNo++);
				getActivity().getSupportLoaderManager().restartLoader(0, bundle, ContentFragment.this);
				
			}
		});
		Bundle bundle = new Bundle();
		bundle.putString(ARG_NEWS_URL, IT_ANDROID);
		bundle.putInt(ARG_CURRENT_PAGENO, mCurrentPageNo);
		getActivity().getSupportLoaderManager().initLoader(0, bundle, this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setListener();
	}

	private void setListener()
	{
		mListView.setOnItemClickListener(new OnListItemClickListenerImpl());
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		mBtnLocation.setOnClickListener(onClickListenerImpl);
		mBtnMood.setOnClickListener(onClickListenerImpl);
		mBtnPicture.setOnClickListener(onClickListenerImpl);
		mBtnText.setOnClickListener(onClickListenerImpl);
	}

	 
	private class OnListItemClickListenerImpl implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
		}

	}

	private class OnClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btn_picture:// 图片
				picture();
				break;
			case R.id.btn_location:// 位置
				location();
				break;
			case R.id.btn_mood:// 心情
				mood();
				break;
			case R.id.btn_text:// 文字
				text();
				break;
			}
		}

		/** 图片 */
		public void picture()
		{
			PictureActivity.actionPicture(getActivity());
		}

		/** 位置 */
		public void location()
		{
			LocationActivity.actionLocation(getActivity());
		}

		/** 心情 */
		public void mood()
		{
			MoodActivity.actionMood(getActivity());
		}

		/** 文字 */
		public void text()
		{

		}
	}

	@Override
	public Loader<LinkedList<NewsItem>> onCreateLoader(int id, Bundle args)
	{
		System.out.println("args.int = " + args.getInt(ARG_CURRENT_PAGENO, 1));
		return new NewsAsyncTaskLoader(getActivity(), args.getString(ARG_NEWS_URL), args.getInt(ARG_CURRENT_PAGENO, 1));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<NewsItem>> loader, LinkedList<NewsItem> data)
	{
		if (null != data)
		{
			mAdapter = new NewsListViewAdapter(getActivity(), data);
			mListView.setAdapter(mAdapter);
		}
		mPullToRefreshAttacher.setRefreshComplete();
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<NewsItem>> loader)
	{

	}
}

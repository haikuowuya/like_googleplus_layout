package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.LocationActivity;
import com.roboo.like.google.MoodActivity;
import com.roboo.like.google.NewsActivity;
import com.roboo.like.google.PictureActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.NewsListViewAdapter;
import com.roboo.like.google.async.NewsAsyncTaskLoader;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.CardToastUtils;
import com.roboo.like.google.views.helper.PoppyListViewHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper.OnRefreshListener;

public class MainFragment extends BaseFragment implements LoaderCallbacks<LinkedList<NewsItem>>
{

	/** Bundle当前加载数据的页数Key */
	private static final String ARG_CURRENT_PAGENO = "current_pageno";
	/** Bundle当前获取新闻URL */
	private static final String ARG_NEWS_URL = "news_url";
	/** 获取的是当前第几页的新闻数据 */
	private int mCurrentPageNo = 1;
	/** ListView */
	private ListView mListView;
	/** 当ListView向上滚动时会出现的View的辅助类 */
	private PoppyListViewHelper mPoppyListViewHelper;
	/** ActionBar下拉刷新的辅助类 */
	private PullToRefreshHelper mPullToRefreshAttacher;
	/** 当ListView向上滚动时会出现的View */
	private View mPoppyView;
	/** 图片 */
	private Button mBtnPicture;
	/** 我的位置 */
	private Button mBtnLocation;
	/** 心情 */
	private Button mBtnMood;
	/** 文字 */
	private Button mBtnText;
	/** 新闻列表适配器 */
	private NewsListViewAdapter mAdapter;
	/** ListView最后一列是否可见的标志 */
	public boolean mLastItemVisible;
	/** ListView 的 FooterView */
	private View mFooterView;
	/** 新闻列表适配器的数据源 */
	private LinkedList<NewsItem> mData;
	/** 当点击FooterView时显示加载数据标识 */
	private ProgressBar mProgressBar;
	/** 正在加载数据中…… */
	private TextView mTvText;

	/** 创建一个 ContentFragment 实例 */
	public static MainFragment newInstance(String newsUrl)
	{
		MainFragment fragment = new MainFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_NEWS_URL, newsUrl);
		fragment.setArguments(bundle);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_content, null);
		mFooterView = inflater.inflate(R.layout.listview_footer_view, null);
		mProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_progress);
		mTvText = (TextView) mFooterView.findViewById(R.id.tv_load_next);
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
				loadFirstData();
			}

		});
		loadFirstData();
	}

	private void loadFirstData()
	{
		Bundle bundle = getArguments();
		mCurrentPageNo = 1;
		bundle.putInt(ARG_CURRENT_PAGENO, mCurrentPageNo);
		mPullToRefreshAttacher.setRefreshing(true);
		getActivity().getSupportLoaderManager().restartLoader(0, bundle, MainFragment.this);
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
		// mListView.setOnScrollListener(new OnScrollListenerImpl());
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		mBtnLocation.setOnClickListener(onClickListenerImpl);
		mBtnMood.setOnClickListener(onClickListenerImpl);
		mBtnPicture.setOnClickListener(onClickListenerImpl);
		mBtnText.setOnClickListener(onClickListenerImpl);
	}

	private class OnScrollListenerImpl implements OnScrollListener
	{
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisible)
			{
				loadNextData();
			}

		}

		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
		}
	}

	private class OnListItemClickListenerImpl implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (parent.getAdapter().getItemViewType(position) == AbsListView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER)
			{
				loadNextData();
			}
			else
			{
				NewsActivity.actionNews(getActivity(), (NewsItem) parent.getAdapter().getItem(position));
			}
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

	/** initLoader/reStartLoader方法被调用时会执行onCreateLoader */
	public Loader<LinkedList<NewsItem>> onCreateLoader(int id, Bundle args)
	{
		GoogleApplication.TEST = false;
		if(GoogleApplication.TEST )
		{
			System.out.println("当前加载的是第   " + args.getInt(ARG_CURRENT_PAGENO, 1)+" 页数据");
		}
		mProgressBar.setVisibility(View.VISIBLE);
		mTvText.setText("正在加载数据中……");
		return new NewsAsyncTaskLoader(getActivity(), args.getString(ARG_NEWS_URL), args.getInt(ARG_CURRENT_PAGENO, 1));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<NewsItem>> loader, LinkedList<NewsItem> data)
	{
		if (data != null)
		{
			if (null == mData)
			{
				mData = data;
				mAdapter = new NewsListViewAdapter(getActivity(), mData);
				mListView.addFooterView(mFooterView);
				mListView.setAdapter(mAdapter);
			}
			else
			{
				mData.addAll(data);
				mAdapter.notifyDataSetChanged();
			}
			for (NewsItem item : data)
			{
				GoogleApplication.TEST = false;
				if (GoogleApplication.TEST)
				{
					System.out.println("Item = 【 " + item + " 】 ");
				}
			}

			String messageText = "加载  " + data.size() + " 条新数据";
			if (mCurrentPageNo == 1)
			{
				messageText = " 更新  " + data.size() + " 条新数据";
			}
			new CardToastUtils(getActivity()).showAndAutoDismiss(messageText);
			mProgressBar.setVisibility(View.INVISIBLE);
			mTvText.setText("点击加载下一页");
		}
		mPullToRefreshAttacher.setRefreshComplete();
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<NewsItem>> loader)
	{

	}

	private void loadNextData()
	{
		Bundle bundle = getArguments();
		bundle.putInt(ARG_CURRENT_PAGENO, ++mCurrentPageNo);
		getActivity().getSupportLoaderManager().restartLoader(0, bundle, this);

	}
}

package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.LocationActivity;
import com.roboo.like.google.MoodActivity;
import com.roboo.like.google.NewsActivity;
import com.roboo.like.google.PictureActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.TextActivity;
import com.roboo.like.google.adapters.NewsListViewAdapter;
import com.roboo.like.google.async.NewsListAsyncTaskLoader;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.CardToastUtils;
import com.roboo.like.google.utils.NetWorkUtils;
import com.roboo.like.google.views.helper.PoppyListViewHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper.DefaultHeaderTransformer;
import com.roboo.like.google.views.helper.PullToRefreshHelper.OnRefreshListener;

public class MainFragment extends BaseFragment implements LoaderCallbacks<LinkedList<NewsItem>>
{

	/** Bundle当前加载数据的页数Key */
	private static final String ARG_CURRENT_PAGENO = "current_pageno";
	/** Bundle当前获取新闻URL */
	private static final String ARG_NEWS_URL = "news_url";
	/** 获取的是当前第几页的新闻数据 */
	private int mCurrentPageNo = 1;
	private int mStubCurrentPageNo = mCurrentPageNo;
	 
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
	private Button mBtnLoadNext;

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
		View view = inflater.inflate(R.layout.fragment_content, null);//TODO
		mFooterView = inflater.inflate(R.layout.listview_footer_view, null);
		mProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_progress);
		mBtnLoadNext = (Button) mFooterView.findViewById(R.id.btn_load_next);
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
		if (!NetWorkUtils.isNetworkAvailable(getActivity()))
		{
			DefaultHeaderTransformer transformer = (DefaultHeaderTransformer) mPullToRefreshAttacher.getHeaderTransformer();
			transformer.setRefreshingText("正在获取离线数据");
		}
		Bundle bundle = getArguments();
		mCurrentPageNo = 1;
		bundle.putInt(ARG_CURRENT_PAGENO, mCurrentPageNo);
		mPullToRefreshAttacher.setRefreshing(true);
		getActivity().getSupportLoaderManager().restartLoader(0, bundle, MainFragment.this);
	}
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}
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
			case R.id.btn_load_next://加载下一页
				loadNextData();
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
			TextActivity.actionText(getActivity());
		}
	}

	/** initLoader/reStartLoader方法被调用时会执行onCreateLoader */
	public Loader<LinkedList<NewsItem>> onCreateLoader(int id, Bundle args)
	{
		GoogleApplication.TEST = false;
		if (GoogleApplication.TEST)
		{
			System.out.println("当前加载的是第   " + args.getInt(ARG_CURRENT_PAGENO, 1) + " 页数据");
		}
		mProgressBar.setVisibility(View.VISIBLE);
		mBtnLoadNext.setText("正在加载数据中……");
		return new NewsListAsyncTaskLoader(getActivity(), args.getString(ARG_NEWS_URL), args.getInt(ARG_CURRENT_PAGENO, 1));
	}
	/**跳转到设置界面*/
	public void networkSettings()
	{
		Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<NewsItem>> loader, LinkedList<NewsItem> data)
	{
		if (data != null)
		{
			int updateCount = 0;
			getActivity().findViewById(android.R.id.empty).setVisibility(View.GONE);
			if (null == mData)
			{
				mData = data;
				updateCount = mData.size();
				mAdapter = new NewsListViewAdapter(getActivity(), mData);
				mListView.addFooterView(mFooterView);
				mListView.setAdapter(mAdapter);
				mBtnLoadNext.setOnClickListener(new OnClickListenerImpl());
			}
			else
			{
				updateCount = handleAddData(data).size();
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
			String messageText = "加载  " + updateCount+ " 条新数据";
			if (mCurrentPageNo == 1)
			{
				messageText = " 更新  " + updateCount + " 条新数据";
			}
			new CardToastUtils(getActivity()).showAndAutoDismiss(messageText);

			mBtnLoadNext.setText("点击加载下一页");
		}
		else
		{
			getActivity().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
			mBtnLoadNext.setText("所有数据加载完毕");
		}
		if(!NetWorkUtils.isNetworkAvailable(getActivity()))
		{
			mBtnLoadNext.setText("设置网络");
		}
		mProgressBar.setVisibility(View.INVISIBLE);
		mPullToRefreshAttacher.setRefreshComplete();
	}
	/**处理数据重复问题*/
	private LinkedList<NewsItem>  handleAddData(LinkedList<NewsItem> data)
	{
		LinkedList<NewsItem> tmpItems = new LinkedList<NewsItem>();
		 for(NewsItem item :data)
		 {
			 if(!mData.contains(item))
			 {
				 tmpItems.add(item);
				 mData.add(item);
			 }
		 }
		 return tmpItems;
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<NewsItem>> loader)
	{
	}
	private void loadNextData()
	{
		// =========================================================================
		// =========================================================================
		// 如果网络可用的话点击就去加载下一页数据， 如果不可用的话点击就跳转到系统网络设置界面
		// =========================================================================
		// =========================================================================
		if (!NetWorkUtils.isNetworkAvailable(getActivity()))
		{
			 networkSettings();
		}
		else
		{
			Bundle bundle = getArguments();
			mStubCurrentPageNo +=1;
			mCurrentPageNo = mStubCurrentPageNo;
			bundle.putInt(ARG_CURRENT_PAGENO, mStubCurrentPageNo);
			
			getActivity().getSupportLoaderManager().restartLoader(0, bundle, this);
		}
	}
}

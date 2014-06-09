package com.roboo.like.google.fragments;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.LocationActivity;
import com.roboo.like.google.MoodActivity;
import com.roboo.like.google.NewsActivity;
import com.roboo.like.google.PictureActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.TextActivity;
import com.roboo.like.google.adapters.NewsListAdapter;
import com.roboo.like.google.async.NewsListAsyncTaskLoader;
import com.roboo.like.google.infinite.InfiniteViewPager;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.progressbutton.ProcessButton;
import com.roboo.like.google.progressbutton.ProgressGenerator;
import com.roboo.like.google.progressbutton.ProgressGenerator.OnCompleteListener;
import com.roboo.like.google.utils.CardToastUtils;
import com.roboo.like.google.utils.NetWorkUtils;
import com.roboo.like.google.views.FooterView;
import com.roboo.like.google.views.HeaderView;
import com.roboo.like.google.views.StickyListHeadersListView;
import com.roboo.like.google.views.helper.PoppyListViewHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper;
import com.roboo.like.google.views.helper.PullToRefreshHelper.DefaultHeaderTransformer;
import com.roboo.like.google.views.helper.PullToRefreshHelper.OnRefreshListener;

public class MainFragment extends BaseFragment implements LoaderCallbacks<LinkedList<NewsItem>>
{
	private static final String DECLARED_OPERA_FAST_SCROLLER_FIELD = "mFastScroller";// FastScroller
	private static final String DECLARED_OVERLAY_SIZE = "mOverlaySize";// int
	private static final String DECLARED_OVERLAY_POS = "mOverlayPos"; // RectF
	private static final String DECLARED_PAINT = "mPaint"; // Paint
	/** Bundle当前加载数据的页数Key */
	private static final String ARG_CURRENT_PAGENO = "current_pageno";
	/** Bundle当前获取新闻URL */
	private static final String ARG_NEWS_URL = "news_url";
	/** 获取的是当前第几页的新闻数据 */
	private int mCurrentPageNo = 1;
	private int mStubCurrentPageNo = mCurrentPageNo;
	/** 用于记录兩條新聞日期不相同时，该比较字符串所在List集合的索引位置，在生成HeaderId时进行获取 */
	private LinkedList<Integer> mSectionIndex = new LinkedList<Integer>();

	/** ListView */
	private StickyListHeadersListView mListView;
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
	private NewsListAdapter mAdapter;
	/** ListView最后一列是否可见的标志 */
	public boolean mLastItemVisible;
	/** ListView 的 FooterView */
	private FooterView mFooterView;
	/** ListView 的 HeaderView */
	private HeaderView mHeaderView;
	/** 新闻列表适配器的数据源 */
	private LinkedList<NewsItem> mData;
	/** 当点击FooterView时显示加载数据标识 */
	private ProgressBar mProgressBar;
	/** 正在加载数据中…… */
	private Button mBtnLoadNext;
	/** HeaderView中的ViewPager */
	private InfiniteViewPager mAdViewPager;

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
		View view = inflater.inflate(R.layout.fragment_main, null);// TODO
		mFooterView = new FooterView(getActivity(), FooterView.TYPE_PROGRESS_BUTTON);
		mHeaderView = new HeaderView(getActivity());
		mAdViewPager = mHeaderView.getViewPager();
		mProgressBar = mFooterView.getProgressBar();
		mBtnLoadNext = mFooterView.getButton();
		mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);
		mPoppyListViewHelper = new PoppyListViewHelper(getActivity());
		mPullToRefreshAttacher = PullToRefreshHelper.get(getActivity());
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mPoppyView = mPoppyListViewHelper.createPoppyViewOnListView(R.id.slhlv_list, R.layout.poppyview);
		mBtnPicture = (Button) mPoppyView.findViewById(R.id.btn_picture);
		mBtnLocation = (Button) mPoppyView.findViewById(R.id.btn_location);
		mBtnMood = (Button) mPoppyView.findViewById(R.id.btn_mood);
		mBtnText = (Button) mPoppyView.findViewById(R.id.btn_text);
		mPullToRefreshAttacher.addRefreshableView(mListView, getOnRefreshListener());
		loadFirstData();
		modifyDefaultListViewFieldValue();
	}

	private OnRefreshListener getOnRefreshListener()
	{
		return new OnRefreshListener()
		{
			public void onRefreshStarted(View view)
			{
				loadFirstData();
			}

		};
	}

	/** 修改ListView一些默认属性值 */
	private void modifyDefaultListViewFieldValue()
	{
		try
		{
			Field field = AbsListView.class.getDeclaredField(DECLARED_OPERA_FAST_SCROLLER_FIELD);
			field.setAccessible(true);
			Object object = field.get(mListView);
			field = field.getType().getDeclaredField(DECLARED_PAINT);// 获取绘制文字的画笔
			field.setAccessible(true);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.red_color));
			paint.setAntiAlias(true);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			field.set(object, paint);

			field = field.getType().getDeclaredField(DECLARED_OVERLAY_SIZE);
			field.setAccessible(true);
			field.set(object, 50);

			field = field.getType().getDeclaredField(DECLARED_OVERLAY_POS);
			field.setAccessible(true);
			RectF rectF = (RectF) field.get(object);
			RectF newRectF = new RectF();
			newRectF.left = rectF.left;
			newRectF.top = rectF.top;
			newRectF.right = rectF.left + 150;
			newRectF.bottom = rectF.top + 50;
			field.set(object, newRectF);
			System.out.println("修改滑动时现在字体大小成功  " + field.getInt(object));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
				if (mFooterView.getType() == FooterView.TYPE_PROGRESS_BUTTON)
				{
					loadNextData();
				}
			}
			else
			{
				NewsActivity.actionNews(getActivity(), (NewsItem) parent.getAdapter().getItem(position));
			}
		}

		private OnCompleteListener<Object> getOnCompleteListener()
		{
			return new OnCompleteListener<Object>()
			{
				public Object onComplete()
				{
					mBtnLoadNext.setEnabled(true);
					return null;
				}

				@Override
				public Object doInBackgroundProcess()
				{
					loadNextData();
					return null;
				}
			};
		}
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
			case R.id.btn_load_next:// 加载下一页
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

	/** 跳转到设置界面 */
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
				mAdapter = new NewsListAdapter(getActivity(), mData, mSectionIndex);
				mListView.addFooterView(mFooterView);
				mListView.addHeaderView(mHeaderView);
				mListView.setAdapter(mAdapter);
				mBtnLoadNext.setOnClickListener(new OnClickListenerImpl());
			}
			else
			{
				updateCount = handleAddData(data).size();
			}
			Collections.sort(generateHeaderId(mData), new YMDComparator());
			mAdapter.setSectionIndex(mSectionIndex);
			mAdapter.notifyDataSetChanged();

			for (NewsItem item : data)
			{
				GoogleApplication.TEST = false;
				if (GoogleApplication.TEST)
				{
					System.out.println("Item = 【 " + item + " 】 ");
				}
			}
			String messageText = "加载  " + updateCount + " 条新数据";
			if (mCurrentPageNo == 1)
			{
				messageText = " 更新  " + updateCount + " 条新数据";
			}
			new CardToastUtils(getActivity()).setShowToastStyle(CardToastUtils.SHOW_ANIMATION_TOP_TO_DOWN_STYLE).showAndAutoDismiss(messageText);
			mBtnLoadNext.setText("点击加载下一页");
		}
		else
		{
			if (mData == null)
			{
				getActivity().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
			}
			mBtnLoadNext.setText("所有数据加载完毕");
		}
		if (!NetWorkUtils.isNetworkAvailable(getActivity()))
		{
			mBtnLoadNext.setText("设置网络");
		}
		mProgressBar.setVisibility(View.INVISIBLE);
		mPullToRefreshAttacher.setRefreshComplete();
	}

	/** 处理数据重复问题,以及将最新的数据放在最上面 */
	private LinkedList<NewsItem> handleAddData(LinkedList<NewsItem> data)
	{
		LinkedList<NewsItem> tmpItems = new LinkedList<NewsItem>();
		for (int i = data.size() - 1; i >= 0; i--)
		{
			NewsItem item = data.get(i);
			if (!mData.contains(item))
			{
				if (mCurrentPageNo == 1)
				{
					mData.addFirst(item);
				}
				else
				{
					mData.add(item);
				}
				tmpItems.add(item);
			}
		}
		return tmpItems;
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<NewsItem>> loader)
	{}

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
			mStubCurrentPageNo += 1;
			mCurrentPageNo = mStubCurrentPageNo;
			bundle.putInt(ARG_CURRENT_PAGENO, mStubCurrentPageNo);
			getActivity().getSupportLoaderManager().restartLoader(0, bundle, this);
		}
	}

	private LinkedList<NewsItem> generateHeaderId(LinkedList<NewsItem> nonHeaderIdList)
	{
		Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
		int mHeaderId = 1;
		LinkedList<NewsItem> hasHeaderIdList;

		for (int i = 0; i < nonHeaderIdList.size(); i++)
		{
			NewsItem item = nonHeaderIdList.get(i);
			String ymd = item.getTime();
			if (!mHeaderIdMap.containsKey(ymd))
			{
				item.setHeaderId(mHeaderId);
				mHeaderIdMap.put(ymd, mHeaderId);
				mHeaderId++;
				mSectionIndex.add(i);
			}
			else
			{
				item.setHeaderId(mHeaderIdMap.get(ymd));
			}
		}
		hasHeaderIdList = nonHeaderIdList;
		return hasHeaderIdList;
	}

	public class YMDComparator implements Comparator<NewsItem>
	{
		public int compare(NewsItem o1, NewsItem o2)
		{
			if (null != o1 && null != o2)
			{
				return o2.getTime().compareTo(o1.getTime());
			}
			return 0;
		}
	}

}

package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.MainActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.StartNewsTypeListAdapter;
import com.roboo.like.google.dao.impl.NewsTypeItemDaoImpl;
import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.swipelistview.SwipeListView;
import com.roboo.like.google.swipelistview.SwipeListViewListener;
import com.roboo.like.google.views.StickyListHeadersListView;

public class AddFragment extends BaseFragment
{
	private LinkedList<NewsTypeItem> mData;
	private ImageLoader mImageLoader;
	private SwipeListView mSwipeListView;
	private StickyListHeadersListView mListView;
	private StartNewsTypeListAdapter mAdapter;

	public static AddFragment newInstance()
	{
		AddFragment fragment = new AddFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_start, null);// TODO
		mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);
		mSwipeListView = (SwipeListView) view.findViewById(R.id.slv_listview);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getActivity()).discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(imageLoaderConfiguration);
		mListView.setAdapter(getAdapter());
		mSwipeListView.setAdapter(getAdapter());
		mSwipeListView.setEmptyView(getActivity().findViewById(android.R.id.empty));
		setListener();
	}

	private ListAdapter getAdapter()
	{
		NewsTypeItemDaoImpl newsTypeItemDao = new NewsTypeItemDaoImpl(new DBHelper(getActivity()));
		mData = newsTypeItemDao.getNewsTypeItems(false);
		if (null != mData)
		{
			mAdapter = new StartNewsTypeListAdapter(mData, getActivity());
		}
		return mAdapter;
	}

	private void setListener()
	{
		OnItemClickListenerImpl onItemClickListenerImpl = new OnItemClickListenerImpl();
		mListView.setOnItemClickListener(onItemClickListenerImpl);
		mSwipeListView.setSwipeListViewListener(new SwipeListViewListenerImpl());
	}

	private class OnItemClickListenerImpl implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			onListItemClick(position);
		}

	}

	private void onListItemClick(int position)
	{
		NewsTypeItem item = mData.get(position);
		GoogleApplication.mCurrentType = item.id;
		MainActivity.actionMain(getActivity());
	}

	private class SwipeListViewListenerImpl implements SwipeListViewListener
	{
		public void onOpened(int position, boolean toRight)
		{

		}

		@Override
		public void onClosed(int position, boolean fromRight)
		{

		}

		@Override
		public void onListChanged()
		{

		}

		@Override
		public void onMove(int position, float x)
		{

		}

		@Override
		public void onStartOpen(int position, int action, boolean right)
		{

		}

		@Override
		public void onStartClose(int position, boolean right)
		{

		}

		@Override
		public void onClickFrontView(int position)
		{
			onListItemClick(position);
		}

		@Override
		public void onClickBackView(int position)
		{
			mSwipeListView.closeAnimate(position);
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions)
		{

		}

		public int onChangeSwipeMode(int position)
		{
			return SwipeListView.SWIPE_MODE_DEFAULT;
		}

		@Override
		public void onChoiceChanged(int position, boolean selected)
		{

		}

		@Override
		public void onChoiceStarted()
		{

		}

		@Override
		public void onChoiceEnded()
		{

		}

		@Override
		public void onFirstListItem()
		{

		}

		@Override
		public void onLastListItem()
		{

		}

	}
}

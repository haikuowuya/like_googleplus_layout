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
import com.roboo.like.google.models.StartNewsTypeItem;
import com.roboo.like.google.utils.DataUtils;
import com.roboo.like.google.views.StickyListHeadersListView;

public class StartFragment extends BaseFragment
{
	private ImageLoader mImageLoader;
	private StickyListHeadersListView mListView;
	private StartNewsTypeListAdapter mAdapter;

	public static StartFragment newInstance()
	{
		StartFragment fragment = new StartFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_start, null);// TODO
		mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getActivity()).discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(imageLoaderConfiguration);
		mListView.setAdapter(getAdapter());
		setListener();
	}

	private ListAdapter getAdapter()
	{
		LinkedList<StartNewsTypeItem> mData = DataUtils.handleStartNewsType(getActivity());
		mAdapter = new StartNewsTypeListAdapter(mData, getActivity());
		return mAdapter;
	}

	private void setListener()
	{
		mListView.setOnItemClickListener(new OnItemClickListenerImpl());
	}

	private class OnItemClickListenerImpl implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			StartNewsTypeItem item = (StartNewsTypeItem) parent.getAdapter().getItem(position);
			GoogleApplication.mCurrentType = item.typeInt;
			MainActivity.actionMain(getActivity());
		}

	}
}

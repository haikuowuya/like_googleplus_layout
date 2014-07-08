package com.roboo.like.google.fragments;

import java.util.Collections;
import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.ContacterAdapter;
import com.roboo.like.google.async.ContacterAsyncTaskLoader;
import com.roboo.like.google.models.ContacterItem;
import com.roboo.like.google.views.StickyListHeadersListView;
/**读取手机联系人信息*/
public class ContacterFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<ContacterItem>>
{
	private StickyListHeadersListView mListView;

	public static ContacterFragment newInstance()
	{
		ContacterFragment fragment = new ContacterFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_contacter, null);//TODO 
		mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);
		return view;
	}
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getActivity().getSupportLoaderManager().initLoader(0, null, this);
	}

	public Loader<LinkedList<ContacterItem>> onCreateLoader(int id, Bundle args)
	{
		return new ContacterAsyncTaskLoader(getActivity());
	}

	public void onLoadFinished(Loader<LinkedList<ContacterItem>> loader, LinkedList<ContacterItem> data)
	{
		if (null != data)
		{
			Collections.sort(data);
			mListView.setAdapter(new ContacterAdapter(getActivity(), data));
			for (ContacterItem item : data)
			{
				System.out.println(" item = " + item);
			}
		}
		mProgressBar.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<LinkedList<ContacterItem>> loader)
	{

	}
}

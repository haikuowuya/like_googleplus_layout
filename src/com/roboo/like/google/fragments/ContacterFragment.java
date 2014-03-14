package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.ContacterAdapter;
import com.roboo.like.google.async.ContacterAsyncTaskLoader;
import com.roboo.like.google.models.ContacterItem;

public class ContacterFragment extends BaseFragment implements LoaderCallbacks<LinkedList<ContacterItem>>
{
	private ProgressBar mProgressBar;
	private ListView mListView;
	public static ContacterFragment newInstance()
	{
		ContacterFragment fragment = new ContacterFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_contacter, null);
		mListView = (ListView) view.findViewById(R.id.lv_list);
		mProgressBar = (ProgressBar) view.findViewById(R.id.pb_progress);
		return view;
	}
	@Override
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
		mProgressBar.setVisibility(View.GONE);
		 mListView.setAdapter(new ContacterAdapter(getActivity(), data));
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<ContacterItem>> loader)
	{
		 
	}

}

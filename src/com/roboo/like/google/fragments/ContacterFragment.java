package com.roboo.like.google.fragments;

import java.util.Collections;
import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.ContacterAdapter;
import com.roboo.like.google.async.ContacterAsyncTaskLoader;
import com.roboo.like.google.models.ContacterItem;
import com.roboo.like.google.views.StickyListHeadersListView;

public class ContacterFragment extends BaseFragment implements LoaderCallbacks<LinkedList<ContacterItem>>
{
	private ProgressBar mProgressBar;
	private StickyListHeadersListView mListView;

	public static ContacterFragment newInstance()
	{
		ContacterFragment fragment = new ContacterFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_contacter, null);
		mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);
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

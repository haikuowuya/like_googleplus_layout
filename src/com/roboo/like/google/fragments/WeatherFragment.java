package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.WeatherAdapter;
import com.roboo.like.google.async.WeatherAsyncTaskLoader;
import com.roboo.like.google.models.WeatherItem;

public class WeatherFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<WeatherItem>>
{
	/** 向 ViewGroup 中添加view时动画持续时间 */
	private static final String ARG_WEATHER_URL = "weather_url";
	private ListView mListView;
	private LinkedList<WeatherItem> mData;
	private WeatherAdapter mAdapter;

	public static WeatherFragment newInstance(String weatherUrl)
	{
		WeatherFragment fragment = new WeatherFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_WEATHER_URL, weatherUrl);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_weather, null);// TODO
		mListView = (ListView) view.findViewById(R.id.dlv_list);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (mData == null)
		{
			getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), this);
		}
	}

	@Override
	public Loader<LinkedList<WeatherItem>> onCreateLoader(int id, Bundle args)
	{
		return new WeatherAsyncTaskLoader(getActivity(), getArguments().getString(ARG_WEATHER_URL));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<WeatherItem>> loader, LinkedList<WeatherItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (null != data && data.size() > 0)
		{
			mData = data;
			mAdapter = new WeatherAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
		}
	}

	public void onLoaderReset(Loader<LinkedList<WeatherItem>> loader)
	{

	}

}

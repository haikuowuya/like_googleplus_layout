package com.roboo.like.google.fragments;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.roboo.like.google.R;
import com.roboo.like.google.WebViewActivity;
import com.roboo.like.google.adapters.WeatherAdapter;
import com.roboo.like.google.async.WeatherAsyncTaskLoader;
import com.roboo.like.google.models.CityItem;
import com.roboo.like.google.models.WeatherItem;

public class WeatherFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<WeatherItem>>
{
	/** 向 ViewGroup 中添加view时动画持续时间 */
	private static final String ARG_CITY_ITEM = "city_item";
	private ListView mListView;
	private LinkedList<WeatherItem> mData;
	private WeatherAdapter mAdapter;
	private View mFooterView;
	private CityItem mCityItem;

	public static WeatherFragment newInstance(CityItem cityItem)
	{
		WeatherFragment fragment = new WeatherFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_CITY_ITEM, cityItem);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_weather, null);// TODO
		mListView = (ListView) view.findViewById(R.id.dlv_list);
		mFooterView = createFooterView();
		mListView.addFooterView(mFooterView);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
		super.onActivityCreated(savedInstanceState);
		mCityItem = (CityItem) getArguments().getSerializable(ARG_CITY_ITEM);
		if (mData == null)
		{
			getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), this);
		}
	}

	private View createFooterView()
	{
		Button btnToWebView = new Button(getActivity());
		btnToWebView.setId(R.id.btn_webview);
		btnToWebView.setClickable(true);
		btnToWebView.setText("查看原文");
		btnToWebView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		btnToWebView.setBackgroundResource(R.drawable.list_item_selector);
		btnToWebView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				WebViewActivity.actionWebView(getActivity(), mCityItem.cUrl, mCityItem.cName + "天气");
			}
		});
		return btnToWebView;
	}

	@Override
	public Loader<LinkedList<WeatherItem>> onCreateLoader(int id, Bundle args)
	{ 
		System.out.println(" onCreateLoader ");
		return new WeatherAsyncTaskLoader(getActivity(), mCityItem.cUrl);
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<WeatherItem>> loader, LinkedList<WeatherItem> data)
	{
		System.out.println(" onLoadFinished ");
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

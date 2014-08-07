package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;

import com.roboo.like.google.R;
import com.roboo.like.google.WeatherActivity;
import com.roboo.like.google.adapters.ProvCityAdapter;
import com.roboo.like.google.async.CityAsyncTaskLoader;
import com.roboo.like.google.models.CityItem;
import com.roboo.like.google.views.StickyListHeadersListView;

@SuppressLint("NewApi")
public class CityFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<LinkedList<CityItem>>>
{
	private static final String SUZHOU_WEATHER_URL = "http://m.weathercn.com/index.do?cid=01011706&pid=010117";
	private StickyListHeadersListView mListView;
	private LinkedList<Integer> mSectionIndex = new LinkedList<Integer>();
	private LinkedList<CityItem> mData;
	private ProvCityAdapter mAdapter;
	private View mHeaderView;

	public static CityFragment newInstance()
	{
		CityFragment fragment = new CityFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_city, null);// TODO
			mListView = (StickyListHeadersListView) view.findViewById(R.id.slhlv_list);
		}
		mHeaderView = createHeaderView();
//		mListView.addHeaderView(mHeaderView);
		return view;
	}

	private View createHeaderView()
	{
		mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_city_header_view, null);// TODO ListView的HeaderView布局文件
		final EditText editText = (EditText) mHeaderView.findViewById(R.id.et_text);
		ImageButton ibtnSearch = (ImageButton) mHeaderView.findViewById(R.id.ibtn_search);
		return mHeaderView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (null == mData)
		{
			getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), this);
		}
		setListener();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.activity_weather, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_sz:
			// WebViewActivity.actionWebView(getActivity(), url, "苏州");
			CityItem cityItem = new CityItem();
			cityItem.cName = "苏州";
			cityItem.cUrl = SUZHOU_WEATHER_URL;
			WeatherActivity.actionWeather(getActivity(), cityItem);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setListener()
	{
		mListView.setOnItemClickListener(getOnItemClickListener());
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				CityItem item = (CityItem) mAdapter.getItem(position);
				WeatherActivity.actionWeather(getActivity(), item);
			}
		};
	}
	@Override
	public Loader<LinkedList<LinkedList<CityItem>>> onCreateLoader(int id, Bundle args)
	{
		return new CityAsyncTaskLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<LinkedList<CityItem>>> loader, LinkedList<LinkedList<CityItem>> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (data != null)
		{
			mData = handleDataWithSectionIndex(data);
			mAdapter = new ProvCityAdapter(getActivity(), mData, mSectionIndex);
			mAdapter.setSectionIndex(mSectionIndex);
			mListView.setAdapter(mAdapter);
		}
	}

	private LinkedList<CityItem> handleDataWithSectionIndex(LinkedList<LinkedList<CityItem>> data)
	{
		LinkedList<CityItem> items = new LinkedList<CityItem>();
		for (LinkedList<CityItem> tmp : data)
		{
			for (CityItem item : tmp)
			{
				if (null != item)
				{
					items.add(item);
				}
			}
		}
		Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
		int mHeaderId = 1;
		LinkedList<CityItem> hasHeaderIdList = new LinkedList<CityItem>();
		for (int i = 0; i < items.size(); i++)
		{
			CityItem item = items.get(i);
			String pName = item.pName;
			if (!mHeaderIdMap.containsKey(pName))
			{
				item.headerId = mHeaderId;
				mHeaderIdMap.put(pName, mHeaderId);
				mHeaderId++;
				mSectionIndex.add(i);
			}
			else
			{
				item.headerId = mHeaderIdMap.get(pName);
			}
			hasHeaderIdList.add(item);
		}
		return hasHeaderIdList;
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<LinkedList<CityItem>>> loader)
	{

	}

	/***
	 * 执行Root 命令
	 * 
	 * @param command
	 *            要执行的命令[chmod 777 /data/misc/wifi/wpa_supplicant.conf]
	 * @return true 命令成功执行 或者 false 命令执行失败
	 */
	public static boolean runRootCommand(String command)
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{}
		}
		return true;
	}
}

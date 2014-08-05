package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.droidux.trial.bu;
import com.roboo.like.google.R;
import com.roboo.like.google.WeatherActivity;
import com.roboo.like.google.adapters.ProvinceCityAdapter;
import com.roboo.like.google.async.WeatherAsyncTaskLoader;
import com.roboo.like.google.models.CityItem;

@SuppressLint("NewApi")
public class WeatherFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<CityItem>>
{
	private ListView mListView;
	public static final String ARG_ITEM = "item";

	public static WeatherFragment newInstance(Serializable item)
	{
		WeatherFragment fragment = new WeatherFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_ITEM, item);
		fragment.setArguments(bundle);
		return fragment;
	}

	public static WeatherFragment newInstance()
	{
		WeatherFragment fragment = new WeatherFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_weather, null);// TODO
			mListView = (ListView) view.findViewById(R.id.dlv_list);
		}
		getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setListener();
	}

	public void setListener()
	{
		mListView.setOnItemClickListener(getOnItemClickListener());
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			WeatherActivity weatherActivity = (WeatherActivity) getActivity();

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				CityItem cityItem = (CityItem) arg0.getAdapter().getItem(arg2);
				weatherActivity.showCity(cityItem);
			}
		};
	}

	@Override
	public Loader<LinkedList<CityItem>> onCreateLoader(int id, Bundle args)
	{
		CityItem cityItem = null;
		if (null != args)
		{
			cityItem = (CityItem) args.getSerializable(ARG_ITEM);
		}
		return new WeatherAsyncTaskLoader(getActivity(), cityItem);
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<CityItem>> loader, LinkedList<CityItem> data)
	{
		if (null != mProgressBar)
		{
			mProgressBar.setVisibility(View.GONE);
		}
		if (data != null)
		{
			mListView.setAdapter(new ProvinceCityAdapter(getActivity(), data));
		}
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<CityItem>> loader)
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

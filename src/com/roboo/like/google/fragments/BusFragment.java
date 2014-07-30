package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.BusAdapter;
import com.roboo.like.google.adapters.WIFIAdapter;
import com.roboo.like.google.async.BusAsyncTaskLoader;
import com.roboo.like.google.async.WifiAsyncTaskLoader;
import com.roboo.like.google.models.BusItem;

@SuppressLint("NewApi")
public class BusFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<BusItem>>
{
	private ListView mListView;

	public static BusFragment newInstance()
	{
		BusFragment fragment = new BusFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_bus, null);// TODO
			mListView = (ListView) view.findViewById(R.id.dlv_list);
			getActivity().getSupportLoaderManager().initLoader(0, null, this);
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	public void setListener()
	{
		mListView.setOnItemClickListener(getOnItemClickListener());
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

			}
		};
	}

	@Override
	public Loader<LinkedList<BusItem>> onCreateLoader(int id, Bundle args)
	{
		return new BusAsyncTaskLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<BusItem>> loader, LinkedList<BusItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (data != null)
		{
			mListView.setAdapter(new BusAdapter(getActivity(), data));
		}
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<ScanResult>> loader)
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

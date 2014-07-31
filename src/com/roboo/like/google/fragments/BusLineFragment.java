package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.LinkedList;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.roboo.like.google.BusStationActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.BusLineAdapter;
import com.roboo.like.google.async.BusLineAsyncTaskLoader;
import com.roboo.like.google.models.BusLineItem;

@SuppressLint("NewApi")
public class BusLineFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<BusLineItem>>
{
	private ListView mListView;
	public static final String ARG_BUS_LINE = "bus_line";
	public static final String ARG_BUS_ITEM = "bus_item";
	private LinkedList<BusLineItem> mData;
	private View mHeaderView;
	private BusLineAdapter mAdapter;
	private int mListViewFirstPosition   = 0;

	public static BusLineFragment newInstance(String busLineUrl)
	{
		BusLineFragment fragment = new BusLineFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_BUS_LINE, busLineUrl);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_bus_line, null);// TODO
			mListView = (ListView) view.findViewById(R.id.dlv_list);
		}
		mHeaderView = createHeaderView();
		mListView.addHeaderView(mHeaderView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		doLoadData();
		setListener();
	}

	 

	private void doLoadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/***
	 * 为ListView通过代码创建一个HeaderView
	 * 
	 * @return
	 */
	private View createHeaderView()
	{
		mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_bus_line_header_view, null);// TODO ListView的HeaderView布局文件
		return mHeaderView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.activity_bus, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_refresh:// 重试
			onRefresh();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void onRefresh()
	{
		mHeaderView.setVisibility(View.GONE);
		mData = new LinkedList<BusLineItem>();
		mAdapter = new BusLineAdapter(getActivity(), mData);
		mListView.setAdapter(mAdapter);
		doLoadData();
	}

	public void setListener()
	{
		mListView.setOnItemClickListener(getOnItemClickListener());
		mListView.setOnScrollListener(new OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE)
				{
					 mListViewFirstPosition = view.getFirstVisiblePosition();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
			}
		});
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				BusLineItem item = (BusLineItem) parent.getAdapter().getItem(position);
				BusStationActivity.actionBusStation(getActivity(), item);
			}

		};
	}

	@Override
	public Loader<LinkedList<BusLineItem>> onCreateLoader(int id, Bundle args)
	{
		return new BusLineAsyncTaskLoader(getActivity(), getArguments().getString(ARG_BUS_LINE, null));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<BusLineItem>> loader, LinkedList<BusLineItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (data != null)
		{
			mData = data;
			mAdapter = new BusLineAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
			mHeaderView.setVisibility(View.VISIBLE);
			if(mListViewFirstPosition > 0)
			{
				mListView.setSelection(mListViewFirstPosition);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<BusLineItem>> loader)
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

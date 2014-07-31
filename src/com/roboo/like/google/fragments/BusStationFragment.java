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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.roboo.like.google.BusLineActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.BusStationAdapter;
import com.roboo.like.google.async.BusStationAsyncTaskLoader;
import com.roboo.like.google.models.BusItem;
import com.roboo.like.google.models.BusStationItem;
import com.roboo.like.google.utils.NetWorkUtils;

@SuppressLint("NewApi")
public class BusStationFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<BusStationItem>>
{
	private ListView mListView;
	public static final String ARG_BUS_LINE = "bus_line";
	public static final String ARG_BUS_ITEM = "bus_item";
	private LinkedList<BusStationItem> mData;
	private View mHeaderView;
	private BusStationAdapter mAdapter;
	private int mListViewFirstPosition = 0;

	public static BusStationFragment newInstance(String busLineUrl)
	{
		BusStationFragment fragment = new BusStationFragment();
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
		mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_bus_station_header_view, null);// TODO ListView的HeaderView布局文件
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
		mData = new LinkedList<BusStationItem>();
		mAdapter = new BusStationAdapter(getActivity(), mData);
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
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
				{
					mListViewFirstPosition = view.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{}
		});
	}

	private OnItemClickListener getOnItemClickListener()
	{
		return new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				BusStationItem item = (BusStationItem) parent.getAdapter().getItem(position);
				BusItem busItem = new BusItem();
				busItem.busNo = item.busNo;
				busItem.busUrl = item.stationUrl;
				BusLineActivity.actionBusLine(getActivity(), busItem);
			}

		};
	}

	@Override
	public Loader<LinkedList<BusStationItem>> onCreateLoader(int id, Bundle args)
	{
		return new BusStationAsyncTaskLoader(getActivity(), getArguments().getString(ARG_BUS_LINE, null));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<BusStationItem>> loader, LinkedList<BusStationItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (data != null)
		{
			mData = data;
			mHeaderView.setVisibility(View.VISIBLE);
			mAdapter = new BusStationAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
			if (mListViewFirstPosition > 0)
			{
				mListView.setSelection(mListViewFirstPosition);
			}
		}
		else
		{
			
			if (NetWorkUtils.isNetworkAvailable(getActivity()))
			{

			}
			else
			{

			}
		}
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<BusStationItem>> loader)
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

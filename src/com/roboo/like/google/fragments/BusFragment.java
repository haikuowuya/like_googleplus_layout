package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.roboo.like.google.BaseActivity;
import com.roboo.like.google.BusActivity;
import com.roboo.like.google.BusLineActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.BusAdapter;
import com.roboo.like.google.async.BusAsyncTaskLoader;
import com.roboo.like.google.models.BusItem;
import com.roboo.like.google.utils.NetWorkUtils;

@SuppressLint("NewApi")
public class BusFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<BusItem>>
{
	private ListView mListView;
	private LinkedList<BusItem> mData;
	private BusAdapter mAdapter;
	private View mHeaderView;
	private View mEmptyView;
	private String mBusNo = "18";
	private int mListViewFirstPosition = 0;

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
		}
		mHeaderView = createHeaderView();
		mEmptyView = createEmptyView();
		mListView.addHeaderView(mHeaderView);
		mListView.setEmptyView(mEmptyView);
		return view;
	}

	private View createEmptyView()
	{
		mEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_bus_empty_view, null);// TODO ListView的HeaderView布局文件
		return mEmptyView;
	}

	/***
	 * 为ListView创建一个HeaderView
	 * 
	 * @return
	 */
	private View createHeaderView()
	{
		mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_bus_header_view, null);// TODO ListView的HeaderView布局文件
		final EditText editText = (EditText) mHeaderView.findViewById(R.id.et_text);
		ImageButton ibtnSearch = (ImageButton) mHeaderView.findViewById(R.id.ibtn_search);
		ibtnSearch.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (!TextUtils.isEmpty(editText.getText()))
				{
					if (TextUtils.isDigitsOnly(editText.getText()))
					{
						mBusNo = editText.getText().toString();
						BaseActivity baseActivity = (BaseActivity) getActivity();
						baseActivity.hideKeyBoard(editText);
						editText.setText(null);
						onRefresh();
					}
					else
					{
						Toast.makeText(getActivity(), "输入的线路番号不合法", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "请输入的线路番号", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return mHeaderView;
	}

	private void doLoadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		mProgressBar.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (mData == null)
		{
			doLoadData();
		}
		else
		{
			mProgressBar.setVisibility(View.GONE);
			mAdapter = new BusAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
		}
		setListener();
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

	/**
	 * 刷新
	 */
	private void onRefresh()
	{
		mHeaderView.setVisibility(View.GONE);
		mData = new LinkedList<BusItem>();
		mAdapter = new BusAdapter(getActivity(), mData);
		mListView.setAdapter(mAdapter);
		doLoadData();
	}

	public void setListener()
	{

		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				BusItem item = (BusItem) parent.getAdapter().getItem(position);
				BusLineActivity.actionBusLine(getActivity(), item);
			}
		});
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

	@Override
	public Loader<LinkedList<BusItem>> onCreateLoader(int id, Bundle args)
	{
		return new BusAsyncTaskLoader(getActivity(), mBusNo);
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<BusItem>> loader, LinkedList<BusItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		if (data != null)
		{
			mData = data;
			mHeaderView.setVisibility(View.VISIBLE);
			mAdapter = new BusAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
			if (mListViewFirstPosition > 0)
			{
				mListView.setSelection(mListViewFirstPosition);
			}
		}
		else
		{
			mEmptyView.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					if (NetWorkUtils.isNetworkAvailable(getActivity()))
					{

					}
					else
					{

					}

				}
			});

		}
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<BusItem>> loader)
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

package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roboo.like.google.BusLineActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.async.BusLineAsyncTaskLoader;
import com.roboo.like.google.models.BusLineItem;
import com.roboo.like.google.views.BusSiteView;

@SuppressLint("NewApi")
public class BusLineFragment2 extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<BusLineItem>>
{
	public static final String ARG_BUS_LINE = "bus_line";
	public static final String ARG_BUS_NAME = "bus_name";
	private LinkedList<BusLineItem> mData;
	private LinearLayout mLinearContainer;
	private FrameLayout mFrameLayout;
	public static BusLineFragment2 newInstance(String busLineUrl,String busName)
	{
		BusLineFragment2 fragment = new BusLineFragment2();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_BUS_LINE, busLineUrl);
		bundle.putString(ARG_BUS_NAME, busName);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_bus_line2, null);// TODO
			mLinearContainer = (LinearLayout) view.findViewById(R.id.linear_container);
			mFrameLayout = (FrameLayout) view.findViewById(R.id.framelayout);
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		doLoadData();
	}
 

	private void doLoadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	 
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.activity_bus_line, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_refresh:// 重试
			onRefresh();
			break;
		case R.id.menu_invert://换方向
			invert();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void invert()
	{
		BusLineActivity busLineActivity = (BusLineActivity) getActivity();
		busLineActivity.invert();
	}

	private void onRefresh()
	{
		mData = new LinkedList<BusLineItem>();
		doLoadData();
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
			onGetDataSuccess();
		}
	}

	private void onGetDataSuccess()
	{
		LayoutParams params = new LinearLayout.LayoutParams(144,
			LayoutParams.MATCH_PARENT);
		for (int i = 0; i < mData.size() ;i++)
		{
			final BusSiteView busItemView = new BusSiteView(getActivity());
			busItemView.setPosition(i + 1);
			busItemView.setIsEnd(i == mData.size() - 1);
			busItemView.setIsStart(i == 0);
		 
			if ((i + 1) % 4 == 0)
			{
				ImageView imageView = new ImageView(getActivity());
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					48, 48);
				layoutParams.gravity = Gravity.TOP;
				layoutParams.leftMargin = 144 * i-20 ;
				layoutParams.topMargin = 10;
				imageView.setImageResource(R.drawable.ic_bus_ontheway);
				if((i+1)%8==0)
				{
					layoutParams.leftMargin = 144*i-96;
					imageView.setImageResource(R.drawable.ic_bus_arrive);
				}
				mFrameLayout.addView(imageView, layoutParams);

			}
			busItemView.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), busItemView.getText(),
						Toast.LENGTH_SHORT).show();
				}
			});
			mLinearContainer.addView(busItemView, params);
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

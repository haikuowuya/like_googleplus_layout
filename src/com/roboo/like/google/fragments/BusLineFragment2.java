package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roboo.like.google.BusLineActivity;
import com.roboo.like.google.BusStationActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.async.BusLineAsyncTaskLoader;
import com.roboo.like.google.models.BusLineItem;
import com.roboo.like.google.views.BusSiteView;

@SuppressLint("NewApi")
public class BusLineFragment2 extends BaseWithProgressFragment implements
	LoaderCallbacks<LinkedList<BusLineItem>>
{
	private static final long NEXT_QUERY_DELAY_TIME = 10000L;
	private static final long ONE_MINUTE_IN_MM = 60 * 1000L;
	public static final String ARG_BUS_LINE = "bus_line";
	public static final String ARG_BUS_NAME = "bus_name";
	private LinkedList<BusLineItem> mData;
	private HorizontalScrollView mHorizontalScrollView;
	private Handler mHandler = new Handler();
	private Runnable mQueryRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			doLoadData();
		}
	};

	public static BusLineFragment2 newInstance(String busLineUrl, String busName)
	{
		BusLineFragment2 fragment = new BusLineFragment2();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_BUS_LINE, busLineUrl);
		bundle.putString(ARG_BUS_NAME, busName);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		View view = null;
		if (savedInstanceState == null)
		{
			view = inflater.inflate(R.layout.fragment_bus_line2, null);// TODO
			mHorizontalScrollView = (HorizontalScrollView) view
				.findViewById(R.id.hsv_scrollview);

		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		doLoadData();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mProgressBar.setVisibility(View.GONE);
		mHandler.removeCallbacks(mQueryRunnable);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (mData != null)
		{
			mHandler.postDelayed(mQueryRunnable, NEXT_QUERY_DELAY_TIME);
		}
	}

	private void setListener()
	{}

	private void doLoadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.menu_other).setVisible(false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.activity_bus_line, menu);
		menu.findItem(R.id.menu_other).setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_refresh:// 重试
			onRefresh();
			break;
		case R.id.menu_invert:// 换方向
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
		return new BusLineAsyncTaskLoader(getActivity(), getArguments()
			.getString(ARG_BUS_LINE, null));
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<BusLineItem>> loader,
		LinkedList<BusLineItem> data)
	{
		mProgressBar.setVisibility(View.GONE);
		mHandler.postDelayed(mQueryRunnable, NEXT_QUERY_DELAY_TIME);

		if (data != null)
		{
			mData = data;
			onGetDataSuccess();
		}
	}

	private void onGetDataSuccess()
	{
		mHorizontalScrollView.removeAllViews();
		int dp_48 = (int) (48 * getResources().getDisplayMetrics().density);
		FrameLayout frameLayout = new FrameLayout(getActivity());
		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, 4 * dp_48);
		frameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		mHorizontalScrollView.addView(frameLayout, frameLayoutParams);
		LinearLayout linearLayout = new LinearLayout(getActivity());
		frameLayout.addView(linearLayout, new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LayoutParams params = new LinearLayout.LayoutParams(dp_48,
			LayoutParams.MATCH_PARENT);
		for (int i = 0; i < mData.size(); i++)
		{
			final BusLineItem item = mData.get(i);
			final BusSiteView busItemView = new BusSiteView(getActivity());
			busItemView.setPosition(i + 1);
			busItemView.setText(item.stationName);
			busItemView.setIsEnd(i == mData.size() - 1);
			busItemView.setIsStart(i == 0);
			if (!TextUtils.isEmpty(item.incomingBusNo))
			{
				ImageView imageView = new ImageView(getActivity());
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					dp_48 / 3, dp_48 / 3);
				layoutParams.topMargin = dp_48 / 10;
				layoutParams.gravity = Gravity.TOP;
				if (isArrived(item))
				{
					layoutParams.leftMargin = dp_48 * (1 + i) - dp_48 * 2 / 3;
					imageView.setImageResource(R.drawable.ic_bus_arrive);
				}
				else if (i < mData.size() - 1)
				{
					layoutParams.leftMargin = dp_48 * (1+i) - dp_48 / 8;
					imageView.setImageResource(R.drawable.ic_bus_ontheway);
				}
				frameLayout.addView(imageView, layoutParams);

			}
			busItemView.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					BusStationActivity.actionBusStation(getActivity(), item);
				}
			});
			linearLayout.addView(busItemView, params);
		}

	}

	private boolean isArrived(BusLineItem item)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = 0, minute = 0, second = 0;
		if (!TextUtils.isEmpty(item.incomingBusTime)
			&& item.incomingBusTime.contains(":"))
		{
			String[] tmp = item.incomingBusTime.split(":");

			hourOfDay = Integer.parseInt(tmp[0]);
			minute = Integer.parseInt(tmp[1]);
			second = Integer.parseInt(tmp[2]);
		}
		// System.out.println("year = " + year + " month = " + month + " day = "
		// + day);
		Calendar busCalendar = Calendar.getInstance();
		busCalendar.set(year, month, day, hourOfDay, minute, second);
		// System.out.println(calendar.getTimeInMillis() + "\n"
		// + busCalendar.getTimeInMillis());
		return calendar.getTimeInMillis() - busCalendar.getTimeInMillis()< ONE_MINUTE_IN_MM;
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

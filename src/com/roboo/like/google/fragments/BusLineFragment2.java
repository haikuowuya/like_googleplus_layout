package com.roboo.like.google.fragments;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.roboo.like.google.adapters.BusStationAdapter2;
import com.roboo.like.google.async.BusLineAsyncTaskLoader;
import com.roboo.like.google.models.BusLineItem;
import com.roboo.like.google.models.BusStationItem;
import com.roboo.like.google.utils.BusUtils;
import com.roboo.like.google.views.BusSiteView;

@SuppressLint("NewApi")
public class BusLineFragment2 extends BaseWithProgressFragment implements
	LoaderCallbacks<LinkedList<BusLineItem>>
{
	private static final String NO_CAR = "前方暂无到达车辆";
	private static final String IN_CAR = "进站";
	private static final String ZERO = "0";
	private static final long NEXT_QUERY_DELAY_TIME = 12000L;
	private static final long ONE_MINUTE_IN_MM = 60 * 1000L;
	public static final String ARG_BUS_LINE = "bus_line";
	public static final String ARG_BUS_NO = "bus_no";
	private LinkedList<BusLineItem> mData;
	/**是否自动刷新*/
	private boolean mIsAutoRefresh = true;
	private LinkedList<BusStationItem> mBusStationItems = null;
	private int mClickPosition = -1;
	private int mPreviousClickPosition = -1;
	private HorizontalScrollView mHorizontalScrollView;
	private BusLineItem mClickedBusLineItem;
	private BusLineActivity mHostActivity;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			mBusStationItems = (LinkedList<BusStationItem>) msg.obj;
			if (null != mBusStationItems && mBusStationItems.size() > 0)
			{
				 
				BusStationItem busStationItem = null;
				 for(int i = 0;i < mBusStationItems.size();i++)
				 {
					 BusStationItem tmp = mBusStationItems.get(i);
					 if(tmp.busNo.equals(getArguments().getString(ARG_BUS_NO)))
					 {
						 busStationItem = tmp;
						 break;
					 }
				 }
				 if(null != busStationItem)
				 {
					 mBusStationItems.remove(busStationItem);
					 mBusStationItems.addFirst(busStationItem);
				 }
			 
				BusStationAdapter2 adapter = new BusStationAdapter2(
					mHostActivity, mBusStationItems,getArguments().getString(ARG_BUS_NO));
				mHostActivity.showNavActionBar(adapter);
			}
		};
	};
	private Runnable mQueryRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if (mIsAutoRefresh && mProgressBar.getVisibility() == View.GONE)
			{
				doLoadData();
			}
		}
	};
	private Runnable mGetBusStationRunnable = new Runnable()
	{
		public void run()
		{
			LinkedList<BusStationItem> data = BusUtils
				.getBusStation(mClickedBusLineItem.stationUrl);
			Message message = mHandler.obtainMessage();
			message.obj = data;
			mHandler.sendMessage(message);
		}
	};

	public static BusLineFragment2 newInstance(String busLineUrl, String busName)
	{
		BusLineFragment2 fragment = new BusLineFragment2();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_BUS_LINE, busLineUrl);
		bundle.putString(ARG_BUS_NO, busName);
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
		mHostActivity = (BusLineActivity) getActivity();
	 
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

	private void doLoadBusStationData()
	{
		new Thread(mGetBusStationRunnable).start();
	}

	private void doLoadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		mProgressBar.setVisibility(View.VISIBLE);
		if (null != mBusStationItems)// 前面点击过
		{
			doLoadBusStationData();
		}
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
		case R.id.menu_auto_refresh:// 自动刷新
			mIsAutoRefresh = !item.isChecked();
			item.setChecked(mIsAutoRefresh);
			if (mIsAutoRefresh)
			{
				mHandler.post(mQueryRunnable);
			}
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
		final FrameLayout frameLayout = new FrameLayout(getActivity());
		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, 5 * dp_48);
		frameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		mHorizontalScrollView.addView(frameLayout, frameLayoutParams);
		final LinearLayout linearLayout = new LinearLayout(getActivity());
		frameLayout.addView(linearLayout, new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LayoutParams params = new LinearLayout.LayoutParams(dp_48,
			LayoutParams.MATCH_PARENT);
		if (TextUtils.isEmpty(mHostActivity.getStationMark()))
		{
			mClickPosition = mData.size() / 2;
			mHostActivity.setStationMark(mData.get(mClickPosition).stationMark);
		}
		else
		{
			mClickPosition = getDefaultPosition();
		}
		for (int i = 0; i < mData.size(); i++)
		{
			final BusLineItem item = mData.get(i);
			final BusSiteView busItemView = new BusSiteView(getActivity());
			busItemView.setPosition(i + 1);
			busItemView.setText(item.stationName);
			busItemView.setIsEnd(i == mData.size() - 1);
			busItemView.setIsStart(i == 0);
			if (item.stationMark.equals(mHostActivity.getStationMark()))
			{
				busItemView.getVerticalTextView().setTextColor(
					busItemView.getClickTextColor());
			}
			if (!TextUtils.isEmpty(item.incomingBusNo))
			{
				ImageView imageView = new ImageView(getActivity());
				int imageViewWH = dp_48 * 2 / 3;
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					imageViewWH, imageViewWH);
				layoutParams.topMargin = 0;
				int paddingLTRB = dp_48 / 12;
				layoutParams.gravity = Gravity.TOP;
				if (isArrived(item))
				{
					layoutParams.leftMargin = dp_48 * i + imageViewWH / 4;
					imageView.setImageResource(R.drawable.ic_bus_arrive);
				}
				else if (i < mData.size() - 1)
				{
					layoutParams.leftMargin = dp_48 * (1 + i) - imageViewWH / 2;
					imageView.setImageResource(R.drawable.ic_bus_ontheway);
				}
				frameLayout.addView(imageView, layoutParams);
				imageView.setPadding(paddingLTRB, paddingLTRB, paddingLTRB,
					paddingLTRB);
				imageView.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
							"到达" + item.stationName + "时间是"
								+ item.incomingBusTime);
						spannableStringBuilder.setSpan(new ForegroundColorSpan(
							0xFFFF0000), 2, 2 + item.stationName.length(),
							Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						Toast.makeText(getActivity(), spannableStringBuilder,
							Toast.LENGTH_SHORT).show();
					}
				});
			}
			busItemView.getVerticalTextView().setOnClickListener(
				new OnClickListener()
				{
					public void onClick(View v)
					{
						mClickedBusLineItem = item;
						mHostActivity.setStationMark(item.stationMark);
						mPreviousClickPosition = mClickPosition;
						View childView = linearLayout
							.getChildAt(mPreviousClickPosition);
						if (childView instanceof BusSiteView)
						{
							((BusSiteView) childView).getVerticalTextView()
								.setTextColor(busItemView.getTextColor());
							// System.out.println("mPreviousClickPosition ="
							// + mPreviousClickPosition);
						}
						onTextViewClick(item, busItemView);
						doLoadBusStationData();
					}

					private void onTextViewClick(final BusLineItem item,
						final BusSiteView busItemView)
					{
						mClickPosition = getClickPosition();
						busItemView.getVerticalTextView().setTextColor(
							busItemView.getClickTextColor());
						String busStopSpacing = getNearestCar();

						if (isArrived(item))
						{
							SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
								"正在进站");
							spannableStringBuilder.setSpan(
								new ForegroundColorSpan(0xFFFF0000), 0, 4,
								Spannable.SPAN_INCLUSIVE_INCLUSIVE);
							Toast.makeText(getActivity(),
								spannableStringBuilder, Toast.LENGTH_SHORT)
								.show();
						}
						else if (NO_CAR.equals(busStopSpacing))
						{
							Toast.makeText(getActivity(), NO_CAR,
								Toast.LENGTH_SHORT).show();
						}
						else if (ZERO.equals(busStopSpacing))
						{
							SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
								"即将到达" + item.stationName);
							spannableStringBuilder.setSpan(
								new ForegroundColorSpan(0xFFFF0000), 4,
								4 + item.stationName.length(),
								Spannable.SPAN_INCLUSIVE_INCLUSIVE);
							Toast.makeText(getActivity(),
								spannableStringBuilder, Toast.LENGTH_SHORT)
								.show();
						}
						else
						{
							SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
								"还有" + busStopSpacing + "站到达"
									+ item.stationName);
							spannableStringBuilder.setSpan(
								new ForegroundColorSpan(0xFFFF0000), 2,
								2 + busStopSpacing.length(),
								Spannable.SPAN_INCLUSIVE_INCLUSIVE);

							Toast.makeText(getActivity(),
								spannableStringBuilder, Toast.LENGTH_SHORT)
								.show();
						}
					}

					private String getNearestCar()
					{
						String busStopSpacing = "-1";
						// System.out.println("position = " + position);
						for (int i = mClickPosition; i >= 0; i--)
						{
							BusLineItem tmp = mData.get(i);
							if (!TextUtils.isEmpty(tmp.incomingBusTime))
							{
								busStopSpacing = mClickPosition - (i + 1) + "";
								break;
							}
						}
						if ("-1".equals(busStopSpacing))
						{
							busStopSpacing = NO_CAR;
						}
						if (IN_CAR.equals(busStopSpacing))
						{
							busStopSpacing = "0";
						}
						return busStopSpacing;
					}

					private int getClickPosition()
					{
						for (int ii = 0; ii < mData.size(); ii++)
						{
							if (mData.get(ii).stationMark
								.equals(item.stationMark))
							{
								return ii;
							}
						}
						return 0;
					}
				});
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

	private int getDefaultPosition()
	{
		for(int i = 0; i< mData.size();i++)
		{
			if(mData.get(i).stationMark.equals(mHostActivity.getStationMark()))
			{
				return i ;
			}
		}
		return  mData.size()/2;
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
		return calendar.getTimeInMillis() - busCalendar.getTimeInMillis() < ONE_MINUTE_IN_MM;
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

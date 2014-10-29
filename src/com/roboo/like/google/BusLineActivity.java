package com.roboo.like.google;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.roboo.like.google.adapters.BusStationAdapter2;
import com.roboo.like.google.fragments.BusLineFragment2;
import com.roboo.like.google.models.BusItem;
import com.roboo.like.google.models.BusStationItem;

/** 公交界面 */
public class BusLineActivity extends BaseLayoutActivity
{
	private static final String EXTRA_BUS_ITEM = "bus_item";
	/** 反向的BusItem信息 */
	private static final String EXTRA_BUS__INVERT_ITEM = "bus_invert_item";
	private BusItem mBusItem;
	private BusItem mBusInVertItem;
	private boolean mIsInvert = false;
	private boolean mIsActuralSelected = false;
	/**公交站台编号*/
	private String mStationMark;

	public static void actionBusLine(Activity activity, BusItem item)
	{
		Intent intent = new Intent(activity, BusLineActivity.class);
		intent.putExtra(EXTRA_BUS_ITEM, item);
		activity.startActivity(intent);
	}

	public static void actionBusLine(Activity activity, BusItem item,
		BusItem invertingItem)
	{
		Intent intent = new Intent(activity, BusLineActivity.class);
		intent.putExtra(EXTRA_BUS_ITEM, item);
		intent.putExtra(EXTRA_BUS__INVERT_ITEM, invertingItem);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);// TODO
		initView();
		mBusItem = (BusItem) getIntent().getSerializableExtra(EXTRA_BUS_ITEM);
		if (null != getIntent().getSerializableExtra(EXTRA_BUS__INVERT_ITEM))
		{
			mBusInVertItem = (BusItem) getIntent().getSerializableExtra(
				EXTRA_BUS__INVERT_ITEM);
		}
		customActionBar(mBusItem.busNo);
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager()
				.beginTransaction()
				.add(
					R.id.frame_container,
					BusLineFragment2.newInstance(mBusItem.busUrl,
						mBusItem.busName)).commit();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initView()
	{

	}

	public void invert()
	{
		if (null != mBusInVertItem)
		{
			if (!mIsInvert)
			{
				beginTransaction().replace(
					R.id.frame_container,
					BusLineFragment2.newInstance(mBusInVertItem.busUrl,
						mBusInVertItem.busName)).commit();
				mIsInvert = true;
			}
			else
			{
				beginTransaction().replace(
					R.id.frame_container,
					BusLineFragment2.newInstance(mBusItem.busUrl,
						mBusItem.busName)).commit();
				mIsInvert = false;
			}
		}
	}

	public void customActionBar(String busNo)
	{
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		if (TextUtils.isDigitsOnly(mBusItem.busNo))
		{
			mActionBar.setTitle("公交 - " + busNo + " 路");
		}
		else
		{
			mActionBar.setTitle("公交 - " + busNo);
		}
		mActionBar.setLogo(R.drawable.ic_abs_bus_up);
		mIsActuralSelected = false;
	}

	public void showNavActionBar(final BusStationAdapter2 adapter)
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setListNavigationCallbacks(adapter,
			new OnNavigationListener()
			{
				public boolean onNavigationItemSelected(int itemPosition,
					long itemId)
				{// 在这里进行切换 ，不可行 ， 默认情况下会执行一次该方法
					if (!mIsActuralSelected)
					{
						mIsActuralSelected = true;
						return true;
					}
//					Toast.makeText(
//						getBaseContext(),
//						"" + itemPosition
//							+ mActionBar.getSelectedNavigationIndex(),
//						Toast.LENGTH_SHORT).show();

					mActionBar
						.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					BusStationItem busStationItem = (BusStationItem) adapter
						.getItem(itemPosition);
					beginTransaction().replace(
						R.id.frame_container,
						BusLineFragment2.newInstance(busStationItem.stationUrl,
							busStationItem.busNo)).commit();
					customActionBar(busStationItem.busNo);
					return false;

				}
			});
	}

	@Override
	public void onBackPressed()
	{
		if (getSupportFragmentManager().getBackStackEntryCount() > 0)
		{
			getSupportFragmentManager().popBackStack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		// Toast.makeText(this, "用户触摸屏幕", Toast.LENGTH_SHORT).show();
	}

	public String getStationMark()
	{
		return mStationMark;
	}

	public void setStationMark(String stationMark)
	{
		mStationMark = stationMark;
	}

}

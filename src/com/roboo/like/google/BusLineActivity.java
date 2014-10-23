package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.roboo.like.google.fragments.BusLineFragment2;
import com.roboo.like.google.models.BusItem;

/** 公交界面 */
public class BusLineActivity extends BaseLayoutActivity
{
	private static final String EXTRA_BUS_ITEM = "bus_item";
	/** 反向的BusItem信息 */
	private static final String EXTRA_BUS__INVERT_ITEM = "bus_invert_item";
	private BusItem mBusItem;
	private BusItem mBusInVertItem;
	private boolean mIsInvert = false;

	public static void actionBusLine(Activity activity, BusItem item)
	{
		Intent intent = new Intent(activity, BusLineActivity.class);
		intent.putExtra(EXTRA_BUS_ITEM, item);
		activity.startActivity(intent);
	}

	public static void actionBusLine(Activity activity, BusItem item, BusItem invertingItem)
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
			mBusInVertItem = (BusItem) getIntent().getSerializableExtra(EXTRA_BUS__INVERT_ITEM);
		}
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, BusLineFragment2.newInstance(mBusItem.busUrl, mBusItem.busName)).commit();
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
				beginTransaction().replace(R.id.frame_container, BusLineFragment2.newInstance(mBusInVertItem.busUrl, mBusInVertItem.busName)).commit();
				mIsInvert = true;
			}
			else
			{
				beginTransaction().replace(R.id.frame_container, BusLineFragment2.newInstance(mBusItem.busUrl, mBusItem.busName)).commit();
				mIsInvert = false;
			}
		}
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		if (TextUtils.isDigitsOnly(mBusItem.busNo))
		{
			mActionBar.setTitle("公交 - " + mBusItem.busNo + " 路");
		}
		else
		{
			mActionBar.setTitle("公交 - " + mBusItem.busNo);
		}
		mActionBar.setLogo(R.drawable.ic_abs_bus_up);
	}
	@Override
	public void onBackPressed()
	{
		if(getSupportFragmentManager().getBackStackEntryCount() > 0)
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
}

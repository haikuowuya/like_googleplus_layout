package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.fragments.BusStationFragment;
import com.roboo.like.google.models.BusLineItem;

/** 公交界面 */
public class BusStationActivity extends BaseLayoutActivity
{
	 
	private static final String EXTRA_BUS_LINE_ITEM= "bus_line_item";
	private BusLineItem mBusLineItem;
	public static void actionBusStation(Activity activity,BusLineItem item)
	{
		Intent intent = new Intent(activity, BusStationActivity.class);
		intent.putExtra(EXTRA_BUS_LINE_ITEM, item);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);//TODO
		initView();
		mBusLineItem = (BusLineItem) getIntent().getSerializableExtra(EXTRA_BUS_LINE_ITEM);
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, BusStationFragment.newInstance(mBusLineItem.stationUrl)).commit();
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
	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("站点 - "+mBusLineItem.stationName);
		mActionBar.setLogo(R.drawable.ic_abs_bus_up);
	}
	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		// Toast.makeText(this, "用户触摸屏幕", Toast.LENGTH_SHORT).show();
	}
}

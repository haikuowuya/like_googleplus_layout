package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.fragments.BusFragment;
import com.roboo.like.google.fragments.BusLineFragment;
import com.roboo.like.google.models.BusItem;

/** 公交界面 */
public class BusActivity extends BaseLayoutActivity
{
	public static void actionBus(Activity activity)
	{
		Intent intent = new Intent(activity, BusActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);//TODO
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, BusFragment.newInstance()).commit();
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
	public void skipToBusLineFragment(String busLineUrl)
	{
		beginTransaction().replace(R.id.frame_container, BusLineFragment.newInstance(busLineUrl)).addToBackStack("bus").commit();
	}
	public void skipToBusLineFragment(BusItem item)
	{
		mActionBar.setTitle("公交 - "+item.busNo+ " 路");
		 skipToBusLineFragment(item.busUrl);
	}
	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("公交");
		mActionBar.setLogo(R.drawable.ic_abs_bus_up);
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		// Toast.makeText(this, "用户触摸屏幕", Toast.LENGTH_SHORT).show();
	}
}

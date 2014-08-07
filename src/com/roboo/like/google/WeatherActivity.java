package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.fragments.WeatherFragment;
import com.roboo.like.google.models.CityItem;
 
public class WeatherActivity extends BaseLayoutActivity
{
	private static final String EXTRA_CITY_ITEM="city_item";
	private CityItem mCityItem;
	public static void actionWeather(Activity activity, CityItem cityItem)
	{
		Intent intent = new Intent(activity, WeatherActivity.class);
		intent.putExtra(EXTRA_CITY_ITEM, cityItem);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		mCityItem = (CityItem) getIntent().getSerializableExtra(EXTRA_CITY_ITEM);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, WeatherFragment.newInstance(mCityItem)).commit();
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
		mActionBar.setTitle(mCityItem.cName+"天气");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		// Toast.makeText(this, "用户触摸屏幕", Toast.LENGTH_SHORT).show();
	}
}

package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.roboo.like.google.fragments.BusLineFragment;
import com.roboo.like.google.fragments.WeatherFragment;
import com.roboo.like.google.models.CityItem;

/** 短信界面 */
public class WeatherActivity extends BaseLayoutActivity
{
	public static void actionWeather(Activity activity)
	{
		Intent intent = new Intent(activity, WeatherActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, WeatherFragment.newInstance()).commit();
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
	public void showCity(CityItem cityItem)
	{
		if(TextUtils.isEmpty(cityItem.cName))
		{
			beginTransaction().replace(R.id.frame_container, WeatherFragment.newInstance(cityItem)).addToBackStack("province").commit();
		}
		else
		{
			System.out.println(" cityItem.cUrl = " + cityItem.cUrl);
			WebViewActivity.actionWebView(this, cityItem.cUrl, cityItem.cName);
		}
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("天气信息");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		// Toast.makeText(this, "用户触摸屏幕", Toast.LENGTH_SHORT).show();
	}
}

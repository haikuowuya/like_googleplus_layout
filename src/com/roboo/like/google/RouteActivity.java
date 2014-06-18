package com.roboo.like.google;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class RouteActivity extends BaseActivity
{
	public static final String EXTRA_ROUTE="route";
	public static void actionRoute(Activity activity)
	{
		Intent intent = new Intent(activity,RouteActivity.class);
		activity.startActivity(intent);
	}
	public static void actionRoute(Activity activity,Serializable serializable)
	{
		Intent intent = new Intent(activity,RouteActivity.class);
		intent.putExtra(EXTRA_ROUTE, serializable);
		activity.startActivity(intent);
	}
	
	public static void actionRouteForResult (Activity activity,int requestCode)
	{
		Intent intent = new Intent(activity,RouteActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);//TODO
		initView();
		customActionBar();
		 
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
		mActionBar.setTitle("路线详情");
		mActionBar.setLogo(R.drawable.ic_abs_route_up);
	}
}

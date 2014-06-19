package com.roboo.like.google;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteActivity extends BaseLayoutActivity
{
	private LinearLayout mLinearContainer;
	public static final String EXTRA_ROUTE = "route";
	public ArrayList<String> mRouteList;

	public static void actionRoute(Activity activity)
	{
		Intent intent = new Intent(activity, RouteActivity.class);
		activity.startActivity(intent);
	}

	public static void actionRoute(Activity activity, ArrayList<String> list)
	{
		Intent intent = new Intent(activity, RouteActivity.class);
		intent.putExtra(EXTRA_ROUTE, list);
		activity.startActivity(intent);
	}

	public static void actionRouteForResult(Activity activity, int requestCode)
	{
		Intent intent = new Intent(activity, RouteActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);// TODO
		mRouteList = getIntent().getStringArrayListExtra(EXTRA_ROUTE);
		initView();
		customActionBar();
		fillData(mRouteList);

	}

	private void fillData(ArrayList<String> routeList)
	{
		if (null != routeList)
		{
			for (int i = 0; i < routeList.size(); i++)
			{
				String str = routeList.get(i);
				if (i == 0)
				{
					str = "我的位置";
				}
				TextView textView = new TextView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				int paddingLTBR = (int) getResources().getDimension(R.dimen.dimen_textview_padding_ltbr);
				params.gravity = Gravity.CENTER_VERTICAL;
				textView.setLayoutParams(params);
				Drawable drawableLeft = getDrawableLeft(str);
				mLinearContainer.addView(textView);
				if (i != routeList.size() - 1)
				{
					if (i != 0)
					{
						View view = new View(this);
						view.setBackgroundColor(0xFFFF0000);
						params.topMargin = paddingLTBR / 2;
						view.setLayoutParams(params);
						mLinearContainer.addView(view);
					}
				}
				else
				{
					drawableLeft = getResources().getDrawable(R.drawable.ic_nav_turn_end_s);
				}
				textView.setClickable(true);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setBackgroundResource(R.drawable.list_item_selector);
				textView.setPadding(paddingLTBR, paddingLTBR, paddingLTBR, paddingLTBR);
				textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
				textView.setCompoundDrawablePadding(paddingLTBR);
				textView.setText(str);
			}
		}
	}

	private Drawable getDrawableLeft(String str)
	{
		Drawable drawable = null;
		if (str.equals("我的位置"))
		{
			drawable = getResources().getDrawable(R.drawable.ic_nav_turn_start_s);
		}
		else if (str.contains("左"))
		{
			drawable = getResources().getDrawable(R.drawable.ic_nav_turn_left_s);
		}
		else if (str.contains("右"))
		{
			drawable = getResources().getDrawable(R.drawable.ic_nav_turn_right_s);
		}
		else if (str.contains("调头"))
		{
			drawable = getResources().getDrawable(R.drawable.ic_nav_turn_back_s);

		}
		else
		{
			drawable = getResources().getDrawable(R.drawable.ic_nav_turn_front_s);
		}
		return drawable;
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
		mLinearContainer = (LinearLayout) findViewById(R.id.linear_container);

	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("路线详情");
		mActionBar.setLogo(R.drawable.ic_abs_route_up);
	}
}

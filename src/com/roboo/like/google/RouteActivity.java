package com.roboo.like.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteActivity extends BaseLayoutActivity
{
	public static final int TYPE_WALKING = 0;
	public static final int TYPE_DIVIDER = 1;
	public static final int TYPE_BUS = 2;

	private LinearLayout mLinearContainer;
	public static final String EXTRA_ROUTE = "route";
	public static final String EXTRA_TYPE = "type";
	public ArrayList<String> mRouteList;
	private TextView mTvRouteType;
	private String toAddres;

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

	public static void actionRoute(Activity activity, ArrayList<String> list, int type)
	{
		Intent intent = new Intent(activity, RouteActivity.class);
		intent.putExtra(EXTRA_ROUTE, list);
		intent.putExtra(EXTRA_TYPE, type);
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
		String text;
		if (getIntent().getIntExtra(EXTRA_TYPE, 0) == TYPE_WALKING)
		{
			text = "步行路线";
		}
		else if (getIntent().getIntExtra(EXTRA_TYPE, 0) == TYPE_DIVIDER)
		{
			text = "驾车路线";
		}
		else
		{
			text = "公交路线";
		}
		mTvRouteType.setText(text);

	}

	private void fillData(ArrayList<String> routeList)
	{
		if (null != routeList)
		{
			int paddingLTBR = (int) getResources().getDimension(R.dimen.dimen_textview_padding_ltbr);

			for (int i = 0; i < routeList.size(); i++)
			{
				String str = routeList.get(i);
				if (i == 0)
				{
					str = "我的位置";
				}
				TextView textView = new TextView(this, null, R.style.Base_List_Item_TextView_Style);
				textView.setTextColor(R.drawable.text_color_selector);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.CENTER_VERTICAL;
				textView.setLayoutParams(params);
				Drawable drawableLeft = getDrawableLeft(str);
				mLinearContainer.addView(textView);
				View view = new View(this);
				view.setBackgroundColor(0xFFFFFFFF);
				params.topMargin = paddingLTBR / 2;
				view.setLayoutParams(params);
				mLinearContainer.addView(view);
				if (i != routeList.size() - 1)
				{
				}
				else
				{
					toAddres = str;
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
			Button button = new Button(this);
			button.setGravity(Gravity.CENTER);
			button.setBackgroundResource(R.drawable.list_item_selector);
			button.setPadding(paddingLTBR, paddingLTBR, paddingLTBR, paddingLTBR);
			button.setText("嘀嘀打车");
			button.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					WebViewActivity.actionDidi(RouteActivity.this, getDidiUrl(toAddres, null)); 
				}
			});
			mLinearContainer.addView(button);
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
		mTvRouteType = (TextView) findViewById(R.id.tv_route_type);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("路线详情");
		mActionBar.setLogo(R.drawable.ic_abs_route_up);
	}

	public String getDidiUrl(String toAddress, String toShop)
	{
		String url = null;
		String city = mPreferences.getString(PREF_LOACTION_CITY, DEFAULT_CITY);
		String longitude = mPreferences.getString(PREF_LOACTION_LONGITUDE, SUZHOU_LONGITUDE + "");
		String latitude = mPreferences.getString(PREF_LOACTION_LATITUDE, SUZHOU_LATITUDE + "");
		String fromAddress = mPreferences.getString(PREF_LOACTION_ADDRESS, DEFAULT_ADDRESS);
		try
		{
			city = URLEncoder.encode(city, HTTP.UTF_8);
			fromAddress = URLEncoder.encode(fromAddress, HTTP.UTF_8);
			if (!TextUtils.isEmpty(toAddress))
			{
				toAddress = URLEncoder.encode(toAddress, HTTP.UTF_8);
			}

			if (!TextUtils.isEmpty(toShop))
			{
				toShop = URLEncoder.encode(toShop, HTTP.UTF_8);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		url = GoogleApplication.BASE_DIDI_URL + city + "&maptype=baidu&fromlat=" + latitude + "&fromlng=" + longitude + "&fromaddr=" + fromAddress + "&channel=1224&d=" + System.currentTimeMillis();
		if (!TextUtils.isEmpty(toAddress))
		{
			url = url + "&toaddr=" + toAddress;
		}
		if (!TextUtils.isEmpty(toShop))
		{
			url = url + "&toshop=" + toShop;
		}
		System.out.println("didiUrl = " + url);
		return url;
	}
}

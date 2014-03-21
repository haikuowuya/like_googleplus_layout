package com.roboo.like.google;

import java.util.LinkedList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.roboo.like.google.adapters.NewsTypeListAdapter;
import com.roboo.like.google.fragments.ContentFragment;
import com.roboo.like.google.fragments.LeftFragment;
import com.roboo.like.google.fragments.RightFragment;
import com.roboo.like.google.models.NewsTypeItem;

public class MainActivity extends FragmentActivity
{
	/** 默认是Android之家 */
	public static final String IT_ANDROID = "http://it.ithome.com/category/10_";
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	protected ActionBar mActionBar;
	private NewsTypeListAdapter mAdapter;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_left_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_left_container, LeftFragment.newInstance()).commit();
		}
		if (getSupportFragmentManager().findFragmentById(R.id.frame_right_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_right_container, RightFragment.newInstance()).commit();
		}
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, ContentFragment.newInstance(IT_ANDROID)).commit();
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
			{
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
			{
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
			else
			{
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
			return true;

		case R.id.menu_notification:
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
			{
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
			{
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
			else
			{
				mDrawerLayout.openDrawer(Gravity.RIGHT);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void customActionBar()
	{
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setListNavigationCallbacks(getAdapter(), new OnNavigationListener()
		{
			public boolean onNavigationItemSelected(int itemPosition, long itemId)
			{
				NewsTypeItem item = (NewsTypeItem) mAdapter.getItem(itemPosition);
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ContentFragment.newInstance(item.url)).commit();
				 return true;
			}
		});
	}

	public void initView()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);

	}

	private BaseAdapter getAdapter()
	{
		String[] arrays = getResources().getStringArray(R.array.actionbar_navigation_list_text_arrays);

		LinkedList<NewsTypeItem> data = new LinkedList<NewsTypeItem>();
		for (String str : arrays)
		{
			String[] tmp = str.split("#");
			if (tmp.length > 1)
			{
				NewsTypeItem item = new NewsTypeItem();
				item.name = tmp[0];
				item.url = tmp[1];
				data.add(item);
			}
		}
		 mAdapter =  new NewsTypeListAdapter(data, this);
		 return mAdapter;
	}

	public void closeLeftDrawer()
	{
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
		{
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}

}

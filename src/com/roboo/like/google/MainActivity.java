package com.roboo.like.google;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import com.roboo.like.google.adapters.NewsTypeListAdapter;
import com.roboo.like.google.fragments.LeftFragment;
import com.roboo.like.google.fragments.MainFragment;
import com.roboo.like.google.fragments.RightFragment;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.utils.DataUtils;

public class MainActivity extends BaseActivity
{
	/** 默认是IT之家 */
	public static final String IT_HOME = "http://it.ithome.com/category/1_";
	private static final String WIFI = "WIFI";
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	protected ActionBar mActionBar;
	private NewsTypeListAdapter mAdapter;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// TODO
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
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, MainFragment.newInstance(IT_HOME)).commit();
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
			return true;
		case R.id.menu_download:
			showDownloadDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void customActionBar()
	{
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setListNavigationCallbacks(getAdapter(), new OnNavigationListener()
		{
			public boolean onNavigationItemSelected(int itemPosition, long itemId)
			{
				NewsTypeItem item = (NewsTypeItem) mAdapter.getItem(itemPosition);
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, MainFragment.newInstance(item.url)).commit();
				mDrawerLayout.closeDrawers();
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
		mAdapter = new NewsTypeListAdapter(DataUtils.handleNewsType(this), this);
		return mAdapter;
	}

	public void closeLeftDrawer()
	{
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
		{
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			GoogleApplication application = (GoogleApplication) getApplication();
			application.unBindNetworkService();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void wifiDownload()
	{
		if (WIFI.equals(GoogleApplication.mNetworkType))
		{
			Intent intent = new Intent(this, WIFIDownloadService.class);
			startService(intent);
		}
	}

	private void showDownloadDialog()
	{
		AlertDialog dialog = new AlertDialog.Builder(this).setIcon(getApplicationInfo().icon).setTitle("离线下载新闻").setMessage("是否WIFI下离线下载所有新闻").setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				wifiDownload();
			}
		}).create();
		dialog.show();
	}
	public void showCreateDesktopDialog()
	{
		AlertDialog dialog = new AlertDialog.Builder(this).setIcon(getApplicationInfo().icon).setTitle("创建快捷方式").setMessage("是否在桌面上创建一个快捷方式图标").setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				 createDesktop();
			}
		}).create();
		dialog.show();
	}

	private void createDesktop()
	{
		// 安装的Intent
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "IT之家");
		// 快捷图标是允许重复
		shortcut.putExtra("duplicate", false);
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(getPackageName(), getPackageName() + ".MainActivity");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 快捷图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		// 发送广播
		sendBroadcast(shortcut);
		// Toast.makeText(mActivity, "发送到桌面", Toast.LENGTH_SHORT).show();
	}
}

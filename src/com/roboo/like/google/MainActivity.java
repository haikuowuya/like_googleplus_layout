package com.roboo.like.google;

import java.util.LinkedList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.roboo.like.google.adapters.NewsTypeListAdapter;
import com.roboo.like.google.fragments.ActionSheetFragment;
import com.roboo.like.google.fragments.BaseMainFragment;
import com.roboo.like.google.fragments.LeftFragment;
import com.roboo.like.google.fragments.MainGridFragment;
import com.roboo.like.google.fragments.MainListFragment;
import com.roboo.like.google.fragments.MainPinGridFragment;
import com.roboo.like.google.fragments.RightFragment;
import com.roboo.like.google.models.SubNewsTypeItem;

public class MainActivity extends BaseActivity
{
	public boolean mIsDownload = false;
	private static final int STYLE_LIST = 1;
	private static final int STYLE_GRID = 2;
	private static final int STYLE_PINTEREST = 3;
	/** 默认是IT之家 */
	public static final String IT_HOME = "http://it.ithome.com/category/1_";
	private static final String WIFI = "WIFI";
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	protected ActionBar mActionBar;
	private NewsTypeListAdapter mAdapter;
	private int mDisplayStyle = STYLE_LIST;
	private BaseMainFragment mMainFragment = null;
	private String mCurrentURL = IT_HOME;
	private Menu mMenu;
	private LinkedList<SubNewsTypeItem> mData;
	private boolean mIsOnlyAndroid;
	/** 当mMainFragment为MainListFragment时 ，是否快速滑动开启 */
	private boolean mFastScrollEnable;
	/**为了减少一次数据加载，onCreate方法和设置ActionBar导航列表模式时都会去加载数据*/
	private boolean mIsFirstEnter = true;

	public static void actionMain(Activity activity)
	{
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// TODO
		testLockScreen();
		init();
		initView();
		initData();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_left_container) == null)
		{
			getSupportFragmentManager().beginTransaction()
				.add(R.id.frame_left_container, LeftFragment.newInstance()).commit();
		}
		if (getSupportFragmentManager().findFragmentById(R.id.frame_right_container) == null)
		{
			getSupportFragmentManager().beginTransaction()
				.add(R.id.frame_right_container, RightFragment.newInstance()).commit();
		}
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, mMainFragment)
				.commit();
		}
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
			R.string.app_name, R.string.app_name);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	private void testLockScreen()
	{
//		makeFullScreen();
		startService(new Intent(this, LockScreenService.class));
	}

	public void makeFullScreen()
	{
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (Build.VERSION.SDK_INT < 19)
		{ // View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
			this.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
		else
		{
			this.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 0x00000800);// View.SYSTEM_UI_FLAG_IMMERSIVE);
		}
	}

	private void init()
	{
		mFastScrollEnable = mPreferences.getBoolean(PREF_FAST_SCROLL, true);
		mIsOnlyAndroid = GoogleApplication.mIsOnlyAndroid;
	}

	private void initData()
	{
		mData = SubNewsTypeItem.handleNewsType(this);
		mAdapter = new NewsTypeListAdapter(mData, this);
		mCurrentURL = mData.get(0).url;
		mMainFragment = MainListFragment.newInstance(mCurrentURL);
	}

	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mIsOnlyAndroid != GoogleApplication.mIsOnlyAndroid)
		{
			initData();
			mActionBar.setListNavigationCallbacks(mAdapter, new OnNavigationListener()
			{
				public boolean onNavigationItemSelected(int itemPosition, long itemId)
				{
					SubNewsTypeItem item = (SubNewsTypeItem) mAdapter.getItem(itemPosition);
					mCurrentURL = item.url;
					if (!mIsFirstEnter)
					{
						updateFragment();
						mIsFirstEnter = false;
					}
					return true;
				}
			});
			mIsOnlyAndroid = GoogleApplication.mIsOnlyAndroid;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.menu_fast_scroll).setChecked(mFastScrollEnable);
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
		case R.id.menu_list:// 列表显示
			mDisplayStyle = STYLE_LIST;
			mMenu.findItem(R.id.menu_grid).setChecked(false);
			mMenu.findItem(R.id.menu_pinterest).setChecked(false);
			mMenu.findItem(R.id.menu_fast_scroll).setCheckable(true);
			if (!item.isChecked())
			{
				updateFragment();
			}
			item.setChecked(true);
			return true;
		case R.id.menu_grid:// 网格显示
			mDisplayStyle = STYLE_GRID;
			mMenu.findItem(R.id.menu_list).setChecked(false);
			mMenu.findItem(R.id.menu_pinterest).setChecked(false);
			mMenu.findItem(R.id.menu_fast_scroll).setCheckable(true);
			if (!item.isChecked())
			{
				updateFragment();
			}
			item.setChecked(true);

			return true;
		case R.id.menu_pinterest:// 瀑布流显示
			mDisplayStyle = STYLE_PINTEREST;
			mMenu.findItem(R.id.menu_list).setChecked(false);
			mMenu.findItem(R.id.menu_grid).setChecked(false);
			mMenu.findItem(R.id.menu_fast_scroll).setCheckable(false);

			if (!item.isChecked())
			{
				updateFragment();
			}
			item.setChecked(true);
			return true;
		case R.id.menu_fast_scroll:// 快速滑动
			if (item.isCheckable())
			{
				mFastScrollEnable = !item.isChecked();
				item.setChecked(mFastScrollEnable);
				mPreferences.edit().putBoolean(PREF_FAST_SCROLL, mFastScrollEnable).commit();
				mMainFragment.setFastScrollEnable(mFastScrollEnable);
			}
			return true;
		case R.id.menu_settings:// 设置
			SettingsActivity.actionSettings(this);
			return true;
		case R.id.menu_help:// 帮助
			return true;
		case R.id.menu_gallery_flow:// GalleryFlow
			DroidGalleryFlowActivity.actionPinterest(this);
			return true;
		case R.id.menu_download:
			// showDownloadDialog();
			if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			{
				getSupportFragmentManager().beginTransaction()
					.add(Window.ID_ANDROID_CONTENT, ActionSheetFragment.newInstance())
					.addToBackStack("actionSheet").commit();
			}
			else
			{
				getSupportFragmentManager().popBackStack();
			}
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
		mActionBar.setListNavigationCallbacks(mAdapter, new OnNavigationListener()
		{
			public boolean onNavigationItemSelected(int itemPosition, long itemId)
			{
				SubNewsTypeItem item = (SubNewsTypeItem) mAdapter.getItem(itemPosition);
				mCurrentURL = item.url;
				updateFragment();
				return true;
			}
		});
	}

	private void updateFragment()
	{
		if (mDisplayStyle == STYLE_LIST)
		{
			mMainFragment = MainListFragment.newInstance(mCurrentURL);
		}
		if (mDisplayStyle == STYLE_GRID)
		{
			mMainFragment = MainGridFragment.newInstance(mCurrentURL);
		}
		else if (mDisplayStyle == STYLE_PINTEREST)
		{
			mMainFragment = MainPinGridFragment.newInstance(mCurrentURL);
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mMainFragment)
			.commit();
		mDrawerLayout.closeDrawers();
	}

	public void initView()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);
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
			mIsDownload = true;
			Intent intent = new Intent(this, WIFIDownloadService.class);
			startService(intent);
		}
	}

	private void showDownloadDialog()
	{
		AlertDialog dialog = new AlertDialog.Builder(this).setIcon(getApplicationInfo().icon)
			.setTitle("离线下载新闻").setMessage("是否WIFI下离线下载所有新闻").setNegativeButton("取消", null)
			.setPositiveButton("确定", new OnClickListener()
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
		AlertDialog dialog = new AlertDialog.Builder(this).setIcon(getApplicationInfo().icon)
			.setTitle("创建快捷方式").setMessage("是否在桌面上创建一个快捷方式图标").setNegativeButton("取消", null)
			.setPositiveButton("确定", new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					createDesktop();
				}
			}).create();
		dialog.setCanceledOnTouchOutside(false);
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
		shortcutIntent.setClassName(getPackageName(), getPackageName() + ".StartActivity");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 快捷图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this,
			R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 发送广播
		sendBroadcast(shortcut);
		// Toast.makeText(mActivity, "发送到桌面", Toast.LENGTH_SHORT).show();
	}
}

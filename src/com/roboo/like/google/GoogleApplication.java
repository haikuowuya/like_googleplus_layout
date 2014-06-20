package com.roboo.like.google;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class GoogleApplication extends Application
{
	public static final int  TYPE_ITHOME = 0;
	public static final int  TYPE_CSDN = 1;
	public static final int  TYPE_PHONEKR = 2;
	public static int  mCurrentType = TYPE_CSDN;
	private LinkedList<Activity> mActivities = new LinkedList<Activity>();
	public static final String BASE_DIDI_URL = "http://pay.xiaojukeji.com/api/v2/webapp?city=";
	private static GoogleApplication mInstance;
	/** IT之家的图片地址前缀 */
	public static final String PREFIX_ITHOME_IMG_URL = "http://img.ithome.com";
	/** CSDN的图片地址前缀 */
	public static final String PREFIX_CSDN_IMG_URL = "http://cms.csdnimg.cn";
	/** 用于测试切换ITHOME和CSDN的一个条件 */
	public static final String BASE_OFFICE_URL = "http://it.ithome.com/category/8_";
	public static final String TEST_CSDN_BASE_URL = "http://mobile.csdn.net/mobile/";

	public static final String BASE_COMMENT_URL = "http://www.ithome.com/ithome/GetAjaxData.aspx?type=commentpage";
	/** 获取评论的URL */
	public static final String DUMMY_COMMENT_URL = "http://www.ithome.com/ithome/GetAjaxData.aspx?newsid=78507&type=commentpage&page=1";
	/** 用于测试 */
	public static boolean TEST = true;
	/** DEBUG TAG */
	private static final String DEBUG_LOG_TAG = "GoogleApplication";
	private Intent mIntent;
	/** 当前设备处于的网络类型 */
	public static String mNetworkType = "NONE";

	protected ServiceConnection mServiceConnection = new ServiceConnection()
	{
		public void onServiceDisconnected(ComponentName name)
		{
			if (TEST)
			{
				Log.d(DEBUG_LOG_TAG, "网络服务断开连接");
			}
		}

		public void onServiceConnected(ComponentName name, IBinder service)
		{
			if (TEST)
			{
				Log.d(DEBUG_LOG_TAG, "网络服务已经连接");
			}
		}
	};

	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
		bindNetworkService();
	}

	private void bindNetworkService()
	{

		mIntent = new Intent(this, NetworkService.class);
		startService(mIntent);
		// bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	public void unBindNetworkService()
	{
		// unbindService(mServiceConnection);
		stopService(mIntent);
	}

	public BMapManager mBMapManager;
	public boolean m_bKeyRight = true;

	public void initEngineManager(Context context)
	{
		if (mBMapManager == null)
		{
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener()))
		{
			Toast.makeText(GoogleApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener
	{
		@Override
		public void onGetNetworkState(int iError)
		{
			if (iError == MKEvent.ERROR_NETWORK_CONNECT)
			{
				Toast.makeText(GoogleApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG).show();
			}
			else if (iError == MKEvent.ERROR_NETWORK_DATA)
			{
				Toast.makeText(GoogleApplication.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}

		}

		@Override
		public void onGetPermissionState(int iError)
		{
			// 非零值表示key验证未通过
			if (iError != 0)
			{
				// 授权Key错误：
				Toast.makeText(GoogleApplication.getInstance().getApplicationContext(), "请 输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
				GoogleApplication.getInstance().m_bKeyRight = false;
			}
			else
			{
				GoogleApplication.getInstance().m_bKeyRight = true;
				Toast.makeText(GoogleApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG).show();
			}
		}
	}

	public static GoogleApplication getInstance()
	{

		return mInstance;
	}

	public void recordActivity(Activity activity)
	{
		mActivities.add(activity);
	}

	public void exitClient()
	{
		if (mActivities.size() > 0)
		{
			for (int i = 0; i < mActivities.size(); i++)
			{
				Activity activity = mActivities.get(i);
				if (null != activity)
				{
					activity.finish();
				}
			}
		}
	}
}

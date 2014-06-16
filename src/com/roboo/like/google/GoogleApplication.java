package com.roboo.like.google;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class GoogleApplication extends Application
{
	public static final String TYPE_ITHOME = "ithome";
	public static final String TYPE_CSDN="csdn";
	public static String mCurrentType = TYPE_CSDN;
	/**用于测试切换ITHOME和CSDN的一个条件*/
	public static final String BASE_OFFICE_URL="http://it.ithome.com/category/8_";
	public static final String BASE_COMMENT_URL= "http://www.ithome.com/ithome/GetAjaxData.aspx?type=commentpage";
	/**获取评论的URL*/
	public static final String DUMMY_COMMENT_URL= "http://www.ithome.com/ithome/GetAjaxData.aspx?newsid=78507&type=commentpage&page=1";
	/**用于测试*/
	public static boolean TEST = true;
	/**DEBUG TAG*/
	private static final String DEBUG_LOG_TAG = "GoogleApplication";
	private Intent mIntent;
	/**当前设备处于的网络类型*/
	public static  String mNetworkType = "NONE";

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

}

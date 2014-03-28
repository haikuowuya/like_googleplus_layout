package com.roboo.like.google;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NetworkService extends Service
{
	private static final String DEBUG_LOG_TAG = "NetworkService";
	private NetworkInfo mNetworkInfo = null;
	private ConnectivityManager mConnectivityManager = null;
	private BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
			{
				NotificationCompat.BigPictureStyle mPictureStyle = new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).setBigContentTitle("大图片标题").setSummaryText("大图片概述标题");
				NotificationCompat.BigTextStyle mTextStyle = new NotificationCompat.BigTextStyle().bigText("大文字").setBigContentTitle("大文字标题").setSummaryText("概述标题");
				// 以前的notification
				// sendNotification("网络状态已经改变");
				// 可以制定样式的notification
//				sendNotification(mPictureStyle);
//				sendNotification(mTextStyle);
				System.out.println("网络状态已经改变");
				mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
				if (mNetworkInfo != null && mNetworkInfo.isAvailable())
				{
					String name = mNetworkInfo.getTypeName();
					System.out.println("当前网络名称：" + name);
					GoogleApplication.mNetworkType = name;
				}
				else
				{
					System.out.println("没有可用网络");
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		Binder binder = new Binder();
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent)
	{
		super.onRebind(intent);
	}

	@Override
	public void onCreate()
	{
		IntentFilter mFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (GoogleApplication.TEST)
		{
			Log.d(DEBUG_LOG_TAG, "服务开启");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy()
	{
		unregisterReceiver(mReceiver);
		if (GoogleApplication.TEST)
		{
			Log.d(DEBUG_LOG_TAG, "服务停止");
		}
		super.onDestroy();
	}

	public void sendNotification(String state)
	{
		/**
		 * 一个通知的实现过程 1.获取通知管理器对象 2.创建通知对象和一个Intent对象 3.创建一个预处理的Intent对像
		 * 4.通过通知管理器对象发送通知
		 * 
		 */

		// 获取通知管理器对象
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		// 创建通知对象
		/***
		 * icon:通知的图标 tickerText：当用户第一次收到通知时显示在状态栏中的文字 when:长整型的，应该是通知到达的时间
		 */
		Notification n = new Notification(R.drawable.ic_launcher, state, System.currentTimeMillis());
		// 创建Intent对象
		Intent intent = new Intent(this, MainActivity.class);

		// 创建预处理的Intent对象
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(this, "hello", "23232323232223", pendingIntent);

		// 发送通知
		nm.notify("李波", 1, n);
	}

	/***
	 * 使用新版本创建并发送通知
	 * 
	 * @param style
	 *            :创建通知时使用的样式
	 */
	public void sendNotification(NotificationCompat.Style style)
	{

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("你好").setContentText("消息").setWhen(System.currentTimeMillis())
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		if (null != style)
		{
			mBuilder.setStyle(style);
		}
		Notification mNotification = mBuilder.build();
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify("libo", 1, mNotification);
	}
}

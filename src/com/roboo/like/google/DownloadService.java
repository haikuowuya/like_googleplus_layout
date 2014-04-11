package com.roboo.like.google;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

public class DownloadService extends Service
{
	public static final int NOTIFY_ID = 100;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;

	private String saveFileName = "RobooTemplate.apk";

	private DownloadBinder binder;
	private boolean serviceIsDestroy = false;
	private Context mContext = this;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case 0:
				// 下载完毕
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				break;
			case 2:
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				if (rate < 100)
				{
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate, false);
				}
				else
				// 下载完毕后变换通知形式
				{

					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.contentView = null;
					// Intent intent = new Intent(mContext, MainActivity.class);
					Intent intent = new Intent();
					// 告知已完成
					intent.putExtra("completed", "yes");
					// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
					PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					mNotification.setLatestEventInfo(mContext, "下载完成", "文件已下载完毕", contentIntent);
					serviceIsDestroy = true;
					stopSelf();// 停掉服务自身
					installApk();
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	public IBinder onBind(Intent intent)
	{
		System.out.println("是否执行了 onBind");
		return binder;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		System.out.println("downloadservice ondestroy");
		// 假如被销毁了，无论如何都默认取消了。
		serviceIsDestroy = true;
		stopSelf();
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.cancel(NOTIFY_ID);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		System.out.println("downloadservice onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent)
	{
		super.onRebind(intent);
		System.out.println("downloadservice onRebind");
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		saveFileName = TemplateApplication.getSaveApkFilePath() + saveFileName;
		System.out.println("saveFilePathName = " + saveFileName);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		binder = new DownloadBinder();
		binder.start();
		return super.onStartCommand(intent, flags, startId);
	}

	public class DownloadBinder extends Binder
	{
		public void start()
		{
			if (downLoadThread == null || !downLoadThread.isAlive())
			{
				progress = 0;
				setUpNotification();
				new Thread()
				{
					public void run()
					{
						startDownload();
					};
				}.start();
			}
		}

		public void cancel()
		{
			canceled = true;
		}

		public int getProgress()
		{
			return progress;
		}

		public boolean isCanceled()
		{
			return canceled;
		}

		public boolean serviceIsDestroy()
		{
			return serviceIsDestroy;
		}

		public void cancelNotification()
		{
			mHandler.sendEmptyMessage(2);
		}
	}

	private void startDownload()
	{
		canceled = false;
		downloadApk();
	}

	Notification mNotification;

	/**
	 * 创建通知
	 */
	private void setUpNotification()
	{

		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(getApplicationInfo().icon, tickerText, when);
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification);
		contentView.setTextViewText(R.id.name, getString(R.string.app_name) + "  正在下载中……");

		contentView.setImageViewResource(R.id.image, getApplicationInfo().icon);

		// 指定个性化视图
		mNotification.contentView = contentView;
		Intent intent = new Intent(this, MainActivity.class);
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;

	private void downloadApk()
	{
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk()
	{
		File apkfile = new File(saveFileName);
		String[] command = { "chmod", "777", saveFileName };
		ProcessBuilder builder = new ProcessBuilder(command);
		try
		{
			builder.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (!apkfile.exists())
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);

	}

	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable()
	{
		public void run()
		{
			try
			{
				String updateURL = getString(R.string.apk_url);
				URL url = new URL(updateURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode = " + responseCode);
				if (responseCode == HttpStatus.SC_OK)
				{
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File apkFile = new File(saveFileName);
					if (!apkFile.getParentFile().exists())
					{
						apkFile.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[4096];
					do
					{
						int numread = is.read(buf);
						count += numread;

						progress = (int) (((float) count / length) * 100);
						// 更新进度
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						msg.arg1 = progress;
						System.out.println("下载进度 = " + progress + " file length = " + length + " count = " + count);

						if (progress >= lastRate + 1)
						{
							mHandler.sendMessage(msg);
							lastRate = progress;
						}
						if (numread <= 0)
						{
							// 下载完成通知安装
							mHandler.sendEmptyMessage(0);
							// 下载完了，cancelled也要设置
							canceled = true;
							break;
						}
						fos.write(buf, 0, numread);
						if (serviceIsDestroy)
						{
							mHandler.sendEmptyMessage(2);
						}
					}
					while (!canceled && !serviceIsDestroy);// 点击取消就停止下载.
					fos.close();
					is.close();
				}

			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	};

}

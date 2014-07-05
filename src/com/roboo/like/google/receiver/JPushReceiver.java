package com.roboo.like.google.receiver;

import com.roboo.like.google.JPushActivity;
import com.roboo.like.google.R;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver
{
	private static final String TAG = "PushReceiver";
	private Context mContext;

	public void onReceive(final Context context, Intent intent)
	{
		mContext = context;
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		}
		else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		}
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

		}
		else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

		}
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			Log.d(TAG, "用户点击打开了通知");
			System.out.println("bundle = " + bundle.toString());
			System.out.println("printBundle = " + printBundle(bundle));
			String extraString = bundle.getString(JPushInterface.EXTRA_EXTRA);
			// 打开自定义的Activity
			if (!TextUtils.isEmpty(extraString))
			{
				if ("{}".equals(extraString))
				{
					extraString = bundle.getString(JPushInterface.EXTRA_ALERT);
				}
				if (!TextUtils.isEmpty(extraString))
				{
					JPushActivity.actionJPush(context, extraString);
					Toast.makeText(context, extraString, Toast.LENGTH_SHORT).show();
				}
			}
		}
		else
		{
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle)
	{
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet())
		{
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}
			else
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

 
}

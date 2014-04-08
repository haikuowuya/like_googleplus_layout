package com.roboo.like.google.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
/***
 * 检查当前的网络是否可用
 * @author 李波
 * @version 1.0
 *
 */
public class NetWorkUtils
{
	/**
	 * 判断当前网络是否可用
	 * @param context
	 * @return
	 */
	 public static  boolean isNetworkAvailable(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();     
			return (info != null && info.isConnected());
		}
	 
	 /**
	  * 弹出一个网络不可以使用的对话框
	  * @param context
	  * @return
	  */
	 public static AlertDialog showNetworkDisableDialog(Context context)
	 {
		 AlertDialog dialog ;
			dialog= new AlertDialog.Builder(context).create();
			dialog.setTitle("网络状况");
			dialog.setIcon(context.getApplicationInfo().icon);
			dialog.setMessage("当前网络不可用,请设置网络");
			dialog.setCanceledOnTouchOutside(false);
			final Context context2 = context;
			dialog.setButton(DialogInterface.BUTTON_POSITIVE , "设置", new DialogInterface.OnClickListener()
			{				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context2.startActivity(intent);
				}
			});
			return dialog;
	 }
	 
}

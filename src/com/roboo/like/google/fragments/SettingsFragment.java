package com.roboo.like.google.fragments;

import java.io.DataOutputStream;

import net.dynamicandroid.listview.DynamicScrollView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.roboo.like.google.BaseActivity;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;

/***
 * 设置Fragment
 * 
 * @author bo.li 2014-7-21 下午5:40:54 TODO
 */
public class SettingsFragment extends BaseFragment
{
	private DynamicScrollView mScrollView;
	/**获取WIFI信息*/
	private  TextView mTvWifi;
	/**去应用市场给程序评分*/
	private TextView mTvAppGrade;
	/** 测试获取系统最高权限 */
	private TextView mTvRoot;
	/** 只显示Android资讯的TextView */
	private CheckedTextView mCtvAndroid;

	// {{创建SettingsFragment实例方法体
	/**
	 * 创建一个SettingsFragment实例
	 * 
	 * @return SettingsFragment实例
	 */
	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}

	// }}
	// {{ onCreateView方法体

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = null;
		if (null == savedInstanceState)
		{
			view = inflater.inflate(R.layout.fragment_settings, null);// TODO
			initView(view);
		}
		return view;
	}

	// }}
	// {{ initView 方法体
	private void initView(View view)
	{
		mScrollView = (DynamicScrollView) view.findViewById(R.id.dsv_scrollview);
		mCtvAndroid = (CheckedTextView) view.findViewById(R.id.ctv_android);
		mTvAppGrade = (TextView) view.findViewById(R.id.tv_app_grade);
		mTvRoot = (TextView) view.findViewById(R.id.tv_root);
		mTvWifi = (TextView) view.findViewById(R.id.tv_wifi);
	}

	// }} initView 方法体

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		init();
		setListener();
	}

	private void init()
	{
		mCtvAndroid.setChecked(GoogleApplication.mIsOnlyAndroid);
	}

	private void setListener()
	{
		OnclickListenerImpl onclickListenerImpl = new OnclickListenerImpl();
		mCtvAndroid.setOnClickListener(onclickListenerImpl);
		mTvRoot.setOnClickListener(onclickListenerImpl);
		mTvAppGrade.setOnClickListener(onclickListenerImpl);
		mTvWifi.setOnClickListener(onclickListenerImpl);

	}

	private   class OnclickListenerImpl implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.ctv_android:
				boolean isChecked = mCtvAndroid.isChecked();
				mCtvAndroid.setChecked(!isChecked);
				BaseActivity baseActivity = (BaseActivity) getActivity();
				GoogleApplication.mIsOnlyAndroid = !isChecked;
				baseActivity.getSharedPreferences().edit().putBoolean(BaseActivity.PREF_ONLY_ANDROID, !isChecked).commit();
				break;
			case R.id.tv_root:// 请求Root权限
				boolean hasRoot = hasRootPermission();
				if (hasRoot)
				{
					Toast.makeText(getActivity(), "手机已经Root", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getActivity(), "手机还没有Root", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.tv_app_grade://给程序打分
				grade(getActivity(), "com.roboo.explorer");
				break;
			case R.id.tv_wifi://获取WIFI信息
				wifiInfo();
				
				break;
			default:
				break;
			}
		}
	}
	private void wifiInfo()
	{
		WifiManager mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
	
	}
	/***
	 * 去应用市场给程序打分
	 * @param context 应用程序Context
	 * @param packageName 应用程序的包名称
	 */
	private  void grade(Context context , String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String uriString = "market://details?id="+packageName;
		intent.setData(Uri.parse(uriString));
		context.startActivity(intent);
	}
	/**
	 * 应用程序请求获取最高权限，即Root权限
	 * @return true  获取Root权限成功 或者 false 获取Root权限失败
	 */
	public static boolean hasRootPermission()
	{
		Process process = null;
		DataOutputStream os = null;
		boolean rooted = true;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			if (process.exitValue() != 0)
			{
				rooted = false;
			}
		}
		catch (Exception e)
		{
			rooted = false;
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
					process.destroy();
				}
				catch (Exception e)
				{}
			}
		}
		return rooted;
	}

}

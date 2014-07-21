package com.roboo.like.google.fragments;

import net.dynamicandroid.listview.DynamicScrollView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.roboo.like.google.BaseActivity;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;

public class SettingsFragment extends BaseFragment
{
	private DynamicScrollView mScrollView;
	/** 只显示Android资讯的TextView */
	private CheckedTextView mCtvAndroid;

	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}

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

	private void initView(View view)
	{
		mScrollView = (DynamicScrollView) view.findViewById(R.id.dsv_scrollview);
		mCtvAndroid = (CheckedTextView) view.findViewById(R.id.ctv_android);
	}

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

	}

	private class OnclickListenerImpl implements OnClickListener
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
				GoogleApplication.mIsOnlyAndroid  = !isChecked;
				baseActivity.getSharedPreferences().edit().putBoolean(BaseActivity.PREF_ONLY_ANDROID, !isChecked).commit();
				break;

			default:
				break;
			}
		}

	}

}

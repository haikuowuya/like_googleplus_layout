package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{
	protected  boolean mDebug = true;
	protected boolean  mOnCreateViewExec = false;
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
}

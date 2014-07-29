package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

public class BaseWithProgressFragment extends BaseFragment
{

	protected ProgressBar mProgressBar;
	private FrameLayout mDecorView;
	
 
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		addProgressBar();
	}

	private void addProgressBar()
	{
		mProgressBar = new ProgressBar(getActivity());
		mDecorView = (FrameLayout) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		mDecorView.addView(mProgressBar, params);
		 
	}
 
}

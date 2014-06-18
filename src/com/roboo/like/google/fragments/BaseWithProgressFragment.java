package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.FrameLayout.LayoutParams;

public class BaseWithProgressFragment extends Fragment
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

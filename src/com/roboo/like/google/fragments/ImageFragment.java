package com.roboo.like.google.fragments;
 
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.roboo.like.google.R;

public class ImageFragment extends BaseFragment
{
	public static ImageFragment newInstance()
	{
		ImageFragment fragment = new ImageFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		 ImageView imageView = new ImageView(getActivity());
		 imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		 imageView.setImageResource(R.drawable.ic_test);
		 return imageView;
	}
}

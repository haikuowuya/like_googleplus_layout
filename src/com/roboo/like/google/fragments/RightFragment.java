package com.roboo.like.google.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.roboo.like.google.R;
import com.roboo.like.google.views.PullToZoomListView;

public class RightFragment extends BaseFragment
{
	private PullToZoomListView mListView;
	private SlidingDrawer mSlidingDrawer;
	private Button mBtnNotification;
	public static RightFragment newInstance()
	{
		RightFragment fragment = new RightFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_right, null);
		mListView = (PullToZoomListView) view.findViewById(R.id.ptzlv_list);
		mSlidingDrawer = (SlidingDrawer) view.findViewById(R.id.sliding_drawer);
		mBtnNotification = (Button) view.findViewById(R.id.btn_notification);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		 mListView.setAdapter(getAdapter());
		 OnDrawerListenerImpl onDrawerListenerImpl = new OnDrawerListenerImpl();
		 mSlidingDrawer.setOnDrawerCloseListener(onDrawerListenerImpl);
		 mSlidingDrawer.setOnDrawerOpenListener(onDrawerListenerImpl);
		 mSlidingDrawer.setOnDrawerScrollListener(onDrawerListenerImpl);
		 mListView.getHeaderView().setImageResource(R.drawable.ic_test);
		 mListView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
	}

	private ListAdapter getAdapter()
	{
		 return ArrayAdapter.createFromResource(getActivity(), R.array.actionbar_navigation_list_text_arrays, android.R.layout.simple_list_item_1);	 
	}
	private class OnDrawerListenerImpl implements OnDrawerCloseListener,OnDrawerOpenListener,OnDrawerScrollListener
	{
		public void onScrollStarted()
		{
			mBtnNotification.setVisibility(View.GONE);
		}

		public void onScrollEnded()
		{
		}

		public void onDrawerOpened()
		{
			
		}

		@Override
		public void onDrawerClosed()
		{
			mBtnNotification.setVisibility(View.VISIBLE);
		}
		
	}

}

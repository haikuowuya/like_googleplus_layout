package com.roboo.like.google.listener;

import com.roboo.like.google.views.SwipeBackFrameLayout;

 
public interface SwipeBackListener
{
	/**
	 * @return the SwipeBackLayout associated with this activity.
	 */
	public abstract SwipeBackFrameLayout getSwipeBackLayout();

	public abstract void setSwipeBackEnable(boolean enable);

	/**
	 * Scroll out contentView and finish the activity
	 */
	public abstract void scrollToFinishActivity();

}

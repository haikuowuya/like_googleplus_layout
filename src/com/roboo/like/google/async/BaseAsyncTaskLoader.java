package com.roboo.like.google.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public  abstract class BaseAsyncTaskLoader<T> extends AsyncTaskLoader<T>
{
	protected  boolean mDebug = true;
	protected static final long THREAD_LEAST_DURATION_TIME=2000L;
	protected long mStartTime = 0;
	protected long mEndTime = 0;
	public BaseAsyncTaskLoader(Context context)
	{
		super(context);
	
	}
	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		forceLoad();
		mStartTime = System.currentTimeMillis();
	}
}

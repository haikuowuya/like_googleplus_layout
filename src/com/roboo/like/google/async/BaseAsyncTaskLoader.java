package com.roboo.like.google.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public  abstract class BaseAsyncTaskLoader<T> extends AsyncTaskLoader<T>
{
	public BaseAsyncTaskLoader(Context context)
	{
		super(context);
	
	}
	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		forceLoad();
	}
}

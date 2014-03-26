package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.NewsUtils;

public class NewsAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<NewsItem>>
{
	private String mChannelUrl;
	private int mPageNo;

	public NewsAsyncTaskLoader(Context context, String channelUrl)
	{
		this(context, channelUrl, 1);
	}

	public NewsAsyncTaskLoader(Context context, String channelUrl, int pageNo)
	{
		super(context);
		mChannelUrl = channelUrl;
		mPageNo = pageNo;
	}

	@Override
	public LinkedList<NewsItem> loadInBackground()
	{
		LinkedList<NewsItem> data = null;
		try
		{
			long startTime = System.currentTimeMillis();
			data = NewsUtils.getITHomeNewsList(mChannelUrl, mPageNo);
			long endTime = System.currentTimeMillis();
			if (endTime - startTime < 1000L)
			{
				Thread.sleep(1000);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return data;
	}

}

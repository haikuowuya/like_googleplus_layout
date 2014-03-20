package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.NewsUtils;

public class NewsAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<NewsItem>>
{
	private String mChannelUrl;
	private int  mPageNo;

	public NewsAsyncTaskLoader(Context context, String channelUrl)
	{
		this(context, channelUrl, 1);
	}
	public NewsAsyncTaskLoader(Context context, String channelUrl,int pageNo)
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
			data = NewsUtils.getITHomeNewsList(mChannelUrl, mPageNo);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

}

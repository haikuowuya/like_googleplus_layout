package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.CommentItem;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.CommentUtils;
import com.roboo.like.google.utils.NewsUtils;

public class CommentAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<CommentItem>>
{
	private String mNewsId;
	private int mPageNo;

	public CommentAsyncTaskLoader(Context context, String newsId)
	{
		this(context, newsId, 1);
	}

	public CommentAsyncTaskLoader(Context context, String newsId, int pageNo)
	{
		super(context);
		mNewsId = newsId;
		mPageNo = pageNo;
	}

	@Override
	public LinkedList<CommentItem> loadInBackground()
	{
		LinkedList<CommentItem> data = null;
		try
		{
			long startTime = System.currentTimeMillis();
			String commentUrl = GoogleApplication.BASE_COMMENT_URL+"&newsid="+mNewsId+"&page="+ mPageNo;
			GoogleApplication.TEST = true;
			if(GoogleApplication.TEST)
			{
				System.out.println("评论URL = " + commentUrl);
			}
			
			data =CommentUtils.getCommentList(commentUrl);
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

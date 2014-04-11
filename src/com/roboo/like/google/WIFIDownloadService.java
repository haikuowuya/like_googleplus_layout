package com.roboo.like.google;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.roboo.like.google.models.CommentItem;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.utils.CommentUtils;
import com.roboo.like.google.utils.DataUtils;
import com.roboo.like.google.utils.FileUtils;
import com.roboo.like.google.utils.MD5Utils;
import com.roboo.like.google.utils.NewsUtils;

public class WIFIDownloadService extends Service
{
	/** 有进行离线下载的新闻栏目数据 */
	private LinkedList<NewsTypeItem> mData;
	/** 同步辅助类 */
	private CyclicBarrier mCyclicBarrier;	
	private long mStartTime ;
	private Runnable mDownloadFinishRunnable = new Runnable()
	{
		public void run()
		{
			stopSelf();
			System.out.println("所有的任务都完成   耗时  = "+(System.currentTimeMillis() - mStartTime));
		}
	};

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public void onCreate()
	{
		super.onCreate();
		mData = DataUtils.handleNewsType(this);
		mCyclicBarrier = new CyclicBarrier(mData.size(), mDownloadFinishRunnable);
		mStartTime = System.currentTimeMillis();
		for (int i = 0; i < mData.size(); i++)
		{
			new DownloadThread(mData.get(i), mCyclicBarrier).start();
		}
		 
	}

	private class DownloadThread extends Thread
	{
		private NewsTypeItem mTypeItem;
		private CyclicBarrier barrier;

		public DownloadThread(NewsTypeItem mTypeItem, CyclicBarrier barrier)
		{
			super();
			this.mTypeItem = mTypeItem;
			this.barrier = barrier;
		}

		public void run()
		{
			try
			{
				persistentData();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (BrokenBarrierException e)
			{
				e.printStackTrace();
			}
		}

		/** 保存与新闻有关的数据到本地 */
		private void persistentData() throws IOException, InterruptedException, BrokenBarrierException
		{
			long startTime1 = System.currentTimeMillis();
			System.out.println("开始下载 " + mTypeItem.name);
			LinkedList<NewsItem> newsListData = NewsUtils.getITHomeNewsList(mTypeItem.url, 1);
			saveNewsListData(newsListData, mTypeItem);
			if (null != newsListData)
			{
				for (NewsItem item : newsListData)
				{
					long startTime2 = System.currentTimeMillis();
					LinkedList<String> newsContentData = NewsUtils.getITHomeNewsDataList(item.getUrl());
					saveNewsContentData(newsContentData, item.getUrl());
					System.out.println("下载完成新闻内容 " + item.getTitle() + " 耗时  = " + (System.currentTimeMillis() - startTime2));
					
					long startTime3 = System.currentTimeMillis();
					String commentUrl = GoogleApplication.BASE_COMMENT_URL + "&newsid=" + item.getNewsId() + "&page=" + 1;
					LinkedList<CommentItem> newsCommentData = CommentUtils.getCommentList(commentUrl);
					saveNewsCommentData(newsCommentData, commentUrl);
					System.out.println("下载完成新闻评论 " + item.getTitle() + " 耗时  = " + (System.currentTimeMillis() - startTime3));
				}
			}
			System.out.println(mTypeItem.name + " 下载完成 ::  耗时  = " + (System.currentTimeMillis() - startTime1));
			barrier.await();
		}
	}

	/** 将新闻列表保存到本地 */
	private void saveNewsListData(LinkedList<NewsItem> data, NewsTypeItem mTypeItem)
	{
		File dirFile = FileUtils.getFileCacheDir(this, FileUtils.TYPE_NEWS_LIST);
		File dataFile = new File(dirFile, MD5Utils.generate(mTypeItem.url));
		if(dataFile.exists())
		{
			dataFile.delete();
			System.out.println("删除  ：： "+ mTypeItem.name + " 文件");
			
		}
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("新闻::列表::对象写入文件成功 :: 文件路径 = " + dataFile.getAbsolutePath());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** 将新闻列表中的每一条新闻数据保存到本地 */
	private void saveNewsContentData(LinkedList<String> data, String newsUrl)
	{
		File dirFile = FileUtils.getFileCacheDir(this, FileUtils.TYPE_NEWS_CONTENT);
		File dataFile = new File(dirFile, MD5Utils.generate(newsUrl));
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("新闻::内容::对象写入文件成功 :: 文件路径 = " + dataFile.getAbsolutePath());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** 将每一条新闻对应的评论保存到本地 */
	private void saveNewsCommentData(LinkedList<CommentItem> data, String commentUrl)
	{

		File dirFile = FileUtils.getFileCacheDir(this, FileUtils.TYPE_NEWS_COMMENT);
		File dataFile = new File(dirFile, MD5Utils.generate(commentUrl));
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("新闻::评论::对象写入文件成功 :: 文件路径 = " + dataFile.getAbsolutePath());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}

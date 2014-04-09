package com.roboo.like.google.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.FileUtils;
import com.roboo.like.google.utils.MD5Utils;
import com.roboo.like.google.utils.NetWorkUtils;
import com.roboo.like.google.utils.NewsUtils;

public class NewsAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<NewsItem>>
{
	private String mChannelUrl;
	private int mPageNo;
	private Context mContext;

	public NewsAsyncTaskLoader(Context context, String channelUrl)
	{
		this(context, channelUrl, 1);
	}

	public NewsAsyncTaskLoader(Context context, String channelUrl, int pageNo)
	{
		super(context);
		mChannelUrl = channelUrl;
		mContext = context;
		mPageNo = pageNo;
	}

	@Override
	public LinkedList<NewsItem> loadInBackground()
	{
		LinkedList<NewsItem> data = null;
		try
		{

			File file = new File(FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_NEWS_LIST), MD5Utils.generate(mChannelUrl));
			if (!NetWorkUtils.isNetworkAvailable(mContext) && file.exists() && mPageNo == 1)
			{
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
				data = (LinkedList<NewsItem>) objectInputStream.readObject();
				objectInputStream.close();
				GoogleApplication.TEST = true;
				if (GoogleApplication.TEST)
				{
					System.out.println("从本地文件读取对象成功");
				}
				Thread.sleep(1000L);
			}
			else
			{
				data = NewsUtils.getITHomeNewsList(mChannelUrl, mPageNo);
				if (mPageNo == 1 && null != data)
				{
					saveNewsListData(data);
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	private void saveNewsListData(LinkedList<NewsItem> data)
	{
		File dirFile = FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_NEWS_LIST);
		File dataFile = new File(dirFile, MD5Utils.generate(mChannelUrl));
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("新闻列表对象写入文件成功 :: 文件路径 = " + dataFile.getAbsolutePath());
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

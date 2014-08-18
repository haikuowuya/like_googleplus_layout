package com.roboo.like.google.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

import android.content.Context;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.news.utils.NewsListUtils;
import com.roboo.like.google.utils.FileUtils;
import com.roboo.like.google.utils.MD5Utils;

public class NewsListAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<NewsItem>>
{
	private String mChannelUrl;
	private int mPageNo;
	private Context mContext;
	private LinkedList<NewsItem> mData ;

	public NewsListAsyncTaskLoader(Context context, String channelUrl)
	{
		this(context, channelUrl, 1);
	}

	public NewsListAsyncTaskLoader(Context context, String channelUrl, int pageNo)
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
			data = NewsListUtils.getNewsList(mChannelUrl, mPageNo);
			if (null != data)
			{
				if (mPageNo == 1)
				{
					saveNewsListData(data);
				}
				else
				{
					appendNewsListData(data);
				}
			}
			if (data == null && file.exists())
			{
				data = getOfflineData(file);
			}
			mEndTime = System.currentTimeMillis();
			long durationTime = mEndTime - mStartTime;
			if (durationTime < THREAD_LEAST_DURATION_TIME)
			{
				Thread.sleep(THREAD_LEAST_DURATION_TIME - durationTime);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}

	/** 从文件中获取本地的离线数据 */
	@SuppressWarnings("unchecked")
	private LinkedList<NewsItem> getOfflineData(File file) throws StreamCorruptedException, IOException, FileNotFoundException, OptionalDataException, ClassNotFoundException
	{
		LinkedList<NewsItem> data;
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
		data = (LinkedList<NewsItem>) objectInputStream.readObject();
		objectInputStream.close();
		GoogleApplication.TEST = true;
		if (GoogleApplication.TEST)
		{
			System.out.println("从本地文件读取对象成功");
		}
		return data;
	}

	/** 保存第一页数据到本地文件中 */
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

	/**
	 * 将第一页以后的数据追加到本地文件中去
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws OptionalDataException
	 * @throws StreamCorruptedException
	 */
	private void appendNewsListData(LinkedList<NewsItem> data) throws StreamCorruptedException, OptionalDataException, FileNotFoundException, IOException, ClassNotFoundException
	{
		File dirFile = FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_NEWS_LIST);
		File dataFile = new File(dirFile, MD5Utils.generate(mChannelUrl));
		LinkedList<NewsItem> offlineData = getOfflineData(dataFile);
		LinkedList<NewsItem> needAppendData = new LinkedList<NewsItem>();
		if (null != offlineData && data != null)
		{
			for (NewsItem item : data)
			{
				if (!offlineData.contains(item))
				{
					needAppendData.add(item);
				}
			}
			if (needAppendData.size() > 0)
			{
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile, true));
				objectOutputStream.writeObject(data);
				objectOutputStream.close();
				GoogleApplication.TEST = true;
				if (GoogleApplication.TEST)
				{
					System.out.println("追加新闻列表对象写入文件成功 :: 追加新闻个数  = " + needAppendData.size());
				}
			}
		}
	}

}

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
import com.roboo.like.google.models.CommentItem;
import com.roboo.like.google.utils.CommentUtils;
import com.roboo.like.google.utils.FileUtils;
import com.roboo.like.google.utils.MD5Utils;

public class CommentAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<CommentItem>>
{
	private String mNewsId;
	private int mPageNo;
	private Context mContext;

	public CommentAsyncTaskLoader(Context context, String newsId)
	{
		this(context, newsId, 1);
	}

	public CommentAsyncTaskLoader(Context context, String newsId, int pageNo)
	{
		super(context);
		mNewsId = newsId;
		mPageNo = pageNo;
		mContext = context;
	}

	@Override
	public LinkedList<CommentItem> loadInBackground()
	{
		LinkedList<CommentItem> data = null;
		try
		{
			String commentUrl = GoogleApplication.BASE_COMMENT_URL + "&newsid=" + mNewsId + "&page=" + mPageNo;
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("评论URL = " + commentUrl);
			}
			File file = new File(FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_NEWS_COMMENT), MD5Utils.generate(commentUrl));
			if (file.exists())
			{
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
				data = (LinkedList<CommentItem>) objectInputStream.readObject();
				objectInputStream.close();
				GoogleApplication.TEST = true;
				if (GoogleApplication.TEST)
				{
					System.out.println("从本地文件读取对象成功");
				}
			}
			else
			{

				data = CommentUtils.getCommentList(commentUrl);
				saveNewsComment(data, commentUrl);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return data;
	}

	private void saveNewsComment(LinkedList<CommentItem> data, String commentUrl)
	{
		File dirFile = FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_NEWS_COMMENT);
		File dataFile = new File(dirFile, MD5Utils.generate(commentUrl));
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
			GoogleApplication.TEST = true;
			if (GoogleApplication.TEST)
			{
				System.out.println("新闻评论对象写入文件成功 :: 文件路径 = " + dataFile.getAbsolutePath());
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

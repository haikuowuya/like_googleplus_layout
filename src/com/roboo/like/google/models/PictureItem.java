package com.roboo.like.google.models;

import android.graphics.Bitmap;

public class PictureItem
{
	/** 图片的路径 */
	public String path;
	/** 图片加入手机的时间 */
	public String time;
	public Bitmap bitmap;
	public int headerId;

	public PictureItem(String path, String time)
	{
		super();
		this.path = path;
		this.time = time;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public int getHeaderId()
	{
		return headerId;
	}

	public void setHeaderId(int headerId)
	{
		this.headerId = headerId;
	}

	@Override
	public String toString()
	{
		return "图片路径 = " + path + "图片存储到手机时间 = " + time;
	}
}

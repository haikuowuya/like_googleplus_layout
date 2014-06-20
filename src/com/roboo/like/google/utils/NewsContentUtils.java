package com.roboo.like.google.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.like.google.GoogleApplication;

import android.annotation.SuppressLint;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class NewsContentUtils
{
	private static final int TIME_OUT = 20000;
	private static final String IT_HOME_NEWS_CONTENT_ID = "paragraph";
	private static final String FOUR_BLANK = "    ";
	private static final String IT_HOME_LAZY_IMG_URL = "http://img.ithome.com/images/v2/grey.gif";

	public static LinkedList<String> getNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		if (GoogleApplication.mCurrentType==GoogleApplication.TYPE_ITHOME)
		{
			data = getITHomeNewsDataList(newsUrl);
		}
		if(GoogleApplication.mCurrentType == GoogleApplication.TYPE_CSDN)
		{
			data = getCsdnNewsDataList(newsUrl);
		}
		return data;

	}

	/** 获取每条新闻的图片和内容 */
	public static LinkedList<String> getITHomeNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Document document = Jsoup.connect(newsUrl).timeout(TIME_OUT).get();
		if (null != document)
		{
			Element element = document.getElementById(IT_HOME_NEWS_CONTENT_ID);
			if (element != null)
			{
				Elements es = element.getElementsByTag("p");
				if (null != es)
				{
					data = new LinkedList<String>();
					for (Element element2 : es)
					{
						String content = "";
						Elements imgsElement = element2.getElementsByTag("img");
						if (null != imgsElement && imgsElement.size() > 0)
						{
							Element imgElement = imgsElement.first();
							// System.out.println("图片地址  = "+
							// imgElement.attr("data-original"));
							imgElement.attr("onclick", "window.android.toast(\"" + imgElement.attr("src") + "\"" + "," + 0 + ")");
							content = imgElement.attr("src");
							if (IT_HOME_LAZY_IMG_URL.equals(content))
							{
								content = imgElement.attr("data-original");
							}
						}
						else if (!element2.hasClass("content_copyright"))
						{
							content = FOUR_BLANK + element2.text();
						}
						if (content.length() > FOUR_BLANK.length())
						{
							data.add(content);
						}
					}
				}
			}
			else
			{
				System.out.println("出错的URL   = " + newsUrl);
			}
		}
		return data;
	}

	public static LinkedList<String> getCsdnNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Elements majorElements;
		Element majorElement = null;
		Document document = Jsoup.connect(newsUrl).get();
		majorElements = document.getElementsByClass("text");
		System.out.println("majorElements = " + majorElements);
		if (!majorElements.isEmpty())
		{
			majorElement = majorElements.get(0);
			data = new LinkedList<String>();
			majorElements = majorElement.getElementsByTag("p");
			if (!majorElements.isEmpty())
			{
				for (int i = 0; i < majorElements.size(); i++)
				{
					String content = "";
					majorElement = majorElements.get(i);
					Elements imgElements = majorElement.getElementsByTag("img");
					if (!imgElements.isEmpty())
					{
						content = imgElements.get(0).attr("src");
						// System.out.println("src =" + imgElements.get(0).attr("src"));
					}
					else
					{
						content = majorElement.text();
						// System.out.println("text =" + majorElement.text());
					}
					if (!TextUtils.isEmpty(content))
					{
						data.add(content);
					}
				}
			}

		}
		return data;
	}
}

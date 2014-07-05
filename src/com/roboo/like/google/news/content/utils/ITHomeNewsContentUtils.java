package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ITHomeNewsContentUtils
{
	private static final int TIME_OUT = 20000;
	private static final String IT_HOME_NEWS_CONTENT_ID = "paragraph";
	private static final String FOUR_BLANK = "    ";
	private static final String IT_HOME_LAZY_IMG_URL = "http://img.ithome.com/images/v2/grey.gif";

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

	
}

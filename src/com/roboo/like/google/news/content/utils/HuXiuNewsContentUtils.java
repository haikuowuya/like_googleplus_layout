package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class HuXiuNewsContentUtils extends BaseNewsContentUtils
{
	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		return getHuXiuNewsDataList(newsUrl);
	}

	public static LinkedList<String> getHuXiuNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Elements majorElements;
		Element majorElement = null;
		String content = "";
		Document document = Jsoup.connect(newsUrl).timeout(TIME_OUT).get();
		// majorElements = document.getElementsByClass("article");
		majorElement = document.getElementById("article_content");
		if (majorElement != null)
		{
			data = new LinkedList<String>();
			majorElements = majorElement.getElementsByClass("p-img");
			
			if (!majorElements.isEmpty())
			{
			 content = majorElements.get(0).attr("src");
			}
			majorElements = majorElement.getElementsByTag("div");
			if (!majorElements.isEmpty())
			{
				for (int i = 0; i < majorElements.size(); i++)
				{
					majorElement = majorElements.get(i);
					Elements imgElements = majorElement.getElementsByTag("img");
					
					if (!imgElements.isEmpty())
					{
						content = imgElements.get(0).attr("src");
					}
					else
					{
						if (content.contains("http://") || (content.contains("https://")))
						{
							content = "";
						}
						else
						{
							content = majorElement.text();
							content = FOUR_BLANK_SPACE + content;
						}
						// System.out.println("text =" + majorElement.text());
					}
					if (!TextUtils.isEmpty(content) && i < majorElements.size() - 2)// 最后两项不要了
					{
						data.add(content);
					}
				}
			}
		}
		return data;
	}
}

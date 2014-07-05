package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class CSDNNewsContentUtils
{
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

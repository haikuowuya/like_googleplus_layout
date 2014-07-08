package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class IT199NewsContentUtils extends BaseNewsContentUtils
{
	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		return getIT199NewsDataList(newsUrl);
	}

	public static LinkedList<String> getIT199NewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Elements majorElements;
		Element majorElement = null;
		Document document = Jsoup.connect(newsUrl).get();
		majorElements = document.getElementsByClass("entry-content");
		if (!majorElements.isEmpty())
		{
			majorElement = majorElements.get(0);
			if (majorElement != null)
			{
				data = new LinkedList<String>();
				majorElements = majorElement.getElementsByTag("p");
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
						if (content.contains("http://") || (content.contains("https://")))
						{
							content = "";
						}
						else
						{
							content = FOUR_BLANK_SPACE + majorElement.text();
						}
						// System.out.println("text =" + majorElement.text());
					}
					if (!TextUtils.isEmpty(content) && i != majorElements.size() - 1)
					{

						data.add(content);
					}
				}
			}
		}
		return data;
	}
}

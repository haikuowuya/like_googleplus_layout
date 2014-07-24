package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class WLJDNewsContentUtils extends BaseNewsContentUtils
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
		System.out.println("document = " + document);
		majorElements = document.getElementsByClass("art_wz");
		if (!majorElements.isEmpty())
		{
			majorElement = majorElements.get(0);
			if (majorElement != null)
			{
				data = new LinkedList<String>();
				majorElements = majorElement.getElementsByTag("p");
				if (!majorElements.isEmpty())
				{
					for (int i = 0; i < majorElements.size(); i++)
					{
						majorElement = majorElements.get(i);
						content = FOUR_BLANK_SPACE + majorElement.text();
						Elements imgElements = majorElement.getElementsByTag("img");
						if (!imgElements.isEmpty())
						{
							content = imgElements.get(0).attr("src");
						}

						if (!TextUtils.isEmpty(content))
						{
							if (!content.equals("&nbsp;"))
							{
								data.add(content);
							}
						}
					}
				}
			}
		}
		return data;
	}
}

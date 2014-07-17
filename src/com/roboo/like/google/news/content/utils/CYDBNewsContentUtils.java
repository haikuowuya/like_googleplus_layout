package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class CYDBNewsContentUtils extends BaseNewsContentUtils
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
		majorElements = document.getElementsByClass("textbox-content");

		if (!majorElements.isEmpty())
		{
			data = new LinkedList<String>();
			majorElement = majorElements.get(0);
			majorElements = majorElement.getElementsByTag("p");
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
							Elements bElements = majorElement.getElementsByTag("b");
							if (!bElements.isEmpty())
							{
								String strongString = bElements.get(0).text();
								content = content.substring(strongString.length());
								content = "$" + strongString + "  $" + content;
							}
							content = FOUR_BLANK_SPACE + content;
						}
						// System.out.println("text =" + majorElement.text());
					}
					if (!TextUtils.isEmpty(content) && i < majorElements.size() - 2)// 最后两项不要了
					{
						if (content.contains("。"))
						{
							content.replace("。", "");
						}
						data.add(content);

					}
				}
			}

		}
		return data;
	}
}

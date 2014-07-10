package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class KR36NewsContentUtils extends BaseNewsContentUtils
{
	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		return getKR36NewsDataList(newsUrl);
	}

	public static LinkedList<String> getKR36NewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Elements majorElements;
		Element majorElement = null;
		Document document = Jsoup.connect(newsUrl).timeout(TIME_OUT).get();
		majorElements = document.getElementsByClass("article");
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
							content =  majorElement.text();
							Elements strongElements = majorElement.getElementsByTag("strong");
							if (!strongElements.isEmpty())
							{
								 String strongString = strongElements.get(0).text();
//								 content.replace(strongString, "");
								 content = content.substring(strongString.length());
								 if(content.contains(":"))
								 {
									 content.replace(":", "");
								 }
								 content = "$"+strongString +" : $"+content;
							}
							content = FOUR_BLANK_SPACE+content;
						}
						// System.out.println("text =" + majorElement.text());
					}
					if (!TextUtils.isEmpty(content) && i < majorElements.size() - 2)//最后两项不要了
					{
						if(!content.contains("查看大图") ||(!content.contains("<br")))
						{
							data.add(content);
						}
					}
				}
			}
		}
		return data;
	}
}

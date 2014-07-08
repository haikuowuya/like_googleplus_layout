package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class GeekParkNewsContentUtils extends BaseNewsContentUtils
{
	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		 
		return getGeekParkNewsDataList(newsUrl);
	}
	public static LinkedList<String> getGeekParkNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = new LinkedList<String>();
		Elements majorElements;
		Element majorElement = null;
		Document document = Jsoup.connect(newsUrl).get();
		// System.out.println(document);
		majorElements = document.getElementsByClass("tips");
		StringBuffer stringBuffer = new StringBuffer();
		if (!majorElements.isEmpty())
		{
			String coreContent = majorElements.get(0).text();
			if (!TextUtils.isEmpty(coreContent))
			{
				data.add(coreContent);
			}
		}

		majorElements = document.getElementsByClass("g-content");
		if (!majorElements.isEmpty())
		{
			// System.out.println("majorElements.get(0) = " + majorElements.get(0));
			majorElements = majorElements.get(0).getElementsByTag("p");
			if (!majorElements.isEmpty())
			{
				for (int i = 0; i < majorElements.size(); i++)
				{
					String content = "";
					majorElement = majorElements.get(i);

					Elements imgElements = majorElement.getElementsByTag("a");
					if (!imgElements.isEmpty())
					{
						content = imgElements.get(0).attr("href");
						// System.out.println("src =" + imgElements.get(0).attr("src"));
					}
					else
					{
						// Elements spanElements = majorElement.getElementsByTag("span");
						// if (!spanElements.isEmpty())
						// {
						// for (int ii = 0; ii < spanElements.size(); ii++)
						// {
						// majorElement = spanElements.get(ii);
						// content = content + majorElement.text();
						// }
						// // System.out.println("text =" + majorElement.text());
						// }
						// else
						// {
						// }
						content = majorElement.text();
					}
					System.out.println(i + "  ::  content = " + content);
					if (!TextUtils.isEmpty(content))
					{
						if (isImg(content))
						{
							data.add(content);
							data.add(stringBuffer.toString());
							stringBuffer = new StringBuffer();
						}
						else
						{
							if (!content.startsWith("http"))
							{
								stringBuffer.append(FOUR_BLANK_SPACE);
								stringBuffer.append(content);
								stringBuffer.append("\r\n\r\n");
							}
						}
					}
					if (i == majorElements.size() - 1)
					{
						data.add(stringBuffer.toString());
					}
				}
			}
		}
		return data;
	}

	public static boolean isImg(String str)
	{
		return str.toLowerCase(Locale.getDefault()).endsWith(".png") || str.toLowerCase(Locale.getDefault()).endsWith(".jpg") || str.toLowerCase(Locale.getDefault()).endsWith("gif");
	}

	
}

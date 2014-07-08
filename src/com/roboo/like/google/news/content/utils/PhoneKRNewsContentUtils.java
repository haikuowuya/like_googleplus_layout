package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

public class PhoneKRNewsContentUtils extends BaseNewsContentUtils
{
	

	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		 
		return getPhoneKRNewsDataList(newsUrl);
	}

	public static LinkedList<String> getPhoneKRNewsDataList(String newsUrl)
	{
		LinkedList<String> data = null;
		Document document;
		try
		{
			document = Jsoup.connect(newsUrl).get();

			Element element = document.getElementById("xs-post");
			Elements elements = element.getElementsByTag("p");
			if (!elements.isEmpty())
			{
				data = new LinkedList<String>();
				for (int i = 0; i < elements.size(); i++)
				{
					String text = null;
					element = elements.get(i);
					if (element.getElementsByTag("a").isEmpty())
					{
						text = FOUR_BLANK_SPACE +element.text();
					}
					else
					{
						if (!element.getElementsByTag("a").get(0).getElementsByTag("img").isEmpty())
						{
							// System.out.println("图片  = "+element.getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src"));
							text = element.getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
						}
					}
					if (!TextUtils.isEmpty(text))
					{
						data.add(text);
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}
}

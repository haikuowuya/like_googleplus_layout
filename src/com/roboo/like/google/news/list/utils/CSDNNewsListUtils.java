package com.roboo.like.google.news.list.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

public class CSDNNewsListUtils extends BaseNewsListUtils
{
	public static LinkedList<NewsItem> getCSDNNewsList(String csdnUrl, int pageNo) throws IOException
	{
		// http://mobile.csdn.net/mobile/2 【移动开发】
		LinkedList<NewsItem> data = null;
		Document document;
		Elements elements;
		Elements majorElements;
		Element majorElement;
		Element minorElement;
		String url = csdnUrl + pageNo;
		document = Jsoup.connect(url).get();
		majorElements = document.getElementsByClass("unit");
		String title = null, source ="CSDN",subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);

		if (!majorElements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < majorElements.size(); i++)
			{

				majorElement = majorElements.get(i);
				elements = majorElement.getElementsByTag("h1");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					title = minorElement.text();
					System.out.println("title = " + title);
				}

				elements = majorElement.getElementsByClass("ago");
				if (!elements.isEmpty())
				{

					minorElement = elements.get(0);
					time = minorElement.text();
					System.out.println("time = " + time);
					if (!time.contains("小时前"))
					{
						if (time.contains(" "))
						{
							String date = time.split(" ")[0];
							if (date.contains("-") && date.split("-").length > 2)
							{
								time = date.split("-")[1] + "月" + date.split("-")[2] + "日";
							}
							System.out.println("date = " + date);
						}
					}
					else
					{
						time = new SimpleDateFormat("MM月dd日").format(new Date(System.currentTimeMillis()));
					}
				}

				elements = majorElement.getElementsByTag("a");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					newsUrl = minorElement.attr("href");
					// System.out.println("newsUrl = " + newsUrl);
				}
				elements = majorElement.getElementsByTag("img");
				if (!elements.isEmpty())
				{

					minorElement = elements.get(0);
					src = minorElement.attr("src");
					System.out.println("img = " + src);
				}
				elements = majorElement.getElementsByTag("dd");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					subTitle = minorElement.text();
					System.out.println("subTitle = " + subTitle);
					System.out.println("\n");
				}
				NewsItem item = new NewsItem();
				item.setSrc(src);
				item.setTime(time);
				item.setMd5(md5);
				item.setUrl(newsUrl);
				item.setTitle(title);
				item.setSource(source);
				item.setSubTitle(subTitle);
				data.add(item);
			}
		}

		return data;
	}

	@Override
	public LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		return getCSDNNewsList(baseUrl, pageNo);
	}
}

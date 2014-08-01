package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

@SuppressLint("SimpleDateFormat")
public class HiApkNewsUtils
{

	public static LinkedList<NewsItem> getHiApkNewsList(String hiApkUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = hiApkUrl + pageNo + ".html";
		System.out.println("hiApkUrl = " + url);
		Document document = Jsoup.connect(url).get();
		Elements elements = null;
		Element element = document.getElementById("container");
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time = "第" + pageNo + "页";
		if (null != element)
		{
			elements = element.getElementsByTag("dl");
		}
		if (!elements.isEmpty())
		{
			if (!elements.isEmpty())
			{
				data = new LinkedList<NewsItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					element = elements.get(i);
					Elements tmpElements = element.getElementsByTag("dt");
					if (!tmpElements.isEmpty())
					{
						tmpElements = tmpElements.get(0).getElementsByTag("a");
						if (!tmpElements.isEmpty())
						{
							newsUrl = tmpElements.get(0).attr("href");
							tmpElements = tmpElements.get(0).getElementsByTag("img");
							if (!tmpElements.isEmpty())
							{
								src = tmpElements.get(0).attr("src");
							}
						}
					}
					tmpElements = element.getElementsByTag("dd");
					if (!tmpElements.isEmpty())
					{
						if (!tmpElements.get(0).getElementsByTag("strong").isEmpty())
						{
							title = tmpElements.get(0).getElementsByTag("strong").text();
						}
						if (!tmpElements.get(0).getElementsByTag("p").isEmpty())
						{
							subTitle = tmpElements.get(0).getElementsByTag("p").text();
						}
					}
					NewsItem item = new NewsItem();
					item.setSrc(src);
					item.setTime(time);
					item.setMd5(md5);
					item.setUrl(newsUrl);
					item.setTitle(title);
					item.setSubTitle(subTitle);
					if (!data.contains(item))
					{
						data.add(item);
					}
				}
			}
		}
		return data;
	}

	private static String getTime(String time)
	{
		String newTime = "今天";
		if (time.contains(" "))
		{
			String[] tmp = time.split(" ");
			if (tmp.length == 2)
			{
				String ymd = tmp[0];
				if (ymd.contains("-"))
				{
					tmp = ymd.split("-");
					if (tmp.length == 3)
					{
						String year = tmp[0].trim();
						String month = tmp[1].trim();
						String day = tmp[2].trim();
						newTime = month + "月" + day + "日";
					}
				}
			}
		}

		return newTime.trim();
	}
}

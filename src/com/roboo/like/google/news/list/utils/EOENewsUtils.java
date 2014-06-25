package com.roboo.like.google.news.list.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

public class EOENewsUtils
{
	private static final String BASE_EOE_BLOG_URL = "http://www.eoeandroid.com/home.php";

	public static LinkedList<NewsItem> getEOENewsList(String eoeUrl, int pageNo) throws Exception
	{
		// eoeurl = http://news.eoe.cn/news/tag/Android/pageNum/
		// http://news.eoe.cn/news/tag/Android/pageNum/5.html#end_lb
		LinkedList<NewsItem> data = null;
		String url = eoeUrl + pageNo + ".html";
		if (url.startsWith(BASE_EOE_BLOG_URL))
		{
			url = eoeUrl+"&mod=space&do=blog&view=all&page="+pageNo;
		}
		System.out.println("url = " + url);
		Document document = Jsoup.connect(url).get();
		System.out.println("document = " + document);
		Elements elements = document.getElementsByClass("ue-body-new-list");
		System.out.println("KKK"+elements);
		Element element = null;
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		if (!elements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < elements.size(); i++)
			{
				element = elements.get(i);
				Elements tmpElements = element.getElementsByClass("ue-body-new-list-t");
				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("a");
					if (!tmpElements.isEmpty())
					{
						newsUrl = tmpElements.get(0).attr("href");
						System.out.println(newsUrl);
					}
					tmpElements = tmpElements.get(0).getElementsByTag("h2");
					if (!tmpElements.isEmpty())
					{
						title = tmpElements.get(0).text();
						// System.out.println(title);
					}
					else
					{

					}
				}
				tmpElements = element.getElementsByClass("ue-body-new-list-desc-img");
				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("img");
					if (!tmpElements.isEmpty())
					{
						src = tmpElements.get(0).attr("src");
						// String img = tmpElements.get(0).attr("src");
						// System.out.println( img);
					}
				}
				tmpElements = element.getElementsByClass("ue-body-new-list-desc-text");
				if (!tmpElements.isEmpty())
				{
					subTitle = tmpElements.get(0).text();
					// System.out.println(tmpElements.get(0).text());
				}
				tmpElements = element.getElementsByClass("ue-body-new-list-other-r");
				if (!tmpElements.isEmpty())
				{
					time = tmpElements.get(0).text().trim();
					// System.out.println(tmpElements.get(0).text());
					time = getTime(time);
				}
				NewsItem item = new NewsItem();
				item.setSrc(src);
				item.setTime(time);
				item.setMd5(md5);
				item.setUrl(newsUrl);
				item.setTitle(title);
				item.setSubTitle(subTitle);
				data.add(item);

			}
		}

		return data;
	}

	private static String getTime(String time)
	{
		String newTime = "今天";
		if (time.contains("月") && time.contains("日") && time.contains(" "))
		{
			newTime = time.split(" ")[0];
		}
		return newTime;
	}
}

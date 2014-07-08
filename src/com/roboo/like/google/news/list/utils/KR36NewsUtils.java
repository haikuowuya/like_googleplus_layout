package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

public class KR36NewsUtils
{
	private static final String TAG_ARTICAL = "article";
	private static final String KR36_URL = "http://www.36kr.com";
	public static LinkedList<NewsItem> get36KRNewsList(String kr36Url, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = kr36Url + pageNo;
		System.out.println("kr36Url = " + url);
		Document document = Jsoup.connect(url).get();
//		 System.out.println("document = " + document);
		Elements elements = document.getElementsByTag(TAG_ARTICAL);
		Element element = null;
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time ="第"+pageNo+"页";
	
		if (!elements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < elements.size(); i++)
			{
				element = elements.get(i);
				Elements tmpElements = element.getElementsByClass("feature-img");
				System.out.println("tmpElements = " + tmpElements);
				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("a");
					if (!tmpElements.isEmpty())
					{
						newsUrl = KR36_URL+tmpElements.get(0).attr("href");
						System.out.println(newsUrl);
					}
					tmpElements = tmpElements.get(0).getElementsByTag("img");
					if (!tmpElements.isEmpty())
					{
						src = tmpElements.get(0).attr("src");
					}
					else
					{

					}
				}
				tmpElements = element.getElementsByClass("right-col");
				if (!tmpElements.isEmpty())
				{
					title = tmpElements.get(0).text();
//					subTitle = tmpElements.get(1).text();
				}
				if(TextUtils.isEmpty(newsUrl))
				{
					newsUrl = "http://www.baidu.com";
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

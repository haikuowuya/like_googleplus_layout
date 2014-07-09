package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

@SuppressLint("SimpleDateFormat")
public class HuXiuNewsUtils
{
	private static final String CLASS_UL_LIST = "ul-list";
	private static final String HUXIU_URL = "http://m.huxiu.com";

	public static LinkedList<NewsItem> getHuXiuNewsList(String huXiuUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = huXiuUrl + pageNo + ".html";
		System.out.println("huXiuUrl = " + url);
		Document document = Jsoup.connect(url).get();
		// System.out.println("document = " + document);
		Elements elements = document.getElementsByClass(CLASS_UL_LIST);
		Element element = null;
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time = "第" + pageNo + "页";
		if (!elements.isEmpty())
		{
			elements = elements.get(0).getElementsByTag("li");
			if (!elements.isEmpty())
			{
				data = new LinkedList<NewsItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					element = elements.get(i);
					Elements tmpElements = element.getElementsByTag("a");
					if (!tmpElements.isEmpty())
					{
						if (!tmpElements.isEmpty())
						{
							newsUrl = HUXIU_URL + tmpElements.get(0).attr("href");
						}
					}
					tmpElements = element.getElementsByClass("p1");
					if (!tmpElements.isEmpty())
					{
						title = tmpElements.text();
						tmpElements = tmpElements.get(0).getElementsByTag("img");
						if (!tmpElements.isEmpty())
						{
							src = tmpElements.get(0).attr("src");
						}
					}
					tmpElements = element.getElementsByClass("p2");
					if (!tmpElements.isEmpty())
					{
						subTitle = tmpElements.text();
					}
					tmpElements = element.getElementsByClass("p-btm");
					if (!tmpElements.isEmpty())
					{
						tmpElements = tmpElements.get(0).getElementsByTag("time");
						if (!tmpElements.isEmpty())
						{
							time = tmpElements.get(0).attr("title");
							if (!TextUtils.isEmpty(time))
							{
								time = getTime(time);
							}
						}
					}
					if (TextUtils.isEmpty(newsUrl))
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
						newTime = month + " 月 " + day + " 日 ";
					}
				}
			}
		}

		return newTime.trim();
	}
}

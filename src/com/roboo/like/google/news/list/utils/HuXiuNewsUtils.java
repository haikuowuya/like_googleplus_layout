package com.roboo.like.google.news.list.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.droidux.trial.da;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

@SuppressLint("SimpleDateFormat")
public class HuXiuNewsUtils
{
	private static final String CLASS_FOCUS_LIST = "focus-list";
	private static final String HUXIU_URL = "http://www.huxiu.com/";

	public static LinkedList<NewsItem> getHuXiuNewsList(String huXiuUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = huXiuUrl + pageNo;
		System.out.println("huXiuUrl = " + url);
		Document document = Jsoup.connect(url).get();
//		System.out.println("document = " + document);
		Elements elements = document.getElementsByClass(CLASS_FOCUS_LIST);
		Element element = null;
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time = "第" + pageNo + "页";
		if (!elements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < elements.size(); i++)
			{
				element = elements.get(i);
				System.out.println("element = " + element);
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
		return data;
	}

	private static String getTime(String time)
	{
		String newTime = "今天";
		String[] tmp = time.split("•");
		if (tmp.length == 2)
		{
			newTime = tmp[1];
			tmp = newTime.split("/");
			if (tmp.length == 3)
			{
				String year = tmp[0];
				String month = tmp[1];
				String day = tmp[2];
				if (day.contains(" "))
				{
					day = day.split(" ")[0];
				}
				newTime = year.trim() + " 年 " + month.trim() + " 月 ";// + day.trim() + " 日 ";
			}
			else if (tmp.length == 2)
			{
				String month = tmp[0];
				String day = tmp[1];
				if (month.contains(":"))// 08:48/08
				{
					day = tmp[1];
					month = new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis()));

				}
				else if (day.contains(":")) // 07/08 08:45
				{
					{
						tmp = day.split(" ");
						if (tmp.length == 2)
						{
							day = tmp[0];
						}
					}
				}
				newTime = month.trim() + " 月 " + day.trim() + " 日 ";
				newTime = month.trim() + "月";// 按月分比较好一点
			}
		}
		if (time.contains("年前"))
		{
			newTime = time;
		}
		return newTime.trim();
	}
}

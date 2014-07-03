package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.droidux.trial.da;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

/**
 * 获取科技锋芒网站数据的工具类 http://www.phonekr.com/
 * 
 * @author bo.li
 */
public class PhoneKRNewsUtils
{
	private static final String KE_JI_FENG_MANG_URL = "http://www.phonekr.com/page/";

	public static LinkedList<NewsItem> getPhoneKRNewsList(String phoneKRUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> items = null;
		String url = phoneKRUrl + pageNo + "/";
		System.out.println("url = " + url);
		Document document = Jsoup.connect(url).timeout(20000).get();
		Element divTag = document.getElementById("xs-main");
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		if (null != divTag)
		{
			Elements entryTags = divTag.getElementsByClass("xs-entry");
			if (null != entryTags && entryTags.size() > 0)
			{
				items = new LinkedList<NewsItem>();
				for (Element e : entryTags)
				{
					Elements aTags = e.getElementsByTag("a");
					if (null != aTags && aTags.size() > 0)
					{
						newsUrl = aTags.get(0).attr("href");
						System.out.println("newsUrl = " + newsUrl);
						if (newsUrl != null && newsUrl.startsWith("www"))
						{
							newsUrl += "http://";
						}
						url += "index.php";

					}
					Elements timeTags = e.getElementsByClass("xs-entry-meta");
					if (!timeTags.isEmpty())
					{
						time = timeTags.first().textNodes().get(0).text();
						time = getTime(time);
					}

					Elements imgTags = e.getElementsByTag("img");
					if (null != imgTags && imgTags.size() > 0)
					{
						src = imgTags.get(0).attr("src");
						title = imgTags.get(0).attr("alt");
						// System.out.println("img = " + img + " title = " +
						// title);

					}
					Elements pTags = e.getElementsByTag("p");
					if (null != pTags && pTags.size() > 0)
					{
						subTitle = pTags.get(0).text();
						// System.out.println("content = " + content);

					}
					NewsItem item = new NewsItem();
					item.setSrc(src);
					item.setTime(time);
					item.setMd5(md5);
					item.setUrl(newsUrl);
					item.setTitle(title);
					item.setSubTitle(subTitle);
					items.add(item);
				}
			}
		}

		return items;
	}

	private static String getTime(String time)
	{
		String newTime = "今天";
		if (!TextUtils.isEmpty(time))
		{
			if (time.contains("年") && time.contains("月") && time.contains("日"))
			{
				String month = time.split("年")[1].split("月")[0].trim();
				month = getMonth(month);
				String day = time.split("月")[1].split("日")[0].trim();
				if(Integer.parseInt(day) < 10)
				{
					day ="0"+ day;
				}
				newTime = month + " 月 " + day + " 日";
			}
		}
		return newTime;
	}

	private static String getMonth(String month)
	{
		if ("一".equals(month))
		{
			month = "1";
		}
		else if ("二".equals(month))
		{
			month = "2";
		}
		else if ("三".equals(month))
		{
			month = "3";
		}
		else if ("四".equals(month))
		{
			month = "4";
		}
		else if ("五".equals(month))
		{
			month = "5";
		}
		else if ("六".equals(month))
		{
			month = "6";
		}
		else if ("七".equals(month))
		{
			month = "7";
		}
		else if ("八".equals(month))
		{
			month = "8";
		}
		else if ("九".equals(month))
		{
			month = "9";
		}
		else if ("十".equals(month))
		{
			month = "10";
		}
		else if ("十一".equals(month))
		{
			month = "11";
		}
		else
		{
			month = "12";
		}
		return month;
	}
}

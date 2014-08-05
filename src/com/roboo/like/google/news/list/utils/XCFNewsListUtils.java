package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

/***
 * 下厨房
 * 
 * @author bo.li 2014-8-4 上午10:06:08 TODO
 */
public class XCFNewsListUtils extends BaseNewsListUtils
{
	private static final String BASE_XCF_URL = "http://m.xiachufang.com";
	private static final int PAGE_SIZE = 15;

	public static LinkedList<NewsItem> getXCFNewsList(String xcfUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = xcfUrl + "&limit=" + PAGE_SIZE + "&offset=" + (pageNo - 1) * PAGE_SIZE;
		System.out.println("xcfUrl = " + url);
		Document document = Jsoup.connect(url).get();
		Elements elements = null;
		Element element = document.getElementById("add-more-container");
		String title = null,source="下厨房", subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time = "第" + pageNo + "页";
		if (null != element)
		{
			elements = element.getElementsByTag("article");
		}
		if (!elements.isEmpty())
		{
			if (!elements.isEmpty())
			{
				data = new LinkedList<NewsItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					element = elements.get(i);
					Elements tmpElements = element.getElementsByClass("body");
					if (!tmpElements.isEmpty())
					{
						tmpElements = tmpElements.get(0).getElementsByClass("cover");
						if (!tmpElements.isEmpty())
						{
							src = tmpElements.get(0).attr("style");
							src = handlSrc(src);
						}
//						System.out.println("src = " + src);
					}
					tmpElements = element.getElementsByClass("content");
					if (!tmpElements.isEmpty())
					{
						title = tmpElements.get(0).getElementsByTag("header").text();
						subTitle = tmpElements.get(0).getElementsByTag("p").text();
					}
					tmpElements = element.getElementsByTag("a");
					if (!tmpElements.isEmpty())
					{
						newsUrl = BASE_XCF_URL + tmpElements.get(0).attr("href");
					}
					NewsItem item = new NewsItem();
					item.setSrc(src);
					item.setTime(time);
					item.setMd5(md5);
					item.setUrl(newsUrl);
					item.setTitle(title);
					item.setSource(source);
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

	private static String handlSrc(String src)
	{
		// src = background-image: url(http://i3.xiachufang.com/image/280/f55c60929f3611e38844b8ca3aeed2d7.jpg);
		if (!TextUtils.isEmpty(src))
		{
			if (src.contains("url"))
			{
				 src = src.split("url")[1];
				 src = src.substring(1, src.length()-2);
			}
		}
		return src;
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

	@Override
	public LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		return getXCFNewsList(baseUrl, pageNo);
	}
}

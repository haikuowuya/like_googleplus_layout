package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

public class ITHomeUtils
{
	private static final String TODAY = "今日";
	private static final String IT_HOME_STYLE_CLASS_NAME = "cate_list";
	private static final int TIME_OUT = 20000;
	private static final String IT_HOME_NEWS_CONTENT_ID = "paragraph";
	private static final String FOUR_BLANK = "    ";
	private static final String IT_HOME_LAZY_IMG_URL = "http://img.ithome.com/images/v2/grey.gif";

	public static LinkedList<NewsItem> getITHomeNewsList(String ithomeUrl, int pageNo) throws IOException
	{
		LinkedList<NewsItem> data = null;
		// ithomeurl = http://it.ithome.com/category/10_1.html
		String itHomeUrl = ithomeUrl + pageNo + ".html";
		Document document = Jsoup.connect(itHomeUrl).timeout(20000).get();
		if (null != document)
		{
			Elements es = document.getElementsByClass(IT_HOME_STYLE_CLASS_NAME);
			if (null != es && es.size() > 0)
			{
				Element element = es.get(0);
				if (null != element)
				{
					data = new LinkedList<NewsItem>();
					Elements ess = element.getElementsByTag("li");
					if (null != ess && ess.size() > 0)
					{
						for (Element ee : ess)
						{
							Element titleElement = ee.getElementsByTag("h2").get(0).getElementsByTag("a").get(0);
							Element timeElement = ee.getElementsByTag("h2").get(0).getElementsByTag("span").get(0);
							if (null != ee.getElementsByClass("list_thumbnail") && ee.getElementsByClass("list_thumbnail").size() > 0)
							{
								Element aElement = ee.getElementsByClass("list_thumbnail").get(0);
								String url = aElement.attr("href");

								Element imgElement = aElement.getElementsByTag("img").get(0);
								Element pElement = ee.getElementsByTag("p").get(0);
								NewsItem news = new NewsItem();
								String md5 = MD5Utils.generate(url);
								String title = titleElement.text().trim();
								String subTitle = pElement.text();
								String time = timeElement.text();
								String src = imgElement.attr("data-original");
								if (!TextUtils.isEmpty(url))
								{
									int start = url.lastIndexOf("/");
									int end = url.lastIndexOf(".");
									if (start != -1 && end != -1 && end > start)
									{
										String newsId = url.substring(start + 1, end);
										news.setNewsId(newsId);
									}
								}
								if (TODAY.equals(time))
								{
									SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
									time = dateFormat.format(new Date(System.currentTimeMillis()));
								}
								news.setSrc(src);
								news.setTime(time);
								news.setMd5(md5);
								news.setUrl(url);
								news.setTitle(title);
								news.setSubTitle(subTitle);
								data.add(news);
							}
						}
					}
				}
			}
		}
		return data;
	}
}

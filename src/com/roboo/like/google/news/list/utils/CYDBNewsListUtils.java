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
/**
 * 获取穿衣打扮网站信息
 * @author bo.li
 *
 * 2014-8-4 上午11:12:44
 *
 * TODO
 */
@SuppressLint("SimpleDateFormat")
public class CYDBNewsListUtils extends BaseNewsListUtils
{
	private static final String CLASS_TEXTBOX = "textbox";
	private static final String CYDB_URL = "http://www.chong4.com.cn";

	public static LinkedList<NewsItem> getCydbNewsList(String cydbUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = cydbUrl + pageNo;
		System.out.println("cydbUrl = " + url);
		Document document = Jsoup.connect(url).get();
		// System.out.println("document = " + document);
		Elements elements = document.getElementsByClass(CLASS_TEXTBOX);
		Element element = null;
		String title = null, subTitle = null,source="穿衣打扮", md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time = "第" + pageNo + "页";
		if (!elements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < elements.size(); i++)
			{
				element = elements.get(i);
				Elements tmpElements = element.getElementsByClass("textbox-title");

				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("h2");
					if (!tmpElements.isEmpty())
					{
						tmpElements = tmpElements.get(0).getElementsByTag("a");
//						System.out.println("a = " + tmpElements);
						if (!tmpElements.isEmpty())
						{
							newsUrl = CYDB_URL + tmpElements.get(0).attr("href");
							title = tmpElements.get(0).text();
						}
					}
				}
				tmpElements = element.getElementsByClass("textbox-label");
				if (!tmpElements.isEmpty())
				{
					time = tmpElements.get(0).text().trim();
					time = getTime(time);
				}
				tmpElements = element.getElementsByClass("textbox-content");
				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("p");
					if (!tmpElements.isEmpty())
					{
						subTitle = tmpElements.get(0).text();
						if (!TextUtils.isEmpty(subTitle))
						{
							subTitle = subTitle.trim();
						}
						if (tmpElements.size() > 0)
						{
							tmpElements = tmpElements.get(1).getElementsByTag("img");
						}
					}
					if (!tmpElements.isEmpty())
					{
						src = tmpElements.get(0).attr("src");
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
				item.setSource(source);
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
		if (!TextUtils.isEmpty(time))
		{
			time = time.trim();
			if (time.contains(" "))
			{
				String[] tmp = time.split(" ");
				if (tmp.length > 1)
				{
					time = tmp[1];
					if (time.contains("/"))
					{
						tmp = time.split("/");
						if (tmp.length > 2)
						{
							String month = tmp[1];
							String day = tmp[2];
							newTime = month + "月" + day + "日";
						}
					}
				}
			}
		}
		return newTime.trim();
	}

	@Override
	public LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		 return getCydbNewsList(baseUrl, pageNo);
	}
}

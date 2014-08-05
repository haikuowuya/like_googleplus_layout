package com.roboo.like.google.news.list.utils;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;

/**
 * 获取IT199网站信息
 * @author bo.li
 *
 * 2014-8-4 上午11:09:16
 *
 * TODO
 */
public class IT199NewsListUtils extends BaseNewsListUtils
{
	private static final String TAG_ARTICAL = "article";

	public static LinkedList<NewsItem> getIT199NewsList(String it199Url, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = it199Url + pageNo;
		System.out.println("it199Url = " + url);
		Document document = Jsoup.connect(url).get();
		// System.out.println("document = " + document);
		Elements elements = document.getElementsByTag(TAG_ARTICAL);
		Element element = null;
		String title = null, subTitle = null,source="IT199", md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		time = "今天";
		time ="第"+pageNo+"页";
	
		if (!elements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < elements.size(); i++)
			{
				element = elements.get(i);
				Elements tmpElements = element.getElementsByClass("entry-thumb");
				if (!tmpElements.isEmpty())
				{
					tmpElements = tmpElements.get(0).getElementsByTag("a");
					if (!tmpElements.isEmpty())
					{
						newsUrl = tmpElements.get(0).attr("href");
//						System.out.println(newsUrl);
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
				tmpElements = element.getElementsByClass("entry-title");
				if (!tmpElements.isEmpty())
				{
					title = tmpElements.get(0).text();
					
					if(TextUtils.isEmpty(newsUrl))
					{
						newsUrl = tmpElements.get(0).getElementsByTag("a").get(0).attr("href");
					}
				}
				tmpElements = element.getElementsByClass("post-excerpt");
				if (!tmpElements.isEmpty())
				{
					subTitle = tmpElements.get(0).text();
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
				item.setSource(source);
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

	@Override
	public LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		return  getIT199NewsList(baseUrl, pageNo);
	}
}

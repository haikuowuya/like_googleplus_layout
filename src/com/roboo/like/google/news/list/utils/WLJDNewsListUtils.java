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
 *  获取网络尖刀网站信息
 * @author bo.li
 *
 * 2014-8-4 上午11:13:53
 *
 * TODO
 */
@SuppressLint("SimpleDateFormat")
public class WLJDNewsListUtils extends BaseNewsListUtils
{
	private static final String CLASS_LIST = "listbg";
	private static final String HUXIU_URL = "http://m.huxiu.com";

	public static LinkedList<NewsItem> getWljdNewsList(String wljdUrl, int pageNo) throws Exception
	{
		LinkedList<NewsItem> data = null;
		String url = wljdUrl + pageNo ;
		System.out.println("wljdUrl = " + url);
		Document document = Jsoup.connect(url).get();
	 
		Elements elements = document.getElementsByClass(CLASS_LIST);
		Element element = null;
		String title = null, subTitle = null, source ="网络尖刀",md5 = null, time = null, src = null, newsUrl = null;
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
					Elements tmpElements = element.getElementsByClass("list_img");
					if(!tmpElements.isEmpty())
					{
						tmpElements = tmpElements.get(0).getElementsByTag("a");
						if(!tmpElements.isEmpty())
						{
							newsUrl = tmpElements.get(0).attr("href");
							title = tmpElements.get(0).attr("title");
							tmpElements =  tmpElements.get(0).getElementsByTag("img");
							if(!tmpElements.isEmpty())
							{
								src = tmpElements.get(0).attr("src");
							}		
						}
					}
					tmpElements = element.getElementsByClass("list_Brief");
					if(!tmpElements.isEmpty())
					{
						subTitle = tmpElements.get(0).text();
					}
					
					tmpElements = element.getElementsByClass("list_time");
					if(!tmpElements.isEmpty())
					{
						time = tmpElements.get(0).text();
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
		return getWljdNewsList(baseUrl, pageNo);
	}
}

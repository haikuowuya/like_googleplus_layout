package com.roboo.like.google.news.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	public static LinkedList<NewsItem> getPhoneKRNewsList(String phoneKRUrl, int  pageNo) throws IOException
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
}

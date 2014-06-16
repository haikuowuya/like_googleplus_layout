package com.roboo.like.google.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.NewsItem;

@SuppressLint("SimpleDateFormat")
public class NewsUtils
{
	private static final int TIME_OUT = 20000;
	private static final String TODAY = "今日";
	private static final String QQ_NEWS_CONTENT_ID = "Cnt-Main-Article-QQ";
	private static final String IT_HOME_NEWS_CONTENT_ID = "paragraph";
	private static final String IT_HOME_STYLE_CLASS_NAME = "cate_list";
	private static final String FOUR_BLANK = "    ";
	private static final String IT_HOME_LAZY_IMG_URL = "http://img.ithome.com/images/v2/grey.gif";

	public static String getNewsData(String newsUrl) throws IOException
	{
		String data = null;
		Document document = Jsoup.connect(newsUrl).get();
		if (null != document)
		{
			Element element = document.getElementById(QQ_NEWS_CONTENT_ID);
			if (null != element)
			{
				Elements elements = element.getElementsByTag("p");
				StringBuffer sb = new StringBuffer();
				for (Element e : elements)
				{
					String pHtml = e.html();
					if (null != pHtml)
					{
						if (pHtml.contains("<img"))
						{
							Elements imgElements = e.getElementsByTag("img");
							if (null != imgElements && imgElements.size() > 0)
							{
								for (Element ee : imgElements)
								{
									sb.append(ee.attr("src") + ",");
								}
							}
						}
					}
				}
				if (sb.length() > 0)
				{
					sb.deleteCharAt(sb.length() - 1);
				}
				for (Element e : elements)
				{
					String pHtml = e.html();
					if (null != pHtml)
					{
						if (pHtml.contains("<img"))
						{
							Elements imgElements = e.getElementsByTag("img");
							if (null != imgElements && imgElements.size() > 0)
							{
								for (int i = 0; i < imgElements.size(); i++)
								{
									Element ee = imgElements.get(i);
									ee.attr("onclick", "window.android.toast(\"" + sb.toString() + "\"" + "," + i + ")");
								}
							}
						}
					}
				}
				data = element.toString();
				if (null != data)
				{
					return new String(data.getBytes(), "UTF-8");
				}
			}
		}
		return data;
	}

	public static LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws IOException
	{

		if (GoogleApplication.mCurrentType.equals(GoogleApplication.TYPE_CSDN) && baseUrl.equals(GoogleApplication.BASE_OFFICE_URL))
		{
			return getCSDNNewsList(baseUrl, pageNo);
		}
		else if (GoogleApplication.mCurrentType.equals(GoogleApplication.TYPE_ITHOME))
		{
			return getITHomeNewsList(baseUrl, pageNo);
		}
		else
		{
			return getITHomeNewsList(baseUrl, pageNo);
		}
	}

	private static LinkedList<NewsItem> getCSDNNewsList(String csdnUrl, int pageNo) throws IOException
	{
		// http://mobile.csdn.net/mobile/2 【移动开发】
		LinkedList<NewsItem> data = null;
		Document document;
		Elements elements;
		Elements majorElements;
		Element majorElement;
		Element minorElement;
		String url = csdnUrl + pageNo;
		document = Jsoup.connect(url).get();
		majorElements = document.getElementsByClass("unit");
		String title = null, subTitle = null, md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);

		if (!majorElements.isEmpty())
		{
			data = new LinkedList<NewsItem>();
			for (int i = 0; i < majorElements.size(); i++)
			{

				majorElement = majorElements.get(i);
				elements = majorElement.getElementsByTag("h1");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					title = minorElement.text();
					System.out.println("title = " + title);
				}

				elements = majorElement.getElementsByClass("ago");
				if (!elements.isEmpty())
				{

					minorElement = elements.get(0);
					time = minorElement.text();
					System.out.println("time = " + time);
					if (!time.contains("小时前"))
					{
						if (time.contains(" "))
						{
							String date = time.split(" ")[0];
							if (date.contains("-") && date.split("-").length > 2)
							{
								time = date.split("-")[1] + "月" + date.split("-")[2] + "日";
							}
							System.out.println("date = " + date);
						}
					}
					else
					{
						time = new SimpleDateFormat("MM月dd日").format(new Date(System.currentTimeMillis()));
					}
				}

				elements = majorElement.getElementsByTag("a");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					newsUrl = minorElement.attr("href");
					System.out.println("newsUrl = " + newsUrl);
				}
				elements = majorElement.getElementsByTag("img");
				if (!elements.isEmpty())
				{

					minorElement = elements.get(0);
					src = minorElement.attr("src");
					System.out.println("img = " + src);
				}
				elements = majorElement.getElementsByTag("dd");
				if (!elements.isEmpty())
				{
					minorElement = elements.get(0);
					subTitle = minorElement.text();
					System.out.println("subTitle = " + subTitle);
					System.out.println("\n");
				}
				NewsItem item = new NewsItem();
				item.setSrc(src);
				item.setTime(time);
				item.setMd5(md5);
				item.setUrl(url);
				item.setTitle(title);
				item.setSubTitle(subTitle);
				data.add(item);
			}
		}

		return data;
	}

	private static LinkedList<NewsItem> getITHomeNewsList(String ithomeUrl, int pageNo) throws IOException
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

	/** 获取每条新闻的图片和内容 */
	public static String getITHomeNewsData(String newsUrl) throws IOException
	{
		String data = null;
		Document document = Jsoup.connect(newsUrl).get();
		if (null != document)
		{
			Element element = document.getElementById(IT_HOME_NEWS_CONTENT_ID);
			Elements es = element.getElementsByTag("p");
			es.first().html("<font color='red'>IT之家</font>");

			if (null != es)
			{
				for (Element element2 : es)
				{
					Elements imgsElement = element2.getElementsByTag("img");
					if (null != imgsElement && imgsElement.size() > 0)
					{
						Element imgElement = imgsElement.first();
						imgElement.attr("onclick", "window.android.toast(\"" + imgElement.attr("src") + "\"" + "," + 0 + ")");
					}
				}
				data = es.toString();
				if (null != data)
				{
					return new String(data.getBytes(), "UTF-8");
				}
			}
		}
		return data;
	}

	/** 获取每条新闻的图片和内容 */
	public static LinkedList<String> getITHomeNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Document document = Jsoup.connect(newsUrl).timeout(TIME_OUT).get();
		if (null != document)
		{
			Element element = document.getElementById(IT_HOME_NEWS_CONTENT_ID);
			if (element != null)
			{
				Elements es = element.getElementsByTag("p");
				if (null != es)
				{
					data = new LinkedList<String>();
					for (Element element2 : es)
					{
						String content = "";
						Elements imgsElement = element2.getElementsByTag("img");
						if (null != imgsElement && imgsElement.size() > 0)
						{
							Element imgElement = imgsElement.first();
							// System.out.println("图片地址  = "+
							// imgElement.attr("data-original"));
							imgElement.attr("onclick", "window.android.toast(\"" + imgElement.attr("src") + "\"" + "," + 0 + ")");
							content = imgElement.attr("src");
							if (IT_HOME_LAZY_IMG_URL.equals(content))
							{
								content = imgElement.attr("data-original");
							}
						}
						else if (!element2.hasClass("content_copyright"))
						{
							content = FOUR_BLANK + element2.text();
						}
						if (content.length() > FOUR_BLANK.length())
						{
							data.add(content);
						}
					}
				}
			}
			else
			{
				System.out.println("出错的URL   = " + newsUrl);
			}
		}
		return data;
	}

}

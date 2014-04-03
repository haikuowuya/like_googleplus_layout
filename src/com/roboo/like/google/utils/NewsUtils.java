package com.roboo.like.google.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;

public class NewsUtils
{
	private static final int TIME_OUT= 20000;
	private static final String QQ_NEWS_STYLE_CLASS_NAME = "Q-tpWrap";
	private static final String QQ_NEWS_CONTENT_ID = "Cnt-Main-Article-QQ";
	private static final String IT_HOME_NEWS_CONTENT_ID = "paragraph";
	private static final String IT_HOME_STYLE_CLASS_NAME = "cate_list";
	private static final String FOUR_BLANK = "    ";
	private static final String IT_HOME_LAZY_IMG_URL = "http://img.ithome.com/images/v2/grey.gif";

	public static LinkedList<NewsItem> getNewsList(String channelUrl, int pageNo) throws IOException
	{
		LinkedList<NewsItem> data = null;
		Document document = Jsoup.connect(channelUrl).get();
		if (null != document)
		{
			Elements elements = document.getElementsByClass(QQ_NEWS_STYLE_CLASS_NAME);
			if (null != elements && elements.size() >= 0)
			{
				data = new LinkedList<NewsItem>();
				for (int i = 0; i < (elements.size() > 10 ? 10 : elements.size()); i++)
				{
					Element element = elements.get(i);
					String src = element.getElementsByTag("img").get(0).attr("src");
					String title = element.getElementsByClass("linkto").get(0).text();
					String subTitle = element.getElementsByTag("p").get(0).text();
					String url = element.getElementsByClass("linkto").get(0).attr("href");
					NewsItem news = new NewsItem();
					news.setUrl(url);
					news.setTitle(title);
					news.setSubTitle(subTitle);
					news.setMd5(MD5Utils.generate(url));
					news.setSrc(src);
					data.add(news);
				}
			}

		}
		return data;
	}

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

	// public static NewsContent getNewsContent(String newsUrl) throws
	// IOException
	// {
	// NewsContent newsContent = null;
	// Document document = Jsoup.connect(newsUrl).get();
	// if (null != document)
	// {
	// Element element = document.getElementById(QQ_NEWS_CONTENT_ID);
	// if (null != element)
	// {
	// newsContent = new NewsContent();
	// System.out.println("element = " + element);
	// Elements elements = element.getElementsByTag("p");
	// if (null != elements)
	// {
	// LinkedList<String> contentList = new LinkedList<String>();
	// LinkedList<String> srcList = new LinkedList<String>();
	// for (Element e : elements)
	// {
	// String pHtml = e.html();
	// if (null != pHtml)
	// {
	// if (pHtml.contains("<img"))
	// {
	// Elements imgElements = e.getElementsByTag("img");
	// if (null != imgElements && imgElements.size() > 0)
	// {
	// for (Element ee : imgElements)
	// {
	// srcList.add(ee.attr("src"));
	// }
	// }
	// }
	// else
	// {
	// contentList.add(pHtml);
	// }
	// }
	// }
	// newsContent.setSrcList(srcList);
	// newsContent.setContentList(contentList);
	// }
	// }
	// }
	// return newsContent;
	// }
	public static LinkedList<NewsItem> getITHomeNewsList(String ithomeurl, int pageNo) throws IOException
	{
		LinkedList<NewsItem> data = null;
		// ithomeurl = http://it.ithome.com/category/10_1.html
		String itHomeUrl = ithomeurl + pageNo + ".html";
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
							Element h2Element = ee.getElementsByTag("h2").get(0).getElementsByTag("a").get(0);
							Element aElement = ee.getElementsByClass("list_thumbnail").get(0);
							Element imgElement = aElement.getElementsByTag("img").get(0);
							Element pElement = ee.getElementsByTag("p").get(0);
							NewsItem news = new NewsItem();
							String url = aElement.attr("href");
							String md5 = MD5Utils.generate(url);
							String title = h2Element.text();
							String subTitle = pElement.text();
							String src = imgElement.attr("data-original");
							if (!TextUtils.isEmpty(url))
							{
								int start = url.lastIndexOf("/");
								int end = url.lastIndexOf(".");
								if (start != -1 && end != -1)
								{
									String newsId = url.substring(start + 1, end);
									news.setNewsId(newsId);
								}
							}
							news.setSrc(src);
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
		return data;
	}

	public static LinkedList<NewsItem> getITHomeNews(String baseUrl, int category, int pageNo) throws IOException
	{
		LinkedList<NewsItem> data = null;
		String itHomeUrl = baseUrl + category + "_" + pageNo + ".html";
		Document document = Jsoup.connect(itHomeUrl).timeout(TIME_OUT).get();
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
							Element h2Element = ee.getElementsByTag("h2").get(0).getElementsByTag("a").get(0);
							Element aElement = ee.getElementsByClass("list_thumbnail").get(0);
							Element imgElement = aElement.getElementsByTag("img").get(0);
							Element pElement = ee.getElementsByTag("p").get(0);
							NewsItem news = new NewsItem();
							String url = aElement.attr("href");
							String md5 = MD5Utils.generate(url);

							String title = h2Element.text();
							String subTitle = pElement.text();
							String src = imgElement.attr("data-original");
							news.setSrc(src);
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

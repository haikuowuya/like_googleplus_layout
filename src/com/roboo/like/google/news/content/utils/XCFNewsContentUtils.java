package com.roboo.like.google.news.content.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

public class XCFNewsContentUtils extends BaseNewsContentUtils
{
	@Override
	public LinkedList<String> getNewsContentDataList(String newsUrl) throws IOException
	{
		return getXCFNewsDataList(newsUrl);
	}

	public static LinkedList<String> getXCFNewsDataList(String newsUrl) throws IOException
	{
		LinkedList<String> data = null;
		Elements majorElements;
		Element majorElement = null;
		String content = "";
		Document document = Jsoup.connect(newsUrl).timeout(TIME_OUT).get();
		// System.out.println("document = " + document);
		majorElements = document.getElementsByClass("recipe-full");
		if (!majorElements.isEmpty())
		{
			majorElement = majorElements.get(0);
			if (majorElement != null)
			{
				data = new LinkedList<String>();
				majorElements = majorElement.getElementsByTag("div");
				if (!majorElements.isEmpty())
				{
					for (int i = 0; i < majorElements.size(); i++)
					{
						Element subElement = majorElements.get(i);
						if (subElement.hasClass("recipe-cover"))
						{
							content = handlSrc(subElement.attr("style"));
							data.add(content);
						}
						else if (subElement.hasClass("wrapper"))
						{
							Elements tmpElements = subElement.getElementsByTag("section");
							if (!tmpElements.isEmpty())
							{
								for (int ii = 0; ii < tmpElements.size(); ii++)
								{
									switch (ii)
									{
									case 0:
										content = FOUR_BLANK_SPACE + tmpElements.get(ii).text();
										data.add(content);
										break;
									case 1:
										Element subsubElement = tmpElements.get(ii);
										Elements tmptmpElements = subsubElement.siblingElements();
										if (!tmptmpElements.isEmpty() && tmptmpElements.size() == 2)
										{
											content = "$" + tmptmpElements.get(0).text() + "$";
											data.add(content);
											tmptmpElements = tmptmpElements.get(1).getElementsByTag("li");
											if (!tmptmpElements.isEmpty())
											{
												for (int iii = 0; iii < tmptmpElements.size(); iii++)
												{
													content = tmptmpElements.get(iii).text();
													data.add(content);
												}
											}
										}
										break;
									case 2:
										content = FOUR_BLANK_SPACE + tmpElements.get(ii).text();
										data.add(content);
										break;
									case 3:
										content = FOUR_BLANK_SPACE + tmpElements.get(ii).text();
										data.add(content);
										break;
									default:
										content = FOUR_BLANK_SPACE + tmpElements.get(ii).text();
										data.add(content);
									}

								}
							}
						}
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
				src = src.substring(1, src.length() - 1);
			}
		}
		return src;
	}
}

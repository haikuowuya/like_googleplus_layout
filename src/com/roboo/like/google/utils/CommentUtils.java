package com.roboo.like.google.utils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CommentUtils
{
	private static final int TIME_OUT = 20000;
	 

	// ===========================================================================================
	// ===========================================================================================
	// "http://www.ithome.com/ithome/GetAjaxData.aspx?newsid=78507&type=commentpage&page=1";
	// ===========================================================================================
	// ===========================================================================================
	public static void getCommentList(String commentUrl) throws IOException
	{
		Document document = Jsoup.connect(commentUrl).timeout(TIME_OUT).get();
		if (null != document)
		{
			Element bodyElement = document.body();
			Elements elements = bodyElement.getElementsByTag("li");
			if(null != elements && elements.size() > 0)
			{
				for(Element element : elements)
				{
					System.out.println("element = " + element);
				}
			}
		}
	}
}

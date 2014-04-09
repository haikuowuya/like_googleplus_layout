package com.roboo.like.google.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.CommentItem;

public class CommentUtils
{
	private static final int TIME_OUT = 20000;
	private static final String SPLIT_REPLAY = "回复于";
	private static final String FOUR_BLANK = "    ";
	private static final String BR = "<br>";

	// ===========================================================================================
	// ===========================================================================================
	// "http://www.ithome.com/ithome/GetAjaxData.aspx?newsid=78507&type=commentpage&page=1";
	// ===========================================================================================
	// ===========================================================================================
	public static LinkedList<CommentItem> getCommentList(String commentUrl) throws IOException
	{
		LinkedList<CommentItem> data = null;
		Document document = Jsoup.connect(commentUrl).timeout(TIME_OUT).get();
		if (null != document)
		{
			Element bodyElement = document.body();
			Elements elements = bodyElement.getElementsByTag("li");
			if (null != elements && elements.size() > 0)
			{
				data = new LinkedList<CommentItem>();
				for (Element element : elements)
				{
//					System.out.println("element = " + element);
					Elements divElements = element.getElementsByTag("div");
					Element replyElement = null;
					if (element.getElementsByClass("reply") != null && element.getElementsByClass("reply").size() > 0)
					{
						replyElement = element.getElementsByClass("reply").get(0);
					}

					Element infoElement = null;
					if (divElements != null && divElements.size() > 0)
					{
						infoElement = divElements.get(0);
					}
					if (infoElement == null)
					{
						infoElement = element.getElementsByClass("re_info") == null ? null : element.getElementsByClass("re_info").get(0);
					}
					if (replyElement != null)
					{
						LinkedList<CommentItem> replyItems = new LinkedList<CommentItem>();

					}
					String floor = infoElement.getElementsByClass("p_floor").get(0).text();
					String nick = infoElement.getElementsByClass("nick").get(0).text();
					Elements phoneTypeElements = infoElement.getElementsByTag("span");
					String phoneType = "";
					String addressAndrTime = infoElement.ownText();
					String address = "";
					String replyTime = "";
					String agrssCount = "支持(0)";
					String disAgrssCount = "反对(0)";
					String content = "暂无评论";
					if (phoneTypeElements != null)
					{
						phoneType = phoneTypeElements.text();
					}
					if (addressAndrTime.contains(SPLIT_REPLAY))
					{
						address = addressAndrTime.split(SPLIT_REPLAY)[0];
						replyTime = addressAndrTime.split(SPLIT_REPLAY)[1];
					}

					Element commentElement = null;
					if (divElements != null && divElements.size() > 1)
					{
						commentElement = divElements.get(1);
					}
					if (commentElement == null)
					{
						commentElement = element.getElementsByClass("comm") == null ? null : element.getElementsByClass("comm").get(0);
					}
					if (commentElement == null)
					{
						commentElement = element.getElementsByClass("re_comm") == null ? null : element.getElementsByClass("re_comm").get(0);
					}
					 
					if (commentElement.getElementsByTag("p") != null && commentElement.getElementsByTag("p").size() > 0)
					{
						content = commentElement.getElementsByTag("p").get(0).text();
					}

					if (content.contains(BR))
					{
						content.replaceAll(BR, "\r\n");
					}
					content = FOUR_BLANK+ content;
					if (commentElement.getElementsByClass("comm_reply") != null &&commentElement.getElementsByClass("comm_reply").size() > 2)
					{
						agrssCount = commentElement.getElementsByClass("comm_reply").get(0).text();
						disAgrssCount = commentElement.getElementsByClass("comm_reply").get(2).text();
					}
					CommentItem item = new CommentItem();
					item.floor = floor;
					item.address = address;
					item.agreeCount = agrssCount;
					item.disAgressCount = disAgrssCount;
					item.nick = nick;
					item.phoneType = phoneType;
					item.replyContent = content;
					item.replyTime = replyTime;
					data.add(item);
				}
			}
		}
		return data;
	}
}

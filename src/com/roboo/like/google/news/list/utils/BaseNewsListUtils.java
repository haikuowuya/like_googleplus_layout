package com.roboo.like.google.news.list.utils;

import java.io.IOException;
import java.util.LinkedList;

import com.roboo.like.google.models.NewsItem;

public abstract class BaseNewsListUtils
{
	  public abstract LinkedList<NewsItem> getNewsList(String baseUrl, String pageNo) throws IOException;
}

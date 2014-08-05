package com.roboo.like.google.news.list.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.MD5Utils;
/***
 * 获取极客公园网站信息
 * @author bo.li
 *
 * 2014-8-4 上午11:07:47
 *
 * TODO
 */
public class GeekParkNewsListUtils extends BaseNewsListUtils
{
	public static final String GEEKPARK_ANDROID_URL = "http://www.geekpark.net/ajax/load_seeds/?type=img&tag_id=150027&num=10&t=1367647519";
	public static final String BASE_GEEKPARK_URL = "http://www.geekpark.net";

	public static LinkedList<NewsItem> getGeekParkNewsList(String geekparkUrl, int pageNo) throws Exception
	{
		// http://www.geekpark.net/ajax/load_seeds/?tag_id=150027; type=img&num=10&t=
		LinkedList<NewsItem> data = null;
		String url = geekparkUrl + "&type=img&num=10&t=" + pageNo;
		System.out.println("url = " + url);
		String title = null, subTitle = null, source="极客公园",md5 = null, time = null, src = null, newsUrl = null;
		md5 = MD5Utils.generate(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
		if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK)
		{
			InputStream inputStream = httpURLConnection.getInputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			StringBuffer stringBuffer = new StringBuffer();
			while ((len = inputStream.read(buffer)) != -1)
			{
				stringBuffer.append(new String(buffer, 0, len, HTTP.UTF_8));
			}
			if (!TextUtils.isEmpty(stringBuffer.toString()))
			{
				JSONObject jsonObject = new JSONObject(stringBuffer.toString());
				jsonObject = jsonObject.optJSONObject("data");
				if (null != jsonObject)
				{
					String currentT = jsonObject.getString("t");
					data = new LinkedList<NewsItem>();
					JSONArray jsonArray = jsonObject.optJSONArray("seeds");
					if (null != jsonArray)
					{
						for (int i = 0; i < jsonArray.length(); i++)
						{
							jsonObject = jsonArray.getJSONObject(i);

							String t = jsonObject.getString("t");
							title = jsonObject.optString("title");
							subTitle = jsonObject.optString("content");
							src = jsonObject.optString("thumb_img");
							time = jsonObject.optString("pub_time");
							time = getTime(time);
							newsUrl = BASE_GEEKPARK_URL + jsonObject.optString("url");

							NewsItem item = new NewsItem();
							item.setSrc(src);
							item.setTime(time);
							item.setSource(source);
							item.setMd5(md5);
							item.setUrl(newsUrl);
							item.setTitle(title);
							item.setSubTitle(subTitle);
							item.t = t;
							data.add(item);
						}
					}
				}
			}
		}

		return data;
	}

	private static String getTime(String time)
	{
		String newTime = "今天";
		if (time.contains("."))
		{
			String[] tmp = time.split("\\.");
			if (tmp.length > 2)
			{
				String year = tmp[0];
				String month = tmp[1];
				String day = tmp[2];
				newTime = year + "年" + month + "月";
				if (year.equals("2014"))
				{
//					newTime = newTime + day + "日";
				}
			}
		}
		return newTime;
	}

	@Override
	public LinkedList<NewsItem> getNewsList(String baseUrl, int pageNo) throws Exception
	{
		return getGeekParkNewsList(baseUrl, pageNo);
	}
}

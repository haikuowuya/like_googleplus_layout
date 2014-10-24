package com.roboo.like.google.async;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.models.BusItem;

public class BusAsyncTaskLoader extends
	BaseAsyncTaskLoader<LinkedList<BusItem>>
{
	private static final String TAG_INPUT = "input";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_VALUE = "value";
	public static final String BASE_URL = "http://www.szjt.gov.cn/apts/";
	public static final String BASE_BUS_LINE_URL = "http://www.szjt.gov.cn/apts/APTSLine.aspx";
	// public static final String URL =
	// "http://www.szjt.gov.cn/apts/APTSLine.aspx?__VIEWSTATE=%2FwEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLSbkOWJhbw7r9tBdPn33bPCSlJcKXww5ounfGoyhKl3&__EVENTVALIDATION=%2FwEWAwLeub7XBwL88Oh8AqX89aoK1GKT3VlKUTd%2FxyQgZexCetMuo%2Fi%2FLRDnisAyha1YxN0%3D&ctl00%24MainContent%24SearchLine=%E6%90%9C%E7%B4%A2&ctl00%24MainContent%24LineName=";//
	// 18
	private Context mContext;
	public String mBusNo;

	public BusAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}

	public BusAsyncTaskLoader(Context context, String busNo)
	{
		super(context);
		this.mBusNo = busNo;
		this.mContext = context;
	}

	public LinkedList<BusItem> loadInBackground()
	{
		LinkedList<BusItem> data = getBus();
		mEndTime = System.currentTimeMillis();
		if (mEndTime - mStartTime < THREAD_LEAST_DURATION_TIME)
		{
			try
			{
				Thread.sleep(THREAD_LEAST_DURATION_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	/***
	 * 生成查询公交的url
	 * @return
	 */
	private String genBusLineUrl()
	{
		StringBuffer stringBuffer = new StringBuffer(BASE_BUS_LINE_URL + "?");
		try
		{
			Document document = Jsoup.connect(BASE_BUS_LINE_URL).get();
			// System.out.println("document = " + document);
			Elements elements = document.getElementsByTag(TAG_INPUT);
			if (!elements.isEmpty() && elements.size() == 5)
			{
				for (int i = 0; i < elements.size(); i++)
				{
					Element element = elements.get(i);
					String name = element.attr(ATTR_NAME);
					String value = element.attr(ATTR_VALUE);
					if (TextUtils.isEmpty(value))
					{
						value = mBusNo;
					}
					stringBuffer.append(URLEncoder.encode(name, HTTP.UTF_8));
					stringBuffer.append("=");
					stringBuffer.append(URLEncoder.encode(value, HTTP.UTF_8));
					stringBuffer.append("&");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			// System.out.println("busLineUrl = " + stringBuffer.toString() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	private LinkedList<BusItem> getBus()
	{
		LinkedList<BusItem> data = null;
		try
		{
			// Document document = Jsoup.connect(URL + mBusNo).get();
			String busLineUrl = genBusLineUrl();
			if (BASE_BUS_LINE_URL.equals(busLineUrl))
			{
				System.out.println("生成要查询公交路线URL 失败");
				return data;
			}
			Document document = Jsoup.connect(busLineUrl).get();
			Element element = document.getElementById("MainContent_DATA");
			if (!element.getElementsByTag("tbody").isEmpty())
			{
				element = element.getElementsByTag("tbody").get(0);
				Elements elements = element.getElementsByTag("tr");
				Elements tmpEleemnts = null;
				String busUrl = null, busNo = null, busName = null;
				if (!elements.isEmpty())
				{
					data = new LinkedList<BusItem>();
					for (Element e : elements)
					{
						BusItem item = new BusItem();
						tmpEleemnts = e.getElementsByTag("td");
						if (!tmpEleemnts.isEmpty() && tmpEleemnts.size() == 2)
						{
							Elements aElemens = tmpEleemnts.get(0)
								.getElementsByTag("a");
							if (!aElemens.isEmpty())
							{
								busUrl = BASE_URL
									+ aElemens.get(0).attr("href");
								busNo = aElemens.get(0).text();
							}
							busName = tmpEleemnts.get(1).text();
							if (!TextUtils.isEmpty(busName))
							{
								if (busName.contains("=>"))
								{
									busName = busName.replace("=>", " — ");
								}
							}
							item.busName = busName;
							item.busNo = busNo;
							item.busUrl = busUrl;
							if (!TextUtils.isEmpty(item.busNo)
								&& !TextUtils.isEmpty(item.busName))
							{
								if (GoogleApplication.mIsExactBus)
								{
									if (mBusNo.equals(item.busNo))
									{
										data.add(item);
									}
								}
								else
								{
									data.add(item);
								}
							}
						}
					}
					if (data.size() == 0)
					{
						data = null;
					}
				}
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		if (data != null)
		{
			mDebug = false;
			if (mDebug)
			{
				for (BusItem item : data)
				{
					System.out.println("item = " + item);
				}
			}
			Collections.sort(data, new BusNoComparator());
		}
		return data;
	}

	class BusNoComparator implements Comparator<BusItem>
	{
		public int compare(BusItem o1, BusItem o2)
		{
			if (o1 != null && o2 != null)
			{
				if (TextUtils.isDigitsOnly(o1.busNo)
					&& TextUtils.isDigitsOnly(o2.busNo))
				{
					int busNo1 = Integer.parseInt(o1.busNo);
					int busNo2 = Integer.parseInt(o2.busNo);
					return busNo1 - busNo2;
				}
			}
			return 0;
		}

	}
}
package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import com.roboo.like.google.models.BusLineItem;

public class BusLineAsyncTaskLoader extends
	BaseAsyncTaskLoader<LinkedList<BusLineItem>>
{
	public static final String BASE_URL = "http://www.szjt.gov.cn/apts/";
	public static final String URL = "http://www.szjt.gov.cn/apts/APTSLine.aspx?__VIEWSTATE=%2FwEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLSbkOWJhbw7r9tBdPn33bPCSlJcKXww5ounfGoyhKl3&__EVENTVALIDATION=%2FwEWAwLeub7XBwL88Oh8AqX89aoK1GKT3VlKUTd%2FxyQgZexCetMuo%2Fi%2FLRDnisAyha1YxN0%3D&ctl00%24MainContent%24LineName=18&ctl00%24MainContent%24SearchLine=%E6%90%9C%E7%B4%A2";// 18
	private Context mContext;
	private String mBusLineUrl;

	public BusLineAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}

	public BusLineAsyncTaskLoader(Context context, String busLineUrl)
	{
		super(context);
		this.mBusLineUrl = busLineUrl;
		this.mContext = context;
	}

	public LinkedList<BusLineItem> loadInBackground()
	{
		LinkedList<BusLineItem> data = getBusLine();
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

	private LinkedList<BusLineItem> getBusLine()
	{
		LinkedList<BusLineItem> data = null;
		if (!TextUtils.isEmpty(mBusLineUrl))
		{
			try
			{
				Document document = Jsoup.connect(mBusLineUrl).get();
				Element element = document.getElementById("MainContent_DATA");
				if (element.getElementsByTag("tbody") != null
					&& !element.getElementsByTag("tbody").isEmpty())
				{
					element = element.getElementsByTag("tbody").get(0);
					Elements elements = element.getElementsByTag("tr");
					Elements tmpEleemnts = null;
					String stationUrl = null, stationName = null, stationMark = null, incomingBusNo = null, incomingBusTime = null;
					if (!elements.isEmpty())
					{
						data = new LinkedList<BusLineItem>();
						for (Element e : elements)
						{
							BusLineItem item = new BusLineItem();
							tmpEleemnts = e.getElementsByTag("td");
							if (!tmpEleemnts.isEmpty()
								&& tmpEleemnts.size() > 2)// size = 4
							{
								Elements aElemens = tmpEleemnts.get(0)
									.getElementsByTag("a");
								if (!aElemens.isEmpty())
								{
									stationUrl = BASE_URL
										+ aElemens.get(0).attr("href");
									stationName = aElemens.get(0).text();
								}
								stationMark = tmpEleemnts.get(1).text();
								incomingBusNo = tmpEleemnts.get(2).text();
								incomingBusTime = tmpEleemnts.get(3).text();
								if (!TextUtils.isEmpty(stationName))
								{
									if (stationName.contains("（"))
									{
										stationName = stationName.replace("（",
											"[");
									}
									if (stationName.contains("）"))
									{
										stationName = stationName.replace(")",
											"]");
									}
								}
								item.stationName = stationName;
								item.incomingBusNo = incomingBusNo;
								item.incomingBusTime = incomingBusTime;
								item.stationMark = stationMark;
								item.stationUrl = stationUrl;
								data.add(item);
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
					for (BusLineItem item : data)
					{
						System.out.println("item = " + item);
					}
				}
			}
		}
		return data;
	}

	private String getBusStation(String busNo1)
	{
		String busStopSpacing = null, busNo = null;
		if (!TextUtils.isEmpty(mBusLineUrl))
		{
			try
			{
				Document document = Jsoup.connect(mBusLineUrl).get();
				Element element = document.getElementById("MainContent_DATA");
				element = element.getElementsByTag("tbody").get(0);
				Elements elements = element.getElementsByTag("tr");
				Elements tmpEleemnts = null;

				if (!elements.isEmpty())
				{
					for (Element e : elements)
					{
						tmpEleemnts = e.getElementsByTag("td");
						if (!tmpEleemnts.isEmpty() && tmpEleemnts.size() == 5)// size = 5
						{
							Elements aElemens = tmpEleemnts.get(0)
								.getElementsByTag("a");
							if (!aElemens.isEmpty())
							{
								busNo = aElemens.get(0).text();
							}
							if (busNo1.equals(busNo))
							{
								busStopSpacing = tmpEleemnts.get(4).text();
								if ("进站".equals(busStopSpacing))
								{
									busStopSpacing = "0";
								}
								break;
							}
						}
					}
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return busStopSpacing;
	}

}
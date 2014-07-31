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
import com.roboo.like.google.models.BusStationItem;

public class BusStationAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<BusStationItem>>
{
	public static final String BASE_URL = "http://www.szjt.gov.cn/apts/";
	public static final String URL = "http://www.szjt.gov.cn/apts/APTSLine.aspx?__VIEWSTATE=%2FwEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLSbkOWJhbw7r9tBdPn33bPCSlJcKXww5ounfGoyhKl3&__EVENTVALIDATION=%2FwEWAwLeub7XBwL88Oh8AqX89aoK1GKT3VlKUTd%2FxyQgZexCetMuo%2Fi%2FLRDnisAyha1YxN0%3D&ctl00%24MainContent%24LineName=18&ctl00%24MainContent%24SearchLine=%E6%90%9C%E7%B4%A2";// 18
	private Context mContext;
	private String mBusLineUrl;

	public BusStationAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}

	public BusStationAsyncTaskLoader(Context context, String busLineUrl)
	{
		super(context);
		this.mBusLineUrl = busLineUrl;
		this.mContext = context;
	}

	public LinkedList<BusStationItem> loadInBackground()
	{
		LinkedList<BusStationItem> data = getBusStation();
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

	private LinkedList<BusStationItem> getBusStation()
	{
		LinkedList<BusStationItem> data = null;
		if (!TextUtils.isEmpty(mBusLineUrl))
		{
			try
			{
				Document document = Jsoup.connect(mBusLineUrl).get();
				Element element = document.getElementById("MainContent_DATA");
				element = element.getElementsByTag("tbody").get(0);
				Elements elements = element.getElementsByTag("tr");
				Elements tmpEleemnts = null;

				String stationUrl = null, busNo = null, busTo = null, busLicensePlate = null, busUpdateTime = null, busStopSpacing = null;
				if (!elements.isEmpty())
				{
					data = new LinkedList<BusStationItem>();
					for (Element e : elements)
					{
						BusStationItem item = new BusStationItem();
						tmpEleemnts = e.getElementsByTag("td");
						if (!tmpEleemnts.isEmpty() && tmpEleemnts.size() == 5)// size = 5
						{
							Elements aElemens = tmpEleemnts.get(0).getElementsByTag("a");
							if (!aElemens.isEmpty())
							{
								stationUrl = BASE_URL + aElemens.get(0).attr("href");
								busNo = aElemens.get(0).text();
							}
							busTo = tmpEleemnts.get(1).text();
							busLicensePlate = tmpEleemnts.get(2).text();
							busUpdateTime = tmpEleemnts.get(3).text();
							busStopSpacing = tmpEleemnts.get(4).text();
							item.busNo = busNo;
							item.busTo = busTo;
							item.stationUrl = stationUrl;
							item.busLicensePlate = busLicensePlate;
							item.busStopSpacing = busStopSpacing;
							item.busUpdateTime = busUpdateTime;
							data.add(item);
						}
					}
					if(data.size() ==0)
					{
						data = null;
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
					for (BusStationItem item : data)
					{
						System.out.println("item = " + item);
					}
				}
			}
		}
		return data;
	}

}
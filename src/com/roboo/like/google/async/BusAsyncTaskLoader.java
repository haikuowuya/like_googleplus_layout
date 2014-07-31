package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

import com.droidux.trial.da;
import com.roboo.like.google.models.BusItem;

public class BusAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<BusItem>>
{
	public static final String BASE_URL="http://www.szjt.gov.cn/apts/";
	public static final String URL = "http://www.szjt.gov.cn/apts/APTSLine.aspx?__VIEWSTATE=%2FwEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLSbkOWJhbw7r9tBdPn33bPCSlJcKXww5ounfGoyhKl3&__EVENTVALIDATION=%2FwEWAwLeub7XBwL88Oh8AqX89aoK1GKT3VlKUTd%2FxyQgZexCetMuo%2Fi%2FLRDnisAyha1YxN0%3D&ctl00%24MainContent%24SearchLine=%E6%90%9C%E7%B4%A2&ctl00%24MainContent%24LineName=";// 18
	private Context mContext;
	public   String mBusNo;

	public BusAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}
	public BusAsyncTaskLoader(Context context,String busNo)
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

	private LinkedList<BusItem> getBus()
	{
		LinkedList<BusItem> data = null;
		try
		{
			Document document = Jsoup.connect(URL+mBusNo).get();
			Element element = document.getElementById("MainContent_DATA");
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
						Elements aElemens = tmpEleemnts.get(0).getElementsByTag("a");
						if (!aElemens.isEmpty())
						{
							busUrl = BASE_URL+aElemens.get(0).attr("href");
							busNo = aElemens.get(0).text();
						}
						busName = tmpEleemnts.get(1).text();
						item.busName = busName;
						item.busNo = busNo;
						item.busUrl = busUrl;
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
				for (BusItem item : data)
				{
					System.out.println("item = " + item);
				}
			}
		}
		return data;
	}

}
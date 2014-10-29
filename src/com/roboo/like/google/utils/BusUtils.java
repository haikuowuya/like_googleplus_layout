package com.roboo.like.google.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.roboo.like.google.models.BusStationItem;

public class BusUtils
{
	public static final String BASE_URL = "http://www.szjt.gov.cn/apts/";
	protected static boolean mDebug = true;

	/**
	 * 获取当前站台公交信息
	 * @param busStationUrl：当前站台公交查询URL
	 * @return null or LinkedList<BusStationItem> 
	 */
	public static LinkedList<BusStationItem> getBusStation(String busStationUrl)
	{
		LinkedList<BusStationItem> data = null;
		if (!TextUtils.isEmpty(busStationUrl))
		{
			try
			{
				Document document = Jsoup.connect(busStationUrl).get();
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
							Elements aElemens = tmpEleemnts.get(0)
								.getElementsByTag("a");
							if (!aElemens.isEmpty())
							{
								busNo = aElemens.get(0).text();
								stationUrl = BASE_URL
									+ aElemens.get(0).attr("href");
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
							if ("进站".equals(item.busStopSpacing))
							{
								data.addFirst(item);
							}
							else if (TextUtils
								.isDigitsOnly(item.busStopSpacing))
							{
								data.add(data.size(), item);
							}
							else
							{
								data.addLast(item);
							}
						}
					}
					if (data.size() == 0)
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
				Collections.sort(data, new BusStopSpacingComparator());
			}
		}
		return data;
	}

	/**
	 * 对进入当前站台的车辆按照站距进行排序
	 * 
	 * @author bo.li 2014-8-1 上午8:54:50 TODO
	 */
	static class BusStopSpacingComparator implements Comparator<BusStationItem>
	{
		public int compare(BusStationItem o1, BusStationItem o2)
		{
			if (o1 != null && o2 != null)
			{
				if (TextUtils.isDigitsOnly(o2.busStopSpacing))
				{
					if (TextUtils.isDigitsOnly(o1.busStopSpacing))
					{
						int o1Int = Integer.parseInt(o1.busStopSpacing);
						int o2Int = Integer.parseInt(o2.busStopSpacing);
						if (o1Int == o2Int)
						{
							if (TextUtils.isDigitsOnly(o1.busNo)
								&& TextUtils.isDigitsOnly(o1.busNo))
							{
								int busNo1 = Integer.parseInt(o1.busNo);
								int busNo2 = Integer.parseInt(o2.busNo);
								if (busNo2 > busNo1)
								{
									return -1;
								}
							}
						}
						else
						{
							if (o2Int > o1Int)
							{
								return -1;
							}
						}
						return 1;
					}
					else if ("进站".equals(o2.busStopSpacing))
					{
						return 1;
					}
					else if ("无车".equals(o2.busStopSpacing))
					{
						return -1;
					}
				}
				else
				{
					if ("进站".equals(o2.busStopSpacing))
					{
						return 1;
					}
					else
					{
						if ("进站".equals(o1.busStopSpacing))
						{
							return 1;
						}
						else if ("无车".equals(o1.busStopSpacing))
						{
							return -1;
						}
					}
					if ("无车".equals(o2.busStopSpacing))
					{
						return -1;
					}
				}
			}
			return 0;
		}
	}
}
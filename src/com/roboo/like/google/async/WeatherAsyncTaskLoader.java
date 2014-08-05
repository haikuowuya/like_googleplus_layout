package com.roboo.like.google.async;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

import com.roboo.like.google.models.CityItem;

public class WeatherAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<CityItem>>
{
	private static final String BASW_WEATHER_URL = "http://m.weathercn.com/";
	private static final String PROVINCE_WEATHER_URL = "http://m.weathercn.com/province.jsp";
	private static final String REGEX = "dis.do?pid=";
	private Context mContext;
	private CityItem mCityItem;

	public WeatherAsyncTaskLoader(Context context)
	{
		this(context, null);
	}

	public WeatherAsyncTaskLoader(Context context, CityItem mCityItem)
	{
		super(context);
		this.mContext = context;
		this.mCityItem = mCityItem;
	}

	public LinkedList<CityItem> loadInBackground()
	{
		LinkedList<CityItem> data = null;
		if(null == mCityItem)
		{
		  data = getProvinces();
		}
		else
		{
			data = getCities();
		}
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

	private LinkedList<CityItem> getCities()
	{
		LinkedList<CityItem> items = null;
		try
		{
			System.out.println("mCityItem.pUrl "+ mCityItem.pUrl);
			Document document = Jsoup.connect(mCityItem.pUrl).get();
			Elements elements = document.getElementsByTag("a");
			if (!elements.isEmpty())
			{
				items = new LinkedList<CityItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					Element element = elements.get(i);
					String href = element.attr("href");
					if (null != href && href.startsWith(REGEX))
					{
						CityItem item = new CityItem();
						String cUrl = BASW_WEATHER_URL + href;
						String cName = element.text();
						item.pName = mCityItem.pName;
						item.pUrl = mCityItem.pUrl;
						item.cName = cName;
						item.cUrl = cUrl;
						items.add(item);
					}
				}
				if (items.size() == 0)
				{
					items = null;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return items;
	}
	 
	
	public LinkedList<CityItem> getProvinces()
	{
		LinkedList<CityItem> items = null;
		try
		{
			Document document = Jsoup.connect(PROVINCE_WEATHER_URL).get();
			Elements elements = document.getElementsByTag("a");
			if (!elements.isEmpty())
			{
				items = new LinkedList<CityItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					Element element = elements.get(i);
					String href = element.attr("href");
					if (null != href && href.startsWith(REGEX))
					{
						CityItem item = new CityItem();
						String pUrl = BASW_WEATHER_URL + href;
						String pName = element.text();
						item.pName = pName;
						item.pUrl = pUrl;
						items.add(item);
					}
				}
				if (items.size() == 0)
				{
					items = null;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return items;
	}
}
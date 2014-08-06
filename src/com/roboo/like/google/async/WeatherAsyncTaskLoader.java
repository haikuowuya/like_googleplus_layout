package com.roboo.like.google.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import com.roboo.like.google.models.CityItem;
import com.roboo.like.google.models.WeatherItem;

public class WeatherAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<WeatherItem>>
{
	private static final String BASW_WEATHER_URL = "http://m.weathercn.com/";
	private static final String REGEX_TIAN_QI_FU_HAO = "tianqifuhao30hui";
	private Context mContext;
	private String mWeatherUrl;

	public WeatherAsyncTaskLoader(Context context, String weatherUrl)
	{
		super(context);
		this.mContext = context;
		this.mWeatherUrl = weatherUrl;
	}

	public LinkedList<WeatherItem> loadInBackground()
	{
		LinkedList<WeatherItem> data = null;
		// File file = new File(FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_WEATHER), MD5Utils.generate(BASW_WEATHER_URL));
		// if (!file.exists())
		// {
		// data = getProCities();
		// if (null != data)
		// {
		// saveProvCities(data, file);
		// }
		// }
		// else
		// {
		// data = getFileProvCities(file);
		// }
		data = getWeatherItems();
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

	private LinkedList<LinkedList<CityItem>> getFileProvCities(File file)
	{
		LinkedList<LinkedList<CityItem>> data = null;
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
			data = (LinkedList<LinkedList<CityItem>>) objectInputStream.readObject();
			objectInputStream.close();
		}
		catch (Exception e)
		{
			file.delete();
			e.printStackTrace();
		}

		return data;
	}

	private void saveProvCities(LinkedList<LinkedList<CityItem>> data, File file)
	{
		try
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(data);
			objectOutputStream.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public LinkedList<WeatherItem> getWeatherItems()
	{

		LinkedList<WeatherItem> items = null;
		String updateTime = null, todayDesc = null, todayIconUrl = null, todayWind = null, todayTemp = null;
		if (!TextUtils.isEmpty(mWeatherUrl))
		{
			try
			{
				Document document = Jsoup.connect(mWeatherUrl).get();
				Element element = document.getElementById("content");
				if (element != null)
				{

					Elements elements = element.getElementsByClass("skreporttime");
					if (!elements.isEmpty())
					{
						updateTime = elements.text();
					}
					todayDesc = element.getElementsByClass("skweather").text();
					todayIconUrl = BASW_WEATHER_URL + element.getElementById("weatherPic").attr("src");
					todayTemp = element.getElementsByClass("sktemperature").text();
					todayWind = element.getElementsByClass("b").get(0).text();
					elements = element.getElementsByClass("list");
					if (!elements.isEmpty())
					{
						elements = elements.get(0).getElementsByTag("div");
					}
					if (!elements.isEmpty())
					{
						items = new LinkedList<WeatherItem>();
						for (int i = 1; i < elements.size(); i++)
						{
							WeatherItem item = new WeatherItem();
							item.updateTime = updateTime;
							item.todayDesc = todayDesc;
							item.todayIconUrl = todayIconUrl;
							item.todayTemp = todayTemp;
							item.todayWind = todayWind;
							Elements tmpElements = elements.get(i).getElementsByTag("span");
							if (!tmpElements.isEmpty() && tmpElements.size() > 3)
							{
								item.day = tmpElements.get(0).text();
								Elements imgElements = tmpElements.get(1).getElementsByTag("img");
								if (!imgElements.isEmpty())
								{
									String dayIconUrl = imgElements.get(0).attr("src");
									if (!TextUtils.isEmpty(dayIconUrl) && dayIconUrl.startsWith(REGEX_TIAN_QI_FU_HAO))
									{
										item.dayIconUrl = BASW_WEATHER_URL + dayIconUrl.replace(REGEX_TIAN_QI_FU_HAO, "2013icon");
									}
								}
								imgElements = tmpElements.get(2).getElementsByTag("img");
								if (!imgElements.isEmpty())
								{
									String nightIconUrl = imgElements.get(0).attr("src");
									if (!TextUtils.isEmpty(nightIconUrl) && nightIconUrl.startsWith(REGEX_TIAN_QI_FU_HAO))
									{
										item.nightIconUrl = BASW_WEATHER_URL + nightIconUrl.replace(REGEX_TIAN_QI_FU_HAO, "2013icon");
									}
								}
								item.temp = tmpElements.get(3).text();
							}
							items.add(item);
						}
						if (items.size() == 0)
						{
							items = null;
						}
						if (null != items)
						{
							for (WeatherItem item : items)
							{
								// System.out.println("item = " + item);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return items;
	}
}
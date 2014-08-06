package com.roboo.like.google.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import com.droidux.trial.da;
import com.roboo.like.google.models.CityItem;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.FileUtils;
import com.roboo.like.google.utils.MD5Utils;

public class CityAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<LinkedList<CityItem>>>
{
	/** 中国的4个直辖市 */
	private static final String MUNICIPALITY = "北京上海重庆天津";
	private static final String BASW_WEATHER_URL = "http://m.weathercn.com/";
	private static final String PROVINCE_WEATHER_URL = "http://m.weathercn.com/province.jsp";
	private static final String REGEX_PROVINCE = "dis.do?pid=";
	private static final String REGEX_CITY = "cout.do?did=";
	private Context mContext;
	private CityItem mCityItem;

	public CityAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
	}

	public LinkedList<LinkedList<CityItem>> loadInBackground()
	{
		LinkedList<LinkedList<CityItem>> data = null;
		File file = new File(FileUtils.getFileCacheDir(mContext, FileUtils.TYPE_WEATHER), MD5Utils.generate(BASW_WEATHER_URL));
		if (!file.exists())
		{
			data = getProCities();
			if (null != data)
			{
				saveProvCities(data, file);
			}
		}
		else
		{
			data = getFileProvCities(file);
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

	private LinkedList<LinkedList<CityItem>> getFileProvCities(File file)
	{
		LinkedList<LinkedList<CityItem>> data = null;
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
			data = (LinkedList<LinkedList<CityItem>>) objectInputStream.readObject();
			objectInputStream.close();
		}
		catch ( Exception e)
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

	/**
	 * 一次性获取省份[直辖市]以及其下面的城市列表信息
	 * 
	 * @return null 或者 LinkedList<LinkedList<CityItem>>
	 */
	private LinkedList<LinkedList<CityItem>> getProCities()
	{
		LinkedList<LinkedList<CityItem>> data = null;
		try
		{
			Document document = Jsoup.connect(PROVINCE_WEATHER_URL).get();
			Elements elements = document.getElementsByTag("a");
			if (!elements.isEmpty())
			{
				data = new LinkedList<LinkedList<CityItem>>();
				for (int i = 0; i < elements.size(); i++)
				{
					Element element = elements.get(i);
					String href = element.attr("href");
					if (null != href && href.startsWith(REGEX_PROVINCE))
					{
						CityItem item = new CityItem();
						String pUrl = BASW_WEATHER_URL + href;
						String pName = element.text();
						if (MUNICIPALITY.contains(pName))
						{
							pName = "直辖市";
						}
						item.pName = pName;
						item.pUrl = pUrl;
						data.add(getCities(item));
					}
				}
				if (data.size() == 0)
				{
					data = null;
				}
				for (LinkedList<CityItem> tmp : data)
				{
					for (CityItem cityItem : tmp)
					{
						System.out.println("cityItem = " + cityItem);
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取省份[直辖市]下面的城市列表信息
	 * 
	 * @param cityItem
	 * @return null 或者 LinkedList<CityItem>
	 */
	private LinkedList<CityItem> getCities(CityItem cityItem)
	{
		LinkedList<CityItem> items = null;
		if (null != cityItem && !TextUtils.isEmpty(cityItem.pUrl))
		{
			try
			{
				Document document = Jsoup.connect(cityItem.pUrl).timeout(4000).get();
				Elements elements = document.getElementsByTag("a");
				if (!elements.isEmpty())
				{
					items = new LinkedList<CityItem>();
					for (int i = 0; i < elements.size(); i++)
					{
						Element element = elements.get(i);
						String href = element.attr("href");
						if (null != href && href.startsWith(REGEX_CITY))
						{
							href = href.replace("cout", "index");
							href = href.replace("did", "cid");
							CityItem item = new CityItem();
							String cUrl = BASW_WEATHER_URL + href;
							String cName = element.text();
							item.pName = cityItem.pName;
							item.pUrl = cityItem.pUrl;
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
		}
		return items;
	}

	/**
	 * 获取省份[直辖市]下面的城市列表信息
	 * 
	 * @param cityItem
	 * @return null 或者 LinkedList<CityItem>
	 */
	public LinkedList<CityItem> getCities()
	{
		return getCities(mCityItem);
	}

	public LinkedList<CityItem> getProvinces()
	{
		LinkedList<LinkedList<CityItem>> data = null;
		LinkedList<CityItem> items = null;
		try
		{
			Document document = Jsoup.connect(PROVINCE_WEATHER_URL).get();
			Elements elements = document.getElementsByTag("a");
			if (!elements.isEmpty())
			{
				items = new LinkedList<CityItem>();
				data = new LinkedList<LinkedList<CityItem>>();
				for (int i = 0; i < elements.size(); i++)
				{
					Element element = elements.get(i);
					String href = element.attr("href");
					if (null != href && href.startsWith(REGEX_PROVINCE))
					{
						CityItem item = new CityItem();
						String pUrl = BASW_WEATHER_URL + href;
						String pName = element.text();
						item.pName = pName;
						item.pUrl = pUrl;
						data.add(getCities(item));
						items.add(item);
					}
				}
				if (items.size() == 0)
				{
					items = null;
				}
				for (LinkedList<CityItem> tmp : data)
				{
					for (CityItem cityItem : tmp)
					{
						System.out.println("cityItem = " + cityItem);
					}
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
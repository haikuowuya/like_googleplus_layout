package com.roboo.like.google.models;

public class CityItem extends BaseItem
{
	public String pUrl;
	public String pName;
	public String cUrl;
	public String cName;
	public int headerId;

	@Override
	public boolean equals(Object obj)
	{
		if (null != obj && obj instanceof CityItem)
		{
			CityItem item = (CityItem) obj;
			return pName.equals(item.pName) && pUrl.equals(item.pUrl);
		}
		return false;
	}
	@Override
	public String toString()
	{
		return "省份[直辖市]名称 = "+ pName + " \t城市名称 = "+ cName
			+" \t省份[直辖市]URL = "+ pUrl + " \t城市天气URL = " + cUrl;
	}
}

package com.roboo.like.google.models;

public class CityItem extends BaseItem
{
	public String pUrl;
	public String pName;
	public String cUrl;
	public String cName;

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
}

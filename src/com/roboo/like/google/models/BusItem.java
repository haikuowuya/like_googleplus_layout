package com.roboo.like.google.models;

public class BusItem
{
	/**公交线路对应的url*/
	public String busUrl;
	public String busNo;
	public String busName;
	@Override
	public String toString()
	{
		 return "公交号 = "+ busNo + "线路名称  = "+ busName;
	}
}

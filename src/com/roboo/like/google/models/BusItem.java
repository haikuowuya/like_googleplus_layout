package com.roboo.like.google.models;

public class BusItem
{
	/**公交线路对应的url*/
	public String busUrl;
	/**公交车编号*/
	public String busNo;
	/**公交线路名称*/
	public String busName;
	@Override
	public String toString()
	{
		 return "公交号 = "+ busNo + " 线路名称  = "+ busName +" 公交线路URL = " + busUrl;
	}
}

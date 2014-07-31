package com.roboo.like.google.models;

@SuppressWarnings("serial")
public class BusLineItem extends BaseItem
{
	/**站台对应的URL*/
	public String stationUrl;
	/**站台名称*/
	public String stationName;
	/**站台编号*/
	public String stationMark;
	/**如果当前站台有公交车，公交车车牌号*/
	public String incomingBusNo;
	/**如果当前站台有公交车，进站时间*/
	public String incomingBusTime;
	@Override
	public String toString()
	{
	  return "站台名称 = "+ stationName + " 站台编号 = "+stationMark 
		  +" 到站公交车车牌号 = "+ incomingBusNo +" 到此站台时间 = "+ incomingBusTime
		  +"站台URL = " + stationUrl;
	}
}

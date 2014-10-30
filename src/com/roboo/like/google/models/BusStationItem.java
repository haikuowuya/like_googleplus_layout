package com.roboo.like.google.models;

/**
 * 公交站点信息， 站距、经过车辆……
 * 
 * @author bo.li 2014-7-31 下午2:25:42 TODO
 */
public class BusStationItem
{
	public String busNo;
	public String stationUrl;
	public String busTo;
	/** 公交车牌 */
	public String busLicensePlate;
	/** 更新时间 */
	public String busUpdateTime;
	/** 站距 */
	public String busStopSpacing;
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof BusStationItem)
		{
			BusStationItem 	item = (BusStationItem)  obj;
			return item.busNo.equals(busNo);
		}
		 return false;
	}
	 
}

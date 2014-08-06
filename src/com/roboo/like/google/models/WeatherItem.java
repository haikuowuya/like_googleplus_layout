package com.roboo.like.google.models;

import java.util.LinkedList;

import com.droidux.trial.da;

public class WeatherItem extends BaseItem
{

	public String day;
	public String dayIconUrl;
	public String nightIconUrl;
	public String temp;
	public String wind;
	public String updateTime;
	public String todayDesc;
	public String todayWind;
	public String todayTemp;
	public String todayIconUrl;

	@Override
	public String toString()
	{
		return "更新时间 = " + updateTime + " 今天天气 = " + todayDesc + " 今天温度 = " + todayTemp + " 今天风向 = " + todayWind + " day = " + day + " temp = " + temp + " wind = " + wind + " dayIconUrl = " + dayIconUrl + " nightIconUrl = " + nightIconUrl;
	}

}

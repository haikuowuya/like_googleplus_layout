package com.roboo.like.google.models;

import android.graphics.Bitmap;

public class SmsItem
{
	public String phoneNumber;
	public String body;
	public String name;
	public String date;
	public String type;
	public Bitmap bitmap;
	@Override
	public String toString()
	{
		return "手机号码 = "+ phoneNumber + " 类型  = "+ type +  " 日期 = "+ date
			+" 姓名  = "+name + " 内容  = " + body;
	}
}

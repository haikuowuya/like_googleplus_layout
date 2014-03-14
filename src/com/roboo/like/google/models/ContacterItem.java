package com.roboo.like.google.models;

import android.graphics.Bitmap;

public class ContacterItem implements Comparable<ContacterItem>
{
	public String name;
	public String icon;
	public String phone;
	public Bitmap bitmap;

	public ContacterItem()
	{
		super();
	}

	public ContacterItem(String name, String phone)
	{
		super();
		this.name = name;
		this.phone = phone;
	}

	@Override
	public String toString()
	{
		return "联系人姓名 = " + name + "  联系人号码  = " + phone +" 联系人头像 = " + icon;
	}

	public int compareTo(ContacterItem another)
	{
		return this.name.compareTo(another.name);
	}
}

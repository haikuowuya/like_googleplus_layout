package com.roboo.like.google.models;

public class ContacterItem implements Comparable<ContacterItem>
{
	public String name;
	public String phone;

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
		return "联系人姓名 = " + name + "  联系人号码  = " + phone;
	}

	public int compareTo(ContacterItem another)
	{
		return this.name.compareTo(another.name);
	}
}

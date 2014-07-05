package com.roboo.like.google.models;

public class StartNewsTypeItem
{
	public String name;
	public int typeInt ;
	public String src;

	public StartNewsTypeItem()
	{}

	public StartNewsTypeItem(String name, int typeInt)
	{
		this.name = name;
		this.typeInt = typeInt;
	}
	public StartNewsTypeItem(String name, int typeInt,String src)
	{
		this.name = name;
		this.src = src;
		this.typeInt = typeInt;
	}

}

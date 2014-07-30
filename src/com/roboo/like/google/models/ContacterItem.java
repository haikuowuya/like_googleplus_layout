package com.roboo.like.google.models;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import android.graphics.Bitmap;

import com.roboo.like.google.utils.PinYinUtils;

public class ContacterItem implements Comparable<ContacterItem>
{
	public String name;
	/**联系人头像的uri*/
	public String icon;
	public String phone;
	public Bitmap bitmap;
	public long headerId;

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
		return "联系人姓名 = " + name + "  联系人号码  = " + phone + " 联系人头像 = " + icon;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj != null)
		{
			ContacterItem item = (ContacterItem) obj;
			return item.phone.equals(phone) && item.name.equals(name);
		}
		return false;
	}

	public int compareTo(ContacterItem another)
	{
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String currentFirstLetter = PinYinUtils.getPinYinHeadChar(name, defaultFormat).substring(0, 1);
		String anotherFirstLetter = PinYinUtils.getPinYinHeadChar(another.name, defaultFormat).substring(0, 1);
		return currentFirstLetter.compareTo(anotherFirstLetter);
	}
}

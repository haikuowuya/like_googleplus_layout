package com.roboo.like.google.async;

import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.roboo.like.google.models.ContacterItem;

public class ContacterAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<ContacterItem>>
{
	private Context mContext;

	public ContacterAsyncTaskLoader(Context context)
	{
		super(context);
		mContext = context;

	}

	@Override
	public LinkedList<ContacterItem> loadInBackground()
	{

		LinkedList<ContacterItem> data = null;
		ContentResolver resolver = mContext.getContentResolver();
		String[] strings = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };

		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, strings, null, null, null);// 获取手机联系人
		if (phoneCursor != null && phoneCursor.getCount() > 0)
		{
			data = new LinkedList<ContacterItem>();
			while (phoneCursor.moveToNext())
			{
				ContacterItem item = new ContacterItem();
				String contactName = phoneCursor.getString(0); // 得到联系人名称
				String phoneNumber = phoneCursor.getString(1);// 得到手机号码
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
				{
					continue;
				}
				item.name = contactName;
				item.phone = phoneNumber;
				data.add(item);
			}
			phoneCursor.close();
		}
		return data;
	}

}

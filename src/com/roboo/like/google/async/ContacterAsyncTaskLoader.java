package com.roboo.like.google.async;

import java.io.InputStream;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

import com.roboo.like.google.R;
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
		String[] strings = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER,Photo.PHOTO_ID };

		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, strings, null, null, null);// 获取手机联系人
		if (phoneCursor != null && phoneCursor.getCount() > 0)
		{
			data = new LinkedList<ContacterItem>();
			while (phoneCursor.moveToNext())
			{
				ContacterItem item = new ContacterItem();
				String contactName = phoneCursor.getString(0); // 得到联系人名称
				String phoneNumber = phoneCursor.getString(1);// 得到手机号码
				String contactIcon = phoneCursor.getString(2);//得到联系人头像
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
				{
					continue;
				}
		
				item.name = contactName;
				item.phone = phoneNumber;
				item.icon = contactIcon;
				
				data.add(item);
			}
			phoneCursor.close();
		}
		return data;
	}

}

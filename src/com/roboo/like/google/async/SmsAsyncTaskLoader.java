package com.roboo.like.google.async;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore.Images.Media;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.roboo.like.google.models.SmsItem;

public class SmsAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<SmsItem>>
{
	private static final String CHINA_GSM="+86";
	private static String SELF_PHONENUMBER = null;
	private static final String NO_NAME = "无名氏";
	protected static final Uri SMS_ALL = Uri.parse("content://sms/");// 所有的
	protected static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");// 收件箱
	private Context mContext;

	public SmsAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;
		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		SELF_PHONENUMBER = telephonyManager.getLine1Number();
		if(!TextUtils.isEmpty(SELF_PHONENUMBER))
		{
			if(SELF_PHONENUMBER.contains(CHINA_GSM))
			{
				SELF_PHONENUMBER = SELF_PHONENUMBER.substring(CHINA_GSM.length()+SELF_PHONENUMBER.indexOf(CHINA_GSM));
			}
		}
		if(mDebug)
		{
			System.out.println("我的手机号码   = "+ SELF_PHONENUMBER);
		}
	}

	public LinkedList<SmsItem> loadInBackground()
	{
		LinkedList<SmsItem> data = getSms();
		mEndTime = System.currentTimeMillis();
		if (mEndTime - mStartTime < THREAD_LEAST_DURATION_TIME)
		{
			try
			{
				Thread.sleep(THREAD_LEAST_DURATION_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	private LinkedList<SmsItem> getSms()
	{
		LinkedList<SmsItem> items = null;
		ContentResolver resolver = mContext.getContentResolver();

		String[] infos = new String[] { "_id", "address", "person", "body", "date", "type" };
		Cursor cursor = resolver.query(SMS_ALL, infos, null, null, "date DESC");
		if (cursor != null && cursor.getCount() > 0)
		{
			items = new LinkedList<SmsItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				SmsItem item = new SmsItem();
				item.phoneNumber = cursor.getString(1);
				item.name = cursor.getString(2);
				item.body = cursor.getString(3);
				item.date = cursor.getString(4);
				item.type = cursor.getString(5);
				if ((!TextUtils.isEmpty(item.date)) && TextUtils.isDigitsOnly(item.date))
				{
					item.date = handleDate(item.date);
				}
				item.type = handleType(item.type, item);
				if (!TextUtils.isEmpty(item.name))
				{
					item.name = handleName(item.name, item);
				}
				items.add(item);
				mDebug = true;
				if (mDebug)
				{
					System.out.println("item = 【" + item + " 】");
				}
			}
		}
		return items;
	}

	/**
	 * 处理获取姓名， 并且获取对应的头像信息
	 * 
	 * @param name
	 * @param item
	 *            对象列表项
	 * @return
	 */
	private String handleName(String name, SmsItem item)
	{
		String photoUri = null;
		ContentResolver resolver = mContext.getContentResolver();
		String[] strings = new String[] { Phone.DISPLAY_NAME, Photo.PHOTO_THUMBNAIL_URI };
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, strings, "name_raw_contact_id =?", new String[] { name }, null);// 获取手机联系人
		name = NO_NAME;
		if (phoneCursor != null && phoneCursor.getCount() > 0)
		{
			phoneCursor.moveToFirst();
			name = phoneCursor.getString(0);
			if (TextUtils.isEmpty(name))
			{
				name = NO_NAME;
			}
			photoUri = phoneCursor.getString(1);
			if (!TextUtils.isEmpty(photoUri))
			{
				if (mDebug)
				{
					System.out.println("photoUri = " + photoUri);
				}
				try
				{
					Bitmap bitmap = Media.getBitmap(mContext.getContentResolver(), Uri.parse(photoUri));
					if (null != bitmap)
					{
						item.bitmap = bitmap;
					}
					
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}
		return name;
	}

	private String handleType(String type, SmsItem item)
	{
		if (Integer.parseInt(type) == 1)// 已发送
		{
			type = "已接收";
		}
		else if (Integer.parseInt(type) == 2)
		{
			type = "已发送";
			if (!TextUtils.isEmpty(SELF_PHONENUMBER))
			{
				item.phoneNumber = SELF_PHONENUMBER;
			}
		}

		return type;
	}

	/**
	 * 将时间处理成想要的格式
	 * 
	 * @param date
	 *            要处理的时间
	 * @return 处理后的时间
	 */
	private String handleDate(String date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
		return simpleDateFormat.format(new Date(Long.parseLong(date)));
	}
}
/*
 * 关于content://sms/inbox表，大致包含的域有： _id | 短消息序号 如100 thread_id | 对话的序号 如100 address | 发件人地址，手机号.如+8613811810000 person　| 发件人，返回一个数字就是联系人列表里的序号，陌生人为null date | 日期 long型。如1256539465022 protocol | 协议 0 SMS_RPOTO, 1 MMS_PROTO read | 是否阅读 0未读， 1已读 status | 状态 -1接收，0 complete, 64 pending, 128 failed type |
 * 类型 1是接收到的，2是已发出 body | 短消息内容 service_center | 短信服务中心号码编号。如+8613800755500
 */
/*
 * content://sms/ 所有短信 content://sms/inbox 收件箱 content://sms/sent 已发送 content://sms/draft 草稿 content://sms/outbox 发件箱 content://sms/failed 发送失败 content://sms/queued 待发送列表
 */
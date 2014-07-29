package com.roboo.like.google.async;

import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.roboo.like.google.models.SmsItem;

public class SmsAsyncTaskLoader extends BaseAsyncTaskLoader<LinkedList<SmsItem>>
{
	protected static boolean DEBUG = true;
	private Context mContext;

	public SmsAsyncTaskLoader(Context context)
	{
		super(context);
		this.mContext = context;

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
		Uri SMS_INBOX = Uri.parse("content://sms/inbox");//收件箱
		String[] infos = new String[]{"_id", "address", "person",      
            "body", "date", "type"}; 
		Cursor cursor = resolver.query(SMS_INBOX, infos, null, null, "date DESC");
		if (cursor != null && cursor.getCount() > 0)
		{
			items = new LinkedList<SmsItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				System.out.println("cursor = " + cursor.getString(0));
				SmsItem item = new SmsItem();
				item.address = cursor.getString(1);
				item.person = cursor.getString(2);
				item.body = cursor.getString(3);
				item.date = cursor.getString(4);
				item.type = cursor.getString(5);
				items.add(item);
			}
		}
		return items;
	}
}
/*
关于content://sms/inbox表，大致包含的域有： 
_id | 短消息序号 如100   
thread_id | 对话的序号 如100   
address | 发件人地址，手机号.如+8613811810000   
person　| 发件人，返回一个数字就是联系人列表里的序号，陌生人为null   
date | 日期  long型。如1256539465022   
protocol | 协议 0 SMS_RPOTO, 1 MMS_PROTO    
read | 是否阅读 0未读， 1已读    
status | 状态 -1接收，0 complete, 64 pending, 128 failed    
type | 类型 1是接收到的，2是已发出    
body | 短消息内容    
service_center | 短信服务中心号码编号。如+8613800755500 
*/
/*
content://sms/               所有短信
content://sms/inbox        收件箱
content://sms/sent        已发送
content://sms/draft        草稿
content://sms/outbox        发件箱
content://sms/failed        发送失败
content://sms/queued        待发送列表
*/
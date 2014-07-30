package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roboo.like.google.DidiActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.models.SmsItem;

public class SMSAdapter extends BaseAdapter
{
	private LinkedList<SmsItem> mData;
	private Activity mActivity;

	public SMSAdapter(Activity mActivity, LinkedList<SmsItem> data)
	{
		this.mData = data;
		this.mActivity = mActivity;
	}

	@Override
	public int getCount()
	{
		return null == mData ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null == mData ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SmsItem item = mData.get(position);
		if (null != item)
		{
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.sms_list_item, null);// TODO
			TextView tvAddress = ViewHolder.getView(convertView, R.id.tv_address);
			TextView tvBody = ViewHolder.getView(convertView, R.id.tv_body);
			TextView tvType = ViewHolder.getView(convertView, R.id.tv_type);
			TextView tvName = ViewHolder.getView(convertView, R.id.tv_name);
			ImageView ivImageView = ViewHolder.getView(convertView, R.id.iv_image);
			tvAddress.setText(item.phoneNumber);
			tvBody.setText(item.body);
			tvType.setText(item.type);
			tvName.setText(item.name);
			if (item.bitmap != null)
			{
				ivImageView.setImageBitmap(item.bitmap);
			}
//			handleLinkClick(tvBody);
		}
		return convertView;
	}

	protected void handleLinkClick(TextView tvBody)
	{
		tvBody.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tvBody.getText().toString();
		if (text instanceof Spannable)
		{
			int end = text.length();
			Spannable sp = (Spannable) tvBody.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder newText = new SpannableStringBuilder(text);
			newText.clearSpans();// 清除以前的UrlSpan,添加自己的URLSpan
			for (URLSpan url : urls)
			{
				MyUrlSpan myURLSpan = new MyUrlSpan(url.getURL());
				newText.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			tvBody.setText(newText);
		}
	}

	class MyUrlSpan extends URLSpan
	{

		public MyUrlSpan(String url)
		{
			super(url);
		}

		public void onClick(View widget)
		{
			DidiActivity.actionDidi(mActivity, getURL());

		}

	}

}

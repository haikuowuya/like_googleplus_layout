package com.roboo.like.google;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

public class JPushActivity extends BaseActivity
{
	public static final String EXTRA_DATA = "data";
	private TextView mTvMessage;
	private String mMessage;
	
	public static void actionJPush(Context context, String data)
	{
		Intent intent = new Intent(context, JPushActivity.class);
		intent.putExtra(EXTRA_DATA, data);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(null);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jpush);
		initView();
		mMessage = getIntent().getStringExtra(EXTRA_DATA);
		if(!TextUtils.isEmpty(mMessage))
		{
			mTvMessage.setText(mMessage);
		}
	
	}
	private void initView()
	{
		mTvMessage = (TextView) findViewById(R.id.tv_message);
	}
  
}

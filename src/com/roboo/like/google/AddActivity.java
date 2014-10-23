package com.roboo.like.google;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roboo.like.google.fragments.AddFragment;

/**添加订阅界面 */
public class AddActivity extends BaseLayoutActivity
{
	//{{
	 /**
	  *  跳转到添加界面
	  * @param activity
	  */
	public static void actionAdd(Activity activity)
	{
		Intent intent = new Intent(activity,AddActivity.class);
		activity.startActivity(intent);
	}
	//}}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		initView();
		customActionBar();
 
		if(getSupportFragmentManager().findFragmentById(R.id.frame_container) ==null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, AddFragment.newInstance()).commit();
		}
		 
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initView()
	{
		 
	}
	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("添加订阅");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}
	@Override
	protected void onResume()
	{
		super.onResume();
//		String url = "http://c.m.163.com/nc/article/headline/T1348647909107/2-20.html";
//		RequestQueue queue = Volley.newRequestQueue(this);
//		
//		JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(url, null, new Listener<JSONObject>()
//		{
//
//			@Override
//			public void onResponse(JSONObject response)
//			{
//				System.out.println("response = " + response.toString());
//			}}	, null);
//		queue.add(jsonObjectRequest);
	}
}

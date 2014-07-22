package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
}

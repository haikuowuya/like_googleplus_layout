package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.roboo.like.google.fragments.PictureGridFragment;
import com.roboo.like.google.fragments.PicturePinFragment;

/** 图片界面 */
public class PictureActivity extends BaseLayoutActivity
{
	/** 跳转到图片界面 */
	public static void actionPicture(Activity activity)
	{
		Intent intent = new Intent(activity, PictureActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		customActionBar();
		if (getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.frame_container, PictureGridFragment.newInstance()).commit();
		}
	}

	public void initView()
	{

	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("照片");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_picture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_grid_pinterest:
			BitmapDrawable drawable = (BitmapDrawable) item.getIcon();
			BitmapDrawable nextDrawable = (BitmapDrawable) (getResources().getDrawable(R.drawable.ic_menu_pinterest));
			if (drawable.getBitmap() == nextDrawable.getBitmap())
			{
				item.setIcon(R.drawable.ic_menu_grid);
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, PictureGridFragment.newInstance()).commit();
				item.setTitle("网格");
			}
			else
			{
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, PicturePinFragment.newInstance()).commit();
				item.setIcon(R.drawable.ic_menu_pinterest);
				item.setTitle("瀑布流");
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}

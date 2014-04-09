package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.roboo.like.google.async.BitmapAsyncTask;
import com.roboo.like.google.utils.BitmapUtils;
import com.roboo.like.google.views.PhotoView;

/** 图片详情界面 */
public class PictureDetailActivity extends BaseActivity
{

	private PhotoView mPhotoView;
	private String mImagePath;
	private static final String EXTRA_IMAGE_PATH = "image_path";

	/** 跳转到图片详情界面 */
	public static void actionPictureDetail(Activity activity, String imagePath)
	{
		Intent intent = new Intent(activity, PictureDetailActivity.class);
		intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_detail);
		initView();
		customActionBar();
		mImagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
		System.out.println("图片 URL = " + mImagePath);
		if (mImagePath.startsWith(PREFIX_IMG_URL))
		{
			new BitmapAsyncTask(mPhotoView, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels).execute(mImagePath);
		}
		else
		{
			mPhotoView.setImageBitmap(BitmapUtils.getBitmap(mImagePath, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels));
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
		mPhotoView = (PhotoView) findViewById(R.id.pv_image);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("照片详情");
		mActionBar.setLogo(R.drawable.ic_abs_picture_up);
	}
}

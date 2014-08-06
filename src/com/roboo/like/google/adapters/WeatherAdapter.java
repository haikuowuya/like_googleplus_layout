package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.roboo.like.google.R;
import com.roboo.like.google.models.WeatherItem;

public class WeatherAdapter extends BaseAdapter
{
	private LinkedList<WeatherItem> mData;
	private ImageLoader mImageLoader;
	private Activity mActivity;

	public WeatherAdapter(Activity mActivity, LinkedList<WeatherItem> data)
	{
		this.mData = data;
		this.mActivity = mActivity;
		mImageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(mActivity).discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		mImageLoader.init(imageLoaderConfiguration);
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
		WeatherItem item = mData.get(position);
		if (null != item)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_weather_item, null);// TODO
			}
			TextView tvDay = ViewHolder.getView(convertView, R.id.tv_day);
			TextView tvTemp = ViewHolder.getView(convertView, R.id.tv_temp);
			ImageView ivDay = ViewHolder.getView(convertView, R.id.iv_day);
			ImageView ivNight = ViewHolder.getView(convertView, R.id.iv_night);
			DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
			mImageLoader.displayImage(item.dayIconUrl, ivDay, options);
			mImageLoader.displayImage(item.nightIconUrl, ivNight, options);
			tvDay.setText(item.day);
			tvTemp.setText(item.temp);
		}
		return convertView;
	}

}

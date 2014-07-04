package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.R;
import com.roboo.like.google.models.StartNewsTypeItem;

public class StartNewsTypeListAdapter extends BaseAdapter implements StickyHeadersAdapter, SectionIndexer
{
	private LinkedList<StartNewsTypeItem> mData;
	private Activity activity;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;

	public StartNewsTypeListAdapter(LinkedList<StartNewsTypeItem> mData, Activity activity)
	{
		super();
		this.mData = mData;
		this.activity = activity;
		mInflater = LayoutInflater.from(activity);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(activity));
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
		convertView = mInflater.inflate(R.layout.list_news_type_item, null);// TODO
		TextView textView = ViewHolder.getView(convertView, R.id.tv_title);
		ImageView imageView = ViewHolder.getView(convertView, R.id.iv_image);
		textView.setText(mData.get(position).name);
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		mImageLoader.displayImage(mData.get(position).src, imageView, options);
		return convertView;
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

	@Override
	public int getPositionForSection(int section)
	{
		return 0;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return 0;
	}

	@Override
	public long getHeaderId(int position)
	{
		return 0;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
//		convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
//		TextView textView = ViewHolder.getView(convertView, R.id.tv_text);
//		textView.setText("" + (1 + position));
//		textView.setVisibility(View.GONE);
//		ViewHolder.getView(convertView, R.id.frame_container).setVisibility(View.GONE);
		convertView = new View(activity);
		return convertView;
		
	}

}

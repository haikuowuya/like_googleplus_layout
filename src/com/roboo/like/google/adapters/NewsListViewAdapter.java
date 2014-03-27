package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsItem;

public class NewsListViewAdapter extends BaseAdapter
{
	private Context context;
	private LinkedList<NewsItem> data;
	private ImageLoader mImageLoader;

	public NewsListViewAdapter(Context context, LinkedList<NewsItem> data)
	{
		super();
		this.context = context;
		this.data = data;
		this.mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	@Override
	public int getCount()
	{
		return null == data ? 0 : data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null == data ? null : data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent)//TODO getView
	{

		NewsItem item = data.get(position);
		if (null != item)
		{
			int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
			convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, null);

			ImageView imageView = ViewHolder.getView(convertView, R.id.iv_image);
			TextView tvTitle = ViewHolder.getView(convertView, R.id.tv_title);
			TextView tvSubTitle = ViewHolder.getView(convertView, R.id.tv_sub_title);

			// ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView,
			// "translationX",screenWidth/3, screenWidth );
			// AnimatorSet set = new AnimatorSet();
			// set.play(translateX);
			// set.setStartDelay(0);
			// set.setDuration( 0);
			// set.start();
//			AnimatorSet set = new AnimatorSet();
//			ObjectAnimator translateY = ObjectAnimator.ofFloat(convertView, "translationY", 500, 0);
//			set.play(translateY);
//			set.setStartDelay(100);
//			set.setDuration(300);
//			set.start();

			tvTitle.setText(item.getTitle());
			tvSubTitle.setText(item.getSubTitle());
			DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc()
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			mImageLoader.displayImage(item.getSrc(), imageView, options);
			ObjectAnimator scaleX = ObjectAnimator.ofFloat(convertView, "scaleX", 0f, 1f);
			ObjectAnimator scaleY = ObjectAnimator.ofFloat(convertView, "alpha", 0f, 1f);
			 AnimatorSet set = new AnimatorSet();
			// set.playTogether(new Animator[]{scaleX,scaleY});
			ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView, "translationX", screenWidth, 0f);
			 ObjectAnimator translateY = ObjectAnimator.ofFloat(convertView,
			 "translationY", 500, 0);
			 set.play(translateX);
			 set.setStartDelay(100);
			 set.setDuration(300);
			 set.start();
		}
		return convertView;

	}

}

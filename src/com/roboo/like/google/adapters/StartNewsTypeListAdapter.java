package com.roboo.like.google.adapters;

import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.AddActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.dao.impl.NewsTypeItemDaoImpl;
import com.roboo.like.google.databases.DBHelper;
import com.roboo.like.google.models.NewsTypeItem;
import com.roboo.like.google.swipelistview.SwipeListView;
/**
 * 首页新闻类型列表适配器
 * @author bo.li
 *
 * 2014-7-30 上午10:16:36
 *
 * TODO
 */
public class StartNewsTypeListAdapter extends BaseAdapter implements StickyHeadersAdapter, SectionIndexer
{
	private LinkedList<NewsTypeItem> mData;
	private Activity activity;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;

	public StartNewsTypeListAdapter(LinkedList<NewsTypeItem> mData, Activity activity)
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final NewsTypeItem item = mData.get(position);
		convertView = mInflater.inflate(R.layout.list_news_type_item, null);// TODO
		if (parent instanceof SwipeListView)
		{
			convertView = mInflater.inflate(R.layout.list_news_swipe_type_item, null);// TODO
		}
		TextView textView = ViewHolder.getView(convertView, R.id.tv_title);
		ImageView imageView = ViewHolder.getView(convertView, R.id.iv_image);
		Button btnDelete = ViewHolder.getView(convertView, R.id.btn_delete);
		final Button btnFav = ViewHolder.getView(convertView, R.id.btn_fav);
		if (activity instanceof AddActivity)
		{
			btnDelete.setText("添加");
			btnDelete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.swipe_add_selector, 0, 0, 0);
		}
		if(item.fav)
		{
			btnFav.setText("取消收藏");
		}
		else 
		{
			btnFav.setText("收藏");
		}
		btnDelete.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mData.remove(item);
				NewsTypeItemDaoImpl newsTypeItemDaoImpl = new NewsTypeItemDaoImpl(new DBHelper(activity));
				boolean returnFlag = newsTypeItemDaoImpl.updateFlag(item.md5, !item.flag);
				if (returnFlag)
				{
					System.out.println("修改成功");
				}
				notifyDataSetChanged();
			}
		});
		btnFav.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				NewsTypeItemDaoImpl newsTypeItemDaoImpl = new NewsTypeItemDaoImpl(new DBHelper(activity));
				boolean returnFav = newsTypeItemDaoImpl.updateFav(item.md5, !item.fav);
				if (returnFav)
				{
					mData.remove(position);
					boolean fav = newsTypeItemDaoImpl.getFav(item.md5);
					item.fav = fav;
					mData.add(position, item);
					System.out.println("修改成功");
				}
				notifyDataSetChanged();
			}
		});
		textView.setText(item.name);
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		mImageLoader.displayImage(item.img, imageView, options);
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
		// convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
		// TextView textView = ViewHolder.getView(convertView, R.id.tv_text);
		// textView.setText("" + (1 + position));
		// textView.setVisibility(View.GONE);
		// ViewHolder.getView(convertView, R.id.frame_container).setVisibility(View.GONE);
		convertView = new View(activity);
		return convertView;

	}

}

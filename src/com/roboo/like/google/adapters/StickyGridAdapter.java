package com.roboo.like.google.adapters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roboo.like.google.PictureDetailActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.models.PictureItem;
import com.roboo.like.google.utils.BitmapUtils;

public class StickyGridAdapter extends BaseAdapter implements StickyHeadersAdapter
{
	private List<PictureItem> hasHeaderIdList;

	private LayoutInflater mInflater;
	private Activity mActivity;
	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private Set<BitmapWorkerTask> taskCollection = new HashSet<BitmapWorkerTask>();
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	// 获取应用程序的最大内存
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(maxMemory / 8)
	{
		protected int sizeOf(String key, Bitmap bitmap)
		{
			return bitmap.getRowBytes() * bitmap.getHeight();
		};
	};

	public StickyGridAdapter(Activity activity, List<PictureItem> hasHeaderIdList)
	{

		this.mActivity = activity;
		mInflater = LayoutInflater.from(mActivity);
		this.hasHeaderIdList = hasHeaderIdList;
	}
 
	public int getCount()
	{
		return hasHeaderIdList.size();
	}
	public Object getItem(int position)
	{
		return hasHeaderIdList.get(position);
	}
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate(R.layout.picture_grid_item, parent, false);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_image);
		String path = hasHeaderIdList.get(position).getPath();
		if (mLruCache.get(path) == null)
		{
			new BitmapWorkerTask(imageView).execute(path);
		}
		else
		{
			imageView.setImageBitmap(mLruCache.get(path));
		}
		imageView.setOnClickListener(new OnClickListenerImpl(path));
		return convertView;
	}

	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate( R.layout.sticky_header_view, parent, false);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		textView.setText(hasHeaderIdList.get(position).getTime());
		return convertView;
	}

	/**
	 * 获取HeaderId, 只要HeaderId不相等就添加一个Header
	 */
	public long getHeaderId(int position)
	{
		return hasHeaderIdList.get(position).getHeaderId();
	}

	/**
	 * 异步下载图片的任务。
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
	{
		private ImageView mImageView;
		private String imagePath;
		@Override
		protected void onPreExecute()
		{
			 taskCollection.add(this);
			super.onPreExecute();
		}
		public BitmapWorkerTask(ImageView mImageView)
		{
			super();
			this.mImageView = mImageView;
		}

		protected Bitmap doInBackground(String... params)
		{
			imagePath = params[0];
			return BitmapUtils.getBitmap(imagePath);
		}

		
		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			taskCollection.remove(this);
			mImageView.setImageBitmap(bitmap);
			mLruCache.put(imagePath, bitmap);
		}

		
	}

	private class OnScrollListenerImpl implements OnScrollListener
	{
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
			if (scrollState == SCROLL_STATE_IDLE)
			{
				showImageWhenIDLE(view, mFirstVisibleItem, mVisibleItemCount);
			}
			else
			{
				cancelAllTasks();
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;

		}
	}

	private void showImageWhenIDLE(AbsListView view, int firstVisibleItem, int visibleItemCount)
	{
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++)
		{
			PictureItem item = hasHeaderIdList.get(i);
			String imagePath = item.getPath();
			Bitmap bitmap = mLruCache.get(imagePath);
			ImageView imageView = (ImageView) view.findViewWithTag(imagePath);
			if (bitmap != null)
			{
				imageView.setImageBitmap(bitmap);
			}
			else
			{
				new BitmapWorkerTask(imageView).execute(imagePath);
			}
		}
	}

	/**
	 * 取消所有正在下载或等待下载的任务。
	 */
	public void cancelAllTasks()
	{
		if (taskCollection != null)
		{
			for (BitmapWorkerTask task : taskCollection)
			{
				task.cancel(false);
			}
		}
	}
	private class OnClickListenerImpl implements OnClickListener
	{
		String imagePath;
		
		public OnClickListenerImpl(String imagePath)
		{
 
			this.imagePath = imagePath;
		}
 
		public void onClick(View v)
		{ 
			PictureDetailActivity.actionPictureDetail(mActivity, imagePath);
		}
		
	}
}
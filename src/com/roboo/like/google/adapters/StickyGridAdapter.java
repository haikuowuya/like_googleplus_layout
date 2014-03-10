package com.roboo.like.google.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roboo.like.google.R;
import com.roboo.like.google.models.PictureItem;

public class StickyGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter
{
	private List<PictureItem> hasHeaderIdList;
	private LayoutInflater mInflater;
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	// 获取应用程序的最大内存
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(maxMemory / 8)
	{
		protected int sizeOf(String key, Bitmap bitmap)
		{
			return bitmap.getRowBytes() * bitmap.getHeight();
		};
	};

	public StickyGridAdapter(Context context, List<PictureItem> hasHeaderIdList)
	{
		mInflater = LayoutInflater.from(context);
		this.hasHeaderIdList = hasHeaderIdList;
	}

	@Override
	public int getCount()
	{
		return hasHeaderIdList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return hasHeaderIdList.get(position);
	}

	@Override
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
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
		textView.setText(hasHeaderIdList.get(position).getTime());
		return convertView;
	}

	/**
	 * 获取HeaderId, 只要HeaderId不相等就添加一个Header
	 */
	@Override
	public long getHeaderId(int position)
	{
		return hasHeaderIdList.get(position).getHeaderId();
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
	{
		private ImageView mImageView;
		private String imagePath;

		public BitmapWorkerTask(ImageView mImageView)
		{
			super();
			this.mImageView = mImageView;
		}

		protected Bitmap doInBackground(String... params)
		{
			imagePath = params[0];
			return handlerBitmap(imagePath);
		}

		private Bitmap handlerBitmap(String path)
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 设置为true,表示解析Bitmap对象，该对象不占内存
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			// 设置缩放比例
			options.inSampleSize = computeScale(options, 200, 200);
			// 设置为false,解析Bitmap对象加入到内存中
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			mImageView.setImageBitmap(bitmap);
			mLruCache.put(imagePath, bitmap);
		}

		/**
		 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
		 * 
		 * @param options
		 * @param width
		 * @param height
		 */
		private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight)
		{
			int inSampleSize = 1;
			if (viewWidth == 0 || viewWidth == 0)
			{
				return inSampleSize;
			}
			int bitmapWidth = options.outWidth;
			int bitmapHeight = options.outHeight;

			// 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
			if (bitmapWidth > viewWidth || bitmapHeight > viewWidth)
			{
				int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
				int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

				// 为了保证图片不缩放变形，我们取宽高比例最小的那个
				inSampleSize = widthScale < heightScale ? widthScale : heightScale;
			}
			return inSampleSize;
		}

	}

}
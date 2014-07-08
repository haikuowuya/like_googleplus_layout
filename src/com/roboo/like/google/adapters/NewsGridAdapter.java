package com.roboo.like.google.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.R;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.views.StickyGridHeadersGridView;

public class NewsGridAdapter extends BaseAdapter implements StickyHeadersAdapter, SectionIndexer
{
	private static final boolean IS_CUSTOM_FAST_SCROLL_LABEL = false;
	private Context context;
	private LinkedList<NewsItem> mData;
	private ImageLoader mImageLoader;
	private LayoutInflater mInflater;
	/** 用于记录兩條新聞日期不相同时，该比较字符串所在List集合的索引位置，在生成HeaderId时进行获取 */
	private LinkedList<Integer> mSectionIndex = new LinkedList<Integer>();
	private HashMap<String, Integer> mNewsCountInDay = new HashMap<String, Integer>();

	public NewsGridAdapter(Context context, LinkedList<NewsItem> data)
	{
		super();
		this.context = context;
		this.mData = data;
		this.mImageLoader = ImageLoader.getInstance();
		mInflater = LayoutInflater.from(context);
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	public NewsGridAdapter(Context context, LinkedList<NewsItem> data, LinkedList<Integer> sectionIndex)
	{
		super();
		this.context = context;
		this.mData = data;
		this.mImageLoader = ImageLoader.getInstance();
		mInflater = LayoutInflater.from(context);
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		this.mSectionIndex = sectionIndex;
	}

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

	public View getView(int position, View convertView, ViewGroup parent)// TODO
	{
		NewsItem item = mData.get(position);
		if (null != item)
		{
//			String time = mData.get(position).getTime();
//			int count = null == mNewsCountInDay.get(time) ? 0 : mNewsCountInDay.get(time).intValue();
			// if (count % 2 == 1 && (count == (position + 1)))
			// {
			// StickyGridHeadersGridView gridView = (StickyGridHeadersGridView) parent;
			// gridView.setNumColumns(1);
			// convertView = mInflater.inflate(R.layout.list_news_item, null);
			// }
			// else
			// {
			convertView = mInflater.inflate(R.layout.grid_news_item, null);
			// }
			ImageView imageView = ViewHolder.getView(convertView, R.id.iv_image);
			TextView tvTitle = ViewHolder.getView(convertView, R.id.tv_title);
			TextView tvSubTitle = ViewHolder.getView(convertView, R.id.tv_sub_title);
			if (parent instanceof StickyGridHeadersGridView)
			{
				tvTitle.setLines(3);
				tvSubTitle.setLines(4);
			}
			tvTitle.setText(item.getTitle());
			tvSubTitle.setText(item.getSubTitle());
			DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
			mImageLoader.displayImage(item.getSrc(), imageView, options);
			// addAnimation(convertView);
		}
		return convertView;
	}

	protected void addAnimation(View convertView)
	{
		// ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView,
		// "translationX",screenWidth/3, screenWidth );
		// AnimatorSet set = new AnimatorSet();
		// set.play(translateX);
		// set.setStartDelay(0);
		// set.setDuration( 0);
		// set.start();
		// AnimatorSet set = new AnimatorSet();
		// ObjectAnimator translateY = ObjectAnimator.ofFloat(convertView,
		// "translationY", 500, 0);
		// set.play(translateY);
		// set.setStartDelay(100);
		// set.setDuration(300);
		// set.start();

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(convertView, "scaleX", 0f, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(convertView, "alpha", 0f, 1f);
		AnimatorSet set = new AnimatorSet();
		// set.playTogether(new Animator[]{scaleX,scaleY});
		ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView, "translationX", screenWidth, 0f);
		ObjectAnimator translateY = ObjectAnimator.ofFloat(convertView, "translationY", 500, 0);
		set.play(translateX);
		set.setStartDelay(100);
		set.setDuration(300);
		set.start();
	}

	public long getHeaderId(int position)
	{
		return mData.get(position).getHeaderId();
	}

	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		convertView = mInflater.inflate(R.layout.sticky_header_view, parent, false);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		TextView tvTodayNewsCount = (TextView) convertView.findViewById(R.id.tv_today_news_count);
		String time = mData.get(position).getTime();
		int count = null == mNewsCountInDay.get(time) ? 0 : mNewsCountInDay.get(time).intValue();
		textView.setText(time);
		tvTodayNewsCount.setText("总共 " + count + " 条");
		return convertView;
	}

	@Override
	public Object[] getSections()
	{
		String[] sections = new String[mSectionIndex.size()];
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			sections[i] = getFastScrollLabel(mData.get(mSectionIndex.get(i)).getTime());
		}
		return sections;
	}

	/** 获取快速滑动时显示的文字 */
	private String getFastScrollLabel(String time)
	{
		if (IS_CUSTOM_FAST_SCROLL_LABEL)
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月");
			String currentMonth = simpleDateFormat.format(new Date(System.currentTimeMillis()));
			String newsPublishMonth = null;
			String newsPublishDay = null;
			if (time.contains("月") && time.contains("日"))
			{
				newsPublishMonth = time.split("月")[0] + "月";
				newsPublishDay = time.split("月")[1].split("日")[0];
			}
			System.out.println("currentMonth = " + currentMonth + " newsPublishMonth = " + newsPublishMonth);
			if (currentMonth.equals(newsPublishMonth))
			{
				return newsPublishDay;
			}
			else
			{
				return newsPublishMonth;
			}
		}
		else
		{
			return time;
		}
	}

	public int getPositionForSection(int section)
	{
		if (section >= mSectionIndex.size())
		{
			section = mSectionIndex.size() - 1;
		}
		else if (section < 0)
		{
			section = 0;
		}
		if (section < mSectionIndex.size() && mSectionIndex.size() > 0)
		{
			return mSectionIndex.get(section);
		}
		return 0;
	}

	public int getSectionForPosition(int position)
	{
		for (int i = 0; i < mSectionIndex.size(); i++)
		{
			if (position < mSectionIndex.get(i))
			{
				return i - 1;
			}
		}
		return mSectionIndex.size() - 1;
	}

	public void setSectionIndex(LinkedList<Integer> sectionIndex)
	{
		mSectionIndex = sectionIndex;
		String currentNewsTime = mData.get(0).getTime();
		int count = 0;
		for (int i = 0; i < mData.size(); i++)
		{
			NewsItem item = mData.get(i);
			if (currentNewsTime.equals(item.getTime()))
			{
				count++;
			}
			else
			{
				mNewsCountInDay.put(currentNewsTime, count);
				currentNewsTime = item.getTime();
				count = 1;
			}
		}
		mNewsCountInDay.put(currentNewsTime, count);// 最后一个日期
	}

}

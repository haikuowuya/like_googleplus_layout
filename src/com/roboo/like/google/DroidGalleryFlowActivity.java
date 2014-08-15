package com.roboo.like.google;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidux.anim.FloorBounceAnimation;
import com.droidux.components.DroidUxLib;
import com.droidux.interfaces.GalleryFlowInterfaces.Adapters.AdapterLooper;
import com.droidux.interfaces.GalleryFlowInterfaces.GalleryFlowViewInterface;
import com.droidux.widget.adapters.UrlImageAdapter;
import com.droidux.widget.gallery.GalleryFlowCarousel;
import com.droidux.widget.gallery.GalleryFlowZoom;

public class DroidGalleryFlowActivity extends BaseLayoutActivity implements AnimationListener
{
	private GalleryFlowCarousel mGalleryFlowCarousel;
	private GalleryFlowZoom mGalleryFlowZoom;

	DemoImage[] mImages;

	private AdapterLooper mAdapterLooper;

	public static void actionPinterest(Activity activity)
	{
		Intent intent = new Intent(activity, DroidGalleryFlowActivity.class);
		DroidUxLib.register("enter-your-api-key-here", activity.getApplication());
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinterest);//TODO
		customActionBar();
		initView();
	}

	@Override
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
		initGFC();
		initGFZ();
		findViewById(R.id.btn_gallery_flow).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				 GalleryFlowActivity.actionGalleryFlow(DroidGalleryFlowActivity.this);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void initGFZ()
	{
		
		mGalleryFlowZoom = (GalleryFlowZoom) findViewById(R.id.gfz_gallery);
		mGalleryFlowZoom.setReflected(false);
		mAdapterLooper = createAdapter();
		mGalleryFlowZoom.setAdapter(mAdapterLooper);
		mGalleryFlowZoom.setClipChildren(false);
		mGalleryFlowZoom.setCallbackDuringFling(false);
		mGalleryFlowZoom.setMaxFlingVelocity(1500);
		 
		mGalleryFlowZoom.setSelection(mAdapterLooper.getCenterPosition());
	}

	@SuppressWarnings("deprecation")
	private void initGFC()
	{
		mGalleryFlowCarousel = (GalleryFlowCarousel) findViewById(R.id.gfc_gallery);
		mGalleryFlowCarousel.setClipChildren(false);

		mAdapterLooper = createAdapter();
		mGalleryFlowCarousel.setAdapter(mAdapterLooper);
		mGalleryFlowCarousel.setSelection(0, true);
		mGalleryFlowCarousel.setCallbackDuringFling(false);
		mGalleryFlowCarousel.setSpacing(-40);
		mGalleryFlowCarousel.setMaxFlingVelocity(1500);
		mGalleryFlowCarousel.setSelection(mAdapterLooper.getCenterPosition());
		mGalleryFlowCarousel.setViewPoint(GalleryFlowCarousel.VIEW_POINT_OUTSIDE);
		mGalleryFlowCarousel.setEdgeAngle(90);
		mGalleryFlowCarousel.setReflected(false);
		setGalleryFlowListeners();
	}

	void setGalleryFlowListeners()
	{
		mGalleryFlowCarousel.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				Toast.makeText(DroidGalleryFlowActivity.this, (1 + position) + "", Toast.LENGTH_SHORT).show();
				// ignore when the clicked item is not the same as the selected item
				if (position != parent.getSelectedItemPosition())
				{
					return;
				}
				// view.startAnimation(createMediumAnimation(true));
			}
		});
	}

	private Animation createMediumAnimation(boolean setListener)
	{
		Animation anim = new FloorBounceAnimation(0.30f, Animation.RELATIVE_TO_SELF);
		anim.setDuration(2000);
		if (setListener)
		{
			anim.setAnimationListener(this);
		}
		return anim;

	}

	AdapterLooper createAdapter()
	{
		mImages = createItems();
		return new AdapterLooper(new ImageAdapter(this, mImages));
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		// mActionBar.setTitle("瀑布流");
		mActionBar.setTitle("测试");
		mActionBar.setLogo(R.drawable.ic_abs_mood_up);
	}

	private DemoImage[] createItems()
	{
		DemoImage[] items = new DemoImage[15];

		// #1
		items[0] = new DemoImage("Chessboard", "chessboard");
		// #2
		items[1] = new DemoImage("Earth", "earth");
		// #3
		items[2] = new DemoImage("Grapes", "grapes");
		// #4
		items[3] = new DemoImage("Lake", "lake");
		// #5
		items[4] = new DemoImage("Ford Mustang", "mustang");
		// #6
		items[5] = new DemoImage("Nebula", "nebula");
		// #7
		items[6] = new DemoImage("New York", "newyork");
		// #8
		items[7] = new DemoImage("Orange", "orange");
		// #9
		items[8] = new DemoImage("Red Kite", "redkite");
		// #10
		items[9] = new DemoImage("Rose", "rose");
		// #11
		items[10] = new DemoImage("Flying Seagull", "seagull");
		// #12
		items[11] = new DemoImage("Space Shuttle", "shuttle");
		// #13
		items[12] = new DemoImage("Smarties", "smarties");
		// #14
		items[13] = new DemoImage("Sun", "sun");
		// #15
		items[14] = new DemoImage("Tulips", "tulips");
		Arrays.sort(items);
		return items;
	}

	static class ImageAdapter extends BaseAdapter implements UrlImageAdapter
	{
		final Context mContext;
		final DemoImage[] mItems;

		public ImageAdapter(Context context, DemoImage[] items)
		{
			mContext = context;
			mItems = items;
		}

		public int getCount()
		{
			return mItems.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_galleryflow_item, null);// TODO
			TextView tv = (TextView) linearLayout.findViewById(R.id.tv_title);
			ImageView iv = (ImageView) linearLayout.findViewById(R.id.iv_image);
			tv.setText(mItems[position].getTitle());
			iv.setImageResource(R.drawable.ic_image);
			return linearLayout;
		}

		@Override
		public void downloadUrlImages(int position, View itemView, Request request)
		{
			request.download(mItems[position].getImageUrl());
		}

		@Override
		public void onImageFail(int position, View itemView, String url, int refId, Exception exception)
		{

		}

		@Override
		public void onImageReady(int position, View itemView, String url, int refId, Bitmap bitmap)
		{
			if (itemView instanceof ImageView)
			{
				((ImageView) itemView).setImageBitmap(bitmap);
			}
		}

		@Override
		public void onWaitingForImage(int position, View itemView, String url, int refId)
		{

		}
	}

	static class DemoImage implements Comparable<DemoImage>
	{
		private String title;
		private String image;

		DemoImage(String title, String image)
		{
			this.title = title;
			this.image = image;
		}

		String getTitle()
		{
			return title;
		}

		String getImageUrl()
		{
			return "http://e.hiphotos.baidu.com/image/w%3D230/sign=9d5b668dd31373f0f53f689c940e4b8b/267f9e2f07082838532822e2ba99a9014c08f11b.jpg";
			// return String.format("http://www.droidux.com/images/droidux/apidemos_v2/port/%s.jpg", image);
		}

		@Override
		public int compareTo(DemoImage another)
		{
			return this.title.compareTo(another.title);
		}
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		((GalleryFlowViewInterface) mGalleryFlowCarousel).setScrollingEnabled(false);

	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		((GalleryFlowViewInterface) mGalleryFlowCarousel).setScrollingEnabled(true);
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{}
}

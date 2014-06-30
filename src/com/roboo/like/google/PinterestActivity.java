package com.roboo.like.google;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.droidux.anim.FloorBounceAnimation;
import com.droidux.interfaces.GalleryFlowInterfaces.GalleryFlowViewInterface;
import com.droidux.widget.adapters.UrlImageAdapter;

/** 文字界面 */
public class PinterestActivity extends BaseLayoutActivity implements AnimationListener
{
	private Gallery mGalleryFlow;

	DemoImage[] mImages;

	public static void actionPinterest(Activity activity)
	{
		Intent intent = new Intent(activity, PinterestActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinterest);
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
		mGalleryFlow = (Gallery) findViewById(R.id.gfc_gallery);

		mGalleryFlow.setClipChildren(false);
		final Gallery galleryFlow = mGalleryFlow;
		mImages = createItems();
		SpinnerAdapter adapter = createAdapter();
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSelection(0, true);
		galleryFlow.setCallbackDuringFling(false);
		setGalleryFlowListeners();

	}

	void setGalleryFlowListeners()
	{
		mGalleryFlow.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				// ignore when the clicked item is not the same as the selected item
				if (position != parent.getSelectedItemPosition())
				{
					return;
				}
				view.startAnimation(createMediumAnimation(true));
			}
		});
	}

	private Animation createMediumAnimation(boolean setListener)
	{
		Animation anim = new FloorBounceAnimation(0.30f, Animation.RELATIVE_TO_SELF);
		anim.setDuration(getResources().getInteger(400));
		if (setListener)
		{
			anim.setAnimationListener(this);
		}
		return anim;

	}

	SpinnerAdapter createAdapter()
	{
		return new ImageAdapter(this, mImages);
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("瀑布流");
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

			LinearLayout linearLayout;

			linearLayout = new LinearLayout(mContext);

			TextView tv = new TextView(mContext);
			ImageView iv = new ImageView(mContext);
			linearLayout.addView(tv);
			linearLayout.addView(iv);
			tv.setText(mItems[position].getTitle());
			iv.setImageResource(R.drawable.ic_default_image);
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
			if(itemView instanceof ImageView)
			{
				((ImageView)itemView).setImageBitmap(bitmap);
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
			return String.format("http://www.droidux.com/images/droidux/apidemos_v2/port/%s.jpg", image);
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
		((GalleryFlowViewInterface) mGalleryFlow).setScrollingEnabled(false);

	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		((GalleryFlowViewInterface) mGalleryFlow).setScrollingEnabled(true);
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub

	}
}

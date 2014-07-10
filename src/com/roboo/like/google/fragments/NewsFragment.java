package com.roboo.like.google.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import net.dynamicandroid.listview.DynamicScrollView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.roboo.like.google.BaseActivity;
import com.roboo.like.google.CommentActivity;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.PictureDetailActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.async.NewsContentAsyncTaskLoader;
import com.roboo.like.google.models.NewsItem;
import com.roboo.like.google.utils.GifDecoder;

public class NewsFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<String>>
{
	/** 向 ViewGroup 中添加view时动画持续时间 */
	private static final int ANIMATION_DURATION_TIME = 100;
	private static final String ARG_NEWS = "news";
	private static final int[] COLORS_COLLECTION = new int[] { R.color.red_color, R.color.sky_blue_color, R.color.hotpink_color, R.color.lightseagreen_color, R.color.orangered_color, R.color.turquoise_color, R.color.fast_scroll_track_color, R.color.alpha_red_color, R.color.poppyview_default_color };
	private NewsItem mItem;
	/** 线性布局用于存放新闻内容 */
	private LinearLayout mLinearContainer;
	/** 显示新闻标题 */
	private TextView mTvTitle;
	/** 显示新闻日期 */
	private TextView mTvTime;
	private Random mRandom = new Random();
	/** 异步图片加载器 */
	private ImageLoader mImageLoader;
	/** ViewGroup中添加View时的动画操作对象 */
	private LayoutTransition mTransitioner;
	private Handler mHandler = new Handler();
	private DynamicScrollView mScrollView;
	private boolean mHasAddedFrontView = false;
	private ImageView mIvImageView;
	/** 动画结束后执行隐藏进度圈和显示标题 */
	private Runnable mHideProgressBarRunnable = new Runnable()
	{
		public void run()
		{
			mProgressBar.setVisibility(View.GONE);
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTime.setVisibility(View.VISIBLE);
		}
	};

	public static NewsFragment newInstance(NewsItem item)
	{
		NewsFragment fragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_NEWS, item);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_news, null);// TODO
		mLinearContainer = (LinearLayout) view.findViewById(R.id.linear_container);
		mScrollView = (DynamicScrollView) view.findViewById(R.id.dsv_scrollview);
		mIvImageView = (ImageView) view.findViewById(R.id.iv_image);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title);
		mTvTime = (TextView) view.findViewById(R.id.tv_time);
		// Typeface typeface =
		// Typeface.createFromAsset(getActivity().getAssets(), "custom.ttf");
		// mTvTitle.setTypeface(typeface);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getActivity()).discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		mImageLoader.init(imageLoaderConfiguration);
		setListener();
		getActivity().getSupportLoaderManager().initLoader(0, getArguments(), this);
	}

	@Override
	public void onPause()
	{
		stopRendering();
		super.onPause();
		if (mHasAddedFrontView)
		{
			ViewGroup viewGroup = (ViewGroup) imageButton.getParent();
			viewGroup.setVisibility(View.GONE);
		}

	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (null != imageButton)
		{
			ViewGroup viewGroup = (ViewGroup) imageButton.getParent();
			viewGroup.setVisibility(View.VISIBLE);
		}
	}

	private void setListener()
	{
		// mScrollView.setOnTouchListener(new OnTouchListenerImpl());
	}

	public Loader<LinkedList<String>> onCreateLoader(int id, Bundle args)
	{
		mItem = (NewsItem) args.getSerializable(ARG_NEWS);
		return new NewsContentAsyncTaskLoader(getActivity(), mItem.getUrl());
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<String>> loader, LinkedList<String> data)
	{
		int durationTime = ANIMATION_DURATION_TIME;
		int lr = (int) (10 * getActivity().getResources().getDisplayMetrics().density);
		if (null != data && data.size() > 0)
		{
			removeRetryButton();
			durationTime = ANIMATION_DURATION_TIME * data.size();
			setLinearContainerAnimation();
			int tb = 5;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.bottomMargin = lr;
			ArrayList<String> imageUrls = new ArrayList<String>();
			int position = -1;
			for (int i = 0; i < data.size(); i++)
			{
				String str = data.get(i);
				if (isImg(str))
				{
					position++;
					imageUrls.add(str);
					ImageView imageView = new ImageView(getActivity());
					imageView.setId(R.id.iv_image);
					imageView.setLayoutParams(params);
					imageView.setBackgroundResource(R.drawable.list_item_selector);
					if (str.startsWith("file"))// 只有在离线下载时才会发生
					{
						System.out.println("替换后的图片文件路径   = " + mImageLoader.getDiscCache().get(str));
					}

					DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory()
						.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
					mImageLoader.displayImage(str, imageView, options);
					System.out.println("图片文件路径   = " + mImageLoader.getDiscCache().get(str));

					imageView.setPadding(lr, tb, lr, tb);
					mLinearContainer.addView(imageView);
					imageView.setOnClickListener(new OnClickListenerImpl(imageUrls, position));
				}
				else
				{
					params.leftMargin = lr;
					params.rightMargin = lr;
					TextView textView = new TextView(getActivity());
					textView.setClickable(true);
					SpannableString spannableString = new SpannableString(str);
					if(str.contains("$"))
					{
						int start = 1;
						int end = str.lastIndexOf("$");
						if(end > start)
						{
							str = str.replace("$", " ");
							spannableString = new SpannableString(str);
							ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
							spannableString.setSpan(redColorSpan, start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
							
						}
					}
					textView.setText(spannableString);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					textView.setLayoutParams(params);
					textView.setPadding(lr, tb, lr, tb);
					// textView.setBackgroundResource(R.drawable.list_item_default);
					mLinearContainer.addView(textView);

				}

				GoogleApplication.TEST = false;
				if (GoogleApplication.TEST)
				{
					System.out.println("str = " + str);
				}
			}
			if (GoogleApplication.mCurrentType == GoogleApplication.TYPE_ITHOME)
			{
				addCommentButton(params, lr);
			}
			// mHasAddedFrontView = addFrontView();
		}
		else
		{
			addRetryButton(lr);
		}
		int nextIndex = mRandom.nextInt(COLORS_COLLECTION.length);
		((ViewGroup) mTvTitle.getParent()).setBackgroundColor(getResources().getColor(COLORS_COLLECTION[nextIndex]));
		mTvTitle.setText(mItem.getTitle());
		mTvTime.setText(mItem.getTime());
		mHandler.postDelayed(mHideProgressBarRunnable, durationTime);
		// playGif();

	}

	private void removeRetryButton()
	{
		if (mLinearContainer.findViewById(R.id.btn_retry) != null)
		{
			mLinearContainer.removeView(mLinearContainer.findViewById(R.id.btn_retry));
		}
	}

	private boolean isImg(String str)
	{
		BaseActivity activity = (BaseActivity) getActivity();
		return activity.isImg(str);
	}

	private void addCommentButton(android.widget.LinearLayout.LayoutParams params, int ltrb)
	{
		if (mLinearContainer.findViewById(R.id.btn_comment) == null)
		{
			params.leftMargin = ltrb;
			params.rightMargin = ltrb;
			Button button = new Button(getActivity());
			button.setId(R.id.btn_comment);
			button.setClickable(true);
			button.setText("查看评论");
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			button.setLayoutParams(params);
			button.setPadding(ltrb, ltrb, ltrb, ltrb);
			button.setBackgroundResource(R.drawable.list_item_selector);
			mLinearContainer.addView(button);
			button.setOnClickListener(new OnClickListenerImpl());
		}
	}

	/** 当没有获取到数据时添加重试按钮 */
	private void addRetryButton(int ltrb)
	{
		Button button = new Button(getActivity());
		button.setId(R.id.btn_retry);
		if (button.getParent() == null)
		{
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER | Gravity.BOTTOM;
			params.leftMargin = ltrb;
			params.rightMargin = ltrb;
			params.topMargin = ltrb;
			params.bottomMargin = ltrb;
			button.setClickable(true);
			button.setText("点击重新加载");
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			button.setPadding(ltrb, ltrb, ltrb, ltrb);
			button.setBackgroundResource(R.drawable.list_item_selector);
			mLinearContainer.addView(button, params);
			button.setOnClickListener(new OnClickListenerImpl());
		}
		else
		{
			button.setVisibility(View.VISIBLE);
		}
	}

	public void onLoaderReset(Loader<LinkedList<String>> loader)
	{

	}

	private class OnClickListenerImpl implements OnClickListener
	{
		private ArrayList<String> mImageUrls;
		private int mPosition;

		public OnClickListenerImpl()
		{}

		public OnClickListenerImpl(ArrayList<String> mImageUrls, int mPosition)
		{
			super();
			this.mImageUrls = mImageUrls;
			this.mPosition = mPosition;
		}

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.iv_image:
				PictureDetailActivity.actionPictureDetail(getActivity(), mImageUrls, mPosition);
				break;

			case R.id.btn_comment:
				CommentActivity.actionComment(getActivity(), mItem.getNewsId());
				break;
			case R.id.btn_retry:// 重试
				v.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.VISIBLE);
				getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), NewsFragment.this);
//				Toast.makeText(getActivity(), "重试", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

	/** 设置ViewGroup添加子View时的动画 */
	private void setLinearContainerAnimation()
	{
		mTransitioner = new LayoutTransition();
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, ANIMATION_DURATION_TIME);// 添加View
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, ANIMATION_DURATION_TIME);// 移除View
		// 定制动画
		setupCustomAnimations();
		// 设置mLinearContainer布局改变时动画
		mLinearContainer.setLayoutTransition(mTransitioner);

	}

	/** 定制ViewGroup添加View时显示的动画 */
	private void setupCustomAnimations()
	{
		// 添加改变时执行
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// 移除改变时执行
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
		changeOut.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		// 添加时执行
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// 移除View时执行的动画
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter()
		{
			public void onAnimationEnd(Animator anim)
			{
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});
	}

	private ImageButton imageButton;
	private Bitmap initBitmap;
	private WindowManager windowManager;

	/** 在当前窗口中添加一个前端系统窗口(悬浮窗口) */
	private boolean addFrontView()
	{
		// scrollView.fullScroll(ScrollView.FOCUS_DOWN);滚动到底部
		// scrollView.fullScroll(ScrollView.FOCUS_UP);滚动到顶部
		int paddingLTRB = (int) (10 * getResources().getDisplayMetrics().density);
		int imageButtonWH = (int) (64 * getResources().getDisplayMetrics().density);
		windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		FrameLayout frameLayout = new FrameLayout(getActivity());
		frameLayout.setPadding(paddingLTRB, paddingLTRB, paddingLTRB, paddingLTRB);

		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);

		imageButton = new ImageButton(getActivity());
		imageButton.setBackgroundResource(R.drawable.list_item_selector);
		frameLayout.addView(imageButton, frameLayoutParams);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.ic_down);
		initBitmap = bitmapDrawable.getBitmap();
		imageButton.setImageDrawable(bitmapDrawable);
		imageButton.setTag(imageButton.hashCode());
		imageButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				BitmapDrawable imageDrawable = (BitmapDrawable) imageButton.getDrawable();
				Bitmap currentBitmap = imageDrawable.getBitmap();
				System.out.println("initBitmap = " + initBitmap + " currentBitmap = " + currentBitmap);
				if (initBitmap == currentBitmap)
				{
					imageButton.setImageResource(R.drawable.ic_up);
					mScrollView.fullScroll(View.FOCUS_DOWN);
				}
				else
				{
					imageButton.setImageResource(R.drawable.ic_down);
					mScrollView.fullScroll(View.FOCUS_UP);
				}
			}
		});
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(imageButtonWH, imageButtonWH, android.view.WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
			android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | android.view.WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING, PixelFormat.TRANSPARENT);
		layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		// 如果像在任何地方都出现悬浮View 自己新建一个token layoutParams.token = new Binder();
		layoutParams.token = new Binder();
		// layoutParams.token =
		// getActivity().getWindow().getDecorView().getWindowToken();
		windowManager.addView(frameLayout, layoutParams);
		return true;
	}

	private class OnTouchListenerImpl implements OnTouchListener
	{
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction())
			{
			case MotionEvent.ACTION_MOVE:
				System.out.println(" Y = " + mScrollView.getScrollY() + " :: " + mScrollView.getMeasuredHeight() + "  :: " + mScrollView.getTop() + " :: " + mScrollView.getBottom());
				if (mScrollView.getScrollY() > mScrollView.getMeasuredHeight() && ((BitmapDrawable) imageButton.getDrawable()).getBitmap() == initBitmap)
				{
					imageButton.setImageResource(R.drawable.ic_up);
				}
				else if (mScrollView.getScrollY() < 100 && ((BitmapDrawable) imageButton.getDrawable()).getBitmap() != initBitmap)
				{
					imageButton.setImageResource(R.drawable.ic_down);
				}
			}
			return false;
		}
	}

	// ===============================GIF PLAY=================================
	private boolean mIsPlayingGif = false;
	private GifDecoder mGifDecoder;
	private Bitmap mTmpBitmap;
	private Runnable mUpdateResults = new Runnable()
	{
		public void run()
		{
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled())
			{
				mIvImageView.setImageBitmap(mTmpBitmap);
			}
		}
	};

	private void playGif(InputStream stream)
	{

		mGifDecoder = new GifDecoder();
		int resultCode = mGifDecoder.read(stream);
		if (resultCode > 0)
		{
			Bitmap bitmap = BitmapFactory.decodeStream(stream);
			if (null == bitmap)
			{
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			}
			mIvImageView.setImageBitmap(bitmap);
		}
		else
		{
			mIsPlayingGif = true;
			new Thread(new Runnable()
			{
				public void run()
				{
					final int n = mGifDecoder.getFrameCount();
					final int ntimes = mGifDecoder.getLoopCount();
					int repetitionCounter = 0;
					do
					{
						for (int i = 0; i < n; i++)
						{
							mTmpBitmap = mGifDecoder.getFrame(i);
							int t = mGifDecoder.getDelay(i);
							mHandler.post(mUpdateResults);
							try
							{
								Thread.sleep(t);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						if (ntimes != 0)
						{
							repetitionCounter++;
						}
					}
					while (mIsPlayingGif && (repetitionCounter <= ntimes));
				}
			}).start();
		}
	}

	public void stopRendering()
	{
		mIsPlayingGif = false;
	}

	private InputStream playGif()
	{
		InputStream inputStream = null;
		try
		{
			inputStream = getActivity().getAssets().open("animation.gif");
			playGif(inputStream);
		}
		catch (IOException e)
		{

		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception e)
				{}
			}
		}
		return inputStream;
	}

	// ===============================GIF PLAY=================================
}

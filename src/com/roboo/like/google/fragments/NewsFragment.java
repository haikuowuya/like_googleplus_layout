package com.roboo.like.google.fragments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class NewsFragment extends BaseFragment implements LoaderCallbacks<LinkedList<String>>
{
	/** 向 ViewGroup 中添加view时动画持续时间 */
	private static final int ANIMATION_DURATION_TIME = 100;
	private static final String ARG_NEWS = "news";
	private static final int[] COLORS_COLLECTION = new int[] { R.color.red_color, R.color.sky_blue_color, R.color.hotpink_color, R.color.lightseagreen_color, R.color.orangered_color, R.color.turquoise_color };
	private NewsItem mItem;
	/** 显示获取新闻内容进度条 */
	private ProgressBar mProgressBar;
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
	private ScrollView mScrollView;
	private boolean mHasAddedFrontView = false;
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
		mScrollView = (ScrollView) view.findViewById(R.id.sv_scrollview);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title);
		mTvTime = (TextView) view.findViewById(R.id.tv_time);
		// Typeface typeface =
		// Typeface.createFromAsset(getActivity().getAssets(), "custom.ttf");
		// mTvTitle.setTypeface(typeface);
		addProgressBar();
		return view;
	}

	private void addProgressBar()
	{
		mProgressBar = new ProgressBar(getActivity());
		mProgressBar.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.progressbar));
		FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		frameLayout.addView(mProgressBar, params);
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		setListener();
		getActivity().getSupportLoaderManager().initLoader(0, getArguments(), this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(mHasAddedFrontView  )
		{
			ViewGroup viewGroup = (ViewGroup) imageButton.getParent();
			viewGroup.setVisibility(View.GONE);
//			windowManager.removeView(viewGroup);
		}
		
	}
	@Override
	public void onResume()
	{
		super.onResume();
		if(null != imageButton)
		{
		ViewGroup viewGroup = (ViewGroup) imageButton.getParent();
		viewGroup.setVisibility(View.VISIBLE);
		}
		
	}
	 
	
	private void setListener()
	{
		mScrollView.setOnTouchListener(new OnTouchListenerImpl());
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
		if (null != data)
		{
			durationTime = ANIMATION_DURATION_TIME * data.size();
			setLinearContainerAnimation();
			int lr = (int) (10 * getActivity().getResources().getDisplayMetrics().density);
			int tb = 5;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.bottomMargin = lr;
			ArrayList<String> imageUrls = new ArrayList<String>();
			int position = -1;
			for (int i = 0; i < data.size(); i++)
			{
				String str = data.get(i);
				if (str.startsWith(BaseActivity.PREFIX_IMG_URL))
				{
					position++;
					imageUrls.add(str);
					ImageView imageView = new ImageView(getActivity());
					imageView.setId(R.id.iv_image);
					imageView.setLayoutParams(params);
					imageView.setBackgroundResource(R.drawable.list_item_selector);
					DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).showStubImage(R.drawable.ic_default_image).showImageForEmptyUri(R.drawable.ic_default_image).showImageOnFail(R.drawable.ic_default_image).cacheInMemory()
						.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
					mImageLoader.displayImage(str, imageView, options);
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
					textView.setText(str);
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
			addCommentButton(params, lr);
			mHasAddedFrontView = addFrontView();
		}
		int nextIndex = mRandom.nextInt(COLORS_COLLECTION.length);
		mTvTitle.setBackgroundColor(getResources().getColor(COLORS_COLLECTION[nextIndex]));
		mTvTitle.setText(mItem.getTitle());
		mTvTime.setText(mItem.getTime());
		mHandler.postDelayed(mHideProgressBarRunnable, durationTime);
	
	}

	private void addCommentButton(android.widget.LinearLayout.LayoutParams params, int ltrb)
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
	private boolean  addFrontView()
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
}

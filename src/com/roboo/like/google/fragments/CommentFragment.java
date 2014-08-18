package com.roboo.like.google.fragments;

import java.util.LinkedList;

import net.dynamicandroid.listview.DynamicListView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roboo.like.google.GoogleApplication;
import com.roboo.like.google.R;
import com.roboo.like.google.adapters.CommentAdapter;
import com.roboo.like.google.async.CommentAsyncTaskLoader;
import com.roboo.like.google.models.CommentItem;
import com.roboo.like.google.views.FooterView;

public class CommentFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<CommentItem>>
{
	private static final String ARG_NEWS_ID = "news_id";
	/** 异步图片加载器 */
	private ImageLoader mImageLoader;
	/** ViewGroup中添加View时的动画操作对象 */
	private LayoutTransition mTransitioner;
	private CommentAdapter mAdapter;
	private LinkedList<CommentItem> mData;
	private DynamicListView mListView;
	private FooterView mFooterView;
	private Button mBtnLoadNext;
	private ProgressBar mFooterProgressBar;
	private int mCurrentCommentPageNo = 1;
	private PopupWindow mPopupWindow;

	public static CommentFragment newInstance(String newsId)
	{
		CommentFragment fragment = new CommentFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_NEWS_ID, newsId);

		fragment.setArguments(bundle);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_comment, null);// TODO
		mListView = (DynamicListView) view.findViewById(R.id.dlv_list);
		mFooterView = new FooterView(getActivity(), FooterView.TYPE_BUTTON);
		mFooterProgressBar = mFooterView.getFooterProgressBar();
		mBtnLoadNext = mFooterView.getButton();
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		setListener();
		getActivity().getSupportLoaderManager().initLoader(0, getArguments(), this);
	}

	private void setListener()
	{
		mBtnLoadNext.setOnClickListener(new OnClickListenerImpl());
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (parent.getAdapter().getItemViewType(position) != AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER)
				{
					showPopupWindow(view);
				}
			}
		});
	}

	private void showPopupWindow(View view)
	{
		int height = (int) (48 * getResources().getDisplayMetrics().density);
		int width = (int) (getResources().getDisplayMetrics().widthPixels - 20 * getResources().getDisplayMetrics().density);
		if (mPopupWindow == null)
		{
			View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_comment, null);
			mPopupWindow = new PopupWindow(contentView, width, height);
		}
		if (mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
		mPopupWindow.showAsDropDown(view);
	}

	public Loader<LinkedList<CommentItem>> onCreateLoader(int id, Bundle args)
	{
		String newsId = getArguments().getString(ARG_NEWS_ID);
		return new CommentAsyncTaskLoader(getActivity(), newsId, mCurrentCommentPageNo);
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<CommentItem>> loader, LinkedList<CommentItem> data)
	{
		mFooterProgressBar.setVisibility(View.INVISIBLE);
		mBtnLoadNext.setText("点击加载下一页");
		if (null != data)
		{
			setLinearContainerAnimation();
			int ltrb = (int) (10 * getActivity().getResources().getDisplayMetrics().density);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.bottomMargin = ltrb;
			if (mData == null)
			{
				mData = data;
				mAdapter = new CommentAdapter(getActivity(), mData);
				mListView.addFooterView(mFooterView);
				mListView.setAdapter(mAdapter);

			}
			else
			{
				mData.addAll(data);
				mAdapter.notifyDataSetChanged();
			}
			GoogleApplication.TEST = false;
			if (GoogleApplication.TEST)
			{
				for (CommentItem item : data)
				{
					System.out.println("评论Item = 【 " + item + " 】");
				}
			}
		}
		else
		{
			if (mCurrentCommentPageNo == 1)
			{
				mListView.setEmptyView(getActivity().findViewById(android.R.id.empty));
			}
			else if (mCurrentCommentPageNo > 1)
			{
				mCurrentCommentPageNo--;
				mBtnLoadNext.setText("所有数据加载完毕");
				mBtnLoadNext.setClickable(false);
			}
		}
		mProgressBar.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<LinkedList<CommentItem>> loader)
	{

	}

	/** 设置ViewGroup添加子View时的动画 */
	private void setLinearContainerAnimation()
	{
		mTransitioner = new LayoutTransition();
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 100);// 添加View
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 100);// 移除View
		// 定制动画
		setupCustomAnimations();
		// 设置mLinearContainer布局改变时动画
		// mLinearContainer.setLayoutTransition(mTransitioner);

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

	private class OnClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			loadNextData();
		}
	}

	private void loadNextData()
	{
		mCurrentCommentPageNo++;
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mBtnLoadNext.setText("正在获取数据……");
		getActivity().getSupportLoaderManager().restartLoader(0, getArguments(), this);
	}
}

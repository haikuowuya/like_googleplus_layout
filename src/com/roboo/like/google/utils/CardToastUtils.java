package com.roboo.like.google.utils;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.roboo.like.google.R;

public class CardToastUtils
{
	private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout!";
	/** 要显示的CardToast的高度(单位dp) */
	private static final int CARD_TOAST_HEIGHT_IN_DP = 40;
	/** 显示消息内容字体颜色 */
	private static final int MESSAGE_TEXT_COLOR = android.R.color.black;
	/** 显示消息TextView的背景资源颜色 */
	private static final int MESSAGE_TEXT_BACKGROUND_COLOR = 0xFFDCDCDC;
	/** 默认显示持续的时间 */
	private static final long SHOW_DURATION_TIME = 2500L;
	private Activity mActivity;
	private ViewGroup mCardToastContainer;
	private ViewGroup mCardToastView;
	private Handler mHandler;
	private long mDuration = SHOW_DURATION_TIME;
	private TextView mMessageTextView;
	private Runnable mHideRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			dismiss();
		}
	};

	protected Runnable mHideImmediateRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			dismissImmediately();
		}
	};

	private void dismissImmediately()
	{
		if (mHandler != null)
		{
			mHandler.removeCallbacks(mHideRunnable);
			mHandler = null;
		}

		if (mCardToastView != null && mCardToastContainer != null)
		{
			mCardToastContainer.removeView(mCardToastView);
			mCardToastView = null;
		}
	}

	public void dismiss()
	{
		dismissWithAnimation();
	}

	private void dismissWithAnimation()
	{
		if (null != mCardToastView)
		{
			int width = mCardToastView.getWidth();
			AnimationSet animationSet = new AnimationSet(false);
			TranslateAnimation translateAnimation = new TranslateAnimation(0f, width, 0f, 0f);
			translateAnimation.setDuration(500);
			animationSet.addAnimation(translateAnimation);

			AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
			alphaAnimation.setDuration(500);
			animationSet.addAnimation(alphaAnimation);
			animationSet.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationEnd(Animation animation)
				{

					Handler handler = new Handler();

					handler.post(mHideImmediateRunnable);
				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{}

				@Override
				public void onAnimationStart(Animation animation)
				{}
			});

			if (mCardToastView != null)
			{
				mCardToastView.startAnimation(animationSet);
			}
		}
	}

	public CardToastUtils(Activity activity)
	{
		this.mActivity = activity;
		mHandler = new Handler();
		mCardToastContainer = (ViewGroup) activity.findViewById(R.id.card_container);
		if (null != mCardToastContainer)
		{
			// ==================================================================================================
			// ==================================================================================================
			// ==========================DEFAULT========================
			// mCardToastView = (ViewGroup)
			// LayoutInflater.from(mActivity).inflate(R.layout.toast_view,
			// mCardToastContainer, false);
			// mMessageTextView = (TextView)
			// mCardToastView.findViewById(R.id.tv_toast_textview);
			// ==================================================================================================
			// ==================================================================================================
			mCardToastView = new LinearLayout(activity);
			mMessageTextView = new TextView(activity);
			int height = (int) (activity.getResources().getDisplayMetrics().density * CARD_TOAST_HEIGHT_IN_DP);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);

			params.gravity = Gravity.CENTER_VERTICAL;
			mCardToastView.addView(mMessageTextView, params);
			mMessageTextView.setTextColor(activity.getResources().getColor(MESSAGE_TEXT_COLOR));
			mMessageTextView.setBackgroundColor(MESSAGE_TEXT_BACKGROUND_COLOR);
			mMessageTextView.setGravity(Gravity.CENTER_VERTICAL);
			mMessageTextView.setPadding(20, 0, 0, 0);
			mMessageTextView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mHandler.postDelayed(mHideRunnable, mDuration);
				}
			});
		}
		else
		{
			throw new IllegalAccessError(ERROR_CONTAINERNULL);
		}
	}

	public void show()
	{
		showAndAutoDismiss(null);

	}

	public void showAndAutoDismiss(CharSequence text)
	{
		showAndAutoDismissDurationTime(text, SHOW_DURATION_TIME);
	}

	public void show(CharSequence text)
	{
		showAndAutoDismissDurationTime(text, 0);
	}

	public void showAndAutoDismissDurationTime(CharSequence text, long durationTime)
	{
		this.mDuration = durationTime;
		if (mHandler == null)
		{
			mHandler = new Handler();
		}
		if (this.mDuration > 0)
		{
			mHandler.postDelayed(mHideRunnable, mDuration);
		}
		if (mCardToastContainer.getChildCount() == 0)
		{
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mCardToastContainer.addView(mCardToastView, params);
		}
		mCardToastView.startAnimation(showToastAnimation());
		if (null != text)
		{
			mMessageTextView.setText(text);
		}
	}

	private Animation showToastAnimation()
	{
		return getCardAnimation();
	}

	private Animation getCardAnimation()
	{
		AnimationSet animationSet = new AnimationSet(false);

		TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 1f, 0f);
		translateAnimation.setDuration(200);
		animationSet.addAnimation(translateAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(400);
		animationSet.addAnimation(alphaAnimation);
		RotateAnimation rotationAnimation = new RotateAnimation(15f, 0f, 0f, 0f);
		rotationAnimation.setDuration(225);

		animationSet.addAnimation(rotationAnimation);

		return animationSet;

	}

	public View getCardToastView()
	{
		return mCardToastView;
	}
}

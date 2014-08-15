package com.roboo.like.google.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.roboo.like.google.MainActivity;
import com.roboo.like.google.R;

public class ActionSheetFragment extends BaseFragment
{
	private static final String[] ARRAYS_STRINGS = { "离线下载" };
	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;
	private static final int BG_VIEW_ID = 3234;
	private static final int CANCLE_BTN_ID = 32332;
	private LinearLayout mLinearContainer;
	private boolean mIsDownload = false;
	private View mPanel;
	/**背景View*/
	private View mBgView;
	private ListView mListView;
	private Button mBtnCancle;
	private FrameLayout mDecorView;

	public static ActionSheetFragment newInstance()
	{
		ActionSheetFragment fragment = new ActionSheetFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mPanel = createView();
		mDecorView = (FrameLayout) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
		mDecorView.addView(mPanel);
		mBgView.setAnimation(createAlphaInAnimation());
		mLinearContainer.setAnimation(createTranlationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onDestroyView()
	{
		 dismiss();
		super.onDestroyView();
	}

	/***为mPanel创建从底部进入动画*/

	private Animation createTranlationInAnimation()
	{
		TranslateAnimation translateAnimation = new TranslateAnimation(
			TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
			TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
		translateAnimation.setDuration(TRANSLATE_DURATION);
		return translateAnimation;
	}

	private Animation createTranlationOutAnimation()
	{
		TranslateAnimation translateAnimation = new TranslateAnimation(
			TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
			TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
		translateAnimation.setDuration(TRANSLATE_DURATION);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	/**为背景View创建Aplha 进入动画*/
	private Animation createAlphaInAnimation()
	{
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(ALPHA_DURATION);
		return alphaAnimation;

	}

	private Animation createAlphaOutAnimation()
	{
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(ALPHA_DURATION);
		alphaAnimation.setFillAfter(true);
		return alphaAnimation;

	}

	@SuppressWarnings("deprecation")
	private View createView()
	{
		int margin = (int) (10 * getResources().getDisplayMetrics().density);
		FrameLayout frameLayout = new FrameLayout(getActivity());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		frameLayout.setLayoutParams(params);
		// 创建背景View
		mBgView = new View(getActivity());
		mBgView.setLayoutParams(params);
		mBgView.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBgView.setId(BG_VIEW_ID);
		// 创建背景View

		// 创建可操作的View
		mLinearContainer = new LinearLayout(getActivity());
		mLinearContainer.setOrientation(LinearLayout.VERTICAL);
		// mLinearContainer.setBackgroundColor(0xFFFFFFFF);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
			Gravity.BOTTOM);

		params.leftMargin = margin;
		params.rightMargin = margin;
		mLinearContainer.setLayoutParams(params);
		// 创建可操作的View
		mListView = new ListView(getActivity());

		mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
			ARRAYS_STRINGS));
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mBtnCancle = new Button(getActivity());
		mBtnCancle.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_item_selector));
		mBtnCancle.setId(CANCLE_BTN_ID);
		mBtnCancle.setText("取消");

		linearParams.bottomMargin = margin;
		mLinearContainer.addView(mListView, linearParams);
		mLinearContainer.addView(mBtnCancle, linearParams);

		frameLayout.addView(mBgView);
		frameLayout.addView(mLinearContainer);
		OnItemClickListenerImpl onItemClickListenerImpl = new OnItemClickListenerImpl();
		mListView.setOnItemClickListener(onItemClickListenerImpl);
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		mBgView.setOnClickListener(onClickListenerImpl);
		mBtnCancle.setOnClickListener(onClickListenerImpl);
		return frameLayout;
	}

	class OnItemClickListenerImpl implements OnItemClickListener
	{
		MainActivity activity = (MainActivity) getActivity();

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (!activity.mIsDownload)
			{
				activity.wifiDownload();
				Toast.makeText(getActivity(), "开始下载数据", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), "已经开始离线下载了", Toast.LENGTH_SHORT).show();
			}
			dismiss();
		}
	}

	class OnClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case BG_VIEW_ID:
				dismiss();
				break;
			case CANCLE_BTN_ID:
				dismiss();
				break;
			}
		}

	}

	public void dismiss()
	{
		getActivity().getSupportFragmentManager().popBackStack();
		mLinearContainer.startAnimation(createTranlationOutAnimation());
		mBgView.startAnimation(createAlphaOutAnimation());
		mLinearContainer.postDelayed(new Runnable()
		{
			public void run()
			{
				mDecorView.removeView(mPanel);
			}
		}, ALPHA_DURATION);
	}

}

package com.roboo.like.google.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.roboo.like.google.R;
import com.roboo.like.google.progressbutton.ProcessButton;

public class FooterView extends LinearLayout
{

	private ProgressBar mProgressBar;
	private Button mButton;
	private ProcessButton mProcessButton;
	public static final int TYPE_PROGRESS_BUTTON = 0;
	public static final int TYPE_BUTTON = 1;
	private int mType;

	public FooterView(Context context, int type)
	{
		super(context);
		mType = type;
		View child = inflate(context, R.layout.listview_footer_view, null);
		mProgressBar = (ProgressBar) child.findViewById(R.id.pb_progress);
		mButton = (Button) child.findViewById(R.id.btn_load_next);
		mProcessButton = (ProcessButton) child.findViewById(R.id.pbtn_load_next);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (mType == TYPE_BUTTON)
		{
			mProcessButton.setVisibility(View.GONE);
		}
		else
		{
			((ViewGroup) mProgressBar.getParent()).setVisibility(View.GONE);
		}
		addView(child, params);
	}

	public FooterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FooterView, 0, 0);
		if (null == typedArray)
		{
			return;
		}
		try
		{
			mType = typedArray.getInt(R.styleable.FooterView_type, TYPE_PROGRESS_BUTTON);
		}
		finally
		{
			typedArray.recycle();
		}
		View child = inflate(context, R.layout.listview_footer_view, null);
		mProgressBar = (ProgressBar) child.findViewById(R.id.pb_progress);
		mButton = (Button) child.findViewById(R.id.btn_load_next);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (mType == TYPE_BUTTON)
		{
			child = new ProcessButton(context, attrs);
			mButton = (Button) child;
		}
		addView(child, params);
	}

	public ProgressBar getProgressBar()
	{
		return mProgressBar;
	}

	public Button getButton()
	{
		if (mType == TYPE_BUTTON)
		{
			return mButton;
		}
		else
		{
			return mProcessButton;
		}
	}

	public int getType()
	{
		return mType;
	}

}

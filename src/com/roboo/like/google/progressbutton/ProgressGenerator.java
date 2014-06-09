package com.roboo.like.google.progressbutton;

import java.util.Random;

public class ProgressGenerator
{
	public interface OnCompleteListener<T>
	{
		public T onComplete();
		public T doInBackgroundProcess();
		 
	}
	private OnCompleteListener<Object > mListener;

	public ProgressGenerator(OnCompleteListener<Object> listener)
	{
		mListener = listener;
	}
	public Object start(final ProcessButton button)
	{
		button.setEnabled(false);
		return mListener.doInBackgroundProcess();
	}
	/*
	public void start( ProcessButton button,int default)
	{
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{
			public void run()
			{
				mProgress += 10;
				button.setProgress(mProgress);
				if (mProgress < 100)
				{
					handler.postDelayed(this, generateDelay());
				}
				else
				{
					mListener.onComplete();
				}
			}
		}, generateDelay());
	}
	*/
	private Random random = new Random();
	private int generateDelay()
	{
		return random.nextInt(1000);
	}
}

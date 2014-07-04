package com.roboo.like.google.progressbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ProgressBar;

import com.roboo.like.google.R;

public class ProcessButton extends Button
{

	private float mCornerRadius;
	private StateListDrawable mNormalDrawable;
	private StateListDrawable mProgressDrawable;
	private StateListDrawable mCompleteDrawable;
	private StateListDrawable mErrorDrawable;
	private CharSequence mNormalText;

	private int mProgress;
	private Rect mBounds = new Rect();
	private static final int MIN_PROGRESS = 0;
	private static final int MAX_PROGRESS = 100;
	private static final int TYPE_ERROR = -1;
	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_PROGRESS = 1;
	private static final int TYPE_PRESSED = 2;
	private static final int TYPE_COMPLETE = 3;
	public static final int TYPE_DRAW_DEFAULT = 0;
	public static final int TYPE_DRAW_PROGRESS = 1;
	public static final int TYPE_DRAW_PROGRESS_HORIZONAL = 2;
	public static final int TYPE_DRAW_PROGRESS_VERTICAL = 3;

	private CharSequence mLoadingText;
	private CustomProgressBar mProgressBar;
	private CharSequence mCompleteText;
	private CharSequence mErrorText;
	private int mDrawType;
	/** 默认按钮按下时的颜色 */
	private static final int DEFAULT_PRESSED_COLOR = 0xFFFF0000;
	private static final int DEFAULT_NORMAL_COLOR = 0xFF0000FF;
	protected static final int DEFAULT_PROGRESS_COLOR = 0xFFFF0000;
	protected static final int DEFAULT_COMPLETE_COLOR = 0xFF99CC00;
	protected static final int DEFAULT_ERROR_COLOR = 0xFFFF4444;

	public ProcessButton(Context context)
	{
		this(context, null);
	}

	public ProcessButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mNormalDrawable = new StateListDrawable();
		mProgressDrawable = new StateListDrawable();
		mCompleteDrawable = new StateListDrawable();
		mErrorDrawable = new StateListDrawable();
		mNormalText = getText().toString();
		if (null != attrs)
		{
			initAttributes(context, attrs);
		}
		setBackgroundDrawable(mNormalDrawable);
	}

	private void initAttributes(Context context, AttributeSet attrs)
	{
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0);
		if (null == typedArray)
		{
			return;
		}
		try
		{
			float defaultCorner = context.getResources().getDimension(R.dimen.corner_radius);
			mCornerRadius = typedArray.getDimension(R.styleable.ProgressButton_cornerRadius, defaultCorner);
			mLoadingText = typedArray.getString(R.styleable.ProgressButton_textProgress);
			mCompleteText = typedArray.getString(R.styleable.ProgressButton_textComplete);
			mDrawType = typedArray.getInt(R.styleable.ProgressButton_drawType, TYPE_DRAW_DEFAULT);

			mNormalDrawable.addState(new int[] { android.R.attr.state_pressed }, createDrawable(typedArray, TYPE_PRESSED));
			mNormalDrawable.addState(new int[] {}, createDrawable(typedArray, TYPE_NORMAL));
			mProgressDrawable.addState(new int[] {}, createDrawable(typedArray, TYPE_PROGRESS));
			mCompleteDrawable.addState(new int[] {}, createDrawable(typedArray, TYPE_COMPLETE));
			mErrorDrawable.addState(new int[] {}, createDrawable(typedArray, TYPE_ERROR));
		}
		finally
		{
			typedArray.recycle();
		}
	}

	private Drawable createProgressDrawable(TypedArray typedArray)
	{

		GradientDrawable progressDrawable = (GradientDrawable) new GradientDrawable().mutate();
		progressDrawable.setShape(GradientDrawable.RECTANGLE);
		int progressColor = typedArray.getColor(R.styleable.ProgressButton_colorPressed, DEFAULT_PROGRESS_COLOR);
		progressDrawable.setColor(progressColor);
		progressDrawable.setCornerRadius(mCornerRadius);
		return progressDrawable;
	}

	private Drawable createDrawable(TypedArray typedArray, int type)
	{
		Drawable drawable = null;
		switch (type)
		{
		case TYPE_PROGRESS:
			drawable = createProgressDrawable(typedArray);
			break;
		case TYPE_NORMAL:
			drawable = createNormalDrawable(typedArray);
			break;
		case TYPE_PRESSED:
			drawable = createPressedDrawable(typedArray);
			break;
		case TYPE_COMPLETE:
			drawable = createCompleteDrawable(typedArray);
			break;
		case TYPE_ERROR:
			drawable = createErrorDrawable(typedArray);
			break;
		}
		return drawable;
	}

	private Drawable createErrorDrawable(TypedArray typedArray)
	{
		GradientDrawable completeDrawable = (GradientDrawable) new GradientDrawable().mutate();
		completeDrawable.setShape(GradientDrawable.RECTANGLE);
		int completeColor = typedArray.getColor(R.styleable.ProgressButton_colorError, DEFAULT_ERROR_COLOR);
		completeDrawable.setColor(completeColor);
		completeDrawable.setCornerRadius(mCornerRadius);
		return completeDrawable;
	}

	private Drawable createCompleteDrawable(TypedArray typedArray)
	{

		GradientDrawable completeDrawable = (GradientDrawable) new GradientDrawable().mutate();
		completeDrawable.setShape(GradientDrawable.RECTANGLE);
		int completeColor = typedArray.getColor(R.styleable.ProgressButton_colorComplete, DEFAULT_COMPLETE_COLOR);
		completeDrawable.setColor(completeColor);
		completeDrawable.setCornerRadius(mCornerRadius);
		return completeDrawable;
	}

	private Drawable createPressedDrawable(TypedArray typedArray)
	{
		GradientDrawable pressedDrawable = (GradientDrawable) new GradientDrawable().mutate();
		pressedDrawable.setShape(GradientDrawable.RECTANGLE);
		int pressedColor = typedArray.getColor(R.styleable.ProgressButton_colorPressed, DEFAULT_PRESSED_COLOR);
		pressedDrawable.setColor(pressedColor);
		pressedDrawable.setCornerRadius(mCornerRadius);
		return pressedDrawable;
	}

	private Drawable createNormalDrawable(TypedArray typedArray)
	{
		GradientDrawable drawableTop = (GradientDrawable) new GradientDrawable().mutate();
		GradientDrawable drawableBottom = (GradientDrawable) new GradientDrawable().mutate();
		int colorPressed = typedArray.getColor(R.styleable.ProgressButton_colorPressed, DEFAULT_PRESSED_COLOR);
		int colorNormal = typedArray.getColor(R.styleable.ProgressButton_colorNormal, DEFAULT_NORMAL_COLOR);

		drawableTop.setShape(GradientDrawable.RECTANGLE);
		drawableTop.setCornerRadius(mCornerRadius);
		drawableTop.setColor(colorPressed);

		drawableBottom.setShape(GradientDrawable.RECTANGLE);
		drawableBottom.setCornerRadius(mCornerRadius);

		drawableBottom.setColor(colorNormal);
		LayerDrawable drawableNormal = new LayerDrawable(new Drawable[] { drawableTop, drawableBottom });
		return drawableNormal;
	}

	public void setProgress(int progress)
	{
		mProgress = progress;
		if (mProgress == MIN_PROGRESS)
		{
			onNormalState();
		}
		else if (mProgress == MAX_PROGRESS)
		{
			onCompleteState();
		}
		else if (mProgress < MIN_PROGRESS)
		{
			onErrorState();
		}
		else
		{
			onProgress();
		}
		invalidate();
	}

	public CharSequence getNormalText()
	{
		return mNormalText;
	}

	public void setNormalText(CharSequence normalText)
	{
		this.mNormalText = normalText;
	}

	public int getDrawType()
	{
		return mDrawType;
	}

	public void setDrawType(int drawType)
	{
		this.mDrawType = drawType;
	}

	public float getCornerRadius()
	{
		return mCornerRadius;
	}

	public StateListDrawable getNormalDrawable()
	{
		return mNormalDrawable;
	}

	public StateListDrawable getProgressDrawable()
	{
		return mProgressDrawable;
	}

	protected void onErrorState()
	{
		if (getErrorText() != null)
		{
			setText(getErrorText());
		}
		setBackgroundDrawable(getErrorDrawable());
	}

	protected void onProgress()
	{
		if (getLoadingText() != null)
		{
			setText(getLoadingText());
		}
		setBackgroundDrawable(getNormalDrawable());
	}

	public void onCompleteState()
	{
		if (getCompleteText() != null)
		{
			setText(getCompleteText());
		}
		setBackgroundDrawable(getCompleteDrawable());
	}

	public void onNormalState()
	{
		if (getNormalText() != null)
		{
			setText(getNormalText());
		}
		setBackgroundDrawable(getNormalDrawable());
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (mProgress > MIN_PROGRESS && mProgress < MAX_PROGRESS)
		{
			drawProgress(canvas);
		}
		super.onDraw(canvas);
	}

	public void setDrawProgressType(int drawType)
	{
		mDrawType = drawType;
	}

	public void drawProgress(Canvas canvas)
	{
		switch (mDrawType)
		{
		case TYPE_DRAW_DEFAULT:
			drawEndlessProgress(canvas);
			break;
		case TYPE_DRAW_PROGRESS:
			drawLineProgress(canvas);
			break;
		case TYPE_DRAW_PROGRESS_HORIZONAL:
			drawHorizonalProgress(canvas);
			break;
		case TYPE_DRAW_PROGRESS_VERTICAL:
			drawVerticalProgress(canvas);
			break;
		}
	}

	private void drawEndlessProgress(Canvas canvas)
	{
		double indicatorHeightPercent = 0.05; // 5%
		int bottom = (int) (getMeasuredHeight() - getMeasuredHeight() * indicatorHeightPercent);
		if (mProgressBar == null)
		{
			mProgressBar = new CustomProgressBar(this);
			mProgressBar.setBounds(0, bottom, getMeasuredWidth(), getMeasuredHeight());
			mProgressBar.setColorScheme(DEFAULT_ERROR_COLOR, DEFAULT_PRESSED_COLOR, DEFAULT_PROGRESS_COLOR, DEFAULT_COMPLETE_COLOR);
			mProgressBar.start();
		}
		if (getProgress() > 0)
		{
			mProgressBar.draw(canvas);
		}
	}

	private void drawLineProgress(Canvas canvas)
	{
		float scale = (float) getProgress() / (float) getMaxProgress();
		float indicatorWidth = (float) getMeasuredWidth() * scale;

		double indicatorHeightPercent = 0.05; // 5%
		int bottom = (int) (getMeasuredHeight() - getMeasuredHeight() * indicatorHeightPercent);
		getProgressDrawable().setBounds(0, bottom, (int) indicatorWidth, getMeasuredHeight());
		getProgressDrawable().draw(canvas);

	}

	public void setBounds(int left, int top, int right, int bottom)
	{
		mBounds.left = left;
		mBounds.top = top;
		mBounds.right = right;
		mBounds.bottom = bottom;
	}

	private void drawHorizonalProgress(Canvas canvas)
	{
		float scale = (float) getProgress() / (float) getMaxProgress();
		float indicatorWidth = (float) getMeasuredWidth() * scale;

		getProgressDrawable().setBounds(0, 0, (int) indicatorWidth, getMeasuredHeight());
		getProgressDrawable().draw(canvas);
	}

	private void drawVerticalProgress(Canvas canvas)
	{
		float scale = (float) getProgress() / (float) getMaxProgress();
		float indicatorHeight = (float) getMeasuredHeight() * scale;
		getProgressDrawable().setBounds(0, 0, getMeasuredWidth(), (int) indicatorHeight);
		getProgressDrawable().draw(canvas);
	}

	public int getProgress()
	{
		return mProgress;
	}

	public int getMaxProgress()
	{
		return MAX_PROGRESS;
	}

	public int getMinProgress()
	{
		return MIN_PROGRESS;
	}

	public StateListDrawable getCompleteDrawable()
	{
		return mCompleteDrawable;
	}

	public CharSequence getLoadingText()
	{
		return mLoadingText;
	}

	public CharSequence getCompleteText()
	{
		return mCompleteText;
	}

	public void setProgressDrawable(StateListDrawable progressDrawable)
	{
		mProgressDrawable = progressDrawable;
	}

	public void setCompleteDrawable(StateListDrawable completeDrawable)
	{
		mCompleteDrawable = completeDrawable;
	}

	public void setLoadingText(CharSequence loadingText)
	{
		mLoadingText = loadingText;
	}

	public void setCompleteText(CharSequence completeText)
	{
		mCompleteText = completeText;
	}

	public StateListDrawable getErrorDrawable()
	{
		return mErrorDrawable;
	}

	public void setErrorDrawable(StateListDrawable errorDrawable)
	{
		mErrorDrawable = errorDrawable;
	}

	public CharSequence getErrorText()
	{
		return mErrorText;
	}

	public void setErrorText(CharSequence errorText)
	{
		mErrorText = errorText;
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.mProgress = mProgress;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof SavedState)
		{
			SavedState savedState = (SavedState) state;
			mProgress = savedState.mProgress;
			super.onRestoreInstanceState(savedState.getSuperState());
			setProgress(mProgress);
		}
		else
		{
			super.onRestoreInstanceState(state);
		}
	}

	public static class SavedState extends BaseSavedState
	{
		private int mProgress;

		public SavedState(Parcelable parcel)
		{
			super(parcel);
		}

		private SavedState(Parcel in)
		{
			super(in);
			mProgress = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags)
		{
			super.writeToParcel(out, flags);
			out.writeInt(mProgress);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
		{

			@Override
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};
	}

	public static class CustomProgressBar
	{

		// The duration of the animation cycle.
		private static final int ANIMATION_DURATION_MS = 2000;

		// The duration of the animation to clear the bar.
		private static final int FINISH_ANIMATION_DURATION_MS = 1000;

		// Interpolator for varying the speed of the animation.
		private static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

		private final Paint mPaint = new Paint();
		private final RectF mClipRect = new RectF();
		private float mTriggerPercentage;
		private long mStartTime;
		private long mFinishTime;
		private boolean mRunning;

		// Colors used when rendering the animation,
		private int mColor1;
		private int mColor2;
		private int mColor3;
		private int mColor4;
		private View mParent;
		private int[] mColors;
		private Rect mBounds = new Rect();

		public CustomProgressBar(View parent)
		{
			mParent = parent;
		}

		public void setProgressColors(int[] colors)
		{
			mColors = colors;
		}

		void setColorScheme(int color1, int color2, int color3, int color4)
		{
			mColor1 = color1;
			mColor2 = color2;
			mColor3 = color3;
			mColor4 = color4;
		}

		/**
		 * Start showing the progress animation.
		 */
		void start()
		{
			if (!mRunning)
			{
				mTriggerPercentage = 0;
				mStartTime = AnimationUtils.currentAnimationTimeMillis();
				mRunning = true;
				mParent.postInvalidate();
			}
		}

		void draw(Canvas canvas)
		{
			final int width = mBounds.width();
			final int height = mBounds.height();
			final int cx = width / 2;
			final int cy = height / 2;
			boolean drawTriggerWhileFinishing = false;
			int restoreCount = canvas.save();
			canvas.clipRect(mBounds);

			if (mRunning || (mFinishTime > 0))
			{
				long now = AnimationUtils.currentAnimationTimeMillis();
				long elapsed = (now - mStartTime) % ANIMATION_DURATION_MS;
				long iterations = (now - mStartTime) / ANIMATION_DURATION_MS;
				float rawProgress = (elapsed / (ANIMATION_DURATION_MS / 100f));

				// If we're not running anymore, that means we're running through
				// the finish animation.
				if (!mRunning)
				{
					// If the finish animation is done, don't draw anything, and
					// don't repost.
					if ((now - mFinishTime) >= FINISH_ANIMATION_DURATION_MS)
					{
						mFinishTime = 0;
						return;
					}

					// Otherwise, use a 0 opacity alpha layer to clear the animation
					// from the inside out. This layer will prevent the circles from
					// drawing within its bounds.
					long finishElapsed = (now - mFinishTime) % FINISH_ANIMATION_DURATION_MS;
					float finishProgress = (finishElapsed / (FINISH_ANIMATION_DURATION_MS / 100f));
					float pct = (finishProgress / 100f);
					// Radius of the circle is half of the screen.
					float clearRadius = width / 2 * INTERPOLATOR.getInterpolation(pct);
					mClipRect.set(cx - clearRadius, 0, cx + clearRadius, height);
					canvas.saveLayerAlpha(mClipRect, 0, 0);
					// Only draw the trigger if there is a space in the center of
					// this refreshing view that needs to be filled in by the
					// trigger. If the progress view is just still animating, let it
					// continue animating.
					drawTriggerWhileFinishing = true;
				}

				// First fill in with the last color that would have finished drawing.
				if (iterations == 0)
				{
					canvas.drawColor(mColor1);
				}
				else
				{
					if (rawProgress >= 0 && rawProgress < 25)
					{
						canvas.drawColor(mColor4);
					}
					else if (rawProgress >= 25 && rawProgress < 50)
					{
						canvas.drawColor(mColor1);
					}
					else if (rawProgress >= 50 && rawProgress < 75)
					{
						canvas.drawColor(mColor2);
					}
					else
					{
						canvas.drawColor(mColor3);
					}
				}

				// Then draw up to 4 overlapping concentric circles of varying radii, based on how far
				// along we are in the cycle.
				// progress 0-50 draw mColor2
				// progress 25-75 draw mColor3
				// progress 50-100 draw mColor4
				// progress 75 (wrap to 25) draw mColor1
				if ((rawProgress >= 0 && rawProgress <= 25))
				{
					float pct = (((rawProgress + 25) * 2) / 100f);
					drawCircle(canvas, cx, cy, mColor1, pct);
				}
				if (rawProgress >= 0 && rawProgress <= 50)
				{
					float pct = ((rawProgress * 2) / 100f);
					drawCircle(canvas, cx, cy, mColor2, pct);
				}
				if (rawProgress >= 25 && rawProgress <= 75)
				{
					float pct = (((rawProgress - 25) * 2) / 100f);
					drawCircle(canvas, cx, cy, mColor3, pct);
				}
				if (rawProgress >= 50 && rawProgress <= 100)
				{
					float pct = (((rawProgress - 50) * 2) / 100f);
					drawCircle(canvas, cx, cy, mColor4, pct);
				}
				if ((rawProgress >= 75 && rawProgress <= 100))
				{
					float pct = (((rawProgress - 75) * 2) / 100f);
					drawCircle(canvas, cx, cy, mColor1, pct);
				}
				if (mTriggerPercentage > 0 && drawTriggerWhileFinishing)
				{
					// There is some portion of trigger to draw. Restore the canvas,
					// then draw the trigger. Otherwise, the trigger does not appear
					// until after the bar has finished animating and appears to
					// just jump in at a larger width than expected.
					canvas.restoreToCount(restoreCount);
					restoreCount = canvas.save();
					canvas.clipRect(mBounds);
					drawTrigger(canvas, cx, cy);
				}
				// Keep running until we finish out the last cycle.
				ViewCompat.postInvalidateOnAnimation(mParent);
			}
			else
			{
				// Otherwise if we're in the middle of a trigger, draw that.
				if (mTriggerPercentage > 0 && mTriggerPercentage <= 1.0)
				{
					drawTrigger(canvas, cx, cy);
				}
			}
			canvas.restoreToCount(restoreCount);
		}

		private void drawTrigger(Canvas canvas, int cx, int cy)
		{
			mPaint.setColor(mColor1);
			canvas.drawCircle(cx, cy, cx * mTriggerPercentage, mPaint);
		}

		/**
		 * Draws a circle centered in the view.
		 * 
		 * @param canvas
		 *            the canvas to draw on
		 * @param cx
		 *            the center x coordinate
		 * @param cy
		 *            the center y coordinate
		 * @param color
		 *            the color to draw
		 * @param pct
		 *            the percentage of the view that the circle should cover
		 */
		private void drawCircle(Canvas canvas, float cx, float cy, int color, float pct)
		{
			mPaint.setColor(color);
			canvas.save();
			canvas.translate(cx, cy);
			float radiusScale = INTERPOLATOR.getInterpolation(pct);
			canvas.scale(radiusScale, radiusScale);
			canvas.drawCircle(0, 0, cx, mPaint);
			canvas.restore();
		}

		/**
		 * Set the drawing bounds of this SwipeProgressBar.
		 */
		void setBounds(int left, int top, int right, int bottom)
		{
			mBounds.left = left;
			mBounds.top = top;
			mBounds.right = right;
			mBounds.bottom = bottom;
		}
	}
}

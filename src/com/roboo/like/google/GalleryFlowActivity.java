package com.roboo.like.google;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.roboo.like.google.utils.BitmapUtils;

/** 画廊 */
public class GalleryFlowActivity extends BaseLayoutActivity
{
	private GalleryFlow mGallery = null;
	private ArrayList<BitmapDrawable> mBitmaps = new ArrayList<BitmapDrawable>();

	public static void actionGalleryFlow(Activity activity)
	{
		Intent intent = new Intent(activity, GalleryFlowActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_flow);
		customActionBar();
		initView();
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("画廊");
		mActionBar.setLogo(R.drawable.ic_abs_mood_up);
	}

	@Override
	public void initView()
	{
		generateBitmaps();
		mGallery = (GalleryFlow) findViewById(R.id.gallery_flow);
		mGallery.setBackgroundColor(Color.GRAY);
		mGallery.setSpacing(50);
		mGallery.setFadingEdgeLength(0);
		mGallery.setGravity(Gravity.CENTER_VERTICAL);
		mGallery.setAdapter(new GalleryAdapter());
		OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
		findViewById(R.id.space_confirm_btn).setOnClickListener(onClickListenerImpl);
		findViewById(R.id.max_zoom_confirm_btn).setOnClickListener(onClickListenerImpl);
		findViewById(R.id.max_rotate_angle_confirm_btn).setOnClickListener(onClickListenerImpl);

	}

	private void generateBitmaps()
	{
		int[] ids = { R.drawable.ic_test, R.drawable.ic_test2, R.drawable.ic_test1, R.drawable.ic_test3, R.drawable.ic_test1, R.drawable.ic_test2, R.drawable.ic_test1, R.drawable.ic_test3, R.drawable.ic_test1, R.drawable.ic_test2, R.drawable.ic_test1, R.drawable.ic_test3, R.drawable.ic_test1 };

		for (int id : ids)
		{
			Bitmap bitmap = createReflectedBitmapById(id);
			if (null != bitmap)
			{
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				drawable.setAntiAlias(true);
				mBitmaps.add(drawable);
			}
		}
	}

	private Bitmap createReflectedBitmapById(int resId)
	{
		Drawable drawable = getResources().getDrawable(resId);
		if (drawable instanceof BitmapDrawable)
		{
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			Bitmap reflectedBitmap = BitmapUtils.createReflectedBitmap(bitmap);

			return reflectedBitmap;
		}

		return null;
	}

	private class OnClickListenerImpl implements OnClickListener
	{

		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.space_confirm_btn:
				onSpaceBtnClick(v);
				break;

			case R.id.max_zoom_confirm_btn:
				onMaxZoomBtnClick(v);
				break;
			case R.id.max_rotate_angle_confirm_btn:
				onMaxAngleBtnClick(v);
				break;
			}
		}

		private void onSpaceBtnClick(View v)
		{
			EditText editText = (EditText) findViewById(R.id.space_edittext);
			String text = editText.getText().toString();

			try
			{
				int spacing = Integer.parseInt(text);
				if (spacing >= -60 && spacing <= 60)
				{
					mGallery.setSpacing(spacing);
					((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
				}
				else
				{}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private void onMaxZoomBtnClick(View v)
		{
			EditText editText = (EditText) findViewById(R.id.max_zoom_edittext);
			String text = editText.getText().toString();

			try
			{
				int maxZoom = Integer.parseInt(text);
				if (maxZoom >= -120 && maxZoom <= 120)
				{
					mGallery.setMaxZoom(maxZoom);
					((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
				}
				else
				{

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private void onMaxAngleBtnClick(View v)
		{
			EditText editText = (EditText) findViewById(R.id.max_rotate_angle_edittext);
			String text = editText.getText().toString();

			try
			{
				int maxRotationAngle = Integer.parseInt(text);
				if (maxRotationAngle >= -60 && maxRotationAngle <= 60)
				{
					mGallery.setMaxRotationAngle(maxRotationAngle);
					((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
				}
				else
				{

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private class GalleryAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mBitmaps.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (null == convertView)
			{
				convertView = new MyImageView(GalleryFlowActivity.this);
				convertView.setLayoutParams(new Gallery.LayoutParams(110, 184));
			}

			ImageView imageView = (ImageView) convertView;
			imageView.setImageDrawable(mBitmaps.get(position));
			imageView.setScaleType(ScaleType.FIT_XY);

			return imageView;
		}
	}

	private class MyImageView extends ImageView
	{
		public MyImageView(Context context)
		{
			this(context, null);
		}

		public MyImageView(Context context, AttributeSet attrs)
		{
			super(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle)
		{
			super(context, attrs, defStyle);
		}

		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
		}
	}
}

class GalleryFlow extends Gallery
{
	/**
	 * The camera class is used to 3D transformation matrix.
	 */
	private Camera mCamera = new Camera();

	/**
	 * The max rotation angle.
	 */
	private int mMaxRotationAngle = 60;

	/**
	 * The max zoom value (Z axis).
	 */
	private int mMaxZoom = -120;

	/**
	 * The center of the gallery.
	 */
	private int mCoveflowCenter = 0;

	public GalleryFlow(Context context)
	{
		this(context, null);
	}

	public GalleryFlow(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// Enable set transformation.
		this.setStaticTransformationsEnabled(true);
		// Enable set the children drawing order.
		this.setChildrenDrawingOrderEnabled(true);
	}

	public int getMaxRotationAngle()
	{
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle)
	{
		mMaxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom()
	{
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom)
	{
		mMaxZoom = maxZoom;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i)
	{
		// Current selected index.
		int selectedIndex = getSelectedItemPosition() - getFirstVisiblePosition();
		if (selectedIndex < 0)
		{
			return i;
		}

		if (i < selectedIndex)
		{
			return i;
		}
		else if (i >= selectedIndex)
		{
			return childCount - 1 - i + selectedIndex;
		}
		else
		{
			return i;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private int getCenterOfView(View view)
	{
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t)
	{
		super.getChildStaticTransformation(child, t);

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();

		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		// If the child is in the center, we do not rotate it.
		if (childCenter == mCoveflowCenter)
		{
			transformImageBitmap(child, t, 0);
		}
		else
		{
			// Calculate the rotation angle.
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

			// Make the angle is not bigger than maximum.
			if (Math.abs(rotationAngle) > mMaxRotationAngle)
			{
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
			}

			transformImageBitmap(child, t, rotationAngle);
		}

		return true;
	}

	private int getCenterOfCoverflow()
	{
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	private void transformImageBitmap(View child, Transformation t, int rotationAngle)
	{
		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getHeight();
		final int imageWidth = child.getWidth();
		final int rotation = Math.abs(rotationAngle);

		// Zoom on Z axis.
		mCamera.translate(0, 0, mMaxZoom);

		if (rotation < mMaxRotationAngle)
		{
			float zoomAmount = (float) (mMaxZoom + rotation * 1.5f);
			mCamera.translate(0, 0, zoomAmount);
		}

		// Rotate the camera on Y axis.
		mCamera.rotateY(rotationAngle);
		// Get the matrix from the camera, in fact, the matrix is S (scale) transformation.
		mCamera.getMatrix(imageMatrix);

		// The matrix final is T2 * S * T1, first translate the center point to (0, 0),
		// then scale, and then translate the center point to its original point.
		// T * S * T

		// S * T1
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		// (T2 * S) * T1
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

		mCamera.restore();
	}
}

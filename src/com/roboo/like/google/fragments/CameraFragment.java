package com.roboo.like.google.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.roboo.like.google.R;
import com.roboo.like.google.async.BitmapAsyncTaskLoader;
import com.roboo.like.google.views.PhotoView;

public class CameraFragment extends BaseFragment implements LoaderCallbacks<Bitmap>
{
	private static final String INCLUDE_IMAGE_PATH_URI="image_uri";
	private ProgressBar mProgressBar;
	private PhotoView mPhotoView;

	public static CameraFragment newInstance(Uri uri)
	{
		CameraFragment fragment = new CameraFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(INCLUDE_IMAGE_PATH_URI, uri);
		fragment.setArguments(bundle );
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_camera, null);
		mPhotoView = (PhotoView) view.findViewById(R.id.pv_image);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getActivity().getSupportLoaderManager().initLoader(1, getArguments(), this);
	}

	public Loader<Bitmap> onCreateLoader(int id, Bundle args)
	{
		Uri dataUri = args.getParcelable(INCLUDE_IMAGE_PATH_URI);
		return new BitmapAsyncTaskLoader(getActivity(), dataUri);
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> loader,Bitmap data)
	{
		mProgressBar.setVisibility(View.GONE);
		System.out.println("data = " + data);
		mPhotoView.setImageBitmap(data);
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> loader)
	{}
 
}

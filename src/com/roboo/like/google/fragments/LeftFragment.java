package com.roboo.like.google.fragments;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.roboo.like.google.CameraActivity;
import com.roboo.like.google.ContacterActivity;
import com.roboo.like.google.MainActivity;
import com.roboo.like.google.PictureActivity;
import com.roboo.like.google.R;
import com.roboo.like.google.UserActivity;

public class LeftFragment extends BaseFragment
{
	private static final String DUMMY_USERNAME = "haikuo wuya \r\nhaikuowuya@gmail.com";
	private static final int REQUEST_CAMERA_CODE = 1334;
	private ListView mListView;
	private Button mBtnUsername;

	public static LeftFragment newInstance()
	{
		LeftFragment fragment = new LeftFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_left, null);
		mListView = (ListView) view.findViewById(R.id.lv_list);
		mBtnUsername = (Button) view.findViewById(R.id.btn_username);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mListView.setAdapter(getAdapter());
		mBtnUsername.setText(DUMMY_USERNAME);
		setListener();
	}

	private void setListener()
	{
		mListView.setOnItemClickListener(new OnItemClickListenerImpl());
		mBtnUsername.setOnClickListener(new onClickListenerImpl());
	}

	private BaseAdapter getAdapter()
	{
		LinkedList<HashMap<String, String>> data = new LinkedList<HashMap<String, String>>();
		String[] texts = getResources().getStringArray(R.array.left_drawer_text_arrays);
		String[] from = new String[] { "image", "text" };
		for (int i = 0; i < texts.length; i++)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			String[] tmp = texts[i].split("#");
			hashMap.put(from[0], getResources().getIdentifier(tmp[0], "drawable", getActivity().getPackageName()) + "");
			hashMap.put(from[1], tmp[1]);
			data.add(hashMap);
		}
		int[] to = new int[] { R.id.iv_image, R.id.tv_text };
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.drawer_list_item, from, to);
		return simpleAdapter;
	}

	private class onClickListenerImpl implements OnClickListener
	{
		public void onClick(View v)
		{
			MainActivity mainActivity = (MainActivity) getActivity();
			UserActivity.actionUser(mainActivity);
			mainActivity.closeLeftDrawer();
		}

	}

	private class OnItemClickListenerImpl implements OnItemClickListener
	{
		MainActivity mainActivity = (MainActivity) getActivity();

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			switch (position)
			{
			case 1:// 人脉
				ContacterActivity.actionContacter(mainActivity);
				break;
			case 2:// 照片
				PictureActivity.actionPicture(mainActivity);
				break;
			case 3:// 拍照
					// CameraActivity.actionCamera(getActivity());
				startCamera();
			}
			mainActivity.closeLeftDrawer();
		}
	}

	private void startCamera()
	{
		startActivityForResult(createCameraIntent(), REQUEST_CAMERA_CODE);
	}

	/** 拍照Intent */
	private Intent createCameraIntent()
	{
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 拍照
		Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);// 选择图片文件
		imageIntent.setType("image/*");
		return cameraIntent;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		 
		CameraActivity.actionCamera(getActivity(),data.getData());
		super.onActivityResult(requestCode, resultCode, data);
	}

}

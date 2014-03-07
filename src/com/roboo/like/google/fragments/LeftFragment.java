package com.roboo.like.google.fragments;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.roboo.like.google.R;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LeftFragment extends BaseFragment
{
	private static final String DUMMY_USERNAME = "haikuo wuya \r\nhaikuowuya@gmail.com";
	private ListView mListView;
	private TextView mTvUsername;

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
		mTvUsername = (TextView) view.findViewById(R.id.tv_username);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

		super.onActivityCreated(savedInstanceState);
		mListView.setAdapter(getAdapter());
		mTvUsername.setText(DUMMY_USERNAME);
	}

	private BaseAdapter getAdapter()
	{
		LinkedList<HashMap<String, String>> data = new LinkedList<HashMap<String,String>>();
		String[] texts = getResources().getStringArray(R.array.left_drawer_text_arrays);
		String[] from = new String[]{"image","text"}; 
		for(int i = 0;i < texts.length;i++)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			String[] tmp = texts[i].split("#");
			hashMap.put(from[0], getResources().getIdentifier(tmp[0],"drawable",getActivity().getPackageName())+"");
			hashMap.put(from[1], tmp[1]);
			data.add(hashMap);
		}
			
		int[] to = new int[]{R.id.iv_image,R.id.tv_text};
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.drawer_list_item, from, to);
		return simpleAdapter;
	}

}

package com.roboo.like.google.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roboo.like.google.R;
import com.roboo.like.google.adapters.StickyGridAdapter;
import com.roboo.like.google.async.PictureAsyncTaskLoader;
import com.roboo.like.google.models.PictureItem;
import com.roboo.like.google.views.StickyGridHeadersGridView;

public class PictureGridFragment extends BaseWithProgressFragment implements LoaderCallbacks<LinkedList<PictureItem>>
{
	private StickyGridHeadersGridView mSGHGridView;

	public static PictureGridFragment newInstance()
	{
		PictureGridFragment fragment = new PictureGridFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_picture_grid, null);//TODO
		mSGHGridView = (StickyGridHeadersGridView) view.findViewById(R.id.sghgv_gridview);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getActivity().getSupportLoaderManager().initLoader(1, null, this);
	}

	public Loader<LinkedList<PictureItem>> onCreateLoader(int id, Bundle args)
	{
		return new PictureAsyncTaskLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<LinkedList<PictureItem>> loader, LinkedList<PictureItem> data)
	{
		if (null != data)
		{
			LinkedList<PictureItem> dataWrapper = generateHeaderId(data);
			Collections.sort(dataWrapper, new YMDComparator());	// 排序
			mSGHGridView.setAdapter(new StickyGridAdapter(getActivity(), dataWrapper));
		}
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<LinkedList<PictureItem>> loader)
	{}

	/**
	 * 对GridView的Item生成HeaderId, 根据图片的添加时间的年、月、日来生成HeaderId 年、月、日相等HeaderId就相同
	 * 
	 * @param nonHeaderIdList
	 * @return
	 */
	private LinkedList<PictureItem> generateHeaderId(LinkedList<PictureItem> nonHeaderIdList)
	{
		Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
		int mHeaderId = 1;
		LinkedList<PictureItem> hasHeaderIdList;
		for (ListIterator<PictureItem> it = nonHeaderIdList.listIterator(); it.hasNext();)
		{
			PictureItem item = it.next();
			String ymd = item.getTime();
			if (!mHeaderIdMap.containsKey(ymd))
			{
				item.setHeaderId(mHeaderId);
				mHeaderIdMap.put(ymd, mHeaderId);
				mHeaderId++;
			}
			else
			{
				item.setHeaderId(mHeaderIdMap.get(ymd));
			}
		}
		hasHeaderIdList = nonHeaderIdList;

		return hasHeaderIdList;
	}

	public class YMDComparator implements Comparator<PictureItem>
	{
		public int compare(PictureItem o1, PictureItem o2)
		{
			return o1.getTime().compareTo(o2.getTime());
		}
	}

}

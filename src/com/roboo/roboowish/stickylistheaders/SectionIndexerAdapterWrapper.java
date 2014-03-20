package com.roboo.roboowish.stickylistheaders;

import android.content.Context;
import android.widget.SectionIndexer;

import com.roboo.like.google.adapters.StickyHeadersAdapter;

public class SectionIndexerAdapterWrapper extends
		AdapterWrapper implements SectionIndexer {
	
	final SectionIndexer mSectionIndexerDelegate;

	public SectionIndexerAdapterWrapper(Context context,
			StickyHeadersAdapter delegate) {
		super(context, delegate);
		mSectionIndexerDelegate = (SectionIndexer) delegate;
	}

	@Override
	public int getPositionForSection(int section) {
		return mSectionIndexerDelegate.getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return mSectionIndexerDelegate.getSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return mSectionIndexerDelegate.getSections();
	}

}

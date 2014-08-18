package com.roboo.like.google.view.bounce;

import com.roboo.like.google.view.bounce.BounceScroller.State;

public interface BounceListener {

	public void onState(boolean header, State state);

	public void onOffset(boolean header, int offset);

}

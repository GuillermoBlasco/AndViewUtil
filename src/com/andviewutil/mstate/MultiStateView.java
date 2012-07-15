package com.andviewutil.mstate;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This abstract class manages the states of a view.
 * @author GuillermoBlascoJimenez
 * @version 1.1 MultiStateView
 * @version 1.0 AndViewUtil
 */
public abstract class MultiStateView extends MultiStateObject<View>{	
	
	/**
	 * Builds with initial unknown state.
	 * @param view View object which state is managed.
	 */
	public MultiStateView(View view){
		this(view,UNKNOWN_STATE);
	}
	/**
	 * Builds with a given initial state.
	 * @param view View object which state is managed.
	 * @param starting_state Initial state.
	 */
	public MultiStateView(View view, int starting_state){
		super(view, starting_state);
		view.setOnTouchListener(new OnTouchListener(){

			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch(arg1.getAction()){
					case(MotionEvent.ACTION_DOWN):
						performActionDown();
						break;
					case(MotionEvent.ACTION_CANCEL):
					case(MotionEvent.ACTION_OUTSIDE):
						performActionCancel();
						break;
					case(MotionEvent.ACTION_UP):
						performActionUp();
						break;
				}
				return true;	
			};		
		});
	}
	
	
}

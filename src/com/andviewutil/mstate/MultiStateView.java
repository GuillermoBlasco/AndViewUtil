//	LICENSE:
//	MultiStateView.java is part of AndViewUtil.
//
//	AndViewUtil is free software: you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation, either version 3 of the License, or
//	(at your option) any later version.
//
//	AndViewUtil is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with AndViewUtil.  If not, see <http://www.gnu.org/licenses/>.
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

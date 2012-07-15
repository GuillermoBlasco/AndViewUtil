package com.andviewutil.picker;

import java.util.EventListener;

/**
 * Listener of picker events.
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public interface OnPickerEventListener extends EventListener{
	
	/**
	 * Enumeration of causes why an element has been picked. 
	 */
	public enum Cause{
		/**Cause is onPrev call, namely down button pressed*/
		ON_PREV,
		/**Cause is onNext call, namely up button pressed*/
		ON_NEXT,
		/**Cause is onLongClickPrev, namely down button pressed for a long time*/
		ON_PREV_LONG_CLICK,
		/**Cause is onLongClickNext, namely up button pressed for a long time*/
		ON_NEXT_LONG_CLICK,
		/**Cause is user edits element picked and it is valid*/
		ON_EDIT_WELL,
		/**Cause is user edits element picked but it is wrong*/
		ON_EDIT_WRONG,
		/**Cause is not associated with any user action*/
		INTERNAL;
	}
	
	/**
	 * Called when some element has been picked. Is called before view
	 *  actualization.
	 * @param index Index of element picked.
	 * @param cause Cause why element has been picked.
	 */
	public void onElementPicked(int index, Cause cause);
	/**
	 * Called when view object associated with picker has been actualized. It
	 * is called when an element is picked.
	 * @param index Index of element picked on actualization.
	 */
	public void onActualize(int index);
}

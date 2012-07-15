//	LICENSE:
//	Picker.java is part of AndViewUtil.
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
package com.andviewutil.picker;

import java.io.Serializable;
import java.util.LinkedList;

import com.andviewutil.picker.OnPickerEventListener.Cause;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * All pickers extends this class. Provides the data manegament to run a picker
 * except the graphical output.
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public abstract class Picker implements Serializable{

	private static final long serialVersionUID = 12L;
	
	// PROTECTED ATTRIBUTES
	
	/**Up button*/
	protected transient View up_button;
	/**Down button*/
	protected transient View down_button;
	
	// PRIVATE ATTRIBUTES
	
	/*boundaries, current var will be between [min, max] both included*/
	private int max = 99;
	private int min = 0;
	/*current index*/
	private int index = min;
	/*Cyclic means that when max is reached next element will be min*/
	private boolean cyclic = true;
	/*delay between index increment or decrement on long click*/
	private int long_click_refresh_delay = 300; //ms
	/*on long click runnable tasks to be posted*/
	private final Runnable up_long_click = new Runnable(){
		public void run() {
			Picker.this.onLongClickNext();
		}
	};
	private final Runnable down_long_click = new Runnable(){
		public void run() {
			Picker.this.onLongClickPrev();
		}
	};
	
	private LinkedList<OnPickerEventListener> listeners;
	
	// PUBLIC CONSTRUCTOR
	
	/**
	 * Sole Picker constructor.
	 * @param up_button The view with the function of up.
	 * @param down_button The view with the function of down.
	 */
	public Picker(View up_button, View down_button) {
		super();
		this.listeners = new LinkedList<OnPickerEventListener>();
		this.up_button = up_button;
		this.down_button = down_button;
		setListeners(); //set listeners to both buttons
	}
	
	// PUBLIC METHODS
	
	/**
	 * Adds a listener to picker.
 	 * @param listener Listener to add.
	 */
	public void setOnPickerEventListener(OnPickerEventListener listener){
		synchronized(listeners){
			if(!listeners.contains(listener))
				listeners.add(listener);
		}
	}
	/**
	 * Removes the given listener.
	 * @param listener Listener to remove.
	 */
	public void removeListener(Object listener){
		synchronized(listeners){
			listeners.remove(listener);
		}
	}
	/**
	 * Returns maximum index.
	 * @return maximum index.
	 */
	public int getMax(){
		return max;
	}
	/**
	 * Returns minimum index.
	 * @return Minimum index.
	 */
	public int getMin(){
		return min;
	}
	/**
	 * Sets if picker behavior is cyclic or non-cyclic.
	 * @param cyclic true if behavior should be cyclic, false if not.
	 */
	public void setCyclic(boolean cyclic){
		this.cyclic = cyclic;
	}
	/**
	 * Returns cyclic behavior of picker. 
	 * @return true if behavior is cyclic, false otherwise.
	 */
	public boolean isCyclic(){
		return cyclic;
	}
	/**
	 * Sets current index of picker. Picker checks the given value and actualizes.
	 * @param value New index value.
	 */
	public void setIndex(int value){
		this.setIndex(value, true);
	}
	/**
	 * Returns current index of picker.
	 * @return Current index.
	 */
	public int getIndex(){
		return index;
	}
	/**
	 * Sets delay between decrements on long clicks.
	 * @param delay Milliseconds of delay.
	 */
	public void setLongClickRefreshDelay(int delay){
		this.long_click_refresh_delay = delay;
	}
	/**
	 * Gets delay between decrements on long clicks.
	 * @returndsa0 Milliseconds of delay.
	 */
	public int getLongClickRefreshDelay(){
		return this.long_click_refresh_delay;
	}
	/**
	 * Actualizes view.
	 */
	public void actualizeView(){
		this.actualize();
		pickerActualizedPerformed();
	}
	/**
	 * Returns the view with up function.
	 * @return View object with up function associated.
	 */
	public View getUpButton(){
		return this.up_button;
	}
	/**
	 * Returns the view with down function.
	 * @return View object with down function associated.
	 */
	public View getDownButton(){
		return this.down_button;
	}
	/**
	 * Sets the view with down function.
	 * @param down_button View object with down function associated.
	 */
	public void setDownButton(View down_button){
		this.down_button = down_button;
	}
	/**
	 * Sets the view with up function.
	 * @param up_button View object with up function associated.
	 */
	public void setUpButton(View up_button){
		this.up_button = up_button;
	}
	
	// PROTECTED METHODS
	

	/**
	 * Called when up button is clicked. Increments index, checks it and
	 * actualizes.
	 */
	protected void onNext() {
		elementPickedPerformed(OnPickerEventListener.Cause.ON_NEXT);
		this.setIndex(index+1, false);
	}
	/**
	 * Called when down button is clicked. Decrements index, checks it and
	 * actualizes.
	 */
	protected void onPrev() {
		elementPickedPerformed(OnPickerEventListener.Cause.ON_PREV);
		this.setIndex(index-1, false);
	}
	/**
	 * Called when up button is long clicked. Posts on handler onNext() while
	 * button is pressed.
	 */
	protected void onLongClickNext(){
		if(up_button.isPressed()){
			elementPickedPerformed(OnPickerEventListener.Cause.ON_NEXT_LONG_CLICK);
			this.setIndex(index+1, false);
			this.up_button.getHandler().postDelayed(up_long_click, this.long_click_refresh_delay);
		}
	}
	/**
	 * Called when down button is long clicked. Posts on handler onPrev() while
	 * button is pressed.
	 */
	protected void onLongClickPrev(){
		if(down_button.isPressed()){
			elementPickedPerformed(OnPickerEventListener.Cause.ON_PREV_LONG_CLICK);
			this.setIndex(index-1, false);
			this.down_button.getHandler().postDelayed(down_long_click, this.long_click_refresh_delay);
		}
	}
	/**
	 * Sets maximum index if given maximum index is greater than minimum index.
	 * @param max Maximum index.
	 */
	protected void setMax(int max){
		if(max > min)
			this.max = max;
	}
	/**
	 * Sets minimum index if given minimum index is greater than maximum index.
	 * @param min Minimum index.
	 */
	protected void setMin(int min){
		if(min < max)
			this.min = min;
	}
	/**
	 * Checks given value and once it is checked sets it as current index.
	 * @param new_value New value to set as index.
	 * @return Checked index.
	 */
	protected boolean check(int new_value){
		if(cyclic)
			if(new_value > max) new_value = min + (Math.abs(--new_value)%(max));
			else if(new_value < min) new_value = max - (Math.abs(++new_value)%(max));
		else
			if(new_value > max) new_value = max;
			else if(new_value < min) new_value = min;
		boolean ret = index != new_value;
		index = new_value;
		return ret;
	}
	/**
	 * Sets a new index. Given index is checked and if needed view is actualized.
	 * If the method call is not associated to any user action it must be internal
	 * and onElementPicked will be fired. If method call is associated to some
	 * user action then this listener call must not be done.
	 * @param value New value to set.
	 * @param is_internal true if call is not associated to user action, false 
	 * otherwise.
	 */
	protected void setIndex(int value, boolean is_internal){
		if(is_internal)
			elementPickedPerformed(OnPickerEventListener.Cause.INTERNAL);
		if(this.check(value))
			this.actualizeView();
	}
	/**
	 * Fires onElementPicked method on all listeners. 
	 * @param cause Cause of this event.
	 */
	protected void elementPickedPerformed(Cause cause) {
		OnPickerEventListener[] aux = listeners.toArray(new OnPickerEventListener[listeners.size()]);
		Log.d("P", "element picked " + cause.toString());
		for(int i = 0; i < aux.length; i++)
			aux[i].onElementPicked(this.getIndex(),cause);
	}
	/**
	 * Fires onActualize method on all listeners.
	 */
	private void pickerActualizedPerformed() {
		Log.d("P", "element actualized");
		OnPickerEventListener[] aux = listeners.toArray(new OnPickerEventListener[listeners.size()]);
		for(int i = 0; i < aux.length; i++)
			aux[i].onActualize(this.getIndex());
	}
	
	// PRIVATE METHODS
	/*
	 * Sets listeners on up and down buttons.
	 */
	private void setListeners(){
		// onNext case
		up_button.setOnClickListener( new OnClickListener(){
			public void onClick(View arg0) {
				Picker.this.onNext();
			}
		});
		// onPrev case
		down_button.setOnClickListener( new OnClickListener(){
			public void onClick(View arg0) {
				Picker.this.onPrev();
			}
		});
		// onLongClickNext case
		up_button.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick(View arg0) {
				Picker.this.up_button.getHandler().post(up_long_click);
				return false;
			}
			
		});
		// onLongClickPrev case
		down_button.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick(View arg0) {
				Picker.this.down_button.getHandler().post(down_long_click);
				return false;
			}
		});
	}
	
	
	// ABSTRACT PROTECTED METHODS
	
	/**
	 * Actualizes graphically picker. Called automatically when up and down 
	 * buttons are clicked. If necessary actualize view safely then use
	 * actualizeView(), this method will fire actualize event automatically.
	 */
	protected abstract void actualize();
	
}

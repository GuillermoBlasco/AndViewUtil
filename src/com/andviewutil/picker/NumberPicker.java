//	LICENSE:
//	NumberPicker.java is part of AndViewUtil.
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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

/**
 * This picker will show numbers in range [min, max] both included. 
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public class NumberPicker extends Picker{

	// PRIVATE ATTRIBUTES
	
	private static final long serialVersionUID = 12L;
	/*TextView where info is shown*/
	private transient TextView text;
	/*flag to avoid recursive call on edit text*/
	private boolean internal_edit = false;
	/*max string size*/
	private int max_string_size;
	// PUBLIC CONSTRUCTOR
	
	/**
	 * Sole constructor. Bounds are set by default. setBounds(..) should be
	 * called after constructor to set bounds.
	 * @param up_button View with up function.
	 * @param down_button View with down function.
	 * @param text TextView where show information.
	 */
	public NumberPicker(View up_button, View down_button, TextView text) {
		super(up_button, down_button);
		this.text = text;
		text.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable arg0) {
				NumberPicker.this.onTextEdit(arg0.toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
		});
	}

	// PUBLIC METHODS
	
	/**
	 * Sets bounds for this picker. min must be lesser than max.
	 * @param min Minimum value.
	 * @param max Maximum value.
	 */
	public void setBounds(int min, int max){
		if(min < max){
			super.setMin(min);
			super.setMax(max);
			max_string_size = Integer.toString(max).length();
		}
	}
	/**
	 * Returns the output text view.
	 * @return Output text view.
	 */
	public TextView getTextView(){
		return text;
	}
	/**
	 * Sets the output text view.
	 * @param text Output text view.
	 */
	public void setTextView(TextView text){
		this.text = text;
	}
	/**
	 * Asks if has output text view.
	 * @return true if has, false if not.
	 */
	public boolean hasTextView(){
		return this.text != null;
	}
	
	// PROTECTED METHODS

	/**
	 * Called when text has been edited. 
	 * @param input New string.
	 */
	protected void onTextEdit(String input) {
		//if is a internal edit ignore it to avoid recursive call.
		if(!this.internal_edit) try{
			int new_value = Integer.valueOf(input);
			if(new_value > super.getMax() || new_value < super.getMin())
				super.elementPickedPerformed(OnPickerEventListener.Cause.ON_EDIT_WRONG);
			else
				super.elementPickedPerformed(OnPickerEventListener.Cause.ON_EDIT_WELL);
			if(super.check(new_value) || new_value != super.getIndex())
				this.actualizeView();				
		} catch(Exception e){}
	}
	/**
	 * Actualizes the TextView object.
	 */
	@Override
	protected void actualize() {
		if(!hasTextView())
			throw new NullPointerException("Can't actualize without output text view");
		this.internal_edit = true;
		String show =Integer.toString(super.getIndex());
		while(show.length() < this.max_string_size)
			show = "0" + show;
		this.text.setText(show);
		this.internal_edit = false;
	}

}

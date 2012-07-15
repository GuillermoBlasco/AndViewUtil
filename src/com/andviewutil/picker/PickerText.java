package com.andviewutil.picker;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
/**
 * Picks elements of a given array. Useful picker if you need to show objects
 * instead of numbers. Objects are displayed as Strings.
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public class PickerText extends ElementPicker {

	// PRIVATE ATTRIBUTES
	
	private static final long serialVersionUID = 12L;
	/*TextView where element is displayed*/
	private transient TextView text;
	/*flag to avoid recursive call on edit text*/
	private boolean internal_edit = false;
	
	// PUBLIC CONSTRUCTOR
	
	/**
	 * PickerText sole constructor.
	 * @param text TextView object where elements are displayed.
	 * @param up_button View with up function.
	 * @param down_button View with down function.
	 * @param elements Elements to pick.
	 */
	public PickerText(View up_button, View down_button, TextView text, Object[] elements) {
		super(up_button, down_button, elements);
		this.text = text;
		text.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable arg0) {
				PickerText.this.onTextEdit(arg0.toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
		});
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
	// PROTECTED METHOD
	
	/**
	 * Called when text has been edited. 
	 * @param input New string.
	 */
	protected void onTextEdit(String input) {
		if(!this.internal_edit){
			for(int i = 0; i < super.getElements().length; i++)
				if(super.getElements()[i].toString().equals(input) && super.check(i)){
					super.setIndex(i, false);
					super.elementPickedPerformed(OnPickerEventListener.Cause.ON_EDIT_WELL);
					return;
				}
			super.elementPickedPerformed(OnPickerEventListener.Cause.ON_EDIT_WRONG);
			this.actualizeView();
		}
	}
	/**
	 * Actualizes the TextView object.
	 */
	@Override
	protected void actualize() {
		if(!hasTextView())
			throw new NullPointerException("Can't actualize without output text view");
		this.internal_edit = true;
		this.text.setText(super.getElements()[getIndex()].toString());
		this.internal_edit = false;
	}
}

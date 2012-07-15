package com.andviewutil.picker;

import android.view.View;

/**
 * A picker that picks elements from an array of elements.
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public abstract class ElementPicker extends Picker {

	// PRIVATE ATTRIBUTES
	
	private static final long serialVersionUID = 12L;
	/*Elements to pick*/
	private Object[] elements;
	
	// PUBLIC CONSTRUCTOR
	
	/**
	 * Sole constructor. Picker dimension is fixed.
	 * @param up_button The view with the function of up.
	 * @param down_button The view with the function of down.
	 * @param elements Elements to pick.
	 */
	public ElementPicker(View up_button, View down_button, Object[] elements) {
		super(up_button, down_button);
		this.elements = elements;
		super.setMax(elements.length-1);
		super.setMin(0);
	}

	// PUBLIC METHODS

	/**
	 * Returns the current element picked.
	 * @return Current element.
	 */
	public Object getCurrentElement() {
		return elements[getIndex()];
	}
	/**
	 * Returns all elements.
	 * @return All elements.
	 */
	public Object[] getElements(){
		return elements;
	}
	public void setElements(Object[] elements){
		this.elements = elements;
	}
}

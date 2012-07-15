package com.andviewutil.picker;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
/**
 * Picks elements of a given array. Useful picker if you need to show objects
 * instead of numbers. Objects are displayed as images.
 * @author GuillermoBlascoJimenez
 * @version 1.1 Picker
 * @version 1.2 AndViewUtils
 */
public class PickerImage extends ElementPicker{
	
	private enum ImageType {ID_ARRAY, DRAWABLE_ARRAY;}
	
	// PRIVATE ATTRIBUTES
	
	private static final long serialVersionUID = 12L;
	/*ImageView where images are drawn*/
	private transient ImageView view;
	/*Identifier of resources*/
	private int[] ids;
	/*Drawable objects*/
	private transient Drawable[] images;
	/*output type*/
	private ImageType image_type;
	
	// PUBLIC CONSTRUCTORS
	
	/**
	 * Builds a PickerImage using resources.
	 * @param view ImageView where image is displayed.
	 * @param up_button View with up function.
	 * @param down_button View with down function.
	 * @param elements Elements to pick.
	 * @param images_id Resource ID of elements.
	 */
	public PickerImage(ImageView view, View up_button, View down_button, Object[] elements, int[] images_id) {
		super(up_button, down_button, elements);
		this.view = view;
		this.ids = images_id;
		this.images = null;
		this.image_type = ImageType.ID_ARRAY;
	}
	/**
	 * Builds a PickerImage using an array of Drawable objects.
	 * @param view ImageView where image is displayed.
	 * @param up_button View with up function.
	 * @param down_button View with down function.
	 * @param elements Elements to pick.
	 * @param images Drawable objects to show on element picked.
	 */
	public PickerImage(ImageView view, View up_button, View down_button, Object[] elements, Drawable[] images) {
		super(up_button, down_button, elements);
		this.view = view;
		this.images = images;
		this.ids = null;
		this.image_type = ImageType.DRAWABLE_ARRAY;
	}

	// PUBLIC METHODS
	
	/**
	 * Returns current image.
	 * @return Current image.
	 */
	public Drawable getCurrentImage(){
		return this.view.getDrawable();
	}
	/**
	 * Returns output image view.
	 * @return Output image view.
	 */
	public ImageView getImageView(){
		return this.view;
	}
	/**
	 * Sets output image view.
	 * @param view Output ImageView object.
	 */
	public void setImageView(ImageView view){
		this.view = view;
	}
	/**
	 * Asks if has output image view.
	 * @return true if has, false if not.
	 */
	public boolean hasImageView(){
		return this.view != null;
	}
	/**
	 * Returns the array of drawables.
	 * @return
	 */
	public Drawable[] getDrawableArray(){
		return this.images;
	}
	public void setDrawableArray(Drawable[] images){
		this.images = images;
	}
	public int[] getIdResourcesArray(){
		return this.ids;
	}
	public void setIdResourcesArray(int[] ids){
		this.ids = ids;
	}
	public boolean isWorkingWithIDs(){
		return this.image_type.equals(ImageType.ID_ARRAY);
	}
	public boolean isWorkingWithDrawables(){
		return this.image_type.equals(ImageType.DRAWABLE_ARRAY);
	}
	// PROTECTED METHODS
	/**
	 * Actualizes the ImageView object.
	 */
	@Override
	protected void actualize() {
		if(!hasImageView())
			throw new NullPointerException("Can't actualize without output image view");
		switch(image_type){
			case DRAWABLE_ARRAY:
				this.view.setImageDrawable(images[super.getIndex()]);
				break;
			case ID_ARRAY:
				this.view.setImageResource(ids[super.getIndex()]);
				break;
			default:
				break;
		}

	}
}

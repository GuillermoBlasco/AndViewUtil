package com.andviewutil.mstate;

/**
 * This abstract class manages the states of a E object.
 * 
 * @param E Class of object which state is managed.
 * @author GuillermoBlascoJimenez
 * @version 1.1 MultiStateView
 * @version 1.0 AndViewUtil
 */
public abstract class MultiStateObject<E>{	
	
	/** state while view is onClick */
	public static final int WHILE_CLICK_STATE = -1;
	/** state while view is onClick and long_click_delay has been exceeded*/
	public static final int WHILE_LONG_CLICK_STATE = -2;
	/** unknown state */
	public static final int UNKNOWN_STATE = -3;
	/** keep last state */
	public static final int RETURN_IGNORE_STATE = -4;
	
	/** View object which state is managed*/
	protected E target;
	/*last stable state (cant be while click and while long click)*/
	private int last_stable_state;
	/*current state, including while click and while long click*/
	private int current_state;
	/*time to admit that a click is a long click*/
	private int long_click_delay;
	/*long click task*/
	private Runnable long_click_task = new Runnable(){
		public void run(){ 
			try {
				Thread.sleep(long_click_delay);
			} catch (InterruptedException e) { } 
			if (isClicked()) startLongClick();
		}
	};
	/**
	 * Builds with initial state as UNKNOWN_STATE.
	 * @param target E object which state is managed.
	 */
	public MultiStateObject(E view){
		this(view,UNKNOWN_STATE);
	}
	/**
	 * Builds witch a given initial state.
	 * @param target E object which state is managed.
	 * @param starting_state Initial state.
	 */
	public MultiStateObject(E target, int starting_state){
		this.target = target;
		long_click_delay = 200;
		this.current_state = starting_state;
		this.last_stable_state = starting_state;
		this.actualize();
	}
	/**
	 * Performs an action down.
	 */
	public void performActionDown(){
		startTouch();
	}
	/**
	 * Performs an action cancel.
	 */
	public void performActionCancel(){
		unsuccessfulEndTouch();
	}
	/**
	 * Performs an action up.
	 */
	public void performActionUp(){
		successfulEndTouch();
	}
	/**
	 * Returns target.
	 * @return E target.
	 */
	public E getTarget(){
		return this.target;
	}
	/**
	 * Sets the delay to admit a click has been a long click.
	 * @param millis
	 */
	public void setLongClickDelay(int millis){
		this.long_click_delay = millis;
	}
	/**
	 * Asks if is currently long clicked.
	 * @return True if is clicked and long click time has been
	 * 				reached, false otherwise.
	 */
	public boolean isLongClicked(){
		return this.current_state == WHILE_LONG_CLICK_STATE;
	}
	/**
	 * Asks if is currently clicked.
	 * @return true if is currently clicked, false otherwise.
	 */
	public boolean isClicked(){
		return this.current_state <= WHILE_CLICK_STATE || isLongClicked();
	}
	/**
	 * Returns the last stable state.
	 * @return Last stable state.
	 */
	public int getLastStableState(){
		return this.last_stable_state;
	}
	/**
	 * Sets a state. If view is currently clicked the state will be actualized after click.
	 * @param state New state.
	 */
	public void setState(int state){
		
		if(state != WHILE_CLICK_STATE && state != WHILE_LONG_CLICK_STATE){
			this.last_stable_state = state;
			if(!this.isClicked()){
				this.current_state = last_stable_state;
				this.actualize();
			}
		}
	}
	/**
	 * Actualizes the view with the current state.
	 */
	public void actualize(){
		actualizeOnState(target, this.current_state);
	}
	/**
	 * Must actualize the view by given state.
	 * @param tartet E object which state is applied.
	 * @param state Arbitrary state.
	 */
	protected abstract void actualizeOnState(E target, int state);
	/**
	 * Must return the next state when click action is performed.
	 * If return is IGNORE_RETURN_STATE the next state will be the last state
	 * automatically.
	 * 
	 * @param target E object which state is applied.
	 * @param last_state The last stable state
	 * @param long_click If click action has been long or not.
	 * @return Returns the next state.
	 */
	protected abstract int actualizeState(E target, int last_state, boolean long_click);
	
	// PRIVATE METHODS
	
	private void startTouch(){
		current_state = WHILE_CLICK_STATE;
		actualize();
		(new Thread(long_click_task)).start();
	}
	private void successfulEndTouch(){
		int next_state = actualizeState(target, last_stable_state, this.isLongClicked());
		if(next_state != RETURN_IGNORE_STATE)
			this.last_stable_state = next_state;
		this.current_state = this.last_stable_state;
		endTouch();
	}
	private void unsuccessfulEndTouch(){
		onKeepState();
		endTouch();
	}
	private void endTouch(){
		if(isLongClicked()) endLongClick();
		actualize();
	}
	private void startLongClick(){
		this.current_state = WHILE_LONG_CLICK_STATE;
		actualize();
	}
	private void endLongClick(){
		current_state = WHILE_CLICK_STATE;
	}
	private void onKeepState(){
		this.current_state = getLastStableState();
		actualize();
	}
}

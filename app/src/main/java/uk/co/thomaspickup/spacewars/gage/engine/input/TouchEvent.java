package uk.co.thomaspickup.spacewars.gage.engine.input;

/**
 * Touch event.
 * 
 * @version 1.0
 */
public class TouchEvent {

	/**
	 * Touch event constants
	 */
	public static final int TOUCH_DOWN = 0;
	public static final int TOUCH_UP = 1;
	public static final int TOUCH_DRAGGED = 2;

	/**
	 * Type of touch event that has occurred (TOUCH_DOWN, TOUCH_UP,
	 * TOUCH_DRAGGED)
	 */
	public int type;

	/**
	 * Screen position (pixel) at which the touch event occurred.
	 */
	public float x, y;

	/**
	 * Pointer ID associated with this touch event
	 */
	public int pointer;
}
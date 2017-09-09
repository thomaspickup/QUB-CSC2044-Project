package uk.co.thomaspickup.spacewars.game.platformDemo;

import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchHandler;
import uk.co.thomaspickup.spacewars.gage.util.BoundingBox;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * Simple control that can detected if a touch event falls on it.
 * 
 * Important: It is assumed that the control is defined in terms
 * of screen coordinates (not layer coordinates).
 * 
 * @version 1.0
 */
public class SimpleControl extends GameObject {

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new control.
	 * 
	 * @param x
	 *            Centre y location of the control
	 * @param y
	 *            Centre x location of the control
	 * @param width
	 *            Width of the control
	 * @param height
	 *            Height of the control
	 * @param bitmapName
	 *            Bitmap used to represent this control
	 * @param gameScreen
	 *            Gamescreen to which this control belongs
	 */
	public SimpleControl(float x, float y, float width, float height,
						 String bitmapName, GameScreen gameScreen) {
		super(x, y, width, height, gameScreen.getGame().getAssetManager()
				.getBitmap(bitmapName), gameScreen);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Determine if this control has been activated (touched).
	 * 
	 * @return boolean true if the control has been touched, false otherwise
	 */
	public boolean isActivated() {

		// Consider any touch events occurring in this update
		Input input = mGameScreen.getGame().getInput();

		// Check if any of the touch events were on this control
		BoundingBox bound = getBound();
		for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
			if (input.existsTouch(idx)) {
				if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameObject#draw(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D,
	 * uk.ac.qub.eeecs.gage.world.LayerViewport,
	 * uk.ac.qub.eeecs.gage.world.ScreenViewport)
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
					 LayerViewport layerViewport, ScreenViewport screenViewport) {

		// Assumed to be in screen space so just draw the whole thing
		drawScreenRect.set((int) (position.x - mBound.halfWidth),
				(int) (position.y - mBound.halfWidth),
				(int) (position.x + mBound.halfWidth),
				(int) (position.y + mBound.halfHeight));

		graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
	}
}

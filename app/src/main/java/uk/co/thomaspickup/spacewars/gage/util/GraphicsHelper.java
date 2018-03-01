package uk.co.thomaspickup.spacewars.gage.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

public final class GraphicsHelper {
	
	// /////////////////////////////////////////////////////////////////////////
	// Source and Desintation Rects
	// /////////////////////////////////////////////////////////////////////////
		
	/** 
	 * Determine the full source bitmap Rect and destination screen Rect if the
	 * specified game object bound falls within the layer's viewport.
	 * 
	 * The return rects are not clipped against the screen viewport.
	 * 
	 * @param gameObject
	 *            SpaceGame object instance to be considered
	 * @param layerViewport
	 *            Layer viewport region to check the entity against
	 * @param screenViewport
	 *            Screen viewport region that will be used to draw the
	 * @param sourceRect
	 *            Output Rect holding the region of the bitmap to draw
	 * @param screenRect
	 *            Output Rect holding the region of the screen to draw to
	 * @return boolean true if the entity is visible, false otherwise
	 * @return
	 */
	public static final boolean getSourceAndScreenRect(GameObject gameObject,
													   LayerViewport layerViewport, ScreenViewport screenViewport,
													   Rect sourceRect, Rect screenRect) {

		// Get the bounding box for the specified sprite
		BoundingBox spriteBound = gameObject.getBound();

		// Determine if the sprite falls within the layer viewport
		if (spriteBound.x - spriteBound.halfWidth < layerViewport.x + layerViewport.halfWidth && 
			spriteBound.x + spriteBound.halfWidth > layerViewport.x - layerViewport.halfWidth && 
			spriteBound.y - spriteBound.halfHeight < layerViewport.y + layerViewport.halfHeight && 
			spriteBound.y + spriteBound.halfHeight > layerViewport.y - layerViewport.halfHeight) {

			// Define the source rectangle
			Bitmap spriteBitmap = gameObject.getBitmap();
			sourceRect.set(0, 0, spriteBitmap.getWidth(), spriteBitmap.getHeight());

			// Determine the x- and y-aspect rations between the layer and screen viewports
			float screenXScale = (float) screenViewport.width / (2 * layerViewport.halfWidth);
			float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

			// Determine the screen rectangle
			float screenX = screenViewport.left + screenXScale * 					
							((spriteBound.x - spriteBound.halfWidth) 
									- (layerViewport.x - layerViewport.halfWidth));
			float screenY = screenViewport.top + screenYScale * 
							((layerViewport.y + layerViewport.halfHeight) 
									- (spriteBound.y + spriteBound.halfHeight));

			screenRect.set((int) screenX, (int) screenY,
					(int) (screenX + (spriteBound.halfWidth * 2) * screenXScale),
					(int) (screenY + (spriteBound.halfHeight * 2) * screenYScale));

			return true;
		}

		// Not visible
		return false;
	}	
	
	
	/** 
	 * Determine a source bitmap Rect and destination screen Rect if the
	 * specified game object bound falls within the layer's viewport.
	 * 
	 * The returned Rects are clipped against the layer and screen viewport
	 * 
	 * @param gameObject
	 *            SpaceGame object instance to be considered
	 * @param layerViewport
	 *            Layer viewport region to check the entity against
	 * @param screenViewport
	 *            Screen viewport region that will be used to draw the
	 * @param sourceRect
	 *            Output Rect holding the region of the bitmap to draw
	 * @param screenRect
	 *            Output Rect holding the region of the screen to draw to
	 * @return boolean true if the entity is visible, false otherwise
	 */
	public static final boolean getClippedSourceAndScreenRect(GameObject gameObject,
															  LayerViewport layerViewport, ScreenViewport screenViewport,
															  Rect sourceRect, Rect screenRect) {

		// Get the bounding box for the specified sprite
		BoundingBox spriteBound = gameObject.getBound();

		// Determine if the sprite falls within the layer viewport
		if (spriteBound.x - spriteBound.halfWidth < layerViewport.x + layerViewport.halfWidth && 
			spriteBound.x + spriteBound.halfWidth > layerViewport.x - layerViewport.halfWidth && 
			spriteBound.y - spriteBound.halfHeight < layerViewport.y + layerViewport.halfHeight && 
			spriteBound.y + spriteBound.halfHeight > layerViewport.y - layerViewport.halfHeight) {

			// Work out what region of the sprite is visible within the layer viewport,

			float sourceX = Math.max(0.0f,
					(layerViewport.x - layerViewport.halfWidth)
							- (spriteBound.x - spriteBound.halfWidth));
			float sourceY = Math.max(0.0f,
					(spriteBound.y + spriteBound.halfHeight)
							- (layerViewport.y + layerViewport.halfHeight));

			float sourceWidth = ((spriteBound.halfWidth * 2 - sourceX) - Math
					.max(0.0f, (spriteBound.x + spriteBound.halfWidth)
							- (layerViewport.x + layerViewport.halfWidth)));
			float sourceHeight = ((spriteBound.halfHeight * 2 - sourceY) - Math
					.max(0.0f, (layerViewport.y - layerViewport.halfHeight)
							- (spriteBound.y - spriteBound.halfHeight)));

			// Determining the scale factor for mapping the bitmap onto this
			// Rect and set the sourceRect value.

			Bitmap spriteBitmap = gameObject.getBitmap();
			
			float sourceScaleWidth = (float) spriteBitmap.getWidth()
					/ (2 * spriteBound.halfWidth);
			float sourceScaleHeight = (float) spriteBitmap.getHeight()
					/ (2 * spriteBound.halfHeight);

			sourceRect.set((int) (sourceX * sourceScaleWidth),
					(int) (sourceY * sourceScaleHeight),
					(int) ((sourceX + sourceWidth) * sourceScaleWidth),
					(int) ((sourceY + sourceHeight) * sourceScaleHeight));

			// Determine =which region of the screen viewport (relative to the
			// canvas) we will be drawing to.

			// Determine the x- and y-aspect rations between the layer and screen viewports
			float screenXScale = (float) screenViewport.width / (2 * layerViewport.halfWidth);
			float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

			float screenX = screenViewport.left + screenXScale * Math.max(
							0.0f,
							((spriteBound.x - spriteBound.halfWidth) 
									- (layerViewport.x - layerViewport.halfWidth)));
			float screenY = screenViewport.top + screenYScale * Math.max(
							0.0f,
							((layerViewport.y + layerViewport.halfHeight) 
									- (spriteBound.y + spriteBound.halfHeight)));

			// Set the region to the canvas to which we will draw
			screenRect.set((int) screenX, (int) screenY,
					(int) (screenX + sourceWidth * screenXScale),
					(int) (screenY + sourceHeight * screenYScale));

			return true;
		}

		// Not visible
		return false;
	}
	
	// /////////////////////////////////////////////////////////////////////////
	// Aspect Ratios
	// /////////////////////////////////////////////////////////////////////////
			
	/**
	 * Create a 3:2 aspect ratio screen viewport
	 * 
	 * @param game SpaceGame view for which the screenport will be defined
	 * @param screenPort Screen viewport to be defined
	 */
	public static void create3To2AspectRatioScreenViewport(
			Game game, ScreenViewport screenViewport) {
		
		// Create the screen viewport, size it to provide a 3:2 aspect
		float aspectRatio = (float) game.getScreenWidth() / (float) game.getScreenHeight();
		
		if (aspectRatio > 1.5f) { // 16:10/16:9
			int viewWidth = (int) (game.getScreenHeight() * 1.5f);
			int viewOffset = (game.getScreenWidth() - viewWidth) / 2;
			screenViewport.set(viewOffset, 0, viewOffset + viewWidth, game.getScreenHeight());
		} else { // 4:3
			int viewHeight = (int) (game.getScreenWidth() / 1.5f);
			int viewOffset = (game.getScreenHeight() - viewHeight) / 2;
			screenViewport.set(0, viewOffset, game.getScreenWidth(), viewOffset + viewHeight);
		}
	}
}
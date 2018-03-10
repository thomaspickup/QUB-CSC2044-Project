package uk.co.thomaspickup.spacewars.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.game.spaceLevel.SpaceLevelScreen;

/**
 * The main menu of the Space Wars Game
 * It links to the game as well as an options menus
 * 
 * @version 1.0
 */
public class MenuScreen extends GameScreen {

	/**
	 * Define the trigger touch region for navigating to the game or options menu
	 */
	private Rect mPlayButtonBound;
	private Rect mSettingsButtonBound;

	/**
	 * Creates the menu screens
	 * 
	 * @param game
	 *            SpaceGasme to which this screen belongs
	 */
	public MenuScreen(Game game) {
		super("MenuScreen", game);

		// Load in the bitmaps used on the menu screen
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("PlayIcon", "img/PlayButton.png");
		assetManager.loadAndAddBitmap("SettingsIcon", "img/SettingsButton.png");

		// Define the rects what will be used to 'hold' the images
		int spacingX = (game.getScreenWidth() / 2) - 230;
		int spacingY = (game.getScreenHeight() / 2) - 100;
		mPlayButtonBound = new Rect(spacingX, spacingY,spacingX + 560 , spacingY +300);

		spacingY = spacingY + 340;
		spacingX = spacingX + 160;
		mSettingsButtonBound = new Rect(spacingX, spacingY, spacingX + 230, spacingY + 230);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameScreen#update(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime)
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {

		// Process any touch events occurring since the update
		Input input = mGame.getInput();

		List<TouchEvent> touchEvents = input.getTouchEvents();
		if (touchEvents.size() > 0) {
			TouchEvent touchEvent = touchEvents.get(0);

			if (mPlayButtonBound.contains((int) touchEvent.x,
					(int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				SpaceLevelScreen spaceLevelScreen = new SpaceLevelScreen(mGame);

				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(spaceLevelScreen);
			} else if (mSettingsButtonBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the settings icon area has been touched then load up options menu
				mGame.getScreenManager().removeScreen(this.getName());
				OptionScreen optionScreen = new OptionScreen(mGame);

				mGame.getScreenManager().addScreen(optionScreen);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameScreen#draw(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D)
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

		// Get and draw the bitmaps into the defined rectangles
		//Bitmap playSpaceShipGame = mGame.getAssetManager().getBitmap("SpaceshipIcon");
		Bitmap playIcon = mGame.getAssetManager().getBitmap("PlayIcon");
		Bitmap settingsIcon = mGame.getAssetManager().getBitmap("SettingsIcon");

		graphics2D.clear(Color.BLACK);
		graphics2D.drawBitmap(playIcon, null, mPlayButtonBound,null);
		graphics2D.drawBitmap(settingsIcon, null, mSettingsButtonBound, null);
	}
}

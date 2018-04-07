package uk.co.thomaspickup.spacewars.game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;
import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.audio.Music;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
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
	private Rect mTitleBound;
	private Rect mAboutBound;

	// Background Objects
	private GameObject mSpaceBackground;
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;
	private int intXMultiplier = 1;

	// Main Theme Objects
	private Music mMainTheme;

	// New Settings Instance
	private SettingsHandler settingsHandler = new SettingsHandler();

	/**
	 * Creates the menu screens
	 * 
	 * @param game
	 *            SpaceGasme to which this screen belongs
	 */
	public MenuScreen(Game game) {
		super("MenuScreen", game);

		int paddingY = (int) (getGame().getScreenHeight() * 0.02);
		int paddingX = (int) (getGame().getScreenWidth() * 0.026);

		// Load in the bitmaps and sounds used on the menu screen
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("PlayIcon", "img/buttons/btnPlay.png");
		assetManager.loadAndAddBitmap("SettingsIcon", "img/buttons/btnSettings.png");
		assetManager.loadAndAddBitmap("TitleImage", "img/titles/ttlLogo.png");
		assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
		assetManager.loadAndAddMusic("MainTheme", "sfx/sfx_maintheme.mp3");
		assetManager.loadAndAddBitmap("AboutIcon", "img/buttons/btnAbout.png");

		// Plays title theme
		mMainTheme = assetManager.getMusic("MainTheme");
		mMainTheme.setVolume((float) settingsHandler.getSound(getGame().getContext()));
		mMainTheme.setLopping(true);
		mMainTheme.play();

		// Defines the background
		mSpaceBackground = new GameObject(game.getScreenWidth() / 2.0f,
				game.getScreenHeight() / 2.0f, game.getScreenWidth(), game.getScreenHeight(), getGame()
				.getAssetManager().getBitmap("SpaceBackground"), this);
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());

		// Create the layer viewport, taking into account the orientation
		// and aspect ratio of the screen.
		if (mScreenViewport.width > mScreenViewport.height)
			mLayerViewport = new LayerViewport(240.0f, 240.0f
					* mScreenViewport.height / mScreenViewport.width, 240,
					240.0f * mScreenViewport.height / mScreenViewport.width);
		else
			mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
					/ mScreenViewport.width, 240.0f, 240.0f
					* mScreenViewport.height / mScreenViewport.width, 240);

		// Defines the Title Image Rect
		int titleWidth =(int) (game.getScreenWidth() * 0.583); // On 1920 Screen Width = 1120
		int titleHeight = (int) (game.getScreenHeight() * 0.208); // On 1080 Screen Height = 400
		int spacingX = (game.getScreenWidth() / 2) - (titleWidth / 2);
		int spacingY = paddingY * 2;
		mTitleBound = new Rect(spacingX,spacingY, spacingX+titleWidth, spacingY + titleHeight);

		// Defines the Play Button Image Rect
		int btnPlayWidth = (int) (game.getScreenWidth() * 0.370);
		int btnPlayHeight = (int) (game.getScreenHeight() * 0.208);
		spacingX = (game.getScreenWidth() / 2) - (btnPlayWidth / 2);
		spacingY = (game.getScreenHeight() / 2) + paddingY;
		mPlayButtonBound = new Rect(spacingX, spacingY,spacingX + btnPlayWidth , spacingY +btnPlayHeight);

		// Defines the Settings Cog Rect
		int btnSettingsWidth = (int) (game.getScreenWidth() * 0.078);
		int btnSettingsHeight = (int) (game.getScreenHeight() * 0.138);
		spacingY = paddingY;
		spacingX = game.getScreenWidth() - (btnSettingsWidth + paddingX);
		mSettingsButtonBound = new Rect(spacingX, spacingY, spacingX + btnSettingsWidth, spacingY + btnSettingsHeight);

		// Defines the about rect
		int btnAboutWidth = (int) (game.getScreenWidth() * 0.078);
		int btnAboutHeight = (int) (game.getScreenHeight() * 0.138);
		spacingY = (game.getScreenHeight() - paddingY) - btnAboutHeight;
		spacingX = (game.getScreenWidth() - paddingX) - btnAboutWidth;
		mAboutBound = new Rect(spacingX, spacingY, spacingX + btnAboutWidth, spacingY + btnAboutHeight);
	}

	public MenuScreen(Game game, LayerViewport backgroundViewPort) {
		super("MenuScreen", game);

		int paddingY = (int) (getGame().getScreenHeight() * 0.02);
		int paddingX = (int) (getGame().getScreenWidth() * 0.026);

		// Load in the bitmaps and sounds used on the menu screen
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("PlayIcon", "img/buttons/btnPlay.png");
		assetManager.loadAndAddBitmap("SettingsIcon", "img/buttons/btnSettings.png");
		assetManager.loadAndAddBitmap("TitleImage", "img/titles/ttlLogo.png");
		assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
		assetManager.loadAndAddMusic("MainTheme", "sfx/sfx_maintheme.mp3");
		assetManager.loadAndAddBitmap("AboutIcon", "img/buttons/btnAbout.png");

		// Plays title theme
		mMainTheme = assetManager.getMusic("MainTheme");
		mMainTheme.setVolume((float) settingsHandler.getSound(getGame().getContext()));
		mMainTheme.setLopping(true);
		mMainTheme.play();

		// Defines the background
		mSpaceBackground = new GameObject(game.getScreenWidth() / 2.0f,
				game.getScreenHeight() / 2.0f, game.getScreenWidth(), game.getScreenHeight(), getGame()
				.getAssetManager().getBitmap("SpaceBackground"), this);
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());

		// Create the layer viewport, taking into account the orientation
		mLayerViewport = backgroundViewPort;

		// Defines the Title Image Rect
		int titleWidth =(int) (game.getScreenWidth() * 0.583); // On 1920 Screen Width = 1120
		int titleHeight = (int) (game.getScreenHeight() * 0.208); // On 1080 Screen Height = 400
		int spacingX = (game.getScreenWidth() / 2) - (titleWidth / 2);
		int spacingY = paddingY * 2;
		mTitleBound = new Rect(spacingX,spacingY, spacingX+titleWidth, spacingY + titleHeight);

		// Defines the Play Button Image Rect
		int btnPlayWidth = (int) (game.getScreenWidth() * 0.370);
		int btnPlayHeight = (int) (game.getScreenHeight() * 0.208);
		spacingX = (game.getScreenWidth() / 2) - (btnPlayWidth / 2);
		spacingY = (game.getScreenHeight() / 2) + paddingY;
		mPlayButtonBound = new Rect(spacingX, spacingY,spacingX + btnPlayWidth , spacingY +btnPlayHeight);

		// Defines the Settings Cog Rect
		int btnSettingsWidth = (int) (game.getScreenWidth() * 0.078);
		int btnSettingsHeight = (int) (game.getScreenHeight() * 0.138);
		spacingY = paddingY;
		spacingX = game.getScreenWidth() - (btnSettingsWidth + paddingX);
		mSettingsButtonBound = new Rect(spacingX, spacingY, spacingX + btnSettingsWidth, spacingY + btnSettingsHeight);

		// Defines the about rect
		int btnAboutWidth = (int) (game.getScreenWidth() * 0.078);
		int btnAboutHeight = (int) (game.getScreenHeight() * 0.138);
		spacingY = (game.getScreenHeight() - paddingY) - btnAboutHeight;
		spacingX = (game.getScreenWidth() - paddingX) - btnAboutWidth;
		mAboutBound = new Rect(spacingX, spacingY, spacingX + btnAboutWidth, spacingY + btnAboutHeight);
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
				OptionScreen optionScreen = new OptionScreen(mGame, mLayerViewport);

				mGame.getScreenManager().addScreen(optionScreen);
			} else if (mAboutBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the about icon area has been touched then load up about menu
				mGame.getScreenManager().removeScreen(this.getName());
				AboutScreen aboutScreen = new AboutScreen(mGame, mLayerViewport);

				mGame.getScreenManager().addScreen(aboutScreen);
			}
		}

		// Move the background diagonally
		// Changes the multiplier if it hits the bounds
		if (mLayerViewport.x == getGame().getScreenWidth() - mLayerViewport.getRight()) {
			intXMultiplier = -1;
		} else if (mLayerViewport.x == mScreenViewport.left) {
			intXMultiplier = 1;
		}

		// Adds the multiplier to x
		mLayerViewport.x += intXMultiplier;
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
		Bitmap titleImage = mGame.getAssetManager().getBitmap("TitleImage");
		Bitmap playIcon = mGame.getAssetManager().getBitmap("PlayIcon");
		Bitmap settingsIcon = mGame.getAssetManager().getBitmap("SettingsIcon");
		Bitmap aboutIcon = mGame.getAssetManager().getBitmap("AboutIcon");

		// Draws the background with adjusted viewport
		mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);

		// Draws all the static icons on top
		graphics2D.drawBitmap(titleImage, null,mTitleBound,null);
		graphics2D.drawBitmap(playIcon, null, mPlayButtonBound,null);
		graphics2D.drawBitmap(settingsIcon, null, mSettingsButtonBound, null);
		graphics2D.drawBitmap(aboutIcon,null,mAboutBound,null);
	}
}

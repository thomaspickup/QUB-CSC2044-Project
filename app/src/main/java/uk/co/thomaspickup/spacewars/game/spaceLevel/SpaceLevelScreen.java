package uk.co.thomaspickup.spacewars.game.spaceLevel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.util.BoundingBox;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
import uk.co.thomaspickup.spacewars.game.MenuScreen;
import uk.co.thomaspickup.spacewars.game.PauseScreen;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;

/**
 * The Main Game World - A 2D Space Shooter.
 * 
 * @version 1.0
 */
// TODO: Add Music To Space Level Screen
// TODO: Add Health Bar to Space Screen
// TODO: Add Lives Indicator To Space Screen
// TODO: Add Weapon Firing
// TODO: Add AI Difficutly Adjustment

public class SpaceLevelScreen extends GameScreen {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Width and height of the level 
	 */
	private final float LEVEL_WIDTH = 1000.0f;
	private final float LEVEL_HEIGHT = 1000.0f;

	/**
	 * Define viewports for this layer and the associated screen projection
	 */
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;

	/**
	 * Define a background object, alongside a player controlled
	 * space ship and separate lists of asteroids and AI controlled
	 * space ships.
	 */
	private GameObject mSpaceBackground;

	private PlayerSpaceship mPlayerSpaceship;

	private final int NUM_ASTEROIDS = 20;
	private List<Asteroid> mAsteroids;

	private final int NUM_SEEKERS = 5;
	private final int NUM_TURRETS = 5;
	private List<AISpaceship> mAISpaceships;

	// Pause Button
	private Rect mPauseBound;

	// Settings Handler for ease of accessing the settings
	private SettingsHandler settings = new SettingsHandler();

	// Save File used for transfering and receiving a save from other screens
	private SpaceSave saveFile = new SpaceSave();

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create a simple steering game world
	 * 
	 * @param game
	 *            SpaceGame to which this screen belongs
	 */
	public SpaceLevelScreen(Game game) {
		super("SpaceLevelScreen", game);

		// Gets current settings
		int currentDifficultySetting = settings.getDifficulty(getGame().getContext());
		int currentSoundSetting = settings.getSound(getGame().getContext());

		// Create the screen viewport
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

		// Load in the assets used by the steering demo
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
		assetManager.loadAndAddBitmap("Asteroid1", "img/sprites/sprAsteroid1.png");
		assetManager.loadAndAddBitmap("Asteroid2", "img/sprites/sprAsteroid2.png");
		assetManager.loadAndAddBitmap("Spaceship1", "img/sprites/sprSpaceship1.png");
		assetManager.loadAndAddBitmap("Spaceship2", "img/sprites/sprSpaceship2.png");
		assetManager.loadAndAddBitmap("Spaceship3", "img/sprites/sprSpaceship3.png");
		assetManager.loadAndAddBitmap("Turret", "img/sprites/sprTurret.png");
		assetManager.loadAndAddBitmap("PauseButtonWhite", "img/buttons/btnPause-Normal.png");
		assetManager.loadAndAddBitmap("PauseButtonBlack", "img/buttons/btnPause-Selected.png");

		// Create the space background
		mSpaceBackground = new GameObject(LEVEL_WIDTH / 2.0f,
				LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
						.getAssetManager().getBitmap("SpaceBackground"), this);

		// Creates the pause button
		mPauseBound = new Rect(50,50,150,150);

		// Create the player spaceship
		mPlayerSpaceship = new PlayerSpaceship(100, 100, this);

		// Create a number of randomly positioned asteroids
		Random random = new Random();
		mAsteroids = new ArrayList<Asteroid>(NUM_ASTEROIDS);
		for (int idx = 0; idx < NUM_ASTEROIDS; idx++) {
			float x = random.nextFloat() * LEVEL_WIDTH;
			float y = random.nextFloat() * LEVEL_HEIGHT;

			mAsteroids.add(new Asteroid(x, y, this));
		}

		// Create a number of randomly positioned AI controlled ships
		mAISpaceships = new ArrayList<AISpaceship>(NUM_SEEKERS + NUM_TURRETS);
		for (int idx = 0; idx < NUM_SEEKERS; idx++)
			mAISpaceships.add(new AISpaceship(random.nextFloat() * LEVEL_WIDTH,
					random.nextFloat() * LEVEL_HEIGHT,
					AISpaceship.ShipBehaviour.Seeker, this, currentDifficultySetting));

		// Positions Turrets
		for (int idx = 0; idx < NUM_TURRETS; idx++) {
			// Problem -
			// Turrets being overlayed on Asteroids
			// Solution -
			// Added in code to check whether a turret is going
			// to be put within 50px of an asteroid. If so
			// recalculate coordinates.
			boolean valid = false;
			float x, y;

			do {
				valid = true;
				x = random.nextFloat() * LEVEL_WIDTH;
				y = random.nextFloat() * LEVEL_HEIGHT;

				for (Asteroid asteroid : mAsteroids) {
					float asX = asteroid.getBound().x;
					float asY = asteroid.getBound().y;

					// Check if in X Realm
					if (!(asX >+ x + 50.0f || asX <= x + 50.0f)) {
						valid = false;
					}

					if (!(asY >= y + 50.0f || asY <= x + 50.0f)) {
						valid = false;
					}
				}
			} while (valid = false);

			mAISpaceships.add(new AISpaceship(x, y, AISpaceship.ShipBehaviour.Turret, this, currentDifficultySetting));
		}

	}

	/**
	 * Creates the Space Game Level from a save file.
	 *
	 * @param game
	 *            SpaceGame to which this screen belongs
	 * @param saveFile the save file that the game is to be based on
	 */
	public SpaceLevelScreen(Game game, SpaceSave saveFile) {
		super("SpaceLevelScreen", game);

		this.saveFile = saveFile;

		// Gets current settings
		int currentDifficultySetting = settings.getDifficulty(getGame().getContext());
		int currentSoundSetting = settings.getSound(getGame().getContext());

		// Create the screen viewport
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());

		// Gets the Layer View Port from the save file
		mLayerViewport = this.saveFile.getMLayerViewport();

		// Load in the assets used by the steering demo
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
		assetManager.loadAndAddBitmap("Asteroid1", "img/sprites/sprAsteroid1.png");
		assetManager.loadAndAddBitmap("Asteroid2", "img/sprites/sprAsteroid2.png");
		assetManager.loadAndAddBitmap("Spaceship1", "img/sprites/sprSpaceship1.png");
		assetManager.loadAndAddBitmap("Spaceship2", "img/sprites/sprSpaceship2.png");
		assetManager.loadAndAddBitmap("Spaceship3", "img/sprites/sprSpaceship3.png");
		assetManager.loadAndAddBitmap("Turret", "img/sprites/sprTurret.png");
		assetManager.loadAndAddBitmap("PauseButtonWhite", "img/buttons/btnPause-Normal.png");
		assetManager.loadAndAddBitmap("PauseButtonBlack", "img/buttons/btnPause-Selected.png");

		// Create the space background
		mSpaceBackground = new GameObject(LEVEL_WIDTH / 2.0f,
				LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
				.getAssetManager().getBitmap("SpaceBackground"), this);

		// Creates the pause button
		mPauseBound = new Rect(50,50,150,150);

		// Gets the player spaceship from the save file
		mPlayerSpaceship = this.saveFile.getMPlayerSpaceShip();

		// Gets Asteroids from the save file
		mAsteroids = this.saveFile.getMAsteroids();

		// Gets AI Spaceships from the save file
		mAISpaceships = this.saveFile.getMAISpaceships();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Support methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Return the player spaceship 
	 * 
	 * @return Player spaceship
	 */	
	public PlayerSpaceship getPlayerSpaceship() {
		return mPlayerSpaceship;
	}

	/**
	 * Return a list of the AI spaceships in the level
	 * 
	 * @return List of AI controlled spaceships
	 */
	public List<AISpaceship> getAISpaceships() {
		return mAISpaceships;
	}

	/**
	 * Return a list of asteroids in the the level
	 * 
	 * @return List of asteroids in the level
	 */
	public List<Asteroid> getAsteroids() {
		return mAsteroids;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Update and Draw methods
	// /////////////////////////////////////////////////////////////////////////
		
	/*
	 * (non-Javadoc) fs
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

			if (mPauseBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// Remove this screen
				mGame.getScreenManager().removeScreen(this.getName());

				// Sets current states to save file
				saveFile.setMAISpaceships(mAISpaceships);
				saveFile.setMAsteroids(mAsteroids);
				saveFile.setMLayerViewport(mLayerViewport);
				saveFile.setMPlayerSpaceShip(mPlayerSpaceship);

				// Create a new instance of menuScreen and add it to screen manager
				PauseScreen pauseScreen = new PauseScreen(mGame, saveFile);
				mGame.getScreenManager().addScreen(pauseScreen);
			}
		}

		// Update the player spaceship
		mPlayerSpaceship.update(elapsedTime);

		// Ensure the player cannot leave the confines of the world
		BoundingBox playerBound = mPlayerSpaceship.getBound();
		if (playerBound.getLeft() < 0)
			mPlayerSpaceship.position.x -= playerBound.getLeft();
		else if (playerBound.getRight() > LEVEL_WIDTH)
			mPlayerSpaceship.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

		if (playerBound.getBottom() < 0)
			mPlayerSpaceship.position.y -= playerBound.getBottom();
		else if (playerBound.getTop() > LEVEL_HEIGHT)
			mPlayerSpaceship.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

		// Focus the layer viewport on the player
		mLayerViewport.x = mPlayerSpaceship.position.x;
		mLayerViewport.y = mPlayerSpaceship.position.y;

		// Ensure the viewport cannot leave the confines of the world
		if (mLayerViewport.getLeft() < 0)
			mLayerViewport.x -= mLayerViewport.getLeft();
		else if (mLayerViewport.getRight() > LEVEL_WIDTH)
			mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

		if (mLayerViewport.getBottom() < 0)
			mLayerViewport.y -= mLayerViewport.getBottom();
		else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
			mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);

		// Update each of the AI controlled spaceships
		for (AISpaceship aiSpaceship : mAISpaceships)
			aiSpaceship.update(elapsedTime);

		// Update each of the asteroids
		for (Asteroid asteroid : mAsteroids)
			asteroid.update(elapsedTime);
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

		// Create the screen to black and define a clip based on the viewport
		graphics2D.clear(Color.BLACK);
		graphics2D.clipRect(mScreenViewport.toRect());

		// Draw the background first of all
		mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);

		// Draw each of the asteroids
		for (Asteroid asteroid : mAsteroids)
			asteroid.draw(elapsedTime, graphics2D, mLayerViewport,
					mScreenViewport);

		// Draw each of the AI controlled spaceships
		for (AISpaceship aiSpaceship : mAISpaceships)
			aiSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
					mScreenViewport);

		// Draw the player
		mPlayerSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);

		// Draws the Pause Button
		Bitmap imgPauseButton =  mGame.getAssetManager().getBitmap("PauseButtonWhite");
		graphics2D.drawBitmap(imgPauseButton,null,mPauseBound,null);
	}
}

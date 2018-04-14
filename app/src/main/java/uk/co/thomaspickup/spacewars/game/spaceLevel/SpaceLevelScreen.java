package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.util.BoundingBox;
import uk.co.thomaspickup.spacewars.gage.util.CollisionDetector;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

import uk.co.thomaspickup.spacewars.game.HelperTools;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;

/**
 * The main game screen - runs the game.
 *
 * Created by Thomas Pickup
 */
// TODO: Refine weapon firing for turrets
public class SpaceLevelScreen extends GameScreen {
	// /////////////////////////////////////////////////////////////////////////
	// Variables
	// /////////////////////////////////////////////////////////////////////////

	// Private finals to hold the Level Width and Height.
	private final float LEVEL_WIDTH = 1000.0f;
	private final float LEVEL_HEIGHT = 1000.0f;

	// Defines the Screen and Layer viewports.
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;

	// The game object of the background.
	private GameObject mSpaceBackground;

	// The player ships game object.
	private PlayerSpaceship mPlayerSpaceship;

	// The definition of how many asteroids as well as the list of all asteroids.
	private final int NUM_ASTEROIDS = 20;
	private List<Asteroid> mAsteroids;

	// The definitions of how many AISpaceships there are as well as the list that holds them.
	private final int NUM_SEEKERS = 5;
	private final int NUM_TURRETS = 5;
	private List<AISpaceship> mAISpaceships;

	// Pause Button Bound.
	private Rect mPauseBound;

	// Fire Button Bound.
	private Rect mFireBound;

	// Settings Handler to allow the game to access the Shared Preferences.
	private SettingsHandler settingsHandler = new SettingsHandler();

	// Helper tools
	private HelperTools helperTools = new HelperTools();

	// Save File used for transfering and receiving a save from other screens.
	private SpaceSave saveFile = new SpaceSave();

	// Creates a new healthbar.
	int hbXPosition, hbYPosition, hbWidth, hbHeight;

	// Padding used by the game @1920x1080 = 50 x 50 padding.
	int paddingY = (int) (getGame().getScreenHeight() * 0.02);
	int paddingX = (int) (getGame().getScreenWidth() * 0.026);

	// The current settings stored at launch of screen.
	int currentDifficultySetting;
	int currentSoundSetting;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create the Space Game Level from scratch.
	 * 
	 * @param game SpaceGame to which this screen belongs.
	 */
	public SpaceLevelScreen(Game game) {
		super("SpaceLevelScreen", game);

		// Pulls in the latest settings from settings handler
		getSettings();

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

		// Load in the assets used by the level
		loadAssets();

		// Runs the set up UI function
		setUpUI(game);

		// Generates the new game objects
		generateNewGameObjects();
	}

	/**
	 * Creates the Space Game Level from a save file.
	 *
	 * @param game SpaceGame to which this screen belongs.
	 * @param saveFile The save file that the game is to be based on.
	 */
	public SpaceLevelScreen(Game game, SpaceSave saveFile) {
		super("SpaceLevelScreen", game);

		// Imports the save file
		this.saveFile = saveFile;

		// Pulls in the latest settings from settings handler
		getSettings();

		// Create the screen viewport
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());

		// Gets the Layer View Port from the save file
		mLayerViewport = this.saveFile.getMLayerViewport();

		// Load in the assets used by the level
		loadAssets();

		// Runs the set up UI function
		setUpUI(game);

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
	 * Return the player spaceship.
	 * 
	 * @return Player spaceship.
	 */	
	public PlayerSpaceship getPlayerSpaceship() {
		return mPlayerSpaceship;
	}

	/**
	 * Return a list of the AI spaceships in the level.
	 * 
	 * @return List of AI controlled spaceships.
	 */
	public List<AISpaceship> getAISpaceships() {
		return mAISpaceships;
	}

	/**
	 * Return a list of asteroids in the the level.
	 * 
	 * @return List of asteroids in the level.
	 */
	public List<Asteroid> getAsteroids() {
		return mAsteroids;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Start up methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Used by both constructors to load in all bitmaps used at the start of the game.
	 */
	private void loadAssets() {
		// Creates a new instance of the Asset Manager
		AssetStore assetManager = mGame.getAssetManager();

		// Imports all the Bitmaps
		assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
		assetManager.loadAndAddBitmap("Asteroid1", "img/sprites/sprAsteroid1.png");
		assetManager.loadAndAddBitmap("Asteroid2", "img/sprites/sprAsteroid2.png");
		assetManager.loadAndAddBitmap("Spaceship1", "img/sprites/sprSpaceship1.png");
		assetManager.loadAndAddBitmap("Spaceship2", "img/sprites/sprSpaceship2.png");
		assetManager.loadAndAddBitmap("Spaceship3", "img/sprites/sprSpaceship3.png");
		assetManager.loadAndAddBitmap("Turret", "img/sprites/sprTurret.png");
		assetManager.loadAndAddBitmap("PauseButtonWhite", "img/buttons/btnPause-Normal.png");
		assetManager.loadAndAddBitmap("PauseButtonBlack", "img/buttons/btnPause-Selected.png");
		assetManager.loadAndAddBitmap("FireButton", "img/buttons/btnFire-Normal.png");
		assetManager.loadAndAddBitmap("HeartFull", "img/sprites/sprHeart-Full.png");
		assetManager.loadAndAddBitmap("HeartEmpty", "img/sprites/sprHeart-Empty.png");

		// Imports the sound effects
		assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");
		assetManager.loadAndAddSound("WeaponExplosion","sfx/sfx_weaponexplosion.mp3");
	}

	/**
	 * Used by both constructors to create the UI Element bounds used across the game.
	 *
	 * @param game The game to add the UI Elements to.
	 */
	private void setUpUI(Game game) {
		// Create the space background
		mSpaceBackground = new GameObject(LEVEL_WIDTH / 2.0f,
				LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
				.getAssetManager().getBitmap("SpaceBackground"), this);

		// Creates the pause button bound
		int btnPauseWidth = (int) (game.getScreenWidth() * 0.078); // @1920 = 150
		int btnPauseHeight = (int) (game.getScreenHeight() * 0.138); // @1080 = 150
		mPauseBound = new Rect(paddingX,paddingY,btnPauseWidth,btnPauseHeight);

		// Creates the fire button bound
		int btnFireWidth = (int) (game.getScreenWidth() * 0.104); // @1920 = 200
		int btnFireHeight = (int) (game.getScreenHeight() * 0.185); // @1080 = 200
		mFireBound = new Rect(getGame().getScreenWidth() - btnFireWidth, getGame().getScreenHeight() - btnFireHeight, getGame().getScreenWidth() - paddingX, getGame().getScreenHeight() -paddingY);

		// Creates the health bar bound
		hbHeight = (int) (getGame().getScreenHeight() * 0.138); // @1080 = 150
		hbWidth = (getGame().getScreenWidth() / 10) * 6; // Width is 6 10ths of the screen width
		hbXPosition = (getGame().getScreenWidth() / 10) * 2; // Starts at 2 10ths of the screen across
		hbYPosition = paddingY; // Starts at the border of the screen padding
	}

	/**
	 * Used to create from scratch all the game objects needed to run the game.
	 */
	private void generateNewGameObjects() {
		// Create the player spaceship
		mPlayerSpaceship = new PlayerSpaceship(100, 100, this);

		// Set player health to 100
		mPlayerSpaceship.setHealth(100);

		// Sets the lives of the player based on the difficulty
		int playerLives = 0;

		switch (currentDifficultySetting) {
			case 1: // Easy
				playerLives = 5;
				break;
			case 2: // Normal
				playerLives = 3;
				break;
			case 3: // Hard
				playerLives = 2;
				break;
			case 4: // Insane
				playerLives = 1;
				break;
		}

		mPlayerSpaceship.setLives(playerLives);

		// Create a number of randomly positioned asteroids
		Random random = new Random();

		// Initializes the arraylist
		mAsteroids = new ArrayList<Asteroid>(NUM_ASTEROIDS);

		// Loops through the predetermined amount
		for (int idx = 0; idx < NUM_ASTEROIDS; idx++) {
			// Creates the variables
			boolean valid = false;
			float x, y;

			// Loops through whilst valid is false
			do {
				// Sets valid to true and generates two new coordinates based of Level dimensions
				valid = true;
				x = random.nextFloat() * LEVEL_WIDTH;
				y = random.nextFloat() * LEVEL_HEIGHT;

				// Loops through all the existing asteroids
				for (Asteroid asteroid : mAsteroids) {
					// Check if the x or y is in the asteroid bounds
					if (!(asteroid.getBound().contains(x,y))) {
						// If it isn't contained then exit loop
						valid = false;
					}
				}
			} while (valid = false);

			// Create the asteroid
			mAsteroids.add(new Asteroid(x, y, this));
		}

		// Create a number of randomly positioned AI controlled ships
		// Initializes the array list
		mAISpaceships = new ArrayList<AISpaceship>(NUM_SEEKERS + NUM_TURRETS);

		// Loops through the number of seekers
		for (int idx = 0; idx < NUM_SEEKERS; idx++) {
			// Creates a new ship starting at a random location
			mAISpaceships.add(new AISpaceship(random.nextFloat() * LEVEL_WIDTH,
					random.nextFloat() * LEVEL_HEIGHT,
					AISpaceship.ShipBehaviour.Seeker, this, currentDifficultySetting, 100));
		}

		// Loops through the predetermined amount
		for (int idx = 0; idx < NUM_TURRETS; idx++) {
			// Creates the variables
			boolean valid = false;
			float x, y;

			// Loops through whilst valid is false
			do {
				// Sets valid to true and generates two new coordinates based of Level dimensions
				valid = true;
				x = random.nextFloat() * LEVEL_WIDTH;
				y = random.nextFloat() * LEVEL_HEIGHT;

				// Loops through all the existing asteroids
				for (Asteroid asteroid : mAsteroids) {
					// Check if the x or y is in the asteroid bounds
					if (!(asteroid.getBound().contains(x,y))) {
						// If it isn't contained then exit loop
						valid = false;
					}
				}
			} while (valid = false);

			// Create the turret
			mAISpaceships.add(new AISpaceship(x, y, AISpaceship.ShipBehaviour.Turret, this, currentDifficultySetting,100));
		}
	}

	/**
	 * Used to get the current settings from the settings handler and store them in variables.
	 */
	private void getSettings() {
		// Gets current settings from settingsHandler
		currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
		currentSoundSetting = settingsHandler.getSound(getGame().getContext());
	}

	// /////////////////////////////////////////////////////////////////////////
	// Update and Draw methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @param elapsedTime Elapsed time information for the frame
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		// First off check if the players health is less than 0
		if (mPlayerSpaceship.getHealth() <= 0) {
			// Minus one live
			mPlayerSpaceship.minusLive();

			// Set Health to 100 again
			mPlayerSpaceship.setHealth(100);

			// If no lives left then end game
			if (mPlayerSpaceship.getLivesLeft() == 0) {
				// Remove this screen
				mGame.getScreenManager().removeScreen(this.getName());
				int enemiesDefeated = (NUM_SEEKERS + NUM_TURRETS) - mAISpaceships.size();

				// Add the end game screen
				EndGameScreen endGameScreen = new EndGameScreen(mGame, false, enemiesDefeated, mLayerViewport);
				mGame.getScreenManager().addScreen(endGameScreen);
			}
		}

		// If no AI Spaceships exist then end the game with a win
		if (mAISpaceships.size() == 0) {
			// Remove this screen
			mGame.getScreenManager().removeScreen(this.getName());

			// Add the end game screen
			EndGameScreen endGameScreen = new EndGameScreen(mGame, true, (NUM_SEEKERS + NUM_TURRETS), mLayerViewport);
			mGame.getScreenManager().addScreen(endGameScreen);
		}

		// Process any touch events occurring since the update
		Input input = mGame.getInput();

		// Imports touch event into list
		List<TouchEvent> touchEvents = input.getTouchEvents();

		// Only processes if there is an active touch event
		if (touchEvents.size() > 0) {
			// Looks at first touch event
			TouchEvent touchEvent = touchEvents.get(0);

			// If Pause button is pressed
			if (mPauseBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// Plays button sound
				getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));

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

			// If fire button is pressed
			} else if (mFireBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// Fire and then stop processing any more touch events
				mPlayerSpaceship.fire(this);
				return;
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
		// Uses an irerator to remove objects from list on fly
		Iterator<AISpaceship> iterAISpaceships = mAISpaceships.iterator();

		// Keeps processing whilst there is still AI Space ships
		while (iterAISpaceships.hasNext()) {
			// Imports next into a singular instance
			AISpaceship aiSpaceship = iterAISpaceships.next();

			// Checks to see if there is a collision between the ai spaceship and the player
			if (CollisionDetector.isCollision(mPlayerSpaceship.getBound(), aiSpaceship.getBound())) {
				// Remove one health point from both
				mPlayerSpaceship.setHealth(mPlayerSpaceship.getHealth() - 1);
				aiSpaceship.setHealth(aiSpaceship.getHealth() - 1);

				// Resolve the collision
				CollisionDetector.determineAndResolveCollision(getPlayerSpaceship(), aiSpaceship);

				// Check to see if the health is empty
				if (aiSpaceship.getHealth() == 0) {
					// Plays sound
					getGame().getAssetManager().getSound("WeaponExplosion").play(settingsHandler.getSound(getGame().getContext()));

					// Removes the ai spaceship from play
					iterAISpaceships.remove();
				}
			}

			// Loops through all the astroids
			for (Asteroid asteroid : mAsteroids) {
				// Checks to see if there is a collision between the ai spaceship and the asteroid
				if (CollisionDetector.isCollision(aiSpaceship.getBound(), asteroid.getBound())) {
					// Resolves the collision
					CollisionDetector.determineAndResolveCollision(aiSpaceship, asteroid);

					// Remove one health point from the ship
					aiSpaceship.setHealth(aiSpaceship.getHealth() - 1);

					// Check to see if the health is empty
					if (aiSpaceship.getHealth() == 0) {
						// Plays sound
						getGame().getAssetManager().getSound("WeaponExplosion").play(settingsHandler.getSound(getGame().getContext()));

						// Removes the ai spaceship from play
						iterAISpaceships.remove();
					}
				}
			}

			// Checks if there is any hits with player lasers
			// Uses an irerator to remove objects from list on fly
			Iterator<Laser> iterLaser = mPlayerSpaceship.mLasers.iterator();

			// Keeps processing whilst there is still lasers active
			while(iterLaser.hasNext()){
				// Imports next into a singular instance
				Laser laser = iterLaser.next();

				// Checks to see if there is a collision between the ai spaceship and the laser
				if (CollisionDetector.isCollision(aiSpaceship.getBound(),laser.getBound())) {
					// Removes health points from AI Spaceship based on the difficulty multiplier
					aiSpaceship.setHealth(aiSpaceship.getHealth() - helperTools.getDamageMultiplier(currentDifficultySetting));

					// Removes the laser from play
					iterLaser.remove();

					// Check to see if the health is empty
					if (aiSpaceship.getHealth() == 0) {
						// Plays sound
						getGame().getAssetManager().getSound("WeaponExplosion").play(settingsHandler.getSound(getGame().getContext()));

						// Removes the ai spaceship from play
						iterAISpaceships.remove();
					}
				}
			}

			// Checks if there are any hits with the enemy lasers
			// Uses an irerator to remove objects from list on fly
			Iterator<Laser> iterEnemyLaser = aiSpaceship.mLasers.iterator();

			// Keeps processing whilst there is still lasers active
			while(iterEnemyLaser.hasNext()){
				// Imports next into a singular instance
				Laser laser = iterEnemyLaser.next();

				// Checks to see if there is a collision between the player spaceship and the laser
				if (CollisionDetector.isCollision(mPlayerSpaceship.getBound(),laser.getBound())) {
					// Removes health points from Player Spaceship based on the difficulty multiplier
					mPlayerSpaceship.setHealth(mPlayerSpaceship.getHealth() - helperTools.getDamageMultiplier(currentDifficultySetting));

					// Removes the laser from play
					iterEnemyLaser.remove();
				}
			}

			// Updates the AI Spaceship
			aiSpaceship.update(elapsedTime);
		}

		// Loops through all of the asteroids
		for (Asteroid asteroid : mAsteroids) {
			// Checks to see if there is a collision between the player and the asteroid
			if (CollisionDetector.isCollision(mPlayerSpaceship.getBound(), asteroid.getBound())) {
				// Removes one health point from the player ship
				mPlayerSpaceship.setHealth(mPlayerSpaceship.getHealth() - 1);

				// Resolves the collision
				CollisionDetector.determineAndResolveCollision(getPlayerSpaceship(), asteroid);
			}

			// Updates the asteroid
			asteroid.update(elapsedTime);
		}
	}

	/**
	 *
	 * @param elapsedTime Elapsed time information for the frame
	 * @param graphics2D
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

		// Draws the fire button
		Bitmap imgFireButton = mGame.getAssetManager().getBitmap("FireButton");
		graphics2D.drawBitmap(imgFireButton, null,mFireBound,null);

		// Draws the HealthBar
		int borderLine = 10;

		// Draws the backboard that the health bar is drawn on
		Rect backBoard = new Rect(hbXPosition,hbYPosition,hbXPosition+hbWidth,hbYPosition+hbHeight);
		Paint paintCan = new Paint();
		paintCan.setColor(Color.BLACK);
		graphics2D.drawRect(backBoard, paintCan);

		// Works out width of inner part from the player health
		float multiplier = (hbWidth - borderLine) / 100; // Represents Each Percentage per pixel
		int width = mPlayerSpaceship.getHealth() * (int) multiplier;

		// Draws the health indicator
		Rect mHealthBar = new Rect(hbXPosition + borderLine, hbYPosition + 10, hbXPosition + 10 + width , (hbYPosition+hbHeight) - 10);
		paintCan.setColor(Color.RED);
		graphics2D.drawRect(mHealthBar,paintCan);

		// Draws the lives lost
		Bitmap heartFull = mGame.getAssetManager().getBitmap("HeartFull");
		Bitmap heartEmpty = mGame.getAssetManager().getBitmap("HeartEmpty");

		// Defines the heart size
		int heartWidth = (int) (getGame().getScreenWidth() * 0.052);
		int heartHeight = (int) (getGame().getScreenHeight() * 0.092);

		// Defines where the stack should start based on amount of hearts
		int startX = getGame().getScreenWidth() / 2 - (((mPlayerSpaceship.getLivesLeft() + mPlayerSpaceship.getLivesLost()) * heartWidth) / 2);
		int yOffset = getGame().getScreenHeight() - paddingY;
		int endX;

		// Creates empty variable to use as bound
		Rect mHeartBound;

		// Loops through lives left
		for (int c = 0; c < mPlayerSpaceship.getLivesLeft(); c++) {
			// Creates where the heart bound is going to end
			endX = startX + heartWidth;

			// Creates the heart bound
			mHeartBound = new Rect(startX, yOffset - heartHeight, endX, yOffset);

			// Draws the heart
			graphics2D.drawBitmap(heartFull,null, mHeartBound,null);

			// Sets the start as the last hearts end x to carry on
			startX = endX;
		}

		// Loops through lives lost
		for (int c = 0; c < mPlayerSpaceship.getLivesLost(); c++) {
			// Creates where the heart bound is going to end
			endX = startX + heartWidth;

			// Creates the heart bound
			mHeartBound = new Rect(startX, yOffset - heartHeight, endX, yOffset);

			// Draws the heart
			graphics2D.drawBitmap(heartEmpty,null, mHeartBound,null);

			// Sets the start as the last hearts end x to carry on
			startX = endX;
		}
	}
}

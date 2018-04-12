package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;

import uk.co.thomaspickup.spacewars.gage.ai.SteeringBehaviours;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
import uk.co.thomaspickup.spacewars.gage.world.Sprite;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;

/**
 * Player controlled spaceship.
 * 
 * Created by Thomas Pickup
 */
public class PlayerSpaceship extends Sprite {

	// /////////////////////////////////////////////////////////////////////////
	// Variables
	// /////////////////////////////////////////////////////////////////////////

	//Centre of the screen (used to determine the offset of touch events)
	private Vector2 screenCentre = new Vector2();

	// Acceleration vector based on the player's touch input
	private Vector2 playerTouchAcceleration = new Vector2();

	// Contains the lives lost and left
	private int livesLost;
	private int livesLeft;

	// List of lasers
	public List<Laser> mLasers;

	// Used with the delay of firing
	private int reloadTime;
	private int timeToReload;
	private boolean canFire;

	// Creates new Settings Handler to access the settings
	private SettingsHandler settingsHandler = new SettingsHandler();

	// /////////////////////////////////////////////////////////////////////////
	// Constructor
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create a player controlled spaceship
	 * 
	 * @param startX
	 *            x location of the player spaceship
	 * @param startY
	 *            y location of the player spaceship
	 * @param gameScreen
	 *            Gamescreen to which spaceship belongs
	 */
	public PlayerSpaceship(float startX, float startY, GameScreen gameScreen) {
		super(startX, startY, 50.0f, 50.0f, gameScreen.getGame()
				.getAssetManager().getBitmap("Spaceship2"), gameScreen);

		// Gets the weapon fire sfx and puts in AssetManager
		gameScreen.getGame().getAssetManager().loadAndAddSound("WeaponFire","sfx/sfx_weaponfire.mp3");

		// Store the centre of the screen
		screenCentre.x = gameScreen.getGame().getScreenWidth() / 2;
		screenCentre.y = gameScreen.getGame().getScreenHeight() / 2;

		// Define the maximum velocities and accelerations of the spaceship
		maxAcceleration = 300.0f;
		maxVelocity = 100.0f;
		maxAngularVelocity = 1440.0f;
		maxAngularAcceleration = 1440.0f;

		// Creates a new list of Lasers with the initial capacity of 100
		mLasers = new ArrayList<Laser>(100);

		// Allows the player to start firing
		reloadTime = gameScreen.getGame().getTargetFramesPerSecond();
		timeToReload = 0;
		canFire = true;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Update & Draw Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Updates the player spaceship.
	 *
	 * @param elapsedTime
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		// Consider any touch events occurring since the update
		Input input = mGameScreen.getGame().getInput();

		if (input.existsTouch(0)) {
			// Get the primary touch event
			playerTouchAcceleration.x = (input.getTouchX(0) - screenCentre.x)
					/ screenCentre.x;
			playerTouchAcceleration.y = (screenCentre.y - input.getTouchY(0))
					/ screenCentre.y; // Invert the for y axis

			// Convert into an input acceleration
			acceleration.x = playerTouchAcceleration.x * maxAcceleration;
			acceleration.y = playerTouchAcceleration.y * maxAcceleration;
		}

		// Ensure that the ships points in the direction of movement
		angularAcceleration = SteeringBehaviours.alignWithMovement(this);

		// Dampen the linear and angular acceleration and velocity
		angularAcceleration *= 0.95f;
		angularVelocity *= 0.75f;
		acceleration.multiply(0.75f);
		velocity.multiply(0.95f);

		// Apply the determined accelerations
		super.update(elapsedTime);

		// If there is Lasers update them
		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.update(elapsedTime);
		}

		// Checks if the player can't fire
		if (!canFire) {
			// Increments timetoreload
			timeToReload = timeToReload + 1;

			// If the reloadTime is equal to timeToReload then the player can fire
			if (reloadTime == timeToReload) {
				canFire = true;
			}
		}
	}

	/**
	 * Draws the player spaceship on screen.
	 *
	 * @param elapsedTime
	 * @param graphics2D
	 * @param mLayerViewport
	 * @param mScreenViewport
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
		// If there is any lasers to update then update them
		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		}

		super.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the Lives of the Player Spaceship.
	 *
	 * @param lives Lives for the player
	 */
	public void setLives(int lives) {
		livesLeft = lives;
	}

	/**
	 * Takes away one live from the player.
	 */
	public void minusLive() {
		livesLeft = livesLeft - 1;
		livesLost = livesLost + 1;
	}

	/**
	 * Gets the amount of lives lost by the player
	 *
	 * @return Lives Lost
	 */
	public int getLivesLost() {
		return livesLost;
	}

	/**
	 * Gets the amount of the lives left for the player.
	 *
	 * @return Lives Left
	 */
	public int getLivesLeft() {
		return livesLeft;
	}

	/**
	 * Fires a laser from the player spaceship.
	 *
	 * @param gameScreen Game Screen to draw the lasers on.
	 */
	public void fire(GameScreen gameScreen) {
		// Checks if the player can fire a laser
		if (canFire) {
			// Gets the sound effect from the asset store and plays it
			gameScreen.getGame().getAssetManager().getSound("WeaponFire").play(settingsHandler.getSound(gameScreen.getGame().getContext()));

			// Sets canFire to false
			canFire = false;

			// Gets the picture of the laser and creates a Laser with it
			gameScreen.getGame().getAssetManager().loadAndAddBitmap("PlayerBeam", "img/sprites/sprPlayerBeam.png");
			mLasers.add(new Laser((int) position.x, (int) position.y, gameScreen, gameScreen.getGame().getAssetManager().getBitmap("PlayerBeam"), this.acceleration, this.velocity, orientation));

			// Starts the reload process.
			timeToReload = 0;
		}
	}
}

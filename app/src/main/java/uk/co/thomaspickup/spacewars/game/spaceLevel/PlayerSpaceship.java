package uk.co.thomaspickup.spacewars.game.spaceLevel;

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

/**
 * Player controlled spaceship
 * 
 * @version 1.0
 */
public class PlayerSpaceship extends Sprite {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Centre of the screen (used to determine the offset of touch events)
	 */
	private Vector2 screenCentre = new Vector2();

	/**
	 * Acceleration vector based on the player's touch input
	 */
	private Vector2 playerTouchAcceleration = new Vector2();

	// Contains the lives lost and left
	private int livesLost;
	private int livesLeft;

	// List of lasers
	public List<Laser> mLasers;

	private int reloadTime;
	private int timeToReload;
	private boolean canFire;
	// /////////////////////////////////////////////////////////////////////////
	// Constructors
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

		// Store the centre of the screen
		screenCentre.x = gameScreen.getGame().getScreenWidth() / 2;
		screenCentre.y = gameScreen.getGame().getScreenHeight() / 2;

		// Define the maximum velocities and accelerations of the spaceship
		maxAcceleration = 300.0f;
		maxVelocity = 100.0f;
		maxAngularVelocity = 1440.0f;
		maxAngularAcceleration = 1440.0f;

		mLasers = new ArrayList<Laser>(100);

		reloadTime = gameScreen.getGame().getTargetFramesPerSecond();
		timeToReload = 0;
		canFire = true;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.Sprite#update(uk.ac.qub.eeecs.gage.engine.
	 * ElapsedTime)
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

		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.update(elapsedTime);
		}

		if (!canFire) {
			timeToReload = timeToReload + 1;

			if (reloadTime == timeToReload) {
				canFire = true;
			}
		}
	}

	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		}
		super.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
	}

	// Sets the lives of the player ship
	public void setLives(int lives) {
		livesLeft = lives;
	}

	// Minuses a live from the livesLeft and adds one to livesLost
	public void minusLive() {
		livesLeft = livesLeft - 1;
		livesLost = livesLost + 1;
	}

	// Gets the lives lost
	public int getLivesLost() {
		return livesLost;
	}

	// Gets the lives left
	public int getLivesLeft() {
		return livesLeft;
	}

	// Creates a new laser
	public void fire(GameScreen gameScreen) {
		if (canFire) {
			canFire = false;
			gameScreen.getGame().getAssetManager().loadAndAddBitmap("PlayerBeam", "img/sprites/sprPlayerBeam.png");
			mLasers.add(new Laser((int) position.x, (int) position.y, gameScreen, gameScreen.getGame().getAssetManager().getBitmap("PlayerBeam"), this.acceleration, this.velocity, orientation));
			timeToReload = 0;
		}
	}

	public List<Laser> getMLasers() {
		return mLasers;
	}
}

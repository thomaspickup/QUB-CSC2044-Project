package uk.co.thomaspickup.spacewars.game.spaceLevel;

import uk.co.thomaspickup.spacewars.gage.ai.SteeringBehaviours;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
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
}

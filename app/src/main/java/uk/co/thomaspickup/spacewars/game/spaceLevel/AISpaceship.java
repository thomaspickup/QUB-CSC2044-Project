package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;
import uk.co.thomaspickup.spacewars.gage.ai.SteeringBehaviours;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
import uk.co.thomaspickup.spacewars.gage.world.Sprite;
import uk.co.thomaspickup.spacewars.game.HelperTools;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;

/**
 * This class represents an AI Controlled Spaceship.
 * It can be a turret or a seeker.
 * 
 * Created by Thomas Pickup
 */
public class AISpaceship extends Sprite {

	// /////////////////////////////////////////////////////////////////////////
	// Variables
	// /////////////////////////////////////////////////////////////////////////

	// Control Behaviour Enum
	public enum ShipBehaviour {
		Turret, Seeker
	}

	// Object holding the Control Behaviour
	private ShipBehaviour mShipBehaviour;

	// Distance at which the spaceship should avoid other game objects
	private float separateThresholdShip = 75.0f;
	private float separateThresholdAsteroid = 125.0f;

	// Accumulators used to build up the net steering outcome
	private Vector2 accAccumulator = new Vector2();
	private Vector2 accComponent = new Vector2();

	// Creates new instance of helperTools
	HelperTools helperTools = new HelperTools();

	// List of Lasers related to AISpaceship
	public List<Laser> mLasers;

	// Limit on the reloading time of the aispaceship
	private int reloadTime;
	private int timeToReload;
	private boolean canFire;

	// Creates a new instance of the Settings Handler
	private SettingsHandler settingsHandler = new SettingsHandler();

	// Speed multiplier decided by difficulty setting
	private float mSpeedMultiplier;

	// /////////////////////////////////////////////////////////////////////////
	// Constructor
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates an AI controlled spaceship
	 * 
	 * @param startX
	 *            x location of the AI spaceship
	 * @param startY
	 *            y location of the AI spaceship
	 * @param shipBehaviour
	 *            Steering behaviour to be used by the AI ship
	 * @param gameScreen
	 *            Gamescreen to which AI belongs
	 */
	public AISpaceship(float startX, float startY, ShipBehaviour shipBehaviour,
			SpaceLevelScreen gameScreen, int difficulty, int health) {
		super(startX, startY, 50.0f, 50.0f, null, gameScreen);

		// Imports the speed multiplier from helper tools
		this.mSpeedMultiplier = helperTools.getSpeedMultiplier(difficulty);

		// Copies over the ship behaviour
		mShipBehaviour = shipBehaviour;

		// Sets up the ship based on
		setUpShip(gameScreen);

		// Sets health based on supplied
		this.setHealth(health);

		// Sets the ship able to fire
		reloadTime = gameScreen.getGame().getTargetFramesPerSecond();
		timeToReload = 0;
		canFire = true;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Draw and Update Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Updates the AI Spaceship.
	 *
	 * @param elapsedTime
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		switch (mShipBehaviour) {
		case Turret:
			// Turn towards the player
			angularAcceleration = 
				SteeringBehaviours.lookAt(this, 
						((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position);
			break;
		case Seeker:
			// Seek towards the player
			SteeringBehaviours.seek(this, 
					((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position,
					acceleration);

			// Try to avoid a collision with the playership
			SteeringBehaviours.separate(this, 
					((SpaceLevelScreen) mGameScreen).getPlayerSpaceship(),
					separateThresholdShip, 1.0f, accComponent);
			accAccumulator.set(accComponent);
			
			// Try to avoid a collision with the other spaceships			
			SteeringBehaviours.separate(this,
					((SpaceLevelScreen) mGameScreen).getAISpaceships(),
					separateThresholdShip, 1.0f, accComponent);
			accAccumulator.add(accComponent);
			
			// Try to avoid a collision with the asteroids
			SteeringBehaviours.separate(this,
					((SpaceLevelScreen) mGameScreen).getAsteroids(),
					separateThresholdAsteroid, 1.0f, accComponent);
			accAccumulator.add(accComponent);

			// If we are trying to avoid a collision then combine
			// it with the seek behaviour, placing more emphasis on
			// avoiding the collision.			
			if (!accAccumulator.isZero()) {
				acceleration.x = 0.3f * acceleration.x + 0.7f * accAccumulator.x;
				acceleration.y = 0.3f * acceleration.y + 0.7f * accAccumulator.y;
			}

			// Make sure we point in the direction of travel.
			angularAcceleration = SteeringBehaviours.alignWithMovement(this);

			break;
		}

		// Call the sprite's superclass to apply the determine accelerations
		super.update(elapsedTime);

		// Gets the difference in player position and this AI Spaceships position
		float deltaX = ((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position.x - this.position.x;
		float deltaY = ((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position.y - this.position.y;

		// Works out the direct distance from the player
		float distanceFromPlayer = helperTools.getDistance(deltaX, deltaY);

		// If the distance is less than 3 times the width of the spaceship and it can fire then fire.
		if (distanceFromPlayer <= this.getBound().getWidth() * 3 && canFire) {
			fire(mGameScreen);
		}

		// If there is lasers to update then update them
		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.update(elapsedTime);
		}

		// If the ai spaceship can't fire
		if (!canFire) {
			// Increment time to reload by 1
			timeToReload = timeToReload + 1;

			// If the time to reload is equal to the reloadtime then the spaceship can fire again.
			if (reloadTime == timeToReload) {
				canFire = true;
			}
		}
	}

	/**
	 * Draws the AI Spaceship and lasers on screen.
	 *
	 * @param elapsedTime
	 * @param graphics2D
	 * @param mLayerViewport
	 * @param mScreenViewport
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport)  {
		// Draws all the lasers if they exist.
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
	 * This shoots a laser from the AI Spaceship
	 *
	 * @param gameScreen Game Screen to draw onto
	 */
	private void fire(GameScreen gameScreen) {
		// Checks if the Enemey can fire
		if (canFire) {
			// Play the sound effect
			gameScreen.getGame().getAssetManager().getSound("WeaponFire").play(settingsHandler.getSound(gameScreen.getGame().getContext()));

			// Set can fire to false
			canFire = false;

			// Imports the image
			gameScreen.getGame().getAssetManager().loadAndAddBitmap("EnemyBeam", "img/sprites/sprEnemyBeam.png");

			// Creates a new laser
			mLasers.add(new Laser((int) position.x, (int) position.y, gameScreen, gameScreen.getGame().getAssetManager().getBitmap("EnemyBeam"), this.acceleration, this.velocity, orientation));

			// Resets the timer
			timeToReload = 0;
		}
	}

	/**
	 * Sets up the ship based on the Ship Behaviour
	 */
	private void setUpShip(GameScreen gameScreen) {
		// Decides what type the ship is to be.
		switch (mShipBehaviour) {
			// Sets up to be a turret
			case Turret:
				// Limits turrets to being able to turn to look at player
				maxAcceleration = 0.0f;
				maxVelocity = 0.0f;
				maxAngularVelocity = 50.0f* mSpeedMultiplier;
				maxAngularAcceleration = 50.0f * mSpeedMultiplier;
				mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret");

				break;

			// Sets up to be a seeker
			case Seeker:
				// Allows seeker to follow the player around the map
				maxAcceleration = 30.0f * mSpeedMultiplier;
				maxVelocity = 50.0f * mSpeedMultiplier;
				maxAngularVelocity = 150.0f * mSpeedMultiplier;
				maxAngularAcceleration = 300.0f * mSpeedMultiplier;
				mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship1");

				break;
		}

		// Creates a list with a maximum of 100 lasers
		mLasers = new ArrayList<Laser>(100);
	}
}

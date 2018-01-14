package uk.co.thomaspickup.spacewars.game.spaceLevel;

import uk.co.thomaspickup.spacewars.gage.ai.SteeringBehaviours;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.Sprite;

/**
 * AI controlled spaceship
 * 
 * @version 1.0
 */
public class AISpaceship extends Sprite {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * AI control behaviour
	 */
	public enum ShipBehaviour {
		Turret, Seeker
	}

	private ShipBehaviour mShipBehaviour;

	/**
	 * Distance at which the spaceship should avoid other game objects
	 */
	private float separateThresholdShip = 75.0f;
	private float separateThresholdAsteroid = 125.0f;

	/**
	 * Accumulators used to build up the net steering outcome
	 */
	private Vector2 accAccumulator = new Vector2();
	private Vector2 accComponent = new Vector2();
	
	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a AI controlled spaceship
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
			SteeringDemoGameScreen gameScreen) {
		super(startX, startY, 50.0f, 50.0f, null, gameScreen);

		mShipBehaviour = shipBehaviour;

		switch (mShipBehaviour) {
		case Turret:
			maxAcceleration = 0.0f;
			maxVelocity = 0.0f;
			maxAngularVelocity = 50.0f;
			maxAngularAcceleration = 50.0f;
			mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret");
			break;
		case Seeker:
			maxAcceleration = 30.0f;
			maxVelocity = 50.0f;
			maxAngularVelocity = 150.0f;
			maxAngularAcceleration = 300.0f;
			mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship2");
			break;
		}
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

		switch (mShipBehaviour) {
		case Turret:
			// Turn towards the player
			angularAcceleration = 
				SteeringBehaviours.lookAt(this, 
						((SteeringDemoGameScreen) mGameScreen).getPlayerSpaceship().position);
			break;
		case Seeker:
			// Seek towards the player
			SteeringBehaviours.seek(this, 
					((SteeringDemoGameScreen) mGameScreen).getPlayerSpaceship().position, 
					acceleration);

			// Try to avoid a collision with the playership
			SteeringBehaviours.separate(this, 
					((SteeringDemoGameScreen) mGameScreen).getPlayerSpaceship(), 
					separateThresholdShip, 1.0f, accComponent);
			accAccumulator.set(accComponent);
			
			// Try to avoid a collision with the other spaceships			
			SteeringBehaviours.separate(this,
					((SteeringDemoGameScreen) mGameScreen).getAISpaceships(),
					separateThresholdShip, 1.0f, accComponent);
			accAccumulator.add(accComponent);
			
			// Try to avoid a collision with the asteroids
			SteeringBehaviours.separate(this,
					((SteeringDemoGameScreen) mGameScreen).getAsteroids(),
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
	}
}

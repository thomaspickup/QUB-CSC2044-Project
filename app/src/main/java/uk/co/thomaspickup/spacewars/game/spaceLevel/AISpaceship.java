package uk.co.thomaspickup.spacewars.game.spaceLevel;

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
 * AI controlled spaceship
 * 
 * @version 1.0
 */
// TODO: Optimize and annotate
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

	// Creates new instance of helperTools
	HelperTools helperTools = new HelperTools();

	public List<Laser> mLasers;

	private int reloadTime;
	private int timeToReload;
	private boolean canFire;

	private SettingsHandler settingsHandler = new SettingsHandler();

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
			SpaceLevelScreen gameScreen, int difficulty, int health) {
		super(startX, startY, 50.0f, 50.0f, null, gameScreen);

		float speedMultiplier = helperTools.getSpeedMultiplier(difficulty);

		mShipBehaviour = shipBehaviour;

		switch (mShipBehaviour) {
		case Turret:
			maxAcceleration = 0.0f;
			maxVelocity = 0.0f;
			maxAngularVelocity = 50.0f* speedMultiplier;
			maxAngularAcceleration = 50.0f * speedMultiplier;
			mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret");

			break;
		case Seeker:
			maxAcceleration = 30.0f * speedMultiplier;
			maxVelocity = 50.0f * speedMultiplier;
			maxAngularVelocity = 150.0f * speedMultiplier;
			maxAngularAcceleration = 300.0f * speedMultiplier;
			mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship1");
			break;
		}

		mLasers = new ArrayList<Laser>(100);

		this.setHealth(health);
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

		float deltaX = ((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position.x - this.position.x;
		float deltaY = ((SpaceLevelScreen) mGameScreen).getPlayerSpaceship().position.y - this.position.y;

		float distanceFromPlayer = helperTools.getDistance(deltaX, deltaY);

		if (distanceFromPlayer <= this.getBound().getWidth() * 3 && canFire) {
			fire(mGameScreen);
		}

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
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport)  {
		if (mLasers != null) {
			for (Laser laser : mLasers)
				laser.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		}

		super.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
	}

	// Creates a new laser
	public void fire(GameScreen gameScreen) {
		if (canFire) {
			gameScreen.getGame().getAssetManager().getSound("WeaponFire").play(settingsHandler.getSound(gameScreen.getGame().getContext()));
			canFire = false;
			gameScreen.getGame().getAssetManager().loadAndAddBitmap("EnemyBeam", "img/sprites/sprEnemyBeam.png");
			mLasers.add(new Laser((int) position.x, (int) position.y, gameScreen, gameScreen.getGame().getAssetManager().getBitmap("EnemyBeam"), this.acceleration, this.velocity, orientation));
			timeToReload = 0;
		}
	}
}

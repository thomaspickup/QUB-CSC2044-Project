package uk.co.thomaspickup.spacewars.game.spaceDemo;

import java.util.Random;

import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.Sprite;

/**
 * Simple asteroid
 * 
 * @version 1.0
 */
public class Asteroid extends Sprite {

	/**
	 * Random instance used to create the asteroids
	 */
	private static Random random = new Random();

	/**
	 * Create an asteroid
	 * 
	 * @param startX
	 *            x location of the asteroid
	 * @param startY
	 *            y location of the asteroid
	 * @param gameScreen
	 *            Gamescreen to which asteroid belongs
	 */
	public Asteroid(float startX, float startY, GameScreen gameScreen) {
		super(startX, startY, 50.0f, 50.0f, null, gameScreen);

		mBitmap = gameScreen.getGame().getAssetManager()
				.getBitmap(random.nextBoolean() ? "Asteroid1" : "Asteroid2");

		mBound.halfWidth = 25.0f;
		mBound.halfHeight = 25.0f;
		
		angularVelocity = random.nextFloat() * 40.0f - 20.0f;
	}
		
}

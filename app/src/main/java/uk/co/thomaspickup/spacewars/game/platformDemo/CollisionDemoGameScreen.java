package uk.co.thomaspickup.spacewars.game.platformDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.util.BoundingBox;
import uk.co.thomaspickup.spacewars.gage.util.GraphicsHelper;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * Simple 'platform' game world
 * 
 * @version 1.0
 */
public class CollisionDemoGameScreen extends GameScreen {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Define the width and height of the game world
	 */
	private final float LEVEL_WIDTH = 2000.0f;
	private final float LEVEL_HEIGHT = 320.0f;

	/**
	 * Define the layer and screen view ports
	 */
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;

	/**
	 * Create three simple touch controls for player input
	 */
	private SimpleControl moveLeft, moveRight, jumpUp;
	private List<SimpleControl> mControls = new ArrayList<SimpleControl>();

	/**
	 * Define an array of sprites to populate the game world
	 */
	private GameObject[] mPlatforms;

	/**
	 * Define the player
	 */
	private PlayerSphere mPlayer;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a simple platform game level
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public CollisionDemoGameScreen(Game game) {
		super("CollisionDemoGameScreen", game);

		// Create the view ports
		mLayerViewport = new LayerViewport(240, 160, 240, 160);
		mScreenViewport = new ScreenViewport();
		GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);

		// Load in the assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("Platform", "img/Platform.png");
		assetManager.loadAndAddBitmap("Ball", "img/Ball.png");
		assetManager.loadAndAddBitmap("RightArrow", "img/RightArrow.png");
		assetManager.loadAndAddBitmap("LeftArrow", "img/LeftArrow.png");
		assetManager.loadAndAddBitmap("UpArrow", "img/UpArrow.png");

		// Create the touch controls
		int screenWidth = game.getScreenWidth();
		int screenHeight = game.getScreenHeight();

		moveLeft = new SimpleControl(
				100.0f, (screenHeight - 100.0f), 100.0f, 100.0f, "LeftArrow", this);
		mControls.add(moveLeft);

		moveRight = new SimpleControl(
				225.0f, (screenHeight - 100.0f), 100.0f, 100.0f, "RightArrow", this);
		mControls.add(moveRight);

		jumpUp = new SimpleControl(
				(screenWidth - 125.0f), (screenHeight - 100.0f), 100.0f, 100.0f, "UpArrow", this);
		mControls.add(jumpUp);

		// Create the player
		mPlayer = new PlayerSphere(100.0f, 100.0f, this);

		// Create the platforms
		int platformWidth = 70;
		int platformHeight = 70;
		int mNumGroundPlatforms = 30;
		int nNumRandomPlatforms = 25;
		Random random = new Random();
		mPlatforms = new GameObject[mNumGroundPlatforms + nNumRandomPlatforms];

		// Create a series of platform to provide the ground. None it would have
		// been better to have a small number of large ground platforms, instead
		// of lots of small ones.
		for (int idx = 0; idx < mNumGroundPlatforms; idx++) {
			mPlatforms[idx] = new GameObject(platformWidth * (idx + 0.5f),
					platformHeight / 2, platformWidth, platformHeight,
					assetManager.getBitmap("Platform"), this);
		}
		
		// Create a number of platforms at random. They are not created in the
		// first 300 units of the level (this is our safe area for creating the player in).
		for (int idx = 0; idx < nNumRandomPlatforms; idx++) {
			mPlatforms[mNumGroundPlatforms + idx] = new GameObject(
					300.0f + (random.nextFloat() * LEVEL_WIDTH),
					(random.nextFloat() * (LEVEL_HEIGHT - platformHeight)),
					platformWidth, platformHeight,
					assetManager.getBitmap("Platform"), this);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Update and Draw
	// /////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameScreen#update(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime)
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {

		// Update the player
		mPlayer.update(elapsedTime, moveLeft.isActivated(),
				moveRight.isActivated(), jumpUp.isActivated(), mPlatforms);
		
		// Ensure the player cannot leave the confines of the world
		BoundingBox playerBound = mPlayer.getBound();
		if (playerBound.getLeft() < 0)
			mPlayer.position.x -= playerBound.getLeft();
		else if (playerBound.getRight() > LEVEL_WIDTH)
			mPlayer.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

		if (playerBound.getBottom() < 0)
			mPlayer.position.y -= playerBound.getBottom();
		else if (playerBound.getTop() > LEVEL_HEIGHT)
			mPlayer.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

		// Focus the layer viewport on the player's x location
		mLayerViewport.x = mPlayer.position.x;

		// Ensure the viewport cannot leave the confines of the world
		if (mLayerViewport.getLeft() < 0)
			mLayerViewport.x -= mLayerViewport.getLeft();
		else if (mLayerViewport.getRight() > LEVEL_WIDTH)
			mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

		if (mLayerViewport.getBottom() < 0)
			mLayerViewport.y -= mLayerViewport.getBottom();
		else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
			mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);
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

		graphics2D.clear(Color.WHITE);

		// Draw the player
		mPlayer.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

		// Draw each of the platforms
		for (GameObject platform : mPlatforms)
			platform.draw(elapsedTime, graphics2D, mLayerViewport,
					mScreenViewport);

		// Draw the controls last of all
		for (SimpleControl simpleControl : mControls)
			simpleControl.draw(elapsedTime, graphics2D, mLayerViewport,
					mScreenViewport);
	}
}

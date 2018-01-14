package uk.co.thomaspickup.spacewars.demo.alphaBlending;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.ui.ReleaseButton;
import uk.co.thomaspickup.spacewars.gage.ui.ToggleButton;
import uk.co.thomaspickup.spacewars.gage.util.GraphicsHelper;
import uk.co.thomaspickup.spacewars.gage.util.InputHelper;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * Alpha blend demo
 *
 * @version 1.0
 */
public class AlphaBlendingGameScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the layer and screen view ports
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /**
     * Define the touch controls for the demo
     */
    private ToggleButton mToggleCycle;
    private ReleaseButton mNextScreen;

    private boolean mAdditiveDemo = true;            // True for the additive demo, false for the blended
    private boolean mCycleAlphaIncrease = false;    // True if cycling the alpha values

    /**
     * Define the list of objects that will be lit
     */
    private GameObject mBackground;
    private GameObject mLight;

    private GameObject mSky;
    private GameObject mDunes;
    private GameObject mSmallCloud;
    private GameObject mBigCloud;
    private GameObject mSun;
    private GameObject mBiplane;

    private Vector2 mLayerPosition = new Vector2();    // Position of the light/plane in the demo

    /**
     * Paint instances
     */
    private Paint mAdditivePaint;                    // Additive blending
    private Paint mBlendedPaint;                    // Standard alpha blending
    private Paint mFadedPaint;                        // Controlled alpha blend
    private int mCurrentFade = 128;                    // Current alpha blend

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an alpha blending demo
     *
     * @param game SpaceGame to which this screen belongs
     */
    public AlphaBlendingGameScreen(Game game) {
        super("AlphaBlendingGameScreen", game);

        // Create an additive blend
        mAdditivePaint = new Paint();
        mAdditivePaint.setAntiAlias(true);
        mAdditivePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        // Create a default standard alpha blend
        mBlendedPaint = new Paint();
        mBlendedPaint.setAntiAlias(true);

        // Create a controlled standard alpha blend
        mFadedPaint = new Paint();

        // Create the view ports
        int layerWidth = 480, layerHeight = 320;
        mLayerViewport = new LayerViewport(layerWidth / 2, layerHeight / 2, layerWidth / 2, layerHeight / 2);
        mScreenViewport = new ScreenViewport();
        GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);

        // /////////////////////////////////////////////////////////////////////////
        // Load assets
        // /////////////////////////////////////////////////////////////////////////

        AssetStore assetManager = mGame.getAssetManager();

        // Load assets for the additive blend demo
        assetManager.loadAndAddBitmap("Background", "img/demos/alphaBlending/Background.png");
        assetManager.loadAndAddBitmap("Light", "img/demos/alphaBlending/Light.png");

        // Create and position the objects for the additive blend demo
        mBackground = new GameObject(layerWidth / 2,
                layerHeight / 2, layerWidth, layerHeight,
                assetManager.getBitmap("Background"), this);

        mLight = new GameObject(0, 0, layerWidth / 4, layerWidth / 4,
                assetManager.getBitmap("Light"), this);

        // Load assets for the normal blend demo
        assetManager.loadAndAddBitmap("Sky", "img/demos/alphaBlending/Sky.png");
        assetManager.loadAndAddBitmap("Dunes", "img/demos/alphaBlending/Dunes.png");
        assetManager.loadAndAddBitmap("SmallCloud", "img/demos/alphaBlending/SmallCloud.png");
        assetManager.loadAndAddBitmap("BigCloud", "img/demos/alphaBlending/BigCloud.png");
        assetManager.loadAndAddBitmap("Sun", "img/demos/alphaBlending/Sun.png");
        assetManager.loadAndAddBitmap("Biplane", "img/demos/alphaBlending/Biplane.png");

        // Create and position the objects for the normal blend demo
        mSky = new GameObject(
                layerWidth / 2, layerHeight / 2, layerWidth, layerHeight,
                assetManager.getBitmap("Sky"), this);

        mDunes = new GameObject(
                layerWidth * 0.5f, layerHeight * 0.2f, layerWidth, layerHeight * 0.3f,
                assetManager.getBitmap("Dunes"), this);

        mBigCloud = new GameObject(
                layerWidth * 0.7f, layerHeight * 0.65f, layerWidth * 0.7f, layerHeight * 0.5f,
                assetManager.getBitmap("BigCloud"), this);

        mBiplane = new GameObject(
                layerWidth * 0.5f, layerHeight * 0.5f, layerWidth * 0.25f, layerHeight * 0.25f,
                assetManager.getBitmap("Biplane"), this);

        mSmallCloud = new GameObject(
                layerWidth * 0.2f, layerHeight * 0.5f, layerWidth * 0.4f, layerHeight * 0.3f,
                assetManager.getBitmap("SmallCloud"), this);

        mSun = new GameObject(
                layerWidth * 0.9f, layerHeight * 0.9f, layerWidth * 0.15f, layerHeight * 0.15f,
                assetManager.getBitmap("Sun"), this);

        // Load assets for the controls
        assetManager.loadAndAddBitmap("NextButton", "img/icons/refresh.png");
        assetManager.loadAndAddBitmap("NextButtonPressed", "img/icons/refresh_selected.png");
        assetManager.loadAndAddBitmap("PlayButton", "img/icons/play.png");
        assetManager.loadAndAddBitmap("PlayButtonPressed", "img/icons/play_selected.png");
        assetManager.loadAndAddBitmap("StopButton", "img/icons/pause.png");
        assetManager.loadAndAddBitmap("StopButtonPressed", "img/icons/pause_selected.png");

        // Create the touch controls
        int screenWidth = game.getScreenWidth();
        int screenHeight = game.getScreenHeight();

        mNextScreen = new ReleaseButton(
                screenWidth * 0.15f, screenHeight * 0.9f, screenWidth * 0.08f, screenWidth * 0.08f,
                "NextButton", "NextButtonPressed", this);

        mToggleCycle = new ToggleButton(
                screenWidth * 0.25f, screenHeight * 0.9f, screenWidth * 0.08f, screenWidth * 0.08f,
                "StopButton", "StopButtonPressed", "PlayButton", "PlayButtonPressed", this);
        mToggleCycle.setToggled(true);
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

        // Update the position of the torch/plane based on the most recent touch location
        List<TouchEvent> touchEvents = mGame.getInput().getTouchEvents();
        if (touchEvents.size() > 0) {
            // Get the first touch point and convert it into layer coordinates
            TouchEvent touchEvent = touchEvents.get(0);
            InputHelper.convertScreenPosIntoLayer(mScreenViewport, touchEvent.x, touchEvent.y, mLayerViewport, mLayerPosition);

            // Set the position of the relevant demo object
            if (mAdditiveDemo)
                mLight.setPosition(mLayerPosition.x, mLayerPosition.y);
            else
                mBiplane.setPosition(mLayerPosition.x, mLayerPosition.y);
        }

        // Update the next screen control, triggering a screen change if needed
        mNextScreen.update(elapsedTime);
        if (mNextScreen.pushTriggered()) {
            mAdditiveDemo = !mAdditiveDemo;
        }

        // Update the cycle alpha control, stopping/starting the cycle as needed
        mToggleCycle.update(elapsedTime);
        if (mToggleCycle.isToggledOn()) {
            mCurrentFade += mCycleAlphaIncrease ? 1 : -1;
            if (mCurrentFade > 254) mCycleAlphaIncrease = false;
            if (mCurrentFade < 30) mCycleAlphaIncrease = true;
        }
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

        graphics2D.clear(Color.BLACK);

        // Update the alpha value based on the current fade amount
        mFadedPaint.setAlpha(mCurrentFade);

        // Draw the additive or normal alpha blend elements
        if (mAdditiveDemo) {
            // Draw the background and light
            mBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, mFadedPaint);
            mLight.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, mAdditivePaint);
        } else {
            // Draw these objects in the right depth order (back to front)
            mSky.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            mDunes.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            mBiplane.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            mSmallCloud.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, mFadedPaint);
            mBigCloud.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, mFadedPaint);
            mSun.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        // Draw the controls last of all
        mNextScreen.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        mToggleCycle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }
}

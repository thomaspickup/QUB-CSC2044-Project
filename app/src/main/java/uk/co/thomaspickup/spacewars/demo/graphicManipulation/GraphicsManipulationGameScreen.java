package uk.co.thomaspickup.spacewars.demo.graphicManipulation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.ui.ReleaseButton;
import uk.co.thomaspickup.spacewars.gage.util.GraphicsHelper;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * Graphics manipulation demo
 *
 * @version 1.0
 */
public class GraphicsManipulationGameScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Very basic particle class
     */
    private class Particle {
        public Particle(float x, float y) {
            this.x = x;
            this.y = y;
            orientation = 0.0f;
            scale = 1.0f;
            alpha = 1.0f;
            bitmap = mGame.getAssetManager().getBitmap("Particle");
        }

        Bitmap bitmap;          // Particle bitmap
        float x;                // x screen location
        float y;                // y screen location
        float orientation;      // orientation (degrees)
        float scale;            // scale
        float alpha;            // alpha (0-1)
    }

    /**
     * Define the particle array
     */
    private final int NUM_PARTICLES = 50;
    private Particle mParticles[] = new Particle[NUM_PARTICLES];
    private double startTimeReference;

    // /////////////////////////////////////////////////////////////////////////
    // Other Miscallaneous Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the layer and screen view ports
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /**
     * Define the next and prev demo buttons (to be used when showing more than one pattern)
     */
    private ReleaseButton mNextDemo;
    private ReleaseButton mPrevDemo;

    /**
     * Paint and Matrix instance used to support rendering
     */
    private Paint mFadedPaint = new Paint();
    private Matrix drawMatrix = new Matrix();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an alpha blending demo
     *
     * @param game SpaceGame to which this screen belongs
     */
    public GraphicsManipulationGameScreen(Game game) {
        super("AlphaBlendingGameScreen", game);

        AssetStore assetManager = mGame.getAssetManager();

        int screenWidth = game.getScreenWidth();
        int screenHeight = game.getScreenHeight();

        // Setup the viewports
        int layerWidth = 480, layerHeight = 320;
        mLayerViewport = new LayerViewport(layerWidth / 2, layerHeight / 2, layerWidth / 2, layerHeight / 2);
        mScreenViewport = new ScreenViewport();
        GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);

        // Load assets and create the controls - to be used when showing more than one demo
        assetManager.loadAndAddBitmap("NextButton", "img/icons/fast_forward.png");
        assetManager.loadAndAddBitmap("NextButtonPressed", "img/icons/fast_forward_selected.png");
        assetManager.loadAndAddBitmap("PrevButton", "img/icons/rewind.png");
        assetManager.loadAndAddBitmap("PrevButtonPressed", "img/icons/rewind_selected.png");

        // Create the touch controls
        mPrevDemo = new ReleaseButton(
                screenWidth * 0.15f, screenHeight * 0.9f, screenWidth * 0.08f, screenWidth * 0.08f,
                "PrevButton", "PrevButtonPressed", this);
        mNextDemo = new ReleaseButton(
                screenWidth * 0.25f, screenHeight * 0.9f, screenWidth * 0.08f, screenWidth * 0.08f,
                "NextButton", "NextButtonPressed", this);

        // /////////////////////////////////////////////////////////////////////////
        // Create particles
        // /////////////////////////////////////////////////////////////////////////

        // Load assets and create particle array
        assetManager.loadAndAddBitmap("Particle", "img/demos/graphicsManipulation/Particle.png");

        for (int idx = 0; idx < NUM_PARTICLES; idx++) {
            mParticles[idx] = new Particle(screenWidth / 3, screenHeight / 3);
        }

        startTimeReference = -1.0f;
        setupParticles();
    }

    /**
     * Setup the particles in their starting position, orientation, etc.
     */
    private void setupParticles() {
        // Your code to go here - if required
        // Your code to go here - if required
        // Your code to go here - if required
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

        // Update the timing info
        if(startTimeReference == -1.0f)
            startTimeReference = elapsedTime.totalTime;

        // Update the particles
        updateParticles((float)(elapsedTime.totalTime-startTimeReference));

        // Only of use when displaying more than one demo
        // mNextDemo.update(elapsedTime);
        // mPrevDemo.update(elapsedTime);
    }

    /**
     * Update the particles
     */
    private void updateParticles(float runTime) {

        // Your code to go here - if required
        // Your code to go here - if required
        // Your code to go here - if required



        rotateParticle(2, 1);
        scaleParticle(1, 1.1f);
        fadeParticle(1, -0.01f);
        moveParticle(2, 0.01f, 0.01f);
    }


    /**
     * @param particleIdx
     * @param amount      Degrees
     */
    public void rotateParticle(int particleIdx, float amount) {
        if (particleIdx < 0 || particleIdx >= NUM_PARTICLES) return;
        mParticles[particleIdx].orientation += amount;
    }

    public void scaleParticle(int particleIdx, float amount) {
        if (particleIdx < 0 || particleIdx >= NUM_PARTICLES) return;
        mParticles[particleIdx].scale *= amount;
    }

    public void fadeParticle(int particleIdx, float amount) {
        if (particleIdx < 0 || particleIdx >= NUM_PARTICLES) return;
        mParticles[particleIdx].alpha += amount;
    }

    public void moveParticle(int particleIdx, float xamount, float yamount) {
        if (particleIdx < 0 || particleIdx >= NUM_PARTICLES) return;
        int screenWidth = this.getGame().getScreenWidth();
        int screenHeight = this.getGame().getScreenHeight();
        mParticles[particleIdx].x += xamount * screenWidth;
        mParticles[particleIdx].y += yamount * screenHeight;
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

        for (int idx = 0; idx < NUM_PARTICLES; idx++) {
            Particle particle = mParticles[idx];
            float halfWidth = particle.scale * particle.bitmap.getWidth() / 2.0f;
            float halfHeight = particle.scale * particle.bitmap.getHeight() / 2.0f;

            // Update the alpha value based on the current fade amount
            mFadedPaint.setAlpha((int) (255.0f * particle.alpha));

            drawMatrix.reset();
            drawMatrix.postScale(particle.scale, particle.scale);
            drawMatrix.postRotate(particle.orientation, halfWidth, halfHeight);
            drawMatrix.postTranslate(particle.x - halfWidth, particle.y - halfHeight);

            graphics2D.drawBitmap(particle.bitmap, drawMatrix, mFadedPaint);
        }

        mNextDemo.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        mPrevDemo.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }
}

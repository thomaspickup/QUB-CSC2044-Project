package uk.co.thomaspickup.spacewars.gage.ui;

import android.graphics.Bitmap;

import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.audio.Sound;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchHandler;
import uk.co.thomaspickup.spacewars.gage.util.BoundingBox;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * Initial starter for a release button.
 * <p>
 * Note: This is an incomplete class. It is assumed you will complete/refine the functionality.
 * <p>
 * Important: Current this button is assumed to be defined in screen space (not world space)
 */
public class ReleaseButton extends GameObject {

    ///////////////////////////////////////////////////////////////////////////
    // Class data: PushButton look and sound                                 //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Name of the graphical asset used to represent the default button state
     */
    protected Bitmap mDefaultBitmap;

    /**
     * Name of the graphical asset used to represent the pushed button state
     */
    protected Bitmap mPushBitmap;

    /**
     * Name of the sound asset to be played whenever the button is clicked
     */
    protected Sound mReleaseSound;


    ///////////////////////////////////////////////////////////////////////////
    // Constructors                                                          //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create a new release button.
     *
     * @param x             Centre y location of the button
     * @param y             Centre x location of the button
     * @param width         Width of the button
     * @param height        Height of the button
     * @param defaultBitmap Bitmap used to represent this control
     * @param pushBitmap    Bitmap used to represent this control
     * @param releaseSound  Bitmap used to represent this control
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         String releaseSound,
                         GameScreen gameScreen) {
        super(x, y, width, height,
                gameScreen.getGame().getAssetManager().getBitmap(defaultBitmap), gameScreen);

        AssetStore assetStore = gameScreen.getGame().getAssetManager();

        mDefaultBitmap = assetStore.getBitmap(defaultBitmap);
        mPushBitmap = assetStore.getBitmap(pushBitmap);

        mReleaseSound = (releaseSound == null) ? null : assetStore.getSound(releaseSound);

    }

    /**
     * Create a new release button.
     *
     * @param x             Centre y location of the button
     * @param y             Centre x location of the button
     * @param width         Width of the button
     * @param height        Height of the button
     * @param defaultBitmap Bitmap used to represent this control
     * @param pushBitmap    Bitmap used to represent this control
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         GameScreen gameScreen) {
        this(x, y, width, height, defaultBitmap, pushBitmap, null, gameScreen);
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    private boolean mPushTriggered;
    private boolean mIsPushed;

    /**
     * Update the button
     *
     * @param elapsedTime Elapsed time information
     */
    public void update(ElapsedTime elapsedTime) {
        // Consider any touch events occurring in this update
        Input input = mGameScreen.getGame().getInput();
        BoundingBox bound = getBound();


        // Check for a press release on this button
        for (TouchEvent touchEvent : input.getTouchEvents()) {
            if (touchEvent.type == TouchEvent.TOUCH_UP
                    && bound.contains(touchEvent.x, touchEvent.y)) {
                // A touch up has occurred in this control
                mPushTriggered = true;
                // TODO: Also play sound here if it's available.
                return;
            }
        }

        // Check if any of the touch events were on this control
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    if (!mIsPushed) {
                        mBitmap = mPushBitmap;
                        mIsPushed = true;
                    }

                    return;
                }
            }
        }

        // If we have not returned by this point, then there is no touch event on the button
        if (mIsPushed) {
            mBitmap = mDefaultBitmap;
            mIsPushed = false;
        }
    }


    /**
     * Return true if the button has been triggered.
     * <p>
     * Note: This method will return true once, and only once, per push event.
     *
     * @return True if there has been an unconsumed push event, false otherwise.
     */
    public boolean pushTriggered() {
        if (mPushTriggered) {
            mPushTriggered = false;
            return true;
        }
        return false;
    }

    /**
     * Return the current state of the button.
     *
     * @return True if the button is pushed, otherwise false.
     */
    public boolean isPushed() {
        return mIsPushed;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameObject#draw(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D,
     * uk.ac.qub.eeecs.gage.world.LayerViewport,
     * uk.ac.qub.eeecs.gage.world.ScreenViewport)
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Assumed to be in screen space so just draw the whole thing
        drawScreenRect.set((int) (position.x - mBound.halfWidth),
                (int) (position.y - mBound.halfWidth),
                (int) (position.x + mBound.halfWidth),
                (int) (position.y + mBound.halfHeight));

        graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
    }

}

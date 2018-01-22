package uk.co.thomaspickup.spacewars.game.spaceLevel;

import uk.co.thomaspickup.spacewars.gage.ui.ReleaseButton;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;

/**
 * Created by thomaspickup on 22/01/2018.
 */

public class PauseButton extends ReleaseButton {
    // Variables

    // Overiding Constructors
    public PauseButton(float x, float y, float width, float height,
                       String defaultBitmap,
                       String pushBitmap,
                       GameScreen gameScreen) {
        super(x, y, width, height, defaultBitmap, pushBitmap,gameScreen);
    }

    public PauseButton(float x, float y, float width, float height,
                       String defaultBitmap,
                       String pushBitmap,
                       String releaseSound,
                       GameScreen gameScreen) {
        super(x, y, width, height,defaultBitmap,pushBitmap,releaseSound,gameScreen);
    }

    // Overiding Functions

    // New Functions
}

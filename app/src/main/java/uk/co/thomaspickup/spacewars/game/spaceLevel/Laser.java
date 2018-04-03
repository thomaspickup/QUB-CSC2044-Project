package uk.co.thomaspickup.spacewars.game.spaceLevel;

import android.graphics.Bitmap;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.Sprite;

/**
 * Created by thomaspickup on 03/04/2018.
 */

public class Laser extends Sprite {
    // ints to hold positioning
    private int startX, startY, endX, endY;

    /**
     * Constructor for the laser object
     * @param startX
     * @param startY
     * @param gameScreen
     * @param sprite
     */
    public Laser(int startX, int startY, GameScreen gameScreen, Bitmap sprite, float playerAngularAcceleration) {
        super(startX,startY, 9, 3, sprite, gameScreen);

        orientation = playerAngularAcceleration;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        position.add(startX + 1, startY + 1);
    }
}

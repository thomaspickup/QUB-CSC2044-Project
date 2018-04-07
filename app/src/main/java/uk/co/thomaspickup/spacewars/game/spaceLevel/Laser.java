package uk.co.thomaspickup.spacewars.game.spaceLevel;

import android.graphics.Bitmap;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.util.Vector2;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
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
    public Laser(int startX, int startY, GameScreen gameScreen, Bitmap sprite, Vector2 playerAcceleration, Vector2 playerVelocity, float orientation) {
        super(startX,startY, 9, 3, sprite, gameScreen);

        acceleration.x = playerAcceleration.x + 2;
        acceleration.y = playerAcceleration.y + 2;
        velocity.x = playerVelocity.x + 2;
        velocity.y = playerVelocity.y + 2;

        this.orientation = orientation;

        maxAcceleration = 300.0f;
        maxVelocity = 100.0f;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        float dt = (float) elapsedTime.stepTime;

        // Ensure the maximum acceleration isn't exceeded
        if (acceleration.lengthSquared() > maxAcceleration * maxAcceleration) {
            acceleration.normalise();
            acceleration.multiply(maxAcceleration);
        }

        // Update the velocity using the acceleration and ensure the
        // maximum velocity has not been exceeded
        velocity.add(acceleration.x * dt, acceleration.y * dt);

        if (velocity.lengthSquared() > maxVelocity * maxVelocity) {
            velocity.normalise();
            velocity.multiply(maxVelocity);
        }

        // Update the position using the velocity
        position.add(velocity.x *dt , velocity.y * dt);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
        super.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }
}

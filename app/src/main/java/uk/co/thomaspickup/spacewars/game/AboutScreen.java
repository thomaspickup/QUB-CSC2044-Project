package uk.co.thomaspickup.spacewars.game;

// Imports

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;

/**
 * This screen acts as an version / about screen where the features (version) and credits are shown.
 *
 * Created by Thomas Pickup
 */

// TODO: Add In Moving Background to About Screen
// TODO: Add in variables to be passed over when invocing this screen to leave off background as in main screen
public class AboutScreen extends GameScreen {

    // Create instance of SettingsHandler to allow for easy of referencing
    SettingsHandler settings = new SettingsHandler();


    // Bounds for back button
    private Rect mBackBound;

    // ~~~~ Methods Start ~~~~
    /**
     * Create a simple about screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public AboutScreen(Game game) {
        super("AboutScreen", game);

        // Load in the bitmaps used on the options screen
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("btnBack", "img/buttons/btnBack.png");

        // Sets the bounds for the back button
        int btnBackWidth = 150;
        int btnBackHeight = 150;
        int startX = 50;
        int startY = 50;
        mBackBound = new Rect(startX, startY, startX + btnBackWidth, startY + btnBackHeight);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameScreen#update(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime)
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

         if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                // Remove this screen
                mGame.getScreenManager().removeScreen(this.getName());

                // Create a new instance of menuScreen and add it to screen manager
                MenuScreen menuScreen = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(menuScreen);
            }
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
        // Sets Background
        graphics2D.clear(Color.BLACK);

        // Back Button
        Bitmap btnBack = mGame.getAssetManager().getBitmap("btnBack");
        graphics2D.drawBitmap(btnBack, null, mBackBound,null);
    }
}

package uk.co.thomaspickup.spacewars.game;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.content.Context;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.MainActivity;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.game.spaceLevel.SpaceLevelScreen;

/**
 * Created by Thomas Pickup on 09/03/2018.
 */

public class OptionScreen extends GameScreen {
    // Difficulty
    // Easy = 1
    // Normal = 2
    // Hard = 3
    // Insane = 4
    int currentDifficultySetting;

    // Create instance of settingsHandler to allow for easy of referencing
    settingsHandler settings = new settingsHandler();

    /**
     * Create a simple options screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public OptionScreen(Game game) {
        super("OptionScreen", game);

        currentDifficultySetting = settings.getDifficulty(getGame().getContext());
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

    }
}

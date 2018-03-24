package uk.co.thomaspickup.spacewars.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.Log;

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

    // Bounds for difficultySettings
    private Rect mEasyBound, mNormalBound, mHardBound, mInsaneBound;

    /**
     * Create a simple options screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public OptionScreen(Game game) {
        super("OptionScreen", game);

        currentDifficultySetting = settings.getDifficulty(getGame().getContext());

        // TODO: Create Buttons and import them in through the asset manager.
        // Sets bounds for the difficulty settings stack
        // each button 1/10 screen width x
        // and 150px high
        int startX = (getGame().getScreenWidth() / 4);
        int endX = startX + (getGame().getScreenWidth() / 10);
        int startY = (getGame().getScreenHeight() / 3);
        int endY = startY + 150;
        mEasyBound = new Rect(startX, startY, endX, endY);

        startX = endX + (getGame().getScreenWidth() / 20);
        endX = startX + (getGame().getScreenWidth() / 10);
        mNormalBound = new Rect(startX, startY, endX, endY);

        startX = endX + (getGame().getScreenWidth() / 20);
        endX = startX + (getGame().getScreenWidth() / 10);
        mHardBound = new Rect(startX, startY, endX, endY);

        startX = endX + (getGame().getScreenWidth() / 20);
        endX = startX + (getGame().getScreenWidth() / 10);
        mInsaneBound = new Rect(startX, startY, endX, endY);
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

            if (mEasyBound.contains((int) touchEvent.x,
                    (int) touchEvent.y)) {
                settings.setDifficulty(getGame().getContext(), 1);
                currentDifficultySetting = settings.getDifficulty(getGame().getContext());
            } else if (mNormalBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                settings.setDifficulty(getGame().getContext(), 2);
                currentDifficultySetting = settings.getDifficulty(getGame().getContext());
            } else if (mHardBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                settings.setDifficulty(getGame().getContext(), 3);
                currentDifficultySetting = settings.getDifficulty(getGame().getContext());
            } else if (mInsaneBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                Log.i("InputDetected", "Insane Button Pressed");
                settings.setDifficulty(getGame().getContext(), 4);
                currentDifficultySetting = settings.getDifficulty(getGame().getContext());
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
        if (currentDifficultySetting == 1) {
            graphics2D.clear(Color.WHITE);
        } else if (currentDifficultySetting == 2) {
            graphics2D.clear(Color.YELLOW);
        } else if (currentDifficultySetting == 3) {
            graphics2D.clear(Color.GREEN);
        } else if (currentDifficultySetting == 4) {
            graphics2D.clear(Color.RED);
        }

        // TODO: Draw the toggle buttons rather than temporary placeholders
        Paint paintCan = new Paint();
        paintCan.setColor(Color.BLACK);

        graphics2D.drawText("Easy", mEasyBound.centerX(),mEasyBound.centerY(), paintCan);
        graphics2D.drawText("Normal", mNormalBound.centerX(),mEasyBound.centerY(), paintCan);
        graphics2D.drawText("Hard", mHardBound.centerX(),mEasyBound.centerY(), paintCan);
        graphics2D.drawText("Insane", mInsaneBound.centerX(),mEasyBound.centerY(), paintCan);
    }
}

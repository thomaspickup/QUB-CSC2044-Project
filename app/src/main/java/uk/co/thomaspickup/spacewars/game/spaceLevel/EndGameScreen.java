package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

// Android Graphics

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.game.MenuScreen;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;

/**
 * The end game screen - displays if the player wins or not.
 *
 * Created by Thomas Pickup
 */
public class EndGameScreen extends GameScreen {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    // Boolean to denote whether the player won the game
    boolean gameWon;

    // Bounds for the buttons
    private Rect mExitButtonBound;
    private Rect mTitleBound;

    // New instance of SettingsHandler for accessing settings
    private SettingsHandler settingsHandler = new SettingsHandler();

    // Padding for screen (scaled) @1920x1080 = 50x50
    int paddingY = (int) (getGame().getScreenHeight() * 0.02);
    int paddingX = (int) (getGame().getScreenWidth() * 0.026);

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor to create a new EndGameScreen
     *
     * @param game     Game that this screen is to be built on
     */
    public EndGameScreen(Game game, boolean gameWon) {
        super("EndGameScreen", game);

        // Transfers the status of the game
        this.gameWon = gameWon;

        // Loads in the assets used on this screen
        loadAssets();

        // Sets up the UI Element Bounds
        setUpUI(game);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Draw and update methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Runs every interval updating the screen.
     *
     * @param elapsedTime
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        // Converts touch events to list
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // Checks if a touch event has occured
        if (touchEvents.size() > 0) {
            // Gets the first touch event
            TouchEvent touchEvent = touchEvents.get(0);

            if (mExitButtonBound.contains((int) touchEvent.x,
                    (int) touchEvent.y)) {
                // Plays click sound
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));

                // Removes this screen
                mGame.getScreenManager().removeScreen(this.getName());

                // Creates and launches MenuScreen
                MenuScreen menuScreen = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(menuScreen);
            }
        }
    }

    /**
     * Draws the screen.
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draws the title image
        Bitmap titleImage = mGame.getAssetManager().getBitmap("TitleImage");
        graphics2D.drawBitmap(titleImage, null,mTitleBound,null);

        // Draw Result (Win // Lose)
        Paint paintCan = new Paint();
        paintCan.setColor(Color.BLACK);
        paintCan.setTextSize(100f);

        if (gameWon) {
            int startY = mTitleBound.bottom + paddingY;
            int startX = mTitleBound.left;

            graphics2D.drawText("Winner", startX,startY, paintCan);
        } else {
            int startY = mTitleBound.bottom + paddingY;
            int startX = mTitleBound.left;

            graphics2D.drawText("Loss", startX,startY, paintCan);
        }

        // Draw the exit icon
        Bitmap exitIcon = mGame.getAssetManager().getBitmap("ExitIcon");
        graphics2D.drawBitmap(exitIcon, null, mExitButtonBound, null);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sets up the UI Element bounds used by the Screen
     *
     * @param game Passes the screen to base on
     */
    private void setUpUI(Game game) {
        // Defines the Title Image Rect
        int titleWidth =(int) (game.getScreenWidth() * 0.583); // On 1920 Screen Width = 1120
        int titleHeight = (int) (game.getScreenHeight() *  0.373); // On 1080 Screen Height = 400
        int spacingX = (game.getScreenWidth() / 2) - (titleWidth / 2);
        int spacingY = paddingY * 2;
        mTitleBound = new Rect(spacingX,spacingY, spacingX+titleWidth, spacingY + titleHeight);

        // Defines the Exit Icon Rect
        int btnExitWidth = (int) (game.getScreenWidth() * 0.078);
        int btnExitHeight = (int) (game.getScreenHeight() * 0.138);
        spacingY = paddingY;
        spacingX = game.getScreenWidth() - (btnExitWidth + paddingX);
        mExitButtonBound = new Rect(spacingX, spacingY, spacingX + btnExitWidth, spacingY + btnExitHeight);
    }

    /**
     * Loads in the assets used by this screen
     */
    private void loadAssets() {
        // Creates a new instance of the asset manager
        AssetStore assetManager = mGame.getAssetManager();

        // Loads in bitmaps
        assetManager.loadAndAddBitmap("ExitIcon", "img/buttons/btnExit.png");
        assetManager.loadAndAddBitmap("TitleImage", "img/titles/ttlLogo.png");

        // Loads in sounds
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");
    }
}

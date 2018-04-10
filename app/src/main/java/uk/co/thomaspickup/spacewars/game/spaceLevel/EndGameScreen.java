package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;
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
    Rect mTitleBound;

    // Background files
    ScreenViewport mScreenViewport;
    LayerViewport mLayerViewport;
    GameObject mSpaceBackground;

    // New instance of SettingsHandler for accessing settings
    private SettingsHandler settingsHandler = new SettingsHandler();

    // Padding for screen (scaled) @1920x1080 = 50x50
    int paddingY = (int) (getGame().getScreenHeight() * 0.02);
    int paddingX = (int) (getGame().getScreenWidth() * 0.026);

    // Title UI Elements
    int titleStartX;
    int titleStartY;
    Paint titlePaintCan;
    String titleText;
    Rect mTitleTextBound;

    // Stats UI Elements
    int statsStartX;
    int statsStartY;
    Paint statsPaintCan;
    String statsText;
    Rect mStatsTextBound;
    int enemiesDefeated;

    // Defines the width and height of the level
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor to create a new EndGameScreen
     *
     * @param game     Game that this screen is to be built on
     */
    public EndGameScreen(Game game, boolean gameWon, int enemiesDefeated, LayerViewport mLayerViewport) {
        super("EndGameScreen", game);

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        this.mLayerViewport = mLayerViewport;

        // Transfers the status of the game
        this.gameWon = gameWon;
        this.enemiesDefeated = enemiesDefeated;

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
        // Draw the background first of all
        mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        // Draws the title image
        Bitmap titleImage = mGame.getAssetManager().getBitmap("TitleImage");
        graphics2D.drawBitmap(titleImage, null,mTitleBound,null);

        // Draw Result (Win // Lose)
        graphics2D.drawText(titleText, titleStartX, titleStartY, titlePaintCan);

        // Draw Enemies Defeated
        graphics2D.drawText(statsText, statsStartX, statsStartY, statsPaintCan);

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
        // Create the space background
        mSpaceBackground = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("SpaceBackground"), this);

        // Defines the Title Image Rect
        int titleWidth =(int) (game.getScreenWidth() * 0.583); // On 1920 Screen Width = 1120
        int titleHeight = (int) (game.getScreenHeight() *  0.373); // On 1080 Screen Height = 400
        int spacingX = (game.getScreenWidth() / 2) - (titleWidth / 2);
        int spacingY = paddingY * 2;
        mTitleBound = new Rect(spacingX,spacingY, spacingX+titleWidth, spacingY + titleHeight);

        // Decides based on data passed whether to show a Win or Defeat message
        if (gameWon) {
            titleText = "Winner";
        } else {
            titleText = "Defeat";
        }

        // Sets up the Paint used to draw the titleText
        mTitleTextBound = new Rect();
        titlePaintCan = new Paint();
        titlePaintCan.setColor(Color.WHITE);
        titlePaintCan.setTextSize(100f);

        // Works out the size of the text
        titlePaintCan.getTextBounds(titleText,0,titleText.length(),mTitleTextBound);

        // Sets the startX and startY
        titleStartY = mTitleBound.bottom + (paddingY * 2) + mTitleTextBound.height();
        titleStartX = (getGame().getScreenWidth() / 2) - (mTitleTextBound.width() / 2);

        // Creates the paint can used to draw the statsText
        mStatsTextBound = new Rect();
        statsPaintCan = new Paint();
        statsPaintCan.setColor(Color.WHITE);
        statsPaintCan.setTextSize(40f);

        // Sets up the enemies defeated text
        statsText = "Enemies Defeated: " + enemiesDefeated;

        // Gets the bounds for the statsText
        statsPaintCan.getTextBounds(statsText, 0, statsText.length(), mStatsTextBound);

        // Creates the startX & startY
        statsStartY = titleStartY + (paddingY * 4) + mStatsTextBound.height();
        statsStartX = (getGame().getScreenWidth() / 2) - (mStatsTextBound.width() / 2);

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
        assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");

        // Loads in sounds
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");
    }
}

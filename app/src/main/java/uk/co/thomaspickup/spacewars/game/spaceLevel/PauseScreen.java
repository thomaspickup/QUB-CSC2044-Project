package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

// Android Graphics

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

// Java Util
import java.util.List;

// GAGE
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

// Game
import uk.co.thomaspickup.spacewars.game.MenuScreen;
import uk.co.thomaspickup.spacewars.game.SettingsHandler;
import uk.co.thomaspickup.spacewars.game.spaceLevel.AISpaceship;
import uk.co.thomaspickup.spacewars.game.spaceLevel.Asteroid;
import uk.co.thomaspickup.spacewars.game.spaceLevel.PlayerSpaceship;
import uk.co.thomaspickup.spacewars.game.spaceLevel.SpaceLevelScreen;
import uk.co.thomaspickup.spacewars.game.spaceLevel.SpaceSave;

/**
 * The pause screen - creates a freeze frame of the level.
 * Allows the user to resume or exit the game
 *
 * Created by Thomas Pickup
 */
public class PauseScreen extends GameScreen {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    // Bounds for the buttons
    private Rect mPlayButtonBound;
    private Rect mTitleBound;
    private Rect mExitButtonBound;

    // Defines the width and height of the level
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    // Defines the variables to be used to create a freeze frame of the game.
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private GameObject mSpaceBackground;
    private PlayerSpaceship mPlayerSpaceship;
    private List<Asteroid> mAsteroids;
    private List<AISpaceship> mAISpaceships;

    // Save File used for transfering and receiving a save from other screens
    private SpaceSave saveFile = new SpaceSave();

    // New instance of SettingsHandler for accessing settings
    private SettingsHandler settingsHandler = new SettingsHandler();

    // Padding for screen (scaled) @1920x1080 = 50x50
    int paddingY = (int) (getGame().getScreenHeight() * 0.02);
    int paddingX = (int) (getGame().getScreenWidth() * 0.026);

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor to create a new PauseScreen
     *
     * @param game     Game that this screen is to be built on
     * @param saveFile Save file passed from the SpaceLevelScreen
     */
    public PauseScreen(Game game, SpaceSave saveFile) {
        super("PauseScreen", game);

        // Copies over the save file to the class
        this.saveFile = saveFile;

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Gets the Layer View Port from the save file
        mLayerViewport = this.saveFile.getMLayerViewport();

        // Gets the player spaceship from the save file
        mPlayerSpaceship = this.saveFile.getMPlayerSpaceShip();

        // Gets Asteroids from the save file
        mAsteroids = this.saveFile.getMAsteroids();

        // Gets AI Spaceships from the save file
        mAISpaceships = this.saveFile.getMAISpaceships();

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

            // Check if touch event is in the play button bound
            if (mPlayButtonBound.contains((int) touchEvent.x,
                    (int) touchEvent.y)) {
                // Plays click sound
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));

                // Removes this screen
                mGame.getScreenManager().removeScreen(this.getName());

                // Creates and launches a new instance of SpaceLevelScreen passing the save file to resume
                SpaceLevelScreen spaceLevelScreen = new SpaceLevelScreen(mGame, saveFile);
                mGame.getScreenManager().addScreen(spaceLevelScreen);

                // Checks if the touch event is in the exit button bound
            } else if (mExitButtonBound.contains((int) touchEvent.x,
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
        // Create the screen to black and define a clip based on the viewport
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        // Draw the background first of all
        mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        // Draw each of the asteroids
        for (Asteroid asteroid : mAsteroids)
            asteroid.draw(elapsedTime, graphics2D, mLayerViewport,
                    mScreenViewport);

        // Draw each of the AI controlled spaceships
        for (AISpaceship aiSpaceship : mAISpaceships)
            aiSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
                    mScreenViewport);

        // Draw the player
        mPlayerSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        // Draws the title
        Bitmap titleImage = mGame.getAssetManager().getBitmap("TitleImage");
        graphics2D.drawBitmap(titleImage, null, mTitleBound, null);

        // Draw the play button
        Bitmap playIcon = mGame.getAssetManager().getBitmap("PlayIcon");
        graphics2D.drawBitmap(playIcon, null, mPlayButtonBound, null);

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
        int titleWidth = (int) (game.getScreenWidth() * 0.583); // On 1920 Screen Width = 1120
        int titleHeight = (int) (game.getScreenHeight() * 0.373); // On 1080 Screen Height = 400
        int spacingX = (game.getScreenWidth() / 2) - (titleWidth / 2);
        int spacingY = paddingY + paddingY;
        mTitleBound = new Rect(spacingX, spacingY, spacingX + titleWidth, spacingY + titleHeight);

        // Defines the Play Button Image Rect
        int btnPlayWidth = (int) (game.getScreenWidth() * 0.208);
        int btnPlayHeight = (int) (game.getScreenHeight() * 0.373);
        spacingX = (game.getScreenWidth() / 2) - (btnPlayWidth / 2);
        spacingY = (game.getScreenHeight() / 2) + paddingY;
        mPlayButtonBound = new Rect(spacingX, spacingY, spacingX + btnPlayWidth, spacingY + btnPlayHeight);

        // Defines the Exit Icon Rect
        int btnExitWidth = (int) (game.getScreenWidth() * 0.078);
        int btnExitHeight = (int) (game.getScreenHeight() * 0.138);
        spacingY = 50;
        spacingX = game.getScreenWidth() - (btnExitWidth + 50);
        mExitButtonBound = new Rect(spacingX, spacingY, spacingX + btnExitWidth, spacingY + btnExitHeight);
    }

    /**
     * Loads in the assets used by this screen
     */
    private void loadAssets() {
        // Creates a new instance of the asset manager
        AssetStore assetManager = mGame.getAssetManager();

        // Loads in bitmaps
        assetManager.loadAndAddBitmap("SpaceBackground", "img/backgrounds/bgSpace.png");
        assetManager.loadAndAddBitmap("Asteroid1", "img/sprites/sprAsteroid1.png");
        assetManager.loadAndAddBitmap("Asteroid2", "img/sprites/sprAsteroid2.png");
        assetManager.loadAndAddBitmap("Spaceship1", "img/sprites/sprSpaceship1.png");
        assetManager.loadAndAddBitmap("Spaceship2", "img/sprites/sprSpaceship2.png");
        assetManager.loadAndAddBitmap("Spaceship3", "img/sprites/sprSpaceship3.png");
        assetManager.loadAndAddBitmap("Turret", "img/sprites/sprTurret.png");
        assetManager.loadAndAddBitmap("PlayIcon", "img/buttons/btnPlay.png");
        assetManager.loadAndAddBitmap("TitleImage", "img/titles/ttlLogo.png");
        assetManager.loadAndAddBitmap("ExitIcon", "img/buttons/btnExit.png");

        // Loads in sounds
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");
    }
}

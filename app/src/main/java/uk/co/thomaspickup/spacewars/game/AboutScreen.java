package uk.co.thomaspickup.spacewars.game;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

// Android Graphics
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

// Android Util
import android.util.Log;

// Java Util
import java.util.List;

// GAGE
import uk.co.thomaspickup.spacewars.gage.Game;
import uk.co.thomaspickup.spacewars.gage.engine.AssetStore;
import uk.co.thomaspickup.spacewars.gage.engine.ElapsedTime;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.IGraphics2D;
import uk.co.thomaspickup.spacewars.gage.engine.graphics.TextLayout;
import uk.co.thomaspickup.spacewars.gage.engine.input.Input;
import uk.co.thomaspickup.spacewars.gage.engine.input.TouchEvent;
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * This screen acts as an version / about screen where the features (version) and credits are shown.
 *
 * Created by Thomas Pickup
 */
public class AboutScreen extends GameScreen {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    // Creates the asset manager so it can be used accross the class
    AssetStore assetManager;

    // Creates the strings that will hold the credits and feature list
    String strCredits;
    String strFeatures;

    // Bounds for back button
    private Rect mBackBound;

    // Background Objects
    private GameObject mSpaceBackground;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private int intXMultiplier = 1;

    // Create and initilize settingsHandler to allow access to SharedPreferences
    private SettingsHandler settingsHandler = new SettingsHandler();

    // Padding for game screen @1920x1080 = 50x50
    int paddingY = (int) (getGame().getScreenHeight() * 0.02);
    int paddingX = (int) (getGame().getScreenWidth() * 0.026);

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple about screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public AboutScreen(Game game, LayerViewport backgroundViewPort) {
        super("AboutScreen", game);

        // Creates the view port of the screen
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Imports the background from previous screen for seamless transistion
        mLayerViewport = backgroundViewPort;

        // Loads in Assets
        loadAssets();

        // Imports text files
        importTextFiles();

        // Sets up UI
        setUpUI(game);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Updates the screen.
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

         if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                // Remove this screen
                mGame.getScreenManager().removeScreen(this.getName());

                // Create a new instance of menuScreen and add it to screen manager
                MenuScreen menuScreen = new MenuScreen(mGame, mLayerViewport);
                mGame.getScreenManager().addScreen(menuScreen);
            }
        }

        // Move the background diagonally
        // Changes the multiplier if it hits the bounds
        if (mLayerViewport.x == getGame().getScreenWidth() - (mLayerViewport.getWidth() / 2)) {
            intXMultiplier = -1;
        } else if (mLayerViewport.x == getGame().getScreenWidth() - (mLayerViewport.getWidth())) {
            intXMultiplier = 1;
        }

        // Adds the multiplier to x
        mLayerViewport.x += intXMultiplier;
    }

    /**
     * Draws on the screen.
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D The helper to draw on the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draws the background with adjusted viewport
        mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        // Back Button
        Bitmap btnBack = mGame.getAssetManager().getBitmap("btnBack");
        graphics2D.drawBitmap(btnBack, null, mBackBound,null);

        // Text View
        // Gets the canvas
        Canvas thisCanvas = graphics2D.getMCanvas();

        // Defines the width of the textLayout
        int viewWidth = (getGame().getScreenWidth() - (mBackBound.right + 150)) / 2;

        // Creates a new Text Layout and draws to canvas
        TextLayout textViewCredits = new TextLayout(thisCanvas, getGame().getContext(), mBackBound.right + 50,50,getGame().getScreenHeight() - 100, viewWidth, strCredits);
        textViewCredits.draw(thisCanvas);

        // Creates a new Text Layout and draws to canvas
        TextLayout textViewFeatures = new TextLayout(thisCanvas, getGame().getContext(), mBackBound.right + viewWidth + 100,50,getGame().getScreenHeight() - 100, viewWidth, strFeatures);
        textViewFeatures.draw(thisCanvas);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sets up the UI used by this screen.
     * @param game
     */
    public void setUpUI(Game game) {
        // Sets the bounds for the back button
        int btnBackWidth = (int) (game.getScreenWidth() * 0.078);
        int btnBackHeight = (int) (game.getScreenHeight() * 0.138);
        int startX = paddingX;
        int startY = paddingY;
        mBackBound = new Rect(startX, startY, startX + btnBackWidth, startY + btnBackHeight);

        // Defines the background
        mSpaceBackground = new GameObject(game.getScreenWidth() / 2.0f,
                game.getScreenHeight() / 2.0f, game.getScreenWidth(), game.getScreenHeight(), getGame()
                .getAssetManager().getBitmap("SpaceBackground"), this);
    }

    /**
     * Loads in the assets used by this screen.
     */
    public void loadAssets() {
        // Create new instance of Asset Manager
        assetManager = mGame.getAssetManager();

        // Import Bitmaps
        assetManager.loadAndAddBitmap("btnBack", "img/buttons/btnBack.png");

        // Import Sounds
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");
    }

    /**
     * Imports the text files into strings.
     */
    public void importTextFiles() {
        // Tries to import credits
        try {
            strCredits = assetManager.getTextFile(getGame().getContext(), "txt/txtCredits.txt");
        } catch (Exception ex) {
            Log.e("Import Fail", ex.toString());
        }

        // Tries to import features
        try {
            strFeatures = assetManager.getTextFile(getGame().getContext(), "txt/txtFeatures.txt");
        } catch (Exception ex) {
            Log.e("Import Fail", ex.toString());
        }
    }
}

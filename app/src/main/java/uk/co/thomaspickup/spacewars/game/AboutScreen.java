package uk.co.thomaspickup.spacewars.game;

// Imports

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

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
// TODO: Optimize and annotate
public class AboutScreen extends GameScreen {
    // Create instance of SettingsHandler to allow for easy of referencing
    SettingsHandler settings = new SettingsHandler();

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

    private SettingsHandler settingsHandler = new SettingsHandler();

    /**
     * Create a simple about screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public AboutScreen(Game game, LayerViewport backgroundViewPort) {
        super("AboutScreen", game);

        int paddingY = (int) (getGame().getScreenHeight() * 0.02);
        int paddingX = (int) (getGame().getScreenWidth() * 0.026);

        // Initilize the asset manager and
        // load in the bitmaps used on the about screen
        assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("btnBack", "img/buttons/btnBack.png");
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");

        // Imports text files
        try {
            strCredits = assetManager.getTextFile(getGame().getContext(), "txt/txtCredits.txt");
        } catch (Exception ex) {
            Log.e("Import Fail", ex.toString());
        }
        try {
            strFeatures = assetManager.getTextFile(getGame().getContext(), "txt/txtFeatures.txt");
        } catch (Exception ex) {
            Log.e("Import Fail", ex.toString());
        }

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
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());
        mLayerViewport = backgroundViewPort;
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

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameScreen#draw(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D)
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

        // Defines a new layout
        int viewWidth = (getGame().getScreenWidth() - (mBackBound.right + 150)) / 2;
        TextLayout textViewCredits = new TextLayout(thisCanvas, getGame().getContext(), mBackBound.right + 50,50,getGame().getScreenHeight() - 100, viewWidth, strCredits);
        textViewCredits.draw(thisCanvas);

        TextLayout textViewFeatures = new TextLayout(thisCanvas, getGame().getContext(), mBackBound.right + viewWidth + 100,50,getGame().getScreenHeight() - 100, viewWidth, strFeatures);
        textViewFeatures.draw(thisCanvas);
    }
}

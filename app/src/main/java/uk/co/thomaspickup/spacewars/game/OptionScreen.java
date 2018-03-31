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
import uk.co.thomaspickup.spacewars.gage.world.GameObject;
import uk.co.thomaspickup.spacewars.gage.world.GameScreen;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;
import uk.co.thomaspickup.spacewars.gage.world.ScreenViewport;

/**
 * This screen acts as an options screen where the user can adjust various in game options:
 * - Difficulty
 * - Mute
 * Created by Thomas Pickup
 */
// TODO: Add Music To Options Screen & Mute when pressed Mute
// TODO: Add in variables to be passed over when invocing this screen to leave off background as in main screen
public class OptionScreen extends GameScreen {
    // ~~~~ Vars Start ~~~~
    // ~~ Current Settings Vars ~~
    // Difficulty
    // Easy = 1
    // Normal = 2
    // Hard = 3
    // Insane = 4
    int currentDifficultySetting;

    // Mute
    // Mute = 0
    // Un-Mute = 1
    int currentSoundSetting;

    // Create instance of SettingsHandler to allow for easy of referencing
    SettingsHandler settings = new SettingsHandler();

    // ~~ Bounds for objects on screen ~~
    // Bounds for difficultySettings
    private Rect mEasyBound, mNormalBound, mHardBound, mInsaneBound;

    // Bounds for mute button
    private Rect mMuteBound;

    // Bounds for back button
    private Rect mBackBound;

    // Bounds for titles
    private Rect mDifficultyTitle, mMuteTitle;

    // Background Objects
    private GameObject mSpaceBackground;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private int intXMultiplier = 1;

    // ~~~~ Methods Start ~~~~
    /**
     * Create a simple options screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public OptionScreen(Game game) {
        super("OptionScreen", game);

        // Loads in Current Settings from SharedPreferences
        currentDifficultySetting = settings.getDifficulty(getGame().getContext());
        currentSoundSetting = settings.getSound(getGame().getContext());

        // Load in the bitmaps used on the options screen
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("btnEasy-Normal", "img/buttons/btnEasy-Normal.png");
        assetManager.loadAndAddBitmap("btnEasy-Selected", "img/buttons/btnEasy-Selected.png");
        assetManager.loadAndAddBitmap("btnNormal-Normal", "img/buttons/btnNormal-Normal.png");
        assetManager.loadAndAddBitmap("btnNormal-Selected", "img/buttons/btnNormal-Selected.png");
        assetManager.loadAndAddBitmap("btnHard-Normal", "img/buttons/btnHard-Normal.png");
        assetManager.loadAndAddBitmap("btnHard-Selected", "img/buttons/btnHard-Selected.png");
        assetManager.loadAndAddBitmap("btnInsane-Normal", "img/buttons/btnInsane-Normal.png");
        assetManager.loadAndAddBitmap("btnInsane-Selected", "img/buttons/btnInsane-Selected.png");
        assetManager.loadAndAddBitmap("btnSound-Mute","img/buttons/btnSound-Mute.png");
        assetManager.loadAndAddBitmap("btnSound-UnMute", "img/buttons/btnSound-UnMute.png");
        assetManager.loadAndAddBitmap("btnBack", "img/buttons/btnBack.png");
        assetManager.loadAndAddBitmap("txtDifficulty", "img/titles/ttlDifficulty.png");
        assetManager.loadAndAddBitmap("txtMute", "img/titles/ttlMute.png");

        // Bounds for Difficulty Title
        int txtDifficultyWidth = 525;
        int txtDifficultyHeight = 200;
        int startX = (getGame().getScreenWidth() / 2) - (txtDifficultyWidth / 2);
        int startY = 50;
        mDifficultyTitle = new Rect(startX, startY, startX + txtDifficultyWidth, startY + txtDifficultyHeight);

        // Sets bounds for the difficulty settings stack
        // each button 255px width
        // and 120px high
        startX = (getGame().getScreenWidth() / 2) - 525;
        int endX = startX + 255;
        startY = startY + txtDifficultyHeight + 50;
        int endY = startY + 120;
        mEasyBound = new Rect(startX, startY, endX, endY);

        startX = endX + 10;
        endX = startX + 255;
        mNormalBound = new Rect(startX, startY, endX, endY);

        startX = endX + 10;
        endX = startX + 255;
        mHardBound = new Rect(startX, startY, endX, endY);

        startX = endX + 10;
        endX = startX + 255;
        mInsaneBound = new Rect(startX, startY, endX, endY);

        // Sets the Mute Title
        int txtMuteWidth = 525;
        int txtMuteHeight = 200;
        startY = endY + 100;
        endY = startY + txtMuteHeight;
        startX = (getGame().getScreenWidth() / 2) - (txtMuteWidth / 2);
        endX = startX + txtMuteWidth;
        mMuteTitle = new Rect(startX, startY, endX, endY);

        // Sets the bounds for the Mute Button
        int btnSoundWidth = 200;
        int btnSoundHeight = 200;
        startY = endY + 50;
        endY = startY + btnSoundHeight;
        startX = (game.getScreenWidth() / 2) - (btnSoundWidth / 2);
        mMuteBound = new Rect(startX, startY, startX + btnSoundWidth, endY);

        // Sets the bounds for the back button
        int btnBackWidth = 150;
        int btnBackHeight = 150;
        startX = 50;
        startY = 50;
        mBackBound = new Rect(startX, startY, startX + btnBackWidth, startY + btnBackHeight);

        // Defines the background
        mSpaceBackground = new GameObject(game.getScreenWidth() / 2.0f,
                game.getScreenHeight() / 2.0f, game.getScreenWidth(), game.getScreenHeight(), getGame()
                .getAssetManager().getBitmap("SpaceBackground"), this);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Create the layer viewport, taking into account the orientation
        // and aspect ratio of the screen.
        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
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
                settings.setDifficulty(getGame().getContext(), 4);
                currentDifficultySetting = settings.getDifficulty(getGame().getContext());
            } else if (mMuteBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                if (currentSoundSetting == 0) {
                    settings.setSound(getGame().getContext(), 1);
                    currentSoundSetting = settings.getSound(getGame().getContext());
                } else {
                    settings.setSound(getGame().getContext(), 0);
                    currentSoundSetting = settings.getSound(getGame().getContext());
                }
            } else if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                // Remove this screen
                mGame.getScreenManager().removeScreen(this.getName());

                // Create a new instance of menuScreen and add it to screen manager
                MenuScreen menuScreen = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(menuScreen);
            }
        }

        // Move the background diagonally
        // Changes the multiplier if it hits the bounds
        if (mLayerViewport.x == getGame().getScreenWidth() - mLayerViewport.getRight()) {
            intXMultiplier = -1;
        } else if (mLayerViewport.x == mScreenViewport.left) {
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

        // Draws Difficulty Title
        Bitmap txtDifficulty = mGame.getAssetManager().getBitmap("txtDifficulty");
        graphics2D.drawBitmap(txtDifficulty,null,mDifficultyTitle,null);

        // Decide whether the option is selected then display the relevant button for each setting
        // Easy Button
        if (currentDifficultySetting == 1) {
            Bitmap btnEasy_Selected = mGame.getAssetManager().getBitmap("btnEasy-Selected");
            graphics2D.drawBitmap(btnEasy_Selected, null,mEasyBound,null);
        } else {
            Bitmap btnEasy_Normal = mGame.getAssetManager().getBitmap("btnEasy-Normal");
            graphics2D.drawBitmap(btnEasy_Normal, null,mEasyBound,null);
        }

        // Normal Button
        if (currentDifficultySetting == 2) {
            Bitmap btnNormal_Selected = mGame.getAssetManager().getBitmap("btnNormal-Selected");
            graphics2D.drawBitmap(btnNormal_Selected, null,mNormalBound,null);
        } else {
            Bitmap btnNormal_Normal = mGame.getAssetManager().getBitmap("btnNormal-Normal");
            graphics2D.drawBitmap(btnNormal_Normal, null,mNormalBound,null);
        }

        // Hard Button
        if (currentDifficultySetting == 3) {
            Bitmap btnHard_Selected = mGame.getAssetManager().getBitmap("btnHard-Selected");
            graphics2D.drawBitmap(btnHard_Selected, null,mHardBound,null);
        } else {
            Bitmap btnHard_Normal = mGame.getAssetManager().getBitmap("btnHard-Normal");
            graphics2D.drawBitmap(btnHard_Normal, null,mHardBound,null);
        }

        // Insane Button
        if (currentDifficultySetting == 4) {
            Bitmap btnInsane_Selected = mGame.getAssetManager().getBitmap("btnInsane-Selected");
            graphics2D.drawBitmap(btnInsane_Selected, null,mInsaneBound,null);
        } else {
            Bitmap btnInsane_Normal = mGame.getAssetManager().getBitmap("btnInsane-Normal");
            graphics2D.drawBitmap(btnInsane_Normal, null,mInsaneBound,null);
        }

        // Mute Title
        Bitmap txtMute = mGame.getAssetManager().getBitmap("txtMute");
        graphics2D.drawBitmap(txtMute,null,mMuteTitle,null);

        // Mute Button
        if (currentSoundSetting == 1) {
            Bitmap btnSound_UnMute = mGame.getAssetManager().getBitmap("btnSound-UnMute");
            graphics2D.drawBitmap(btnSound_UnMute, null,mMuteBound,null);
        } else {
            Bitmap btnSound_Mute = mGame.getAssetManager().getBitmap("btnSound-Mute");
            graphics2D.drawBitmap(btnSound_Mute, null,mMuteBound,null);
        }

        // Back Button
        Bitmap btnBack = mGame.getAssetManager().getBitmap("btnBack");
        graphics2D.drawBitmap(btnBack, null, mBackBound,null);
    }
}

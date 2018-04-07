package uk.co.thomaspickup.spacewars.game;

// Imports
import android.graphics.Bitmap;
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
    SettingsHandler settingsHandler = new SettingsHandler();

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

    // Delay on button
    int avgFPS;
    int timeLeft;
    boolean canPress;

    // ~~~~ Methods Start ~~~~
    /**
     * Create a simple options screen
     *
     * @param game
     *            SpaceGame to which this screen belongs
     */
    public OptionScreen(Game game, LayerViewport backgroundViewPort) {
        super("OptionScreen", game);

        int paddingY = (int) (getGame().getScreenHeight() * 0.02); // @1080 = 50
        int paddingX = (int) (getGame().getScreenWidth() * 0.026); // @1920 = 50

        // Loads in Current Settings from SharedPreferences
        currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
        currentSoundSetting = settingsHandler.getSound(getGame().getContext());

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
        assetManager.loadAndAddSound("ButtonClick", "sfx/sfx_buttonclick.mp3");

        // Bounds for Difficulty Title
        int txtDifficultyWidth = (int) (getGame().getScreenWidth() * 0.273); // @1920 = 525
        int txtDifficultyHeight = (int) (getGame().getScreenHeight() * 0.185); // @1080 = 200
        int startX = (getGame().getScreenWidth() / 2) - (txtDifficultyWidth / 2);
        int startY = paddingY;
        mDifficultyTitle = new Rect(startX, startY, startX + txtDifficultyWidth, startY + txtDifficultyHeight);

        // Sets bounds for the difficulty settingsHandler stack
        // each button 255px width
        // and 120px high
        int stackPad = (int) (getGame().getScreenWidth() * 0.00520); // @1920 = 10
        int halfStack = (int) (getGame().getScreenWidth() * 0.273); // @1920 = 525
        int buttonWidth = (int) (getGame().getScreenWidth() * 0.132); // @1920 = 255
        int buttonHeight = (int) (getGame().getScreenHeight() * 0.111); // @1080 = 120
        startX = (getGame().getScreenWidth() / 2) - halfStack;
        int endX = startX + buttonWidth;
        startY = startY + txtDifficultyHeight + paddingY;
        int endY = startY + buttonHeight;
        mEasyBound = new Rect(startX, startY, endX, endY);

        startX = endX + stackPad;
        endX = startX + buttonWidth;
        mNormalBound = new Rect(startX, startY, endX, endY);

        startX = endX + stackPad;
        endX = startX + buttonWidth;
        mHardBound = new Rect(startX, startY, endX, endY);

        startX = endX + stackPad;
        endX = startX + buttonWidth;
        mInsaneBound = new Rect(startX, startY, endX, endY);

        // Sets the Mute Title
        int txtMuteWidth = (int) (getGame().getScreenWidth() * 0.273); // @1920 = 525
        int txtMuteHeight = (int) (getGame().getScreenHeight() * 0.185); // @1080 = 200
        startY = endY + (paddingY *2);
        endY = startY + txtMuteHeight;
        startX = (getGame().getScreenWidth() / 2) - (txtMuteWidth / 2);
        endX = startX + txtMuteWidth;
        mMuteTitle = new Rect(startX, startY, endX, endY);

        // Sets the bounds for the Mute Button
        int btnSoundWidth = (int) (game.getScreenWidth() * 0.156); // @1920 = 300
        int btnSoundHeight = (int) (game.getScreenHeight() * 0.231); // @1080 = 250
        startY = endY + paddingY;
        endY = startY + btnSoundHeight;
        startX = (game.getScreenWidth() / 2) - (btnSoundWidth / 2);
        mMuteBound = new Rect(startX, startY, startX + btnSoundWidth, endY);

        // Sets the bounds for the back button
        int btnBackWidth = (int) (game.getScreenWidth() * 0.078); // @1920 = 150
        int btnBackHeight = (int) (game.getScreenHeight() * 0.138); // @1080 = 150
        startX = paddingX;
        startY = paddingY;
        mBackBound = new Rect(startX, startY, startX + btnBackWidth, startY + btnBackHeight);

        // Defines the background
        mSpaceBackground = new GameObject(game.getScreenWidth() / 2.0f,
                game.getScreenHeight() / 2.0f, game.getScreenWidth(), game.getScreenHeight(), getGame()
                .getAssetManager().getBitmap("SpaceBackground"), this);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());
        mLayerViewport = backgroundViewPort;

        // Allow to press the mute button
        canPress = true;
        avgFPS =  getGame().getTargetFramesPerSecond();
        timeLeft = 0;
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
        // Counts up to Average FPS
        if (!canPress) {
            timeLeft += 1;

            if (timeLeft == avgFPS) {
                canPress = true;
            }
        }

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

            if (mEasyBound.contains((int) touchEvent.x,
                    (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                settingsHandler.setDifficulty(getGame().getContext(), 1);
                currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
            } else if (mNormalBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                settingsHandler.setDifficulty(getGame().getContext(), 2);
                currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
            } else if (mHardBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                settingsHandler.setDifficulty(getGame().getContext(), 3);
                currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
            } else if (mInsaneBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                settingsHandler.setDifficulty(getGame().getContext(), 4);
                currentDifficultySetting = settingsHandler.getDifficulty(getGame().getContext());
            } else if (mMuteBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
                getGame().getAssetManager().getSound("ButtonClick").play(settingsHandler.getSound(getGame().getContext()));
                if (canPress) {
                    if (currentSoundSetting == 0) {
                        canPress = false;
                        settingsHandler.setSound(getGame().getContext(), 1);
                        currentSoundSetting = settingsHandler.getSound(getGame().getContext());

                        timeLeft = 0;
                    } else {
                        canPress = false;
                        settingsHandler.setSound(getGame().getContext(), 0);
                        currentSoundSetting = settingsHandler.getSound(getGame().getContext());
                        timeLeft = 0;
                    }
                }
            } else if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
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

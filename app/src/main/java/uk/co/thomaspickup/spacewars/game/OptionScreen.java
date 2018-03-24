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
    // Un-Mute = 0
    // Mute = 1
    int currentSoundSetting;

    // Create instance of SettingsHandler to allow for easy of referencing
    SettingsHandler settings = new SettingsHandler();

    // ~~ Bounds for objects on screen ~~
    // Bounds for difficultySettings
    private Rect mEasyBound, mNormalBound, mHardBound, mInsaneBound;

    // Bounds for mute button
    private Rect mMuteBound;

    // Bounds for back button
    private Rect mBackButton;

    // Bounds for titles
    private Rect mMainTitle, mDifficultyTitle, mSoundTitle;

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

        // Sets bounds for the difficulty settings stack
        // each button 255px width
        // and 120px high
        int startX = (getGame().getScreenWidth() / 2) - 525;
        int endX = startX + 255;
        int startY = (getGame().getScreenHeight() / 3);
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

        // TODO: Create bounds for titles
        // TODO: Create bound for mute button
        // TODO: Create bound for back button
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
            }
            // TODO: Add Touch Listener for mute button
            // TODO: Add Touch Listener for back button
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

        // TODO: Draw the Titles
        // TODO: Draw the mute button
        // TODO: Draw the back button
    }
}

package uk.co.thomaspickup.spacewars.game;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thomaspickup.
 * This class handles all the settings for the app, both setting them and getting them.
 */

public class SettingsHandler {
    // Sets up a new instance of helper tools
    // for commonly used algorithms.
    HelperTools maths = new HelperTools();

    /**
     * Constructor for the SettingsHandler class.
     * Allows for class to be implemented easily.
     */
    public SettingsHandler() {
        // Does nothing when initializing.
    }

    /**
     *  This function returns the difficulty setting that is stored in sharedPreferences.
     *  Difficulty Settings:
     *  Easy - 1
     *  Normal - 2
     *  Hard - 3
     *  Insane - 4
     *
      * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @return The difficulty stored in SharedPreferences.
     */
    public int getDifficulty(Context appContext) {
        int difficultySetting;
        Context myAppContext = appContext;

        // Creates instance of SharePreferences called settings
        SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF(), 0);

        // Creates instance of SharedPreferences Editor called editor
        SharedPreferences.Editor editor;

        if (!settings.contains(AppStrings.getPREF_DIFFICULTY())) {
            // If the key doesn't already exist then create the default key of 0

            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the int
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), 2);

            // Commits changes to shared preferences
            editor.commit();

            difficultySetting = settings.getInt(AppStrings.getPREF_DIFFICULTY(), 2);
        } else {
            // If the key has already been set return the value stored in the key
            difficultySetting = settings.getInt(AppStrings.getPREF_DIFFICULTY(), 2);
        }

        // Returns the result from SharedPreferences
        return difficultySetting;
    }

    /**
     *  This function sets the difficulty setting to be stored in sharedPreferences.
     *  Difficulty Settings:
     *  Easy - 1
     *  Normal - 2
     *  Hard - 3
     *  Insane - 4
     *
     * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @param difficultySetting - Passes through the difficulty setting.
     * @return Success (True or False)
     */
    public boolean setDifficulty(Context appContext, int difficultySetting) {
        if (maths.inBetween(difficultySetting, 1, 4)) {
            int myDifficultySetting = difficultySetting;
            Context myAppContext = appContext;

            // Creates instance of SharePreferences called settings
            SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF(), 0);

            // Creates instance of SharedPreferences Editor called editor
            SharedPreferences.Editor editor;

            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the int
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), myDifficultySetting);

            // Commits changes to shared preferences
            editor.commit();

            if (getDifficulty(myAppContext) == myDifficultySetting) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // TODO: Add Set Mute
    // TODO: Add Get Mute
}

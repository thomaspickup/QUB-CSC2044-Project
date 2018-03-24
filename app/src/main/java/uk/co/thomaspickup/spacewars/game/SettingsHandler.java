package uk.co.thomaspickup.spacewars.game;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class handles all the settings for the app, both setting them and getting them.
 * Created by thomaspickup.
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
        SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

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
            SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

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

    /**
     *  This function sets the sound setting to be stored in sharedPreferences.
     *  Difficulty Settings:
     *  Un-Mute - 0
     *  Mute - 1
     *
     * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @param soundSetting - Passes through the sound setting.
     * @return Success (True or False)
     */
    public boolean setSound(Context appContext, int soundSetting) {
        if (maths.inBetween(soundSetting, 0, 1)) {
            int mySoundSetting = soundSetting;
            Context myAppContext = appContext;

            // Creates instance of SharePreferences called settings
            SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

            // Creates instance of SharedPreferences Editor called editor
            SharedPreferences.Editor editor;

            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the int
            editor.putInt(AppStrings.getPREF_SOUND(), mySoundSetting);

            // Commits changes to shared preferences
            editor.commit();

            if (getSound(myAppContext) == mySoundSetting) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *  This function returns the sound setting that is stored in sharedPreferences.
     *  Difficulty Settings:
     *  UnMute - 0
     *  Mute - 1
     *
     * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @return The difficulty stored in SharedPreferences.
     */
    public int getSound(Context appContext) {
        int soundSetting;
        Context myAppContext = appContext;

        // Creates instance of SharePreferences called settings
        SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

        // Creates instance of SharedPreferences Editor called editor
        SharedPreferences.Editor editor;

        if (!settings.contains(AppStrings.getPREF_SOUND())) {
            // If the key doesn't already exist then create the default key value of 0

            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the int
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), 0);

            // Commits changes to shared preferences
            editor.commit();

            soundSetting = settings.getInt(AppStrings.getPREF_SOUND(), 0);
        } else {
            // If the key has already been set return the value stored in the key
            soundSetting = settings.getInt(AppStrings.getPREF_SOUND(), 0);
        }

        // Returns the result from SharedPreferences
        return soundSetting;
    }
}

package uk.co.thomaspickup.spacewars.game;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class handles the setting and getting of the SharedPreferences.
 * 
 * Created by Thomas Pickup
 */
public class SettingsHandler {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    // Sets up a new instance of helper tools
    // for commonly used algorithms.
    HelperTools maths = new HelperTools();

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * This function returns the difficulty setting that is stored in sharedPreferences.
     * Difficulty Settings:
     * Easy - 1
     * Normal - 2
     * Hard - 3
     * Insane - 4
     *
     * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @return The difficulty stored in SharedPreferences.
     */
    public int getDifficulty(Context appContext) {
        // Integer value to hold setting
        int difficultySetting;

        // Transfers the appContext
        Context myAppContext = appContext;

        // Creates instance of SharedPreferences called settings
        SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

        // Creates instance of SharedPreferences Editor called editor
        SharedPreferences.Editor editor;

        // Checks to see if settings contains a Difficulty setting
        if (!settings.contains(AppStrings.getPREF_DIFFICULTY())) {
            // If it doesn't contain the setting already then we create one and set it as the default (2)
            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the integer 2 into the editor
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), 2);

            // Commits changes to settings (SharedPreferences)
            editor.commit();

            // Gets the difficulty setting from Shared Prefences
            difficultySetting = settings.getInt(AppStrings.getPREF_DIFFICULTY(), 2);
        } else {
            // If the setting has already been set return the value stored in the setting
            difficultySetting = settings.getInt(AppStrings.getPREF_DIFFICULTY(), 2);
        }

        // Returns the value
        return difficultySetting;
    }

    /**
     * This function returns the sound setting that is stored in sharedPreferences.
     * Difficulty Settings:
     * Mute - 0
     * Un-Mute - 1
     *
     * @param appContext - Passes the Context of the application to allow for accessing system level functions.
     * @return The difficulty stored in SharedPreferences.
     */
    public int getSound(Context appContext) {
        // Integer value to hold the setting
        int soundSetting;

        // Transfers the appContext
        Context myAppContext = appContext;

        // Creates instance of SharedPreferences called settings
        SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

        // Creates instance of SharedPreferences Editor called editor
        SharedPreferences.Editor editor;

        if (!settings.contains(AppStrings.getPREF_SOUND())) {
            // If it doesn't contain the setting already then we create one and set it as the default (1)
            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the integer 1 into the editor
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), 1);

            // Commits changes to settings (SharedPreferences)
            editor.commit();

            // Gets the sound setting from Shared Prefences
            soundSetting = settings.getInt(AppStrings.getPREF_SOUND(), 1);
        } else {
            // If the setting has already been set return the value stored in the setting
            soundSetting = settings.getInt(AppStrings.getPREF_SOUND(), 1);
        }

        // Returns the value
        return soundSetting;
    }

    /**
     * This function sets the difficulty setting to be stored in sharedPreferences.
     * Difficulty Settings:
     * Easy - 1
     * Normal - 2
     * Hard - 3
     * Insane - 4
     *
     * @param appContext        - Passes the Context of the application to allow for accessing system level functions.
     * @param difficultySetting - Passes through the difficulty setting.
     * @return Success (True or False)
     */
    public boolean setDifficulty(Context appContext, int difficultySetting) {
        // If the setting (as integer) passed is in between 1 and 4 then we continue
        if (maths.inBetween(difficultySetting, 1, 4)) {
            // Transfers the values passed into variables
            int myDifficultySetting = difficultySetting;
            Context myAppContext = appContext;

            // Creates instance of SharedPreferences called settings
            SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

            // Creates instance of SharedPreferences Editor called editor
            SharedPreferences.Editor editor;

            // Defines the editor as the edit of settings
            editor = settings.edit();

            // Puts the integer that was passed into the editor
            editor.putInt(AppStrings.getPREF_DIFFICULTY(), myDifficultySetting);

            // Commits changes to settings (SharedPreferences)
            editor.commit();

            // Tests that everything has gone through okay by fetching and comparing
            if (getDifficulty(myAppContext) == myDifficultySetting) {
                // Setting has been stored correctly
                return true;
            } else {
                // Setting has been stored incorrectly
                return false;
            }
        } else {
            // Setting (integer) passed was not in between 1 & 4
            return false;
        }
    }

    /**
     * This function sets the sound setting to be stored in sharedPreferences.
     * Difficulty Settings:
     * Mute - 0
     * Un-Mute - 1
     *
     * @param appContext   - Passes the Context of the application to allow for accessing system level functions.
     * @param soundSetting - Passes through the sound setting.
     * @return Success (True or False)
     */
    public boolean setSound(Context appContext, int soundSetting) {
        // If the setting (as integer) passed is in between 0 and 1 then we continue
        if (maths.inBetween(soundSetting, 0, 1)) {
            // Transfers the values passed into variables
            int mySoundSetting = soundSetting;
            Context myAppContext = appContext;

            // Creates instance of SharedPreferences called settings
            SharedPreferences settings = myAppContext.getSharedPreferences(AppStrings.getPREF_(), 0);

            // Creates instance of SharedPreferences Editor called editor
            SharedPreferences.Editor editor;

            // Defines the editor as the edit of setting
            editor = settings.edit();

            // Puts the integer that was passed into the editor
            editor.putInt(AppStrings.getPREF_SOUND(), mySoundSetting);

            // Commits changes to settings (SharedPreferences)
            editor.commit();

            // Tests that everything has gone through okay by fetching and comparing
            if (getSound(myAppContext) == mySoundSetting) {
                // Setting has been stored correctly
                return true;
            } else {
                // Setting has been stored incorrectly
                return false;
            }
        } else {
            // Setting (integer) passed was not in between 0 & 1
            return false;
        }
    }
}

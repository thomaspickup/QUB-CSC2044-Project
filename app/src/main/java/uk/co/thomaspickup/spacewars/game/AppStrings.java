package uk.co.thomaspickup.spacewars.game;

/**
 * This class holds all of the strings that would link to various constants.
 * I.E. the settings paths are all controlled via this!
 *
 * Created by Thomas Pickup
 */
public class AppStrings {
    // /////////////////////////////////////////////////////////////////////////
    // Private Variables
    // /////////////////////////////////////////////////////////////////////////

    // Strings representing the keys for SharedPreferences
    private static String PREF_ = "pref";
    private static String PREF_DIFFICULTY = "prefDifficulty";
    private static String PREF_SOUND = "prefSound";

    // /////////////////////////////////////////////////////////////////////////
    // Get Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Returns the Preference Key
     *
     * @return Preference Key
     */
    public static String getPREF_() {
        return PREF_;
    }

    /**
     * Returns the Difficulty Preference Key
     *
     * @return Difficulty Preference Key
     */
    public static String getPREF_DIFFICULTY() {
        return PREF_DIFFICULTY;
    }

    /**
     * Returns the Sound Preference Key
     *
     * @return Sound Preference Key
     */
    public static String getPREF_SOUND() { return PREF_SOUND; }
}

package uk.co.thomaspickup.spacewars.game;

/**
 * This class holds all of the strings that would link to various constants.
 * I.E. the settings paths are all controlled via this!
 * Created by thomaspickup on 10/03/2018.
 */

public class AppStrings {
    private static String PREF_ = "pref";
    private static String PREF_DIFFICULTY = "prefDifficulty";
    private static String PREF_SOUND = "prefSound";

    public static String getPREF_() {
        return PREF_;
    }

    public static String getPREF_DIFFICULTY() {
        return PREF_DIFFICULTY;
    }

    public static String getPREF_SOUND() { return PREF_SOUND; }
}

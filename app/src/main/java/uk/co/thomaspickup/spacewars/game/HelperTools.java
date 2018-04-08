package uk.co.thomaspickup.spacewars.game;

/**
 * This class is used for various functions that are used globally around the game.
 * Mainly maths based functions
 * Created by thomaspickup on 24/03/2018.
 */

public class HelperTools {
    /**
     * Checks if the value x is in between (including) a lowerBound and an upperBound.
     * @param x The Number to check.
     * @param lowerBound The lower bound to check against.
     * @param upperBound The upper bound to check against.
     * @return Boolean value - true or false to the statement above.
     */
    public boolean inBetween(int x ,int lowerBound, int upperBound) {
        if (x >= lowerBound && x <= upperBound) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a multiplier based upon to difficultySetting supplied
     * Setting to Multiplier Conversions:
     * 1 (Easy) = 0.5 (Ships Are Slower)
     * 2 (Normal) = 1 (Ships Are Normal)
     * 3 (Hard) = 1.5 (Ships a bit faster)
     * 4 (Insane) = 2 (Ships are twice as fast!!)
     * @param difficultySetting
     * @return Speed Multiplier as Int
     */
    public float getSpeedMultiplier(int difficultySetting) {
        if (inBetween(difficultySetting, 1, 4)) {
            switch (difficultySetting) {
                case 1:
                    return 0.5f;
                case 2:
                    return 1.0f;
                case 3:
                    return 1.5f;
                case 4:
                    return 2.0f;
                default:
                    return 1.0f;
            }
        } else {
            return 1.0f;
        }
    }

    public float getDistance(float deltaX, float deltaY) {
        float result;

        result = (float) Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));

        return result;
    }
}

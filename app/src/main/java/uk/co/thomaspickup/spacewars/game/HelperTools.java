package uk.co.thomaspickup.spacewars.game;

/**
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
}

package uk.co.thomaspickup.spacewars.test;

import org.junit.Assert;
import org.junit.Test;
import uk.co.thomaspickup.spacewars.game.HelperTools;

/**
 * General Tests related to the Space Game.
 *
 * Created by Thomas Pickup.
 */
public class GeneralTest {
    private HelperTools helperTools = new HelperTools();

    /**
     * Tests that the helper tools in between works correctly.
     */
    @Test
    public void testHelperToolsInBetween() {
        // Expected: True 2 is in between 1 & 4
        boolean expectedResult = true;
        Assert.assertEquals(expectedResult, helperTools.inBetween(2,1,4));

        // Expected: False 400 is lower than the lower bound
        expectedResult = false;
        Assert.assertEquals(expectedResult, helperTools.inBetween(400, 500,600));
    }

    /**
     * Tests that the helper tools speed multiplier works.
     */
    @Test
    public void testHelperToolsSpeedMultiplier() {
        // Expected: Returns the default value of 1.0f
        float expectedResult = 1.0f;
        Assert.assertEquals(expectedResult, helperTools.getSpeedMultiplier(500),0.0);

        // Expected: Returns the corresponding value of 1.5f
        expectedResult = 1.5f;
        Assert.assertEquals(expectedResult, helperTools.getSpeedMultiplier(3),0.0);
    }

    /**
     * Tests that the helper tools get distance works.
     */
    @Test
    public void testHelperToolsGetDistance() {
        // Expected: 7.07f (Used Calculator)
        float deltaX = 5.0f;
        float deltaY = 5.0f;
        float expectedResult = 7.07f;
        Assert.assertEquals(expectedResult, helperTools.getDistance(deltaX,deltaY), 0.1);

        // Expected: 471.7f (Used Calculator)
        deltaX = 250f;
        deltaY = 400f;
        expectedResult = 471.7f;
        Assert.assertEquals(expectedResult, helperTools.getDistance(deltaX,deltaY), 0.1);
    }
}

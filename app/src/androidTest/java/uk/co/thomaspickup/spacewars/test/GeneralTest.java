package uk.co.thomaspickup.spacewars.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.thomaspickup.spacewars.game.HelperTools;

/**
 * Created by thomaspickup on 08/04/2018.
 */
// TODO: Optimize and annotate
public class GeneralTest {
    private HelperTools helperTools = new HelperTools();

    // Helper Tools Tests
    @Test
    public void testHelperToolsInBetween() {
        // Expected: True 2 is in between 1 & 4
        boolean expectedResult = true;
        Assert.assertEquals(expectedResult, helperTools.inBetween(2,1,4));

        // Expected: False 400 is lower than the lower bound
        expectedResult = false;
        Assert.assertEquals(expectedResult, helperTools.inBetween(400, 500,600));
    }

    @Test
    public void testHelperToolsSpeedMultiplier() {
        // Expected: Returns the default value of 1.0f
        float expectedResult = 1.0f;
        Assert.assertEquals(expectedResult, helperTools.getSpeedMultiplier(500),0.0);

        // Expected: Returns the corresponding value of 1.5f
        expectedResult = 1.5f;
        Assert.assertEquals(expectedResult, helperTools.getSpeedMultiplier(3),0.0);
    }

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

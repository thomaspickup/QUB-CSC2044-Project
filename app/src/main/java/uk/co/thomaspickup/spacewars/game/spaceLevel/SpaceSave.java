package uk.co.thomaspickup.spacewars.game.spaceLevel;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import java.util.List;
import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;

/**
 * A save file which allows screens to transfer over the current state of the game.
 *
 * Created by Thomas Pickup
 */
public class SpaceSave {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport mLayerViewport;
    private PlayerSpaceship mPlayerSpaceShip;
    private List<Asteroid> mAsteroids;
    private List<AISpaceship> mAISpaceships;

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sets the LayerViewport of the save file.
     *
     * @param mLayerViewport LayerViewport
     */
    public void setMLayerViewport(LayerViewport mLayerViewport) {
        this.mLayerViewport = mLayerViewport;
    }

    /**
     * Sets the PlayerSpaceship of the save file.
     *
     * @param mPlayerSpaceShip PlayerSpaceship
     */
    public void setMPlayerSpaceShip(PlayerSpaceship mPlayerSpaceShip) {
        this.mPlayerSpaceShip = mPlayerSpaceShip;
    }

    /**
     * Sets the array of Asteroids of the save file.
     *
     * @param mAsteroids Array of Asteroids
     */
    public void setMAsteroids(List<Asteroid> mAsteroids) {
        this.mAsteroids = mAsteroids;
    }

    /**
     * Sets the array of AISpaceships of the save file.
     *
     * @param mAISpaceships Array of AI Spaceships
     */
    public void setMAISpaceships(List<AISpaceship> mAISpaceships) {
        this.mAISpaceships = mAISpaceships;
    }

    /**
     * Returns the LayerViewport of the save file.
     *
     * @return LayerViewport
     */
    public LayerViewport getMLayerViewport() {
        return mLayerViewport;
    }

    /**
     * Returns the PlayerSpaceship of the save file.
     *
     * @return PlayerSpaceship
     */
    public PlayerSpaceship getMPlayerSpaceShip() {
        return mPlayerSpaceShip;
    }

    /**
     * Returns the array of Asteroids of the save file.
     *
     * @return Array of Asteroids
     */
    public List<Asteroid> getMAsteroids() {
        return mAsteroids;
    }

    /**
     * Returns the array of AISpaceships of the save file.
     *
     * @return Array of AI Spaceships
     */
    public List<AISpaceship> getMAISpaceships() {
        return mAISpaceships;
    }
}

package uk.co.thomaspickup.spacewars.game.spaceLevel;

import java.util.List;

import uk.co.thomaspickup.spacewars.gage.world.LayerViewport;

/**
 * A save file which allows screens to transfer over the current state of the game.
 *
 * Created by thomaspickup on 01/04/2018.
 */
public class SpaceSave {
    // Creates the vars for the save features
    private LayerViewport mLayerViewport;
    private PlayerSpaceship mPlayerSpaceShip;
    private List<Asteroid> mAsteroids;
    private List<AISpaceship> mAISpaceships;

    // Getters and Setters
    public void setMLayerViewport(LayerViewport mLayerViewport) {
        this.mLayerViewport = mLayerViewport;
    }

    public void setMPlayerSpaceShip(PlayerSpaceship mPlayerSpaceShip) {
        this.mPlayerSpaceShip = mPlayerSpaceShip;
    }

    public void setMAsteroids(List<Asteroid> mAsteroids) {
        this.mAsteroids = mAsteroids;
    }

    public void setMAISpaceships(List<AISpaceship> mAISpaceships) {
        this.mAISpaceships = mAISpaceships;
    }

    public LayerViewport getMLayerViewport() {
        return mLayerViewport;
    }

    public PlayerSpaceship getMPlayerSpaceShip() {
        return mPlayerSpaceShip;
    }

    public List<Asteroid> getMAsteroids() {
        return mAsteroids;
    }

    public List<AISpaceship> getMAISpaceships() {
        return mAISpaceships;
    }
}

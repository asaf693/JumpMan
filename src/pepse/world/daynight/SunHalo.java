package pepse.world.daynight;

import static pepse.world.Constants.*;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

/**
 * A class to create the sun halo GameObject.
 */
public class SunHalo {

    /**
     * Creates a sun halo GameObject.
     * @param sun The sun GameObject.
     * @return The sun halo GameObject.
     */
    public static GameObject create(GameObject sun) {
        float sunHaloSize = sun.getDimensions().x() + RADIUS_INCREASE;
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(),
                            new Vector2(sunHaloSize, sunHaloSize),
                            new OvalRenderable(BASIC_SUN_HALO_COLOR)); // oval creates a circle
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sunHalo");

        // Update the sun halo's center to match sun's center using lambda expression of functional interface
        sunHalo.addComponent(deltaTime -> {sunHalo.setCenter(sun.getCenter());});
        return sunHalo;
    }
}

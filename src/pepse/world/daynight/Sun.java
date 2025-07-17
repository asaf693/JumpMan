package pepse.world.daynight;

import static pepse.world.Constants.*;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

/**
 * A class to create the sun GameObject.
 */
public class Sun {

    /**
     * Creates a sun GameObject.
     * @param windowDimensions The dimensions of the window.
     * @param cycleLength The length of the day-night cycle.
     * @return The sun GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        // I think its better not to cut windowDimensions.y() in half
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y());

        GameObject sun = new GameObject(initialSunCenter,
                        new Vector2(SUN_RADIUS, SUN_RADIUS),
                        new OvalRenderable(BASIC_SUN_COLOR)); // oval creates a circle
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");

        new Transition<Float>(
                sun, // the game object being changed
                (Float angle) ->
                        sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                INITIAL_SUN_ANGLE, // initial transition value
                FINAL_SUN_ANGLE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,// use a linear interpolator
                cycleLength * CYCLE_MULTIPLIER, // transition fully over a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null);// nothing further to execute upon reaching final value
        return sun;
    }
}

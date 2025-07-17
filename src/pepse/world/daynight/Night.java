package pepse.world.daynight;

import static pepse.world.Constants.*;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

/**
 * A class to create the night GameObject.
 */
public class Night {

    /**
     * Creates a night GameObject.
     * @param windowDimensions The dimensions of the window.
     * @param cycleLength The length of the day-night cycle.
     * @return The night GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO,
                               windowDimensions,
                               new RectangleRenderable(BASIC_NIGHT_COLOR));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null);// nothing further to execute upon reaching final value
        return night;
    }

}

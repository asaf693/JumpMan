package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class that creates a fruit GameObject.
 */
public class Fruit extends GameObject {
    private static final int WAIT_TIME = 30;
    private final Renderable renderable;
    private final Vector2 dimensions;

    /**
     * Creates a fruit GameObject, which is a rectangle with a specified renderable.
     * @param topLeftCorner The top left corner of the fruit.
     * @param renderable The renderable of the fruit.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.renderable = renderable;
        this.dimensions = dimensions;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Removes the fruit from the game.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")) {
            // make fruit disappear
            fruitDisappear();

            // make fruit reappear after a short time
            fruitReappear();
        }
    }

    /**
     * Makes the fruit disappear.
     */
    private void fruitDisappear() {
        renderer().setRenderable(null);
        this.setDimensions(Vector2.ZERO);
    }

    /**
     * Makes the fruit reappear.
     */
    private void fruitReappear() {
        new ScheduledTask(this,
                WAIT_TIME,
                false,
                () -> {renderer().setRenderable(renderable);
                    this.setDimensions(dimensions);});
    }
}

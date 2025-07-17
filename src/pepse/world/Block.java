package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class that creates a block GameObject.
 */
public class Block extends GameObject {

    /**
     * The size of the block.
     */
    public static final int SIZE = 30;

    /**
     * Creates a block GameObject, which is a rectangle with a specified renderable.
     * @param topLeftCorner The top left corner of the block.
     * @param renderable The renderable of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}

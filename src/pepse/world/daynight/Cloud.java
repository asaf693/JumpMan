package pepse.world.daynight;

import static pepse.world.Constants.*;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to create a cloud object.
 */
public class Cloud {

    private static Vector2 cloudPosition;
    private static Vector2 windowDimensions;
    private static final List<List<Block>> cloudBlocks = new ArrayList<>();

    /**
     * Creates a cloud object.
     * @param windowDimensions The dimensions of the window.
     * @param cloudPosition The starting position of the cloud.
     * @return The cloud object.
     */
    public static List<List<Block>> create(Vector2 windowDimensions, Vector2 cloudPosition) {
        Cloud.cloudPosition = cloudPosition;
        Cloud.windowDimensions = windowDimensions;
        for (int i = 0; i < CLOUD_SHAPE.size(); i++) {
            List<Block> row = new ArrayList<>();
            for (int j = 0; j < CLOUD_SHAPE.get(i).size(); j++) {
                if (CLOUD_SHAPE.get(i).get(j) == CLOUD_SQUARE_EXISTS) {
                    Block block = createCloudBlock(i, j);
                    row.add(block);
                }
            }
            cloudBlocks.add(row);
        }
        return cloudBlocks;
    }

    /**
     * Creates a horizontal transition for the cloud.
     * @param block The block to transition.
     * @param adjustment The adjustment.
     */
    private static void createHorizontalTransition(Block block, int adjustment) {
        new Transition<Float>(
                block, // the game object being changed
                (Float x) -> block.setTopLeftCorner(new Vector2(x, block.getTopLeftCorner().y())),
                (float) adjustment + block.getTopLeftCorner().x(),
                (windowDimensions.x() + CLOUD_WIDTH + block.getTopLeftCorner().x()),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CLOUD_TRANSLATION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * Creates a vertical transition for the cloud.
     * @param block The block to transition.
     * @param gameObjects The game objects.
     */
    private static void createVerticalTransition(Block block, GameObjectCollection gameObjects) {
        new Transition<Float>(
                block, // the game object being changed
                (Float y) -> block.setTopLeftCorner(new Vector2(block.getTopLeftCorner().x(), y)),
                block.getTopLeftCorner().y(),
                windowDimensions.y() + block.getTopLeftCorner().y(),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                VERTICAL_TRANSLATION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                () -> gameObjects.removeGameObject(block, Layer.BACKGROUND));
    }

    /**
     * Creates a fading transition for the cloud.
     * @param block The block to transition.
     */
    private static void createFadingTransition(Block block) {
        new Transition<Float>(
                block, // the game object being changed
                block.renderer()::setOpaqueness,
                FADE_INIT_VALUE,
                FADE_FINAL_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                FADE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * Creates a single rain drop.
     * @param gameObjects The game objects.
     * @param currentCloudPosition The current cloud position.
     * @param i The row index.
     * @param j The column index.
     * @return The rain drop.
     */
    private static Block createSingleDrop(GameObjectCollection gameObjects,
                                          Vector2 currentCloudPosition,
                                          int i, int j) {
        Vector2 topLeftCorner = new Vector2(currentCloudPosition.x() + j * Block.SIZE,
                currentCloudPosition.y() + i * Block.SIZE);
        Block block = new Block(topLeftCorner, new RectangleRenderable(BASE_DROPS_COLOR));
        block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        block.setDimensions(new Vector2(DROP_DIMENSION_SIZE, DROP_DIMENSION_SIZE));
        block.setTag("drop");
        // make the cloud move from left to right on loop until he crosses the screen
        createHorizontalTransition(block, 0);
        // make the rain drop fall
        createVerticalTransition(block, gameObjects);
        // make the drop fade away
        createFadingTransition(block);
        return block;
    }

    /**
     * Creates a cloud block.
     * @param i The row index.
     * @param j The column index.
     * @return The cloud block.
     */
    private static Block createCloudBlock(int i, int j) {
        Vector2 topLeftCorner = new Vector2(cloudPosition.x() + j * Block.SIZE,
                cloudPosition.y() + i * Block.SIZE);
        Block block = new Block(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateMonoColor(
                BASE_CLOUD_COLOR)));
        block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        block.setTag("cloud");
        // make the cloud move from left to right on loop until he crosses the screen
        createHorizontalTransition(block, -CLOUD_WIDTH);
        return block;
    }

    /**
     * Creates rain drops.
     * @param gameObjects The game objects.
     * @return The rain drops.
     */
    public static List<List<Block>> createRainDrops(GameObjectCollection gameObjects) {
        // we are running on rainDrops array, we randomise the rain drops, if value is 1 we create a drop
        List<List<Block>> rainDrops = new ArrayList<>(DROP_ARRAY_SIZE);
        Vector2 currentCloudPosition = cloudBlocks.get(0).get(0).getTopLeftCorner();
        for (int i = 0; i < DROP_ARRAY_SIZE; i++) {
            List<Block> row = new ArrayList<>(DROP_ARRAY_SIZE);
            for (int j = 0; j < DROP_ARRAY_SIZE; j++) {
                if (Math.random() < DROP_CHANCE) {
                    Block block = createSingleDrop(gameObjects, currentCloudPosition, i, j);
                    row.add(block);
                }
            }
            rainDrops.add(row);
        }
        return rainDrops;
    }

}
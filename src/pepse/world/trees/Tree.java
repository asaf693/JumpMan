package pepse.world.trees;

import static pepse.world.Constants.*;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.util.ArrayList;
import java.util.Random;
import danogl.components.Transition;
import danogl.components.ScheduledTask;

/**
 * A class to create the Tree GameObject.
 */
public class Tree {

    private final Vector2 topOfTheTree;
    private final ArrayList<Block> woodBlocks = new ArrayList<>();
    private final ArrayList<ArrayList<Block>> leaveBlocks = new ArrayList<>(TREE_SQUARE_SIZE);
    private final ArrayList<ArrayList<Fruit>> fruitBlocks = new ArrayList<>(TREE_SQUARE_SIZE);

    /**
     * Creates a Tree object.
     * @param topLeftCorner The top left corner of the tree.
     */
    public Tree(Vector2 topLeftCorner) {
        topOfTheTree = new Vector2(topLeftCorner.x(),
                topLeftCorner.y() - getRandomTreeHeight() * BLOCK_SIZE);
        createTree(topLeftCorner);
    }

    /**
     * Creates the tree GameObject.
     * @param topLeftCorner The top left corner of the tree.
     */
    public void createTree(Vector2 topLeftCorner) {

        int rectangleStartX = (int) (topLeftCorner.x() - (NUM_OF_LEAVES/2.0) * BLOCK_SIZE);
        int rectangleStartY = (int) (topOfTheTree.y() - (NUM_OF_LEAVES/2.0) * BLOCK_SIZE);

        // create the wood of the tree in a line going up
        createWoodBlocks(topLeftCorner);

        // create the leaves of the tree - a 8x8 square where the middle of the square is the top of the tree
        createLeaves(rectangleStartX, rectangleStartY);

        // create the fruits of the tree - a 8x8 square where the middle of the square is the top of the tree
        createFruits(rectangleStartX, rectangleStartY);
    }

    /**
     * Creates the wood blocks of the tree.
     * @param topLeftCorner The top left corner of the tree.
     */
    private void createWoodBlocks(Vector2 topLeftCorner) {
        RectangleRenderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_WOOD_COLOR));
        for (int i = 0; i < getRandomTreeHeight(); i++) {
            Vector2 blockPosition = new Vector2(topLeftCorner.x() - BLOCK_SIZE,
                    topLeftCorner.y() - BLOCK_SIZE - i * BLOCK_SIZE);
            Block block = new Block(blockPosition, renderable);
            block.setTag("wood");
            woodBlocks.add(block);
        }
    }

    /**
     * Creates the leaves of the tree.
     * @param rectangleStartX The x-coordinate of the start of the rectangle.
     * @param rectangleStartY The y-coordinate of the start of the rectangle.
     */
    private void createLeaves(int rectangleStartX, int rectangleStartY) {
        RectangleRenderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));

        for (int i = 0; i < NUM_OF_LEAVES; i++) {
            ArrayList<Block> row = new ArrayList<>(NUM_OF_LEAVES);
            for (int j = 0; j < NUM_OF_LEAVES; j++) {
                if (Math.random() < LEAF_CHANCES) {
                    Block leaf = createLeaf(renderable, rectangleStartX, rectangleStartY, i, j);
                    addLeafAnimations(leaf);
                    row.add(leaf);
                } else {
                    row.add(null);
                }
            }
            leaveBlocks.add(row);
        }
    }

    /**
     * Creates a single leaf block.
     * @param renderable The renderable for the leaf.
     * @param rectangleStartX The starting X-coordinate of the leaf grid.
     * @param rectangleStartY The starting Y-coordinate of the leaf grid.
     * @param row The current row in the leaf grid.
     * @param col The current column in the leaf grid.
     * @return The created leaf block.
     */
    private Block createLeaf(RectangleRenderable renderable, int rectangleStartX, int rectangleStartY,
                             int row, int col) {
        Vector2 blockPosition = new Vector2(rectangleStartX + col * BLOCK_SIZE,
                rectangleStartY + row * BLOCK_SIZE);
        Block leaf = new Block(blockPosition, renderable);
        leaf.setTag("leaf");
        return leaf;
    }

    /**
     * Adds animations (angle and width oscillation) to a single leaf.
     * @param leaf The leaf block to animate.
     */
    private void addLeafAnimations(Block leaf) {
        float delay = (float) (Math.random() * ANIMATION_DELAY_TIME); // Random delay up to 0.8 seconds
        new ScheduledTask(leaf, delay, false, () -> {
            addAngleOscillation(leaf);
            addWidthOscillation(leaf);
        });
    }

    /**
     * Adds angle oscillation animation to a leaf.
     * @param leaf The leaf block to animate.
     */
    private void addAngleOscillation(Block leaf) {
        new Transition<>(
                leaf,
                angle -> leaf.renderer().setRenderableAngle(angle),
                ANGLE_OSCILLATION_INIT_VALUE, ANGLE_OSCILLATION_FINAL_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAVE_TRANSITION_TIME, // Duration of the oscillation
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /**
     * Adds width oscillation animation to a leaf.
     * @param leaf The leaf block to animate.
     */
    private void addWidthOscillation(Block leaf) {
        new Transition<>(
                leaf,
                widthFactor -> leaf.setDimensions(new Vector2(BLOCK_SIZE * widthFactor, BLOCK_SIZE)),
                WIDTH_OSCILLATION_INIT_VALUE, WIDTH_OSCILLATION_FINAL_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAVE_TRANSITION_TIME, // Duration of the oscillation
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /**
     * Creates the fruits of the tree.
     * @param rectangleStartX The x-coordinate of the start of the rectangle.
     * @param rectangleStartY The y-coordinate of the start of the rectangle.
     */
    private void createFruits(int rectangleStartX, int rectangleStartY) {
        // create the fruits of the tree - a 8x8 square where the middle of the square is the top of the tree
        OvalRenderable ovalRenderable = new OvalRenderable(ColorSupplier.approximateColor(BASE_FRUIT_COLOR));
        for (int i = 0; i < NUM_OF_LEAVES; i++) {
            ArrayList<Fruit> row = new ArrayList<>(NUM_OF_LEAVES);
            for (int j = 0; j < NUM_OF_LEAVES; j++) {
                // randomly decide if a fruit block should be created
                if (Math.random() < FRUIT_CHANCES) {
                    Vector2 blockPosition = new Vector2(rectangleStartX + j * BLOCK_SIZE,
                            rectangleStartY + i * BLOCK_SIZE);
                    Fruit fruit =
                            new Fruit(blockPosition, new Vector2(FRUIT_SIZE, FRUIT_SIZE), ovalRenderable);
                    fruit.setTag("fruit");
                    row.add(fruit);
                }
                row.add(null);
            }
            fruitBlocks.add(row);
        }
    }

    /**
     * Gets a random tree height.
     * @return The random tree height.
     */
    private int getRandomTreeHeight() {
        Random random = new Random();
        return random.nextInt(RANDOM_RANGE) + MIN_TREE_HEIGHT;
    }

    /**
     * Gets the list of wood blocks of the tree.
     * @return The list of wood blocks of the tree.
     */
    public ArrayList<Block> getWoodBlocks() {
        return woodBlocks;
    }

    /**
     * Gets the list of leave blocks of the tree.
     * @return The list of leave blocks of the tree.
     */
    public ArrayList<ArrayList<Block>> getLeaveBlocks() {
        return leaveBlocks;
    }

    /**
     * Gets the list of fruit blocks of the tree.
     * @return The list of fruit blocks of the tree.
     */
    public ArrayList<ArrayList<Fruit>> getFruitBlocks() {
        return fruitBlocks;
    }

}

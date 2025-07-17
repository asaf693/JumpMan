package pepse.world;

import static pepse.world.Constants.*;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.NoiseGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * A class to create the Ground based on Block GameObjects.
 */
public class Terrain {
    private final float groundHeightAtX0;
    private final int seed;
    private List<List<Block>> blocks;

    /**
     * Creates a Terrain object.
     * @param windowDimensions The dimensions of the window.
     * @param seed The seed for the random number generator.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.seed = seed;
        groundHeightAtX0 = (windowDimensions.y() * HEIGHT_DIVISION_FACTOR);
    }

    /**
     * Returns the height of the ground at a given x-coordinate.
     * @param x The x-coordinate.
     * @return The height of the ground at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Adds the starting blocks of the game.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     */
    private void addStartingGameBlocks(int minX, int maxX) {
        int numOfBlocksColumns = (maxX - minX) / Block.SIZE;
        blocks = new ArrayList<>(numOfBlocksColumns);
        RectangleRenderable renderable = new RectangleRenderable(BASE_GROUND_COLOR);

        for (int i = 0; i < numOfBlocksColumns; i++) {
            float rawCurrentHeightX = groundHeightAt((float) minX + i * Block.SIZE);
            float fixedCurrentHeightX = (float) Math.floor(rawCurrentHeightX / Block.SIZE) * Block.SIZE;
            List<Block> blockList = new ArrayList<>(TERRAIN_DEPTH);

            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                blockList.add(new Block(new Vector2(minX + i * Block.SIZE,
                        fixedCurrentHeightX + j * Block.SIZE),
                        renderable));
            }
            blocks.add(blockList);
        }
    }

    /**
     * Creates a list of Block GameObjects in a given range.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of Block GameObjects.
     */
    public List<List<Block>> createInRange(int minX, int maxX) {
        if ((maxX - minX) % Block.SIZE != 0) {
            // change maxX so that maxX - minX is a multiple of Block.SIZE
            int cellingNum = (int) Math.ceil((double) (maxX - minX) / Block.SIZE);
            maxX = minX + cellingNum * Block.SIZE;
        }
        addStartingGameBlocks(minX, maxX);
        return blocks;
    }


    /**
     * Adds a column of blocks to the right of the terrain.
     * @param addGameObjectPredicate    The predicate to add a GameObject.
     */
    public void addOneBlockRight(BiConsumer<GameObject, Integer> addGameObjectPredicate) {
        // add a column of blocks to the right of the terrain
        RectangleRenderable renderable = new RectangleRenderable(BASE_GROUND_COLOR);
        List<Block> blockList = new ArrayList<>(TERRAIN_DEPTH);
        for (int j = 0; j < TERRAIN_DEPTH; j++) {
            float farthestX = blocks.get(blocks.size() - 1).get(0).getTopLeftCorner().x();
            blockList.add(new Block(new Vector2(farthestX + Block.SIZE,
                    groundHeightAt(farthestX + Block.SIZE) + j * Block.SIZE),
                    renderable));
        }
        blocks.add(blockList);
        for (Block block : blockList) {
            addGameObjectPredicate.accept(block, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Adds 10 columns of blocks to the right of the terrain.
     * @param removeGameObjectPredicate The predicate to remove a GameObject.
     * @param addGameObjectPredicate    The predicate to add a GameObject.
     */
    public void add10BlockColumnsRight(BiPredicate<GameObject, Integer> removeGameObjectPredicate,
                                       BiConsumer<GameObject, Integer> addGameObjectPredicate) {
        // add 10 columns of blocks to the right of the terrain
        for (int i = 0; i < BLOCKS_TO_ADD; i++) {
            addOneBlockRight(addGameObjectPredicate);
        }
        // remove the leftmost column of blocks
        for (Block block : blocks.get(0)) {
            removeGameObjectPredicate.test(block, Layer.STATIC_OBJECTS);
        }
        blocks.remove(0);
    }

    /**
     * Adds a column of blocks to the left of the terrain.
     * @param addGameObjectPredicate    The predicate to add a GameObject.
     */
    public void addOneBlockLeft(BiConsumer<GameObject, Integer> addGameObjectPredicate) {
        // add a column of blocks to the left of the terrain
        RectangleRenderable renderable =
                new RectangleRenderable(BASE_GROUND_COLOR);
        List<Block> blockList = new ArrayList<>(TERRAIN_DEPTH);
        for (int j = 0; j < TERRAIN_DEPTH; j++) {
            float farthestX = blocks.get(0).get(0).getTopLeftCorner().x();
            blockList.add(new Block(new Vector2(farthestX - Block.SIZE,
                    groundHeightAt(farthestX - Block.SIZE) + j * Block.SIZE),
                    renderable));
        }
        blocks.add(0, blockList);
        for (Block block : blockList) {
            addGameObjectPredicate.accept(block, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Adds 2 columns of blocks to the left of the terrain.
     * @param removeGameObjectPredicate The predicate to remove a GameObject.
     * @param addGameObjectPredicate    The predicate to add a GameObject.
     */
    public void add2BlockColumnsLeft(BiPredicate<GameObject, Integer> removeGameObjectPredicate,
                                     BiConsumer<GameObject, Integer> addGameObjectPredicate) {
        // add 2 columns of blocks to the left of the terrain
        for (int i = 0; i < BLOCKS_TO_ADD; i++) {
            addOneBlockLeft(addGameObjectPredicate);
        }
        // remove the rightmost column of blocks
        for (Block block : blocks.get(blocks.size() - 1)) {
            removeGameObjectPredicate.test(block, Layer.STATIC_OBJECTS);
        }
        blocks.remove(blocks.size() - 1);
    }
}
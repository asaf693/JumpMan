package pepse.world.trees;

import java.util.ArrayList;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A class to create the Flora based on Tree GameObjects.
 */
public class Flora {
    private final static double TREE_CHANCE = 0.001;
    private final static double ADDED_TREE_CHANCE = 0.05;
    private final Function<Float, Float> groundHeightAt;
    private final ArrayList<Tree> treeList = new ArrayList<>();
    private Random random;
    private final int seed;

    /**
     * Creates a Flora object.
     * @param groundHeightAt The function that returns the height of the ground at a given x-coordinate.
     */
    public Flora(Function<Float, Float> groundHeightAt, int seed) {
        this.seed = seed;
        this.groundHeightAt = groundHeightAt;
    }

    /**
     * Creates a list of Tree GameObjects in a given range.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of Tree GameObjects.
     */
    public ArrayList<Tree> createInRange(int minX, int maxX) {
        int range = maxX - minX + 1;
        for (int i = 0; i < range; i++) {
            random = new Random(Objects.hash(minX+i, seed));
            if (random.nextDouble() < TREE_CHANCE) {
                Vector2 topLeftCorner = new Vector2(minX + i, groundHeightAt.apply((float) minX + i));
                Tree tree = new Tree(topLeftCorner);
                treeList.add(tree);
            }
        }
        return treeList;
    }

    /**
     * Adds the trees that are in the given range.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @param addTreeParts The consumer to add the parts of the tree.
     */
    public void addInRange(int minX, int maxX, Consumer<Tree> addTreeParts) {
        for (int i = minX; i < maxX + 1; i++) {
            random = new Random(Objects.hash(i, seed));
            if (random.nextDouble() < ADDED_TREE_CHANCE) {
                Vector2 topLeftCorner = new Vector2(i, groundHeightAt.apply((float) i));
                Tree tree = new Tree(topLeftCorner);
                treeList.add(tree);
                addTreeParts.accept(tree);
            }
        }
    }

    /**
     * Removes the trees that are out of the given range.
     * @param treesToKeep The list of trees to keep.
     * @param removeGameObjectPredicate The predicate to remove a GameObject.
     */
    private void removeTree(ArrayList<Tree> treesToKeep,
                            BiPredicate<GameObject, Integer> removeGameObjectPredicate) {
        for (Tree tree : treeList) {
            if (!treesToKeep.contains(tree)) {
                for (GameObject block : tree.getWoodBlocks()) {
                    removeGameObjectPredicate.test(block, Layer.STATIC_OBJECTS);
                }
                for (ArrayList<Block> row : tree.getLeaveBlocks()) {
                    for (GameObject block : row) {
                        if (block != null) {
                            removeGameObjectPredicate.test(block, Layer.BACKGROUND);
                        }
                    }
                }
                for (ArrayList<Fruit> row : tree.getFruitBlocks()) {
                    for (Fruit fruit : row) {
                        if (fruit != null) {
                            removeGameObjectPredicate.test(fruit, Layer.STATIC_OBJECTS);
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes the trees that are out of the given range.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @param removeGameObjectPredicate The predicate to remove a GameObject.
     */
    public void deleteOutOfRange(int minX, int maxX, BiPredicate<GameObject,
            Integer> removeGameObjectPredicate) {
        ArrayList<Tree> treesToKeep = new ArrayList<>();
        for (int i = minX; i <= maxX; i++) {
            for (Tree currentTree : treeList) {
                if (currentTree.getWoodBlocks().get(0).getTopLeftCorner().x() == i) {
                    treesToKeep.add(currentTree);
                    break; // because there is only one tree at each x-coordinate
                }
            }
        }
        removeTree(treesToKeep, removeGameObjectPredicate);
    }

}

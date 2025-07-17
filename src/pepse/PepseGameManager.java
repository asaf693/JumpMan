package pepse;

// danogl imports
import danogl.*;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.*;

// pepse imports
import static pepse.world.Constants.*;
import pepse.world.*;
import pepse.world.daynight.*;
import pepse.world.trees.*;

// Java standard imports
import java.util.*;
import java.util.List;


/**
 * The game manager for the Pepse game.
 */
public class PepseGameManager extends GameManager {
    private GameObject numeric;
    private static Vector2 windowDimensions;
    private Terrain terrain;
    private int moveRightCounter;
    private int moveLeftCounter;
    private GameObject avatar;
    private Camera camera;
    private Flora flora;

    /**
     * Initializes the game.
     * @param imageReader The image reader.
     * @param soundReader The sound reader.
     * @param inputListener The input listener.
     * @param windowController The window controller.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        createSky(windowController);
        createGround(windowController);
        createNight(windowController);
        createSunAndHalo(windowController);
        createAvatar(windowController, inputListener, imageReader);
        createEnergyNumeric();
        createTree();
        createCloud(windowController);
    }

    /**
     * Creates the sky GameObject.
     * @param windowController The window controller.
     */
    private void createSky(WindowController windowController) {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /**
     * Creates the ground GameObject.
     * @param windowController The window controller.
     */
    private void createGround(WindowController windowController) {
        int terrainStart = -TERRAIN_ADJUSTER;
        int terrainEnd = (int) windowController.getWindowDimensions().x() + TERRAIN_ADJUSTER;
        terrain = new Terrain(windowController.getWindowDimensions(), GROUND_RANDOM_SEED);
        List<List<Block>> ground = terrain.createInRange(terrainStart, terrainEnd);
        for (List<Block> row : ground) {
            for (GameObject block : row) {
                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Creates the night GameObject.
     * @param windowController The window controller.
     */
    private void createNight(WindowController windowController) {
        gameObjects().addGameObject(Night.create(windowController.getWindowDimensions(),
                                    NIGHT_CYCLE_LEN/2f), Layer.BACKGROUND);
    }

    /**
     * Creates the sun GameObject.
     * @param windowController The window controller.
     */
    private void createSunAndHalo(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(), NIGHT_CYCLE_LEN);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
    }

    /**
     * Creates the avatar GameObject.
     * @param windowController The window controller.
     * @param inputListener The input listener.
     * @param imageReader The image reader.
     */
    private void createAvatar(WindowController windowController, UserInputListener inputListener,
                              ImageReader imageReader) {
        Vector2 avatarPos =
                new Vector2(windowController.getWindowDimensions().x() / 2, AVATAR_GAME_ENTRY_HEIGHT);
        GameObject avatar = new Avatar(avatarPos, inputListener, imageReader);
        avatar.setTag("avatar");
        camera = new Camera(avatar, new Vector2(0, 0), windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);
        this.avatar = avatar;
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
    }

    /**
     * Creates the energy numeric GameObject.
     */
    private void createEnergyNumeric() {
        int avatarEnergy = Avatar.getEnergy();
        TextRenderable textRenderable = new TextRenderable(avatarEnergy + "%");
        numeric = new GameObject(Vector2.ZERO, Vector2.ONES.mult(NUMERIC_SIZE), textRenderable);
        numeric.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(numeric, Layer.UI);
    }

    /**
     * Adds the tree parts to the game.
     * @param tree The tree object.
     */
    private void addTreeParts(Tree tree) {
        for (GameObject block : tree.getWoodBlocks()) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        // add leaf blocks to the game
        for (ArrayList<Block> row : tree.getLeaveBlocks()) {
            for (GameObject block : row) {
                if (block != null)
                    gameObjects().addGameObject(block, Layer.BACKGROUND);
            }
        }
        // add fruit blocks to the game
        for (ArrayList<Fruit> row : tree.getFruitBlocks()) {
            for (Fruit fruit : row) {
                if (fruit != null)
                    gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Creates the tree object.
     */
    private void createTree() {
        Flora flora = new Flora(terrain::groundHeightAt, GROUND_RANDOM_SEED);
        this.flora = flora;
        for (Tree tree : flora.createInRange(0, (int) windowDimensions.x())) {
            // add wood blocks to the game
            addTreeParts(tree);
        }
    }

    /**
     * Deletes trees that are out of range.
     * @param startingPoint The starting point.
     * @param endingPoint The ending point.
     */
    private void deleteTree(int startingPoint, int endingPoint) {
        flora.deleteOutOfRange(startingPoint, endingPoint, gameObjects()::removeGameObject);
    }

    /**
     * Creates the cloud object.
     * @param windowController The window controller.
     */
    private void createCloud(WindowController windowController) {
        List<List<Block>> clouds = Cloud.create(windowController.getWindowDimensions(),
                                    new Vector2(CLOUD_HEIGHT, CLOUD_HEIGHT));
        for (List<Block> row : clouds) {
            for (GameObject block : row) {
                gameObjects().addGameObject(block, Layer.BACKGROUND);
            }
        }
    }

    /**
     * Updates the energy numeric GameObject.
     */
    private void updateEnergy() {
        gameObjects().removeGameObject(numeric, Layer.UI);
        createEnergyNumeric();
    }

    /**
     * Checks if the avatar is jumping and creates rain drops if it is.
     */
    private void dropRain() {
        if (Avatar.isAvatarJumping()) {
            List<List<Block>> rainDrops = Cloud.createRainDrops(gameObjects());
            for (List<Block> row : rainDrops) {
                for (GameObject block : row) {
                    gameObjects().addGameObject(block, Layer.BACKGROUND);
                }
            }
        }
    }

    /**
     * Handles the avatar moving right.
     * @param cameraRightCornerX The x-coordinate of the right corner of the camera.
     */
    private void avatarMoveRightHandle( int cameraRightCornerX) {
        moveRightCounter++;
        if (moveRightCounter == MOVE_TARGET) {
            terrain.add10BlockColumnsRight(gameObjects()::removeGameObject, gameObjects()::addGameObject);
            deleteTree((int) camera.getTopLeftCorner().x(),
                    (int) (camera.getTopLeftCorner().x() + camera.getDimensions().x()));
            flora.addInRange(cameraRightCornerX,
                    cameraRightCornerX + TERRAIN_ADDITION_RANGE, this::addTreeParts);
            moveRightCounter = 0;
        }
    }

    /**
     * Handles the avatar moving left.
     * @param cameraLeftCornerX The x-coordinate of the left corner of the camera.
     */
    private void avatarMoveLeftHandle(int cameraLeftCornerX) {
        moveLeftCounter++;
        if (moveLeftCounter == MOVE_TARGET) {
            terrain.add2BlockColumnsLeft(gameObjects()::removeGameObject, gameObjects()::addGameObject);
            deleteTree((int) camera.getTopLeftCorner().x(),
                    (int) (camera.getTopLeftCorner().x() + camera.getDimensions().x()));
            flora.addInRange(cameraLeftCornerX - TERRAIN_ADDITION_RANGE,
                    cameraLeftCornerX, this::addTreeParts);
            moveLeftCounter = 0;
        }
    }

    /**
     * Checks if the avatar is moving, creates new terrain and flora if it is, and deletes trees
     * that are out of range.
     */
    private void avatarMovementHandle() {
        // get the x-coordinates of the camera space
        int[] cameraSpace = {(int) camera.getTopLeftCorner().x(),
                (int) camera.getTopLeftCorner().x() + (int) windowDimensions.x()};

        // if the avatar is moving right, create new terrain and flora
        if (Avatar.isAvatarMovingSideways().equals("right") && avatar.getVelocity().x() != 0) {
            avatarMoveRightHandle(cameraSpace[1]);

        // if the avatar is moving left, create new terrain and flora
        } else if (Avatar.isAvatarMovingSideways().equals("left") && avatar.getVelocity().x() != 0) {
            avatarMoveLeftHandle(cameraSpace[0]);
        }
    }

    /**
     * Checks the game state.
     */
    private void gameChecks() {
        updateEnergy();
        dropRain();
        avatarMovementHandle();
    }

    /**
     * Updates the game by checking for ball violations and renewing the game if necessary.
     * @param deltaTime the time passed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        gameChecks();
    }

    /**
     * Runs the game.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
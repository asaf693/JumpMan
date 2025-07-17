package pepse.world;

import static pepse.world.Constants.*;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * The avatar class for the Pep
 * The avatar can move left, right and jump
 * The avatar has an energy bar that decreases when moving left or right and jumping
 * The avatar has an energy bar that increases when idle
 */
public class Avatar extends GameObject {

    private final UserInputListener inputListener;
    private static float avatarEnergy = MAX_ENERGY;
    private final Renderable[] renderableIdle = new Renderable[4];
    private final Renderable[] renderableJump = new Renderable[4];
    private final Renderable[] renderableRun = new Renderable[6];
    private static boolean isJumping = false;
    private static boolean isMovingRight = false;
    private static boolean isMovingLeft = false;
    private AnimationRenderable animationIdle;
    private AnimationRenderable animationJump;
    private AnimationRenderable animationRun;

    /**
     * Creates an avatar object.
     * @param topLeftCorner the top left corner coordinates of the avatar
     * @param inputListener the input listener to listen for key presses
     * @param imageReader the image reader
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner,
                Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(AVATAR_IMAGE_PATH, false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        setRenderable(imageReader);
    }

    /**
     * Updates the avatar's position based on the user input.
     * @param direction the direction the avatar is moving
     */
    private boolean canUpdateEnergy(String direction) {
        boolean moveSignal = false;
        if ((direction.equals("left") || direction.equals("right")) && avatarEnergy >= MOVE_ENERGY) {
            avatarEnergy -= MOVE_ENERGY;
            moveSignal = true;
        } else if (direction.equals("up") && avatarEnergy >= JUMP_ENERGY) {
            avatarEnergy -= JUMP_ENERGY;
            moveSignal = true;
        } else if (direction.equals("idle") && avatarEnergy <= IDLE_MAX) {
            avatarEnergy += IDLE_ENERGY;
            moveSignal = true;
        }
        return moveSignal;
    }

    /**
     * Sets the renderable objects for the avatar.
     * @param imageReader the image reader
     */
    private void setRenderable(ImageReader imageReader) {
        for (int i = 0; i < renderableIdle.length; i++) {
            renderableIdle[i] = imageReader.readImage("assets/idle_" + i + ".png", false);
        }
        animationIdle = new AnimationRenderable(renderableIdle, ANIMATION_SPEED);
        for (int i = 0; i < renderableJump.length; i++) {
            renderableJump[i] = imageReader.readImage("assets/jump_" + i + ".png", false);
        }
        animationJump = new AnimationRenderable(renderableJump, ANIMATION_SPEED);
        for (int i = 0; i < renderableRun.length; i++) {
            renderableRun[i] = imageReader.readImage("assets/run_" + i + ".png", false);
        }
        animationRun = new AnimationRenderable(renderableRun, ANIMATION_SPEED);
    }

    /**
     * Gets the avatar's energy.
     * @return the avatar's energy
     */
    public static int getEnergy() {
        return (int) avatarEnergy;
    }

    /**
     * Gets whether the avatar is jumping.
     * @return whether the avatar is jumping
     */
    public static boolean isAvatarJumping() {
        return isJumping;
    }

    /**
     * Gets whether the avatar is moving sideways.
     * @return whether the avatar is moving sideways
     */
    public static String isAvatarMovingSideways() {
        if (isMovingRight) {
            return "right";
        } else if (isMovingLeft) {
            return "left";
        } else {
            return "idle";
        }
    }

    /**
     * Updates the avatar's position based on the user input.
     * @param deltaTime the time passed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        resetMovementFlags();
        float xVel = handleHorizontalMovement();
        transform().setVelocityX(xVel);
        handleJump();
        handleIdle();
    }

    /**
     * Resets the movement flags.
     */
    private void resetMovementFlags() {
        isMovingRight = false;
        isMovingLeft = false;
        isJumping = false;
    }

    /**
     * Handles the horizontal movement of the avatar.
     * @return the x velocity of the avatar
     */
    private float handleHorizontalMovement() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (canUpdateEnergy("left")) {
                renderer().setRenderable(animationRun);
                renderer().setIsFlippedHorizontally(true);
                xVel -= VELOCITY_X;
                isMovingLeft = true;
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (canUpdateEnergy("right")) {
                renderer().setRenderable(animationRun);
                renderer().setIsFlippedHorizontally(false);
                xVel += VELOCITY_X;
                isMovingRight = true;
            }
        }
        return xVel;
    }

    /**
     * Handles the jump of the avatar.
     */
    private void handleJump() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            if (canUpdateEnergy("up")) {
                renderer().setRenderable(animationJump);
                transform().setVelocityY(VELOCITY_Y);
                isJumping = true;
            }
        }
    }

    /**
     * Handles the idle state of the avatar.
     */
    private void handleIdle() {
        if (!(isMovingRight || isMovingLeft || isJumping) && this.getVelocity().equals(Vector2.ZERO)) {
            canUpdateEnergy("idle");
            renderer().setRenderable(animationIdle);
        }
    }

    /**
     * Calls the onCollision method of the collision strategy.
     * @param other the other object involved in the collision
     * @param collision the collision object
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("fruit")) {
            if (avatarEnergy + FRUIT_ENERGY_ADDITION <= MAX_ENERGY)
                avatarEnergy += FRUIT_ENERGY_ADDITION;
        }
    }
}

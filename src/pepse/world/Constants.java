package pepse.world;

import java.awt.*;
import java.util.List;

public class Constants {
        // cloud constants
        public static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
        public static final Color BASE_DROPS_COLOR = new Color(36, 126, 244);
        public static final int CLOUD_SQUARE_EXISTS = 1;
        public static final double DROP_CHANCE = 0.2;
        public static final int DROP_DIMENSION_SIZE = 20;
        public static final int DROP_ARRAY_SIZE = 6;
        public static final int FADE_TRANSITION_TIME = 2;
        public static final float FADE_INIT_VALUE = 0.8F;
        public static final float FADE_FINAL_VALUE = 0F;
        public static final int VERTICAL_TRANSLATION_TIME = 2;
        public static final int CLOUD_TRANSLATION_TIME = 10;
        public static final List<List<Integer>> CLOUD_SHAPE = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );
        public static final int CLOUD_WIDTH = CLOUD_SHAPE.get(0).size() * Block.SIZE;

        // night constants
        public static final Color BASIC_NIGHT_COLOR = Color.decode("#000000");
        public static final Float MIDNIGHT_OPACITY = 0.5f;

        // sun constants
        public static final Color BASIC_SUN_COLOR = Color.YELLOW;
        public static final int SUN_RADIUS = 120;
        public static final float INITIAL_SUN_ANGLE = 0;
        public static final float FINAL_SUN_ANGLE = 360;
        public static final float CYCLE_MULTIPLIER = 1;

        // sun halo constants
        public static final Color BASIC_SUN_HALO_COLOR = new Color(255, 255, 0, 20);
        public static final int RADIUS_INCREASE = 70;

        // tree constants
        public final static int NUM_OF_LEAVES = 8;
        public final static int RANDOM_RANGE = 3;
        public final static int MIN_TREE_HEIGHT = 8;
        public final static int BLOCK_SIZE = Block.SIZE;
        public final static int FRUIT_SIZE = 20;
        public final static int TREE_SQUARE_SIZE = 8;
        public final static int LEAVE_TRANSITION_TIME = 1;
        public final static double ANIMATION_DELAY_TIME = 0.8;
        public final static float ANGLE_OSCILLATION_INIT_VALUE = -15f;
        public final static float ANGLE_OSCILLATION_FINAL_VALUE = 15f;
        public final static float WIDTH_OSCILLATION_INIT_VALUE = 0.9f;
        public final static float WIDTH_OSCILLATION_FINAL_VALUE = 1.1f;
        public final static float LEAF_CHANCES = 0.6f;
        public final static float FRUIT_CHANCES = 0.1f;
        public final static Color BASE_WOOD_COLOR = new Color(43, 18, 4);
        public final static Color BASE_LEAF_COLOR = new Color(0, 128, 0);
        public final static Color BASE_FRUIT_COLOR = new Color(225, 18, 73);

        // avatar constants
        public static final float VELOCITY_X = 400;
        public static final float VELOCITY_Y = -650;
        public static final float GRAVITY = 600;
        public static final float AVATAR_SIZE = 50;
        public static final float JUMP_ENERGY = 10;
        public static final float MOVE_ENERGY = 0.5f;
        public static final float IDLE_ENERGY = 1;
        public static final float IDLE_MAX = 99;
        public static final float ANIMATION_SPEED = 0.2f;
        public static final float MAX_ENERGY = 100;
        public static final float FRUIT_ENERGY_ADDITION = 10;
        public static final String AVATAR_IMAGE_PATH = "assets/idle_0.png";

        // terrain constants
        public static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
        public static final int TERRAIN_DEPTH = 30;
        public static final int NOISE_FACTOR = 7;
        public static final int BLOCKS_TO_ADD = 2;
        public static final float HEIGHT_DIVISION_FACTOR = (2.0f / 3.0f);

        // pepseGameManager constants
        public static final int NIGHT_CYCLE_LEN = 30;
        public static final int GROUND_RANDOM_SEED = (int) System.currentTimeMillis();
        public static final float NUMERIC_SIZE = 50;
        public static final int AVATAR_GAME_ENTRY_HEIGHT = 200;
        public static final float CLOUD_HEIGHT = 100;
        public static final int MOVE_TARGET = 7;
        public static final int TERRAIN_ADJUSTER = 10 * Block.SIZE;
        public static final int TERRAIN_ADDITION_RANGE = 5;
    }
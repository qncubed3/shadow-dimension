import java.util.ArrayList;
import java.util.Arrays;
import bagel.util.Point;
import bagel.Image;

/**
 * The player controlled by the user
 * @author Quan Nguyen
 */

public class Player extends Entity {

    // Player constants
    private static final int DAMAGE = 20;
    private static final int MAX_HEALTH = 100;
    private static final int ATTACK_TIME = 60;
    private static final int ATTACK_COOLDOWN = 120;
    private static final int INVINCIBLE_TIME = 180;
    private static final int X_IMAGE_DIFF = 6;
    private static final Point HEALTH_DISPLAY_POINT = new Point(20, 25);
    private static final int HEALTH_SIZE = 30;

    // Player state constants
    private static final int PLAYER_RIGHT = 0;
    private static final int PLAYER_LEFT = 1;
    private static final int ATTACK_RIGHT = 2;
    private static final int ATTACK_LEFT = 3;
    
    // Player attributes
    private static String name = "Fae";
    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/fae/faeRight.png"),
        new Image("res/fae/faeLeft.png"),
        new Image("res/fae/faeAttackRight.png"),
        new Image("res/fae/faeAttackLeft.png")
    ));
    private boolean isRightFacing = true;
    private boolean isAttacking = false;
    private boolean isDamaging = false;
    private boolean isInvincible = false;
    private boolean isCoolingDown = false;
    private int attackDuration = 0;
    private int cooldownDuration = 0;
    private int invincibleDuration = 0;
    private boolean pushedBack = false;

    /**
     * Player constructor
     * @param xInitial initial x position
     * @param yInitial initial y position
     */
    public Player(int xInitial, int yInitial) {
        super(name, images, xInitial, yInitial, MAX_HEALTH, DAMAGE);
    }

    /**
     * Draw the player and healthbar
     */
    @Override
    public void draw() {
        if (!isAttacking && pushedBack) {
            pushedBack = false;
        }
        if (isAttacking && isRightFacing && !pushedBack && ShadowDimension.checkObstacleOverlap(this)) {
            this.move(new Point(-X_IMAGE_DIFF, 0));
            this.isRightFacing = true;
            pushedBack = true;
        } 

        // Draw player health bar
        ShadowDimension.drawHealthBar(this, HEALTH_DISPLAY_POINT, HEALTH_SIZE);

        // Draw the player
        super.draw();
    }

    /**
     * Move player by given vector
     * @param vector vector to move player along in the next frame
     */
    public void move(Point vector) {

        super.move(vector);

        // Change player facing direction
        if (vector.x < 0) {
            isRightFacing = false;
        } else if (vector.x > 0) {
            isRightFacing = true;
        }
    }

    // Update player state
    public void updateState() {

        // State change frame trackers
        if (attackDuration >= ATTACK_TIME) {
            isDamaging = false;
            isAttacking = false;
            isCoolingDown = true;
            attackDuration = 0;
        }
        if (cooldownDuration >= ATTACK_COOLDOWN) {
            isCoolingDown = false;
            cooldownDuration = 0;
        }
        if (invincibleDuration >= INVINCIBLE_TIME) {
            isInvincible = false;
            invincibleDuration = 0;
        }

        // Timing increments
        if (isAttacking) {
            attackDuration++;
        }
        if (isCoolingDown) {
            cooldownDuration++;
        }
        if (isInvincible) {
            invincibleDuration++;
        }

        // Change player image based on state
        if (!isRightFacing) {
            if (isAttacking) {
                super.setImageState(ATTACK_LEFT);
            } else {
                super.setImageState(PLAYER_LEFT);
            }
        } else if (isRightFacing) {
            if (isAttacking) {
                super.setImageState(ATTACK_RIGHT);
            } else {
                super.setImageState(PLAYER_RIGHT);
            }
        }

        // Draw updated player
        this.draw();
    }

    // Getter methods
    public boolean getAttacking() {
        return this.isAttacking;
    }

    public boolean getInvincible() {
        return this.isInvincible;
    }

    public boolean getIsDamaging() {
        return this.isDamaging;
    }

    // Setter methods
    public void setAttacking(boolean isAttacking) {
        if (!isCoolingDown) {
            this.isAttacking = isAttacking;
        }
    }

    public void setInvincible(boolean isInvincible) {
        this.invincibleDuration = 0;
        this.isInvincible = isInvincible;
    }
    
    public void setIsDamaging(boolean isDamaging) {
        this.isDamaging = isDamaging;
    }
}

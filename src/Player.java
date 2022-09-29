import java.util.ArrayList;
import java.util.Arrays;
import bagel.util.Point;
import bagel.Image;


public class Player extends Entity {

    // Enemy constants
    private static final int DAMAGE = 20;
    private static final int MAX_HEALTH = 100;
    private static final int ATTACK_TIME = 60;
    private static final int ATTACK_COOLDOWN = 120;
    private static final int INVINCIBLE_TIME = 180;

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

    // Player constructor
    public Player(int xInitial, int yInitial) {
        super(name, images, xInitial, yInitial, MAX_HEALTH, DAMAGE);
    }

    // Move player by given vector
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

    // Inflict damage on given enemy
    public void damageEnemy(Enemy enemy) {
        if (isDamaging == false) {
            isDamaging = true;
            enemy.takeDamage(this.getDamage());
        }
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

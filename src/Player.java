import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;
import bagel.util.Point;

public class Player extends Entity {
    // Player attributes
    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/fae/faeRight.png"),
        new Image("res/fae/faeLeft.png"),
        new Image("res/fae/faeAttackRight.png"),
        new Image("res/fae/faeAttackLeft.png")
    ));
    
    private static final int PLAYER_RIGHT = 0;
    private static final int PLAYER_LEFT = 1;
    private static final int ATTACK_RIGHT = 2;
    private static final int ATTACK_LEFT = 3;

    private static final int ATTACK_TIME = 60;
    private static final int ATTACK_COOLDOWN = 120;
    private static final int INVINCIBLE_TIME = 180;
    private boolean isRightFacing = true;
    private boolean isAttacking = false;
    private boolean isDamaging = false;
    private boolean isInvincible = false;
    private boolean isCoolingDown = false;
    private int attackDuration = 0;
    private int cooldownDuration = 0;
    private int invincibleDuration = 0;

    // Player constructor
    public Player(String name, int xInitial, int yInitial, int maxHealth, int damage) {
        super(name, images, xInitial, yInitial, maxHealth, damage);
    }


    // Move player by given vector
    public void move(Point vector) {
        // Update position and boundary of player
        if (!ShadowDimension.checkBorderCollision(this, vector)) {
            super.move(vector);
        }
        
        if (vector.x < 0) {
            isRightFacing = false;
        } else if (vector.x > 0) {
            isRightFacing = true;
        }
        // Update player image if direction has changed
        updateState();
    }

    public void updateState() {
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
        if (isAttacking) {
            attackDuration++;
        }
        if (isCoolingDown) {
            cooldownDuration++;
        }
        if (isInvincible) {
            invincibleDuration++;
        }
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
        this.draw();
    }

    public void damageEnemy(Enemy enemy) {
        if (isDamaging == false) {
            isDamaging = true;
            enemy.takeDamage(this.getDamage());
        }
    }

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

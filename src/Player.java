import java.util.ArrayList;
import java.util.Arrays;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


public class Player extends Entity implements Movable {
    // Player attributes
    private String name = "Player";
    private final int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;
    private boolean isRightFacing = true;
    private boolean isAttacking = false;
    private boolean isInvincible = false;
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
    private static int attackDuration = 0;

    // Player constructor
    public Player(String name, int xInitial, int yInitial) {
        super(name, images, xInitial, yInitial);
        this.name = name;
    }


    // Move player by given vector
    public void move(Point vector) {
        // Update position and boundary of player
        super.move(vector);
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
            isAttacking = false;
            attackDuration = 0;
        }
        if (isAttacking) {
            attackDuration++;
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

    

    public void takeDamage(int damage) {
        health = Math.max(health - damage, 0);
    }

    // Getter methods
    public double getHeight() {
        return Player.images.get(super.getImageState()).getHeight();
    }

    public double getWidth() {
        return Player.images.get(super.getImageState()).getWidth();
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.MAX_HEALTH;
    }
    
    public boolean getAttacking() {
        return this.isAttacking;
    }

    public boolean getInvincible() {
        return this.isInvincible;
    }

    // Setter methods
    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }


}

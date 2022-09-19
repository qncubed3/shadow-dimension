import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class Player extends Entity {
    // Player attributes
    private final String name;
    private final int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;
    private boolean rightFacing = true;
    private static final Image leftFacingPlayer = new Image("res/faeLeft.png");
    private static final Image rightFacingPlayer = new Image("res/faeRight.png");

    // Player constructor
    public Player(String name, int xInitial, int yInitial) {
        super(xInitial, yInitial);
        this.name = name;
    }

    // Draw player image onto game window
    @Override
    public void draw() {
        if (rightFacing) {
            rightFacingPlayer.drawFromTopLeft(super.getPosition().x, super.getPosition().y);
        } else {
            leftFacingPlayer.drawFromTopLeft(super.getPosition().x, super.getPosition().y);
        }
    }

    // Move player by given vector
    public void move(Point vector) {
        // Update position and boundary of player
        super.move(vector);
        // Update player image if direction has changed
        if (vector.x < 0) {
            rightFacing = false;
        } else if (vector.x > 0) {
            rightFacing = true;
        }
    }

    // Test if player will contact given wall
    public boolean contactsWall(Wall wall, Point direction) {
        // Calculate the next position of the player
        Rectangle nextBoundary = new Rectangle(
                super.getPosition().x + direction.x, super.getPosition().y + direction.y,
                this.getWidth(), this.getHeight()
        );
        // Returns true if player will contact wall given current direction in the next step
        return nextBoundary.intersects(wall.getBoundary());
    }

    // Getter methods
    public double getHeight() {
        return this.leftFacingPlayer.getHeight();
    }

    public double getWidth() {
        return this.leftFacingPlayer.getWidth();
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.MAX_HEALTH;
    }
    public String getName() {
        return this.name;
    }

    // Setter methods
    public void setHealth(int health) {
        this.health = health;
    }

    public void setRightFacing(boolean rightFacing) {
        this.rightFacing = rightFacing;
    }

}

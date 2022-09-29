import java.util.ArrayList;
import bagel.util.Rectangle;
import bagel.DrawOptions;
import bagel.util.Point;
import bagel.Image;

public abstract class Enemy extends Entity {

    // Enemy constants
    private static final int ENEMY_RIGHT = 0;
    private static final int ENEMY_LEFT = 1;
    private static final int INVINCIBLE_RIGHT = 2;
    private static final int INVINCIBLE_LEFT = 3;
    private static final int INVINCIBLE_TIME = 180;
    private static final int MAX_TIMESCALE = 3;
    private static final int MIN_TIMESCALE = -3;

    // Enemy attributes
    private Point vectorToPlayer = new Point(0, 0);
    private Point velocity = new Point(0, 0);
    private boolean isAggressive = false;
    private boolean isInvincible = false;
    private boolean isAttacking = false;
    private boolean isDamaging = false;
    private int invincibleDuration = 0;
    private static int timescale = 0;
    private int attackRange = 0;
    private Fire fire;

    // Enemy constructor
    public Enemy(String name, ArrayList<Image> images, Image fireImage, 
    int xPosition, int yPosition, int attackRange, int maxHealth, int damage) {
        super(name, images, xPosition, yPosition, maxHealth, damage);
        this.fire = new Fire(fireImage, xPosition, yPosition);
        this.attackRange = attackRange;
    }

    // Draw relevant features of an enemy
    @Override
    public void draw() {
        super.draw();

        // Draw fire if attacking
        if (isAttacking) {
            if (vectorToPlayer.x >= 0) {
                if (vectorToPlayer.y < 0) {
                    // Render fire at top right
                    fire.moveTo(this.getBoundary().topRight());
                    fire.move(new Point(0, -fire.getHeight()));
                    fire.draw(new DrawOptions().setRotation(Math.PI / 2));
                } else {
                    // Render fire at bottom right
                    fire.moveTo(this.getBoundary().bottomRight());
                    fire.draw(new DrawOptions().setRotation(Math.PI));
                }
            } else {
                if (vectorToPlayer.y < 0) {
                    // Render fire at top left
                    fire.moveTo(this.getBoundary().topLeft());
                    fire.move(new Point(-fire.getWidth(), -fire.getHeight()));
                    fire.draw();
                } else {
                    // Render fire at bottom left
                    fire.moveTo(this.getBoundary().bottomLeft());
                    fire.move(new Point(-fire.getWidth(), 0));
                    fire.draw(new DrawOptions().setRotation(-Math.PI / 2));
                }
            }
        }
        
        // Draw enemy health bar
        ShadowDimension.drawHealthBar(this, new Point(this.getPosition().x, this.getPosition().y - 6), 15);
    }

    // Move enemy by given vector
    @Override
    public void move(Point velocity) {

        // Only move aggressive entities
        if (this.isAggressive) {

            // Change enemy direction if collision detected
            if (ShadowDimension.checkBorderCollision(this, velocity) 
            || ShadowDimension.checkObstacleCollision(this, velocity)) {
                this.velocity = velocity = new Point(-velocity.x, -velocity.y);
            }
            super.move(scaleVelocity(velocity));
        }

    }

    // Update enemy state
    public void updateState() {
        if (this.getExists() == false) {
            return;
        }
        if (invincibleDuration >= INVINCIBLE_TIME) {
            isInvincible = false;
            invincibleDuration = 0;
        }
        if (isInvincible) {
            invincibleDuration++;
        }
        if (velocity.x < 0) {
            if (isInvincible) {
                super.setImageState(INVINCIBLE_LEFT);
            } else {
                super.setImageState(ENEMY_LEFT);
            }
        } else {
            if (isInvincible) {
                super.setImageState(INVINCIBLE_RIGHT);
            } else {
                super.setImageState(ENEMY_RIGHT);
            }
        }

        this.move(velocity);
        this.draw();
    }
    
    // Inflict damage on player
    public void damagePlayer(Player player) {
        if (isDamaging == false) {
            isDamaging = true;
            player.takeDamage(this.getDamage());
        }
    }

    // Check if given entity is within a radius of attackRange
    public boolean inRange(Entity entity) {
        
        // Calculate difference in x and y coordinates
        double dx = Math.max(0, Math.max(
            entity.getBoundary().left() - this.getCenter().x, 
            this.getCenter().x - entity.getBoundary().right()));
        double dy = Math.max(0, Math.max(
            entity.getBoundary().top() - this.getCenter().y, 
            this.getCenter().y - entity.getBoundary().bottom()));

        // Test for entity within range
        if (dx * dx + dy * dy <= attackRange * attackRange) {
            
            // Update enemy if entity is a player
            if (entity instanceof Player) {
                isAttacking = true;
                vectorToPlayer = this.getVectorTo(entity);
            }

            return true;
        }

        isAttacking = false;
        return false;
    }

    // Timescale controls
    public static void adjustTimescale(int increment) {
        if (increment > 0) {
            System.out.print("Sped up, Speed: ");
            if (Enemy.timescale < MAX_TIMESCALE) {
                timescale++;
            }
        } else if (increment < 0) {
            System.out.print("Slowed down: ");
            if (Enemy.timescale > MIN_TIMESCALE) {
                timescale--;
            }
        }
        System.out.println(timescale);
    }

    // Scale velocity by timescale
    public Point scaleVelocity(Point velocity) {
        return new Point(velocity.x * Math.pow(1.5, timescale), velocity.y * Math.pow(1.5, timescale));
    }

    // Setter methods
    public void setInvincible(boolean isInvincible) {
        this.invincibleDuration = 0;
        this.isInvincible = isInvincible;
    }

    public void setDamaging(boolean isDamaging) {
        this.isDamaging = isDamaging;
    }

    public void setAgressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    // Getter methods
    public boolean getInvincible() {
        return this.isInvincible;
    }

    public boolean getIsDamaging() {
        return this.isDamaging;
    }

    public Rectangle getFireBoundary() {
        return fire.getBoundary();
    }
}

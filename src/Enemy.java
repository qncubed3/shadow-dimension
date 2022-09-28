import java.util.ArrayList;

import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Enemy extends Entity implements Movable {

    private int damage;
    private static final int ENEMY_RIGHT = 0;
    private static final int ENEMY_LEFT = 1;
    private static final int INVINCIBLE_RIGHT = 2;
    private static final int INVINCIBLE_LEFT = 3;
    private static final int MAX_TIMESCALE = 3;
    private static final int MIN_TIMESCALE = -3;
    private static int timescale = 0;
    private boolean isAggressive = false;
    private boolean isInvincible = false;
    private boolean isAttacking = false;
    private boolean isDamaging = false;
    private Point velocity = new Point(0, 0);
    private int attackRange;
    private Fire fire;
    private Point vectorToPlayer = new Point(0, 0);

    public Enemy(String name, ArrayList<Image> images, Image fireImage, int xPosition, int yPosition, int attackRange, int maxHealth, int damage) {
        super(name, images, xPosition, yPosition, maxHealth);
        this.fire = new Fire(fireImage, xPosition, yPosition);
        this.damage = damage;
        this.attackRange = attackRange;
    }

    @Override
    public void draw() {
        super.draw();
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
        // Draw health
        ShadowDimension.drawHealthBar(this, new Point(this.getPosition().x, this.getPosition().y - 6), 15);
    }

    public void move() {
        if (this.isAggressive) {
            if (ShadowDimension.checkBorderCollision(this, velocity) || ShadowDimension.checkObstacleCollision(this, velocity)) {
                velocity = new Point(-velocity.x, -velocity.y);
            }
            super.move(scaleVelocity(velocity));
        }
    }

    public void updateState() {
        if (velocity.x < 0) {
            if (isInvincible) {
                super.setImageState(INVINCIBLE_LEFT);
            } else {
                super.setImageState(ENEMY_LEFT);
            }
        } else if (velocity.x > 0) {
            if (isInvincible) {
                super.setImageState(INVINCIBLE_RIGHT);
            } else {
                super.setImageState(ENEMY_RIGHT);
            }
        }
        this.move();
        this.draw();
    }

    public Point scaleVelocity(Point velocity) {
        return new Point(velocity.x * Math.pow(1.5, timescale), velocity.y * Math.pow(1.5, timescale));
    }
    

    // Check if given entity is within a radius of attackRange
    public boolean inRange(Entity entity) {

        // Calculate difference in x and y coordinates
        double dx = Math.max(0, Math.max(entity.getBoundary().left() - this.getCenter().x, this.getCenter().x - entity.getBoundary().right()));
        double dy = Math.max(0, Math.max(entity.getBoundary().top() - this.getCenter().y, this.getCenter().y - entity.getBoundary().bottom()));

        if (dx * dx + dy * dy <= attackRange * attackRange) {
            isAttacking = true;
            if (entity instanceof Player) {
                vectorToPlayer = this.getVectorTo(entity);
            }
            return true;
        }
        isAttacking = false;
        return false;
    }

    public Rectangle getFireBoundary() {
        return fire.getBoundary();
    }

    public void damagePlayer(Player player) {
        if (this.getIsDamaging() == false) {
            this.setIsDamaging(true);
            player.takeDamage(damage);
        }
    }
    
    public int getDamage() {
        return this.damage;
    }

    public boolean getIsDamaging() {
        return this.isDamaging;
    }

    public void setIsDamaging(boolean isDamaging) {
        this.isDamaging = isDamaging;
    }

    public void setIsAgressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

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
    
}

import java.util.ArrayList;
import bagel.util.Rectangle;
import bagel.DrawOptions;
import bagel.util.Point;
import bagel.Image;

public abstract class Entity {

    // Entity attributes
    private String name = "Entity";
    private ArrayList<Image> images = new ArrayList<>();
    private int imageState = 0;
    private int maxHealth = 0;
    private int damage = 0;
    private int health = 0;
    private boolean exists;
    private Point position;

    // Entity constructors
    public Entity(String name, ArrayList<Image> images, int xPosition, int yPosition, int maxHealth, int damage) {
        this.name = name;
        this.exists = true;  
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.images.addAll(images);
        this.position = new Point(xPosition, yPosition);
    }

    public Entity(String name, Image image, int xPosition, int yPosition, int damage) {
        this.name = name;
        this.exists = true;
        this.damage = damage;
        this.images.add(image);
        this.position = new Point(xPosition, yPosition);
    }

    public Entity(String name, Image image, int xPosition, int yPosition) {
        this.name = name;
        this.exists = true;
        this.images.add(image);
        this.position = new Point(xPosition, yPosition);
    }

    // Draw entity
    public void draw() {
        if (this.exists) {
            images.get(imageState).drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    // Draw entity using given drawOptions
    public void draw(DrawOptions drawOptions) {
        if (this.exists) {
            images.get(imageState).drawFromTopLeft(this.position.x, this.position.y, drawOptions);
        }
    }

    // Move entity by given vector
    public void move(Point vector) {
        // Update position and boundary of entity
        this.setPosition(this.position.x + vector.x, this.position.y + vector.y);
    }

    // Move entity to given position
    public void moveTo(Point position) {
        this.setPosition(position.x, position.y);
    }
    
    // Test if current entity will contact given entity
    public boolean contactsEntity(Entity entity, Point direction) {

        // No contact if entity does not exist
        if (entity.getExists() == false) {
            return false;
        }

        // Calculate the next position of the entity
        Rectangle nextBoundary = new Rectangle(
                this.getPosition().x + direction.x, this.getPosition().y + direction.y,
                this.getWidth(), this.getHeight()
        );

        // Returns true if current entity will contact entity given current direction in the next step
        return nextBoundary.intersects(entity.getBoundary());
    }

    // Returns vector from center of current entity to center of given entity
    public Point getVectorTo(Entity entity) {
        return new Point(
            entity.getCenter().x - this.getCenter().x, 
            entity.getCenter().y - this.getCenter().y);
    }

    // Find the center point of entity
    public Point getCenter() {
        return new Point(
            this.getPosition().x + this.getWidth() / 2,
            this.getPosition().y + this.getHeight() / 2);
    }

    // Reduce health of entity
    public void takeDamage(int damage) {
        this.setHealth(Math.max(this.getHealth() - damage, 0));
    }

    // Inflict damage on given entity
    public void damage(Entity entity) {
        entity.takeDamage(this.getDamage());
}

    // Getter methods
    public Point getPosition() {
        return this.position;
    }

    public boolean getExists() {
        return this.exists;
    }

    public double getWidth() {
        return this.images.get(imageState).getWidth();
    }

    public double getHeight() {
        return this.images.get(imageState).getHeight();
    }

    public Rectangle getBoundary() {
        return new Rectangle(position, this.getWidth(), this.getHeight());
    }

    public Image getImage() {
        return this.images.get(imageState);
    }

    public String getName() {
        return this.name;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getDamage() {
        return this.damage;
    }

    // Setter methods
    public void setPosition(double xPosition, double yPosition) {
        this.position = new Point(xPosition, yPosition);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public void setImageState(int state) {
        this.imageState = state;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

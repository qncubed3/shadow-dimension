import java.util.ArrayList;
import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Entity {
    // Entity attributes
    private String name = "Entity";
    private ArrayList<Image> images = new ArrayList<>();
    private int imageState = 0;
    private Point position;
    private boolean exists;
    private int health = 0;
    private int damage = 0;
    private int maxDamage = 0;

    public Entity(String name, ArrayList<Image> images, int xPosition, int yPosition) {
        this.images.addAll(images);
        this.position = new Point(xPosition, yPosition);
        this.exists = true;
        this.name = name;
    }

    public Entity(String name, Image image, int xPosition, int yPosition) {
        this.images.add(image);
        this.position = new Point(xPosition, yPosition);
        this.exists = true;
        this.name = name;
    }

    // Test if current entity will contact given entity
    public boolean contactsEntity(Entity entity, Point direction) {
        // Calculate the next position of the entity
        if (entity.getExists() == false) {
            return false;
        }
        Rectangle nextBoundary = new Rectangle(
                this.getPosition().x + direction.x, this.getPosition().y + direction.y,
                this.getWidth(), this.getHeight()
        );
        // Returns true if current entity will contact entity given current direction in the next step
        return nextBoundary.intersects(entity.getBoundary());
    }

    // Returns vector (Point) from center of current entity to center of given entity
    public Point getVectorTo(Entity entity) {
        return new Point(entity.getCenter().x - this.getCenter().x, entity.getCenter().y - this.getCenter().y);
    }

    public void draw() {
        if (this.exists) {
            images.get(imageState).drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    public void draw(DrawOptions drawOptions) {
        if (this.exists) {
            images.get(imageState).drawFromTopLeft(this.position.x, this.position.y, drawOptions);
        }
    }

    public void move(Point vector) {
        // Update position and boundary of entity
        this.setPosition(this.position.x + vector.x, this.position.y + vector.y);
    }

    public void moveTo(Point position) {
        this.setPosition(position.x, position.y);
    }

    public Point getCenter() {
        return new Point(
            this.getPosition().x + this.getWidth() / 2,
            this.getPosition().y + this.getHeight() / 2);
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

    public int getImageState() {
        return this.imageState;
    }

    public Image getImage() {
        return this.images.get(imageState);
    }

    public String getName() {
        return this.name;
    }

    public int getHealth() {
        return 0;
    }

    public int getMaxHealth() {
        return 0;
    }

    public int getDamage() {
        return 0;
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
}

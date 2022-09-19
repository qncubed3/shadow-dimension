import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Entity {
    // Entity attributes
    private Image image = new Image("res/entity.png");
    private Point position;
    private Rectangle boundary;
    private boolean exists;

    public Entity(Image image, int xPosition, int yPosition) {
        this.image = image;
        this.position = new Point(xPosition, yPosition);
        this.boundary = new Rectangle(this.getPosition(), image.getWidth(), image.getHeight());
        this.exists = true;
    }

    public Entity(int xPosition, int yPosition) {
        this.position = new Point(xPosition, yPosition);
        this.boundary = new Rectangle(this.getPosition(), image.getWidth(), image.getHeight());
        this.exists = true;
    }

    public void draw() {
        if (this.exists) {
            image.drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    public void move(Point vector) {
        // Update position and boundary of entity
        this.setPosition(this.position.x + vector.x, this.position.y + vector.y);
        boundary = new Rectangle(position, this.getWidth(), this.getHeight());
    }

    // Getter methods
    public Point getPosition() {
        return this.position;
    }

    public boolean getExists() {
        return this.exists;
    }

    public double getWidth() {
        return this.image.getWidth();
    }

    public double getHeight() {
        return this.image.getHeight();
    }

    public Rectangle getBoundary() {
        return this.boundary;
    }

    // Setter methods


    public void setImage(Image image) {
        this.image = image;
    }

    public void setPosition(double xPosition, double yPosition) {
        this.position = new Point(xPosition, yPosition);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }


}

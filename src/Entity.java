import java.util.ArrayList;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Entity {
    // Entity attributes
    private Image image = new Image("res/entity.png");
    private ArrayList<Image> images = new ArrayList<>();
    private int imageState = 0;
    private Point position;
    private Rectangle boundary;
    private boolean exists;

    public Entity(ArrayList<Image> images, int xPosition, int yPosition) {
        this.images.addAll(images);
        this.position = new Point(xPosition, yPosition);
        this.boundary = new Rectangle(this.getPosition(), image.getWidth(), image.getHeight());
        this.exists = true;
    }

    public Entity(Image image, int xPosition, int yPosition) {
        this.images.add(image);
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
            images.get(imageState).drawFromTopLeft(this.position.x, this.position.y);
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

    public int getImageState() {
        return this.imageState;
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

    public void setImageState(int state) {
        this.imageState = state;
    }


}

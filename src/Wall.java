import bagel.Image;

public class Wall extends Entity {
    // Wall attributes
    private static Image wallImage = new Image("res/wall.png");

    // Wall constructor
    public Wall(int xPosition, int yPosition) {
        super(wallImage, xPosition, yPosition);
    }
}

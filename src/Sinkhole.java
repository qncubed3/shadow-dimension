import bagel.Image;

public class Sinkhole extends Entity {
    // Sinkhole attributes
    private static final Image sinkholeImage = new Image("res/sinkhole.png");

    // Sinkhole constructor
    public Sinkhole(int xPosition, int yPosition) {
        super(sinkholeImage, xPosition, yPosition);
    }
}

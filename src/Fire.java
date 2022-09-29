import bagel.Image;

public class Fire extends Entity {
    
    // Fire attributes
    private static final String NAME = "Fire";


    // Fire constructor
    public Fire(Image image, int xPosition, int yPosition) {
        super(NAME, image, xPosition, yPosition);
    }
}

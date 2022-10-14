import bagel.Image;

/**
 * Implements a fire entity for each enemy
 * @author Quan Nguyen
 */

public class Fire extends Entity {

    // Fire attributes
    private static final String NAME = "Fire";

    /**
     * Fire constructor
     * @param image fire image
     * @param xPosition initial x posiiton
     * @param yPosition initial y position
     */
    public Fire(Image image, int xPosition, int yPosition) {
        super(NAME, image, xPosition, yPosition);
    }
}

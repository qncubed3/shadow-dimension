import bagel.Image;

/**
 * Implements a stationary sinkhole
 * @author Quan Nguyen
 */

public class Sinkhole extends Entity {

    // Sinkhole attributes
    private static final String NAME = "Sinkhole";
    private static final int DAMAGE = 30;
    private static final Image sinkholeImage = new Image("res/sinkhole.png");

    /**
     * Sinkhole constructor
     * @param xPosition x position
     * @param yPosition y position
     */
    public Sinkhole(int xPosition, int yPosition) {
        super(NAME, sinkholeImage, xPosition, yPosition, DAMAGE);
    }
}

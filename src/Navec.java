import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;

/**
 * Implements a Navec
 * @author Quan Nguyen
 */

public class Navec extends Enemy {

    // Nevec constants
    private static final int DAMAGE = 20;
    private static final int MAX_HEALTH = 80;
    private static final int ATTACK_RANGE = 200;

    // Navec attributes
    private static String name = "Navec";
    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecInvincibleRight.png"),
        new Image("res/navec/navecInvincibleLeft.png")
    ));
    private static Image fireImage = new Image("res/navec/navecfire.png");

    /**
     * Navec constructor
     * @param xPosition initial x position
     * @param yPosition initial y position
     */
    public Navec(int xPosition, int yPosition) {
        super(name, images, fireImage, xPosition, yPosition, ATTACK_RANGE, MAX_HEALTH, DAMAGE);
    }
}

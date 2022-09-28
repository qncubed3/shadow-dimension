import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;

public class Navec extends Enemy {
    private static final String NAME = "Navec";

    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecInvincibleRight.png"),
        new Image("res/navec/navecInvincibleLeft.png")
    ));

    private static Image fireImage = new Image("res/navec/navecfire.png");

    private static final int ATTACK_RANGE = 200;
    private static final int MAX_HEALTH = 80;
    private static final int DAMAGE = 20;

    public Navec(int xPosition, int yPosition) {
        super(NAME, images, fireImage, xPosition, yPosition, ATTACK_RANGE, MAX_HEALTH, DAMAGE);
    }
    
}

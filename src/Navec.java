import java.util.ArrayList;
import java.util.Arrays;

import bagel.Image;

public class Navec extends Enemy {

    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecInvincibleRight.png"),
        new Image("res/navec/navecInvincibleLeft.png")
    ));

    private static final int NAVEC_RIGHT = 0;
    private static final int NAVEC_LEFT = 1;
    private static final int INVINCIBLE_RIGHT = 2;
    private static final int INVINCIBLE_LEFT = 3;

    public Navec(int xPosition, int yPosition) {
        super(images, xPosition, yPosition);
    }
    
}

import java.util.ArrayList;
import java.util.Arrays;

import bagel.Image;

public class Demon extends Enemy {

    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonInvincibleRight.png"),
        new Image("res/demon/demonInvincibleLeft.png")
    ));

    private static final int DEMON_RIGHT = 0;
    private static final int DEMON_LEFT = 1;
    private static final int INVINCIBLE_RIGHT = 2;
    private static final int INVINCIBLE_LEFT = 3;

    public Demon(int xPosition, int yPosition) {
        super(images, xPosition, yPosition);
    }
    
}

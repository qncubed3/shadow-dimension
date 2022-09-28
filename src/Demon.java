import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;

public class Demon extends Enemy {
    private static final String NAME = "Demon";

    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonInvincibleRight.png"),
        new Image("res/demon/demonInvincibleLeft.png")
    ));

    private static Image fireImage = new Image("res/demon/demonfire.png");

    private static final int ATTACK_RANGE = 150;
    private static final int MAX_HEALTH = 40;
    private static final int DAMAGE = 10;

    public Demon(int xPosition, int yPosition) {
        super(NAME, images, fireImage, xPosition, yPosition, ATTACK_RANGE, MAX_HEALTH, DAMAGE);
    }
    
}

import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;

public class Barrier extends Entity {
    // Barrier types
    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/grass.png"),
        new Image("res/tree.png"),
        new Image("res/tnt.png"),
        new Image("res/grass.png")
    ));

    // Barrier labels (note: order must match arraylist of images above)
    private static ArrayList<String> names = new ArrayList<>(Arrays.asList(
        "Wall",
        "Tree",
        "TNT",
        "Grass"
    ));

    // Barrier constructor
    public Barrier(int xPosition, int yPosition, String name) {
        super(name, images, xPosition, yPosition);
        this.setImageState(names.indexOf(name));
    }

    public static ArrayList<String> getBarrierNames() {
        return Barrier.names;
    }

}

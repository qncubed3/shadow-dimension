import java.util.ArrayList;
import java.util.Arrays;
import bagel.Image;

public class Barrier extends Entity {
    // Barrier attributes
    private final static String NAME = "Barrier";
    private static ArrayList<Image> images = new ArrayList<>(Arrays.asList(
        new Image("res/wall.png"),
        new Image("res/tree.png")
    ));

    // Barrier labels
    private static ArrayList<String> names = new ArrayList<>(Arrays.asList(
        "Tree",
        "Wall"
    ));

    // Wall constructor
    public Barrier(int xPosition, int yPosition, String name) {
        super(NAME, images, xPosition, yPosition);
        this.setImageState(names.indexOf(name));
        this.setName(name);
    }

    public static ArrayList<String> getBarrierNames() {
        return Barrier.names;
    }
    
}

import bagel.Image;

public class Tree extends Entity {
    
    // Tree attributes
    private static final String NAME = "Tree";
    private static Image treeImage = new Image("res/tree.png");

    // Tree constructor
    public Tree(int xPosition, int yPosition) {
        super(NAME, treeImage, xPosition, yPosition);
    }
}

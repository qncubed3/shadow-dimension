import bagel.Image;

public class Tree extends Entity {
    // Tree attributes
    private static Image treeImage = new Image("res/tree.png");

    // Wall constructor
    public Tree(int xPosition, int yPosition) {
        super(treeImage, xPosition, yPosition);
    }
}

import bagel.Image;

public class Tree extends Obstacle {
    // Wall attributes
    private static Image treeImage = new Image("res/tree.png");

    // Wall constructor
    public Tree(int xPosition, int yPosition) {
        super(treeImage, xPosition, yPosition);
    }
}

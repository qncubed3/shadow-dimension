import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.DrawOptions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 *
 * Please enter your name below
 * Quan Nguyen
 */

public class ShadowDimension extends AbstractGame {
    // Game settings and constants
    private static final int SPEED = 2;
    private final int SINKHOLE_DAMAGE = 30;
    private static int WINDOW_WIDTH = 1024;
    private static int WINDOW_HEIGHT = 768;
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private static final int RED_HEALTH_MAX = 35;
    private static final int ORANGE_HEALTH_MAX = 65;
    private static final Point WIN_POINT = new Point(950, 670);
    private final static String GAME_TITLE = "SHADOW DIMENSION";

    // Direction constants
    private static final Point LEFT = new Point(-SPEED, 0);
    private static final Point RIGHT = new Point(SPEED, 0);
    private static final Point UP = new Point(0, -SPEED);
    private static final Point DOWN = new Point(0, SPEED);

    // Game state constants
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int LOSE = -1;
    private static final int WIN = 2;

    // Font, text and colour settings
    private static final String font = "res/frostbite.ttf";
    private static final int TITLE_SIZE = 75;
    private static final int MESSAGE_SIZE = 40;
    private static final int HEALTH_SIZE = 30;
    private final Font titleMessage = new Font(font, TITLE_SIZE);
    private final Font instructionMessage = new Font(font, MESSAGE_SIZE);
    private final Font winMessage = new Font(font, TITLE_SIZE);
    private final Font healthBar = new Font(font, HEALTH_SIZE);
    private static final Colour RED = new Colour(1, 0, 0);
    private static final Colour ORANGE = new Colour(0.9f, 0.6f, 0f);
    private static final Colour GREEN = new Colour(0f, 0.8f, 0.2f);

    // Fixed positions of text
    private static final Point WIN_MESSAGE_POINT = new Point(260, 380);
    private static final Point LOSE_MESSAGE_POINT = new Point(350, 380);
    private static final Point GAME_TITLE_POINT = new Point(260, 250);
    private static final Point INSTRUCTION_POINT1 = new Point(350, 440);
    private static final Point INSTRUCTION_POINT2 = new Point(300, 550);
    private static final Point HEALTH_DISPLAY_POINT = new Point(20, 25);

    // Entities
    private static Player player;
    private static ArrayList<Wall> walls = new ArrayList<>();
    private static ArrayList<Sinkhole> sinkholes = new ArrayList<>();

    // Other constants and attributes
    private static final int COLUMN1 = 1;
    private static final int COLUMN2 = 2;
    private static final double PERCENT = 100.0;
    private static Rectangle boundary;
    private static int gameState;

    // Game constructor
    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    // The entry point for the program.
    public static void main(String[] args) {

        ShadowDimension game = new ShadowDimension();
        readCSV();
        game.run();
    }

    // Reads data from provided level CSV file
    private static void readCSV(){
        try (BufferedReader reader = new BufferedReader(new FileReader("res/level0.csv"))) {
            // Declare buffer parameters
            String buffer = null;
            int playerCount = 0;
            int xPosition;
            int yPosition;
            Point topLeft = new Point();
            Point bottomRight = new Point();
            // Read through each line of CSV file and split into array of strings across commas
            while ((buffer = reader.readLine()) != null) {
                String cells[] = buffer.split(",");
                // If player is read
                if (cells[0].equals("Player")) {
                    // Check that no more than 1 player is read
                    if (playerCount >= 1) {
                        System.out.println("Invalid level");
                        System.exit(1);
                    }
                    // Get player parameters and define player
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[COLUMN2]);
                    player = new Player("Fae", xPosition, yPosition);
                    playerCount++;
                    // If a wall is read
                } else if (cells[0].equals("Wall")) {
                    // Get wall parameters and add wall to arraylist of walls
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[COLUMN2]);
                    walls.add(new Wall(xPosition, yPosition));
                    // If a sinkhole is read
                } else if (cells[0].equals("Sinkhole")) {
                    // Get sinkhole parameters and add sinkhole to arraylist of sinkholes
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[2]);
                    sinkholes.add(new Sinkhole(xPosition, yPosition));
                } else if (cells[0].equals("TopLeft")) {
                    // Get top left coordinate of window
                    topLeft = new Point(Integer.parseInt(cells[COLUMN1]), Integer.parseInt(cells[COLUMN2]));
                } else if (cells[0].equals("BottomRight")) {
                    // Get bottom right coordinate of window
                    bottomRight = new Point(Integer.parseInt(cells[COLUMN1]), Integer.parseInt(cells[COLUMN2]));
                }
            }
            boundary = new Rectangle(topLeft, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Returns true if player is not colliding with the border defined by point, and false if a collision occurs
    private boolean checkBorderCollision(Point direction) {
        // Check collision with left border
        if (direction.equals(LEFT) && player.getPosition().x > boundary.left()) {
            return true;
            // Check collision with right border
        } else if (direction.equals(RIGHT) && player.getPosition().x < boundary.right()) {
            return true;
            // Check collision with upper border
        } else if (direction.equals(UP) && player.getPosition().y > boundary.top()) {
            return true;
            // Check collision with lower border
        } else if (direction.equals(DOWN) && player.getPosition().y < boundary.bottom()) {
            return true;
        } else {
            return false;
        }
    }

    // Returns true if player will not collide with a wall, false otherwise
    private boolean checkWallCollision(Point direction) {
        // Go to each wall
        for (Wall wall: walls) {
            // Return false if current direction of player has/will contact a wall
            if (direction.equals(RIGHT) && player.contactsWall(wall, RIGHT)) {
                return false;
            } else if (direction.equals(LEFT) && player.contactsWall(wall, LEFT)) {
                return false;
            } else if (direction.equals(UP) && player.contactsWall(wall, UP)) {
                return false;
            } else if (direction.equals(DOWN) && player.contactsWall(wall, DOWN)) {
                return false;
            }
        }
        // If no collisions detected for given player direction
        return true;

    }

    private void checkSinkholeCollision() {
        for (Sinkhole sinkhole: sinkholes) {
            if (sinkhole.getExists() && player.getBoundary().intersects(sinkhole.getBoundary())) {
                if (SINKHOLE_DAMAGE > player.getHealth()) {
                    player.setHealth(0);
                    gameState = LOSE;
                } else {
                    player.setHealth(player.getHealth() - SINKHOLE_DAMAGE);
                }
                System.out.printf("Sinkhole inflicts %d damage points on %s. %s's current health: %d/%d\n",
                        SINKHOLE_DAMAGE, player.getName(), player.getName(),
                        player.getHealth(), player.getMaxHealth());
                sinkhole.setExists(false);
            }
        }
    }

    // Display health bar
    private void drawHealthBar() {
        int healthPercentage = (int) Math.round(player.getHealth() * PERCENT / player.getMaxHealth());
        String healthDisplay = healthPercentage + "%";
        if (healthPercentage < RED_HEALTH_MAX) {
            healthBar.drawString(
                    healthDisplay, HEALTH_DISPLAY_POINT.x, HEALTH_DISPLAY_POINT.y,
                    new DrawOptions().setBlendColour(RED));
        } else if (healthPercentage < ORANGE_HEALTH_MAX) {
            healthBar.drawString(
                    healthDisplay, HEALTH_DISPLAY_POINT.x, HEALTH_DISPLAY_POINT.y,
                    new DrawOptions().setBlendColour(ORANGE));
        } else {
            healthBar.drawString(
                    healthDisplay, HEALTH_DISPLAY_POINT.x, HEALTH_DISPLAY_POINT.y,
                    new DrawOptions().setBlendColour(GREEN));
        }
    }

    // Draw game obstacles
    private void drawObstacles() {
        // Draw all the walls in arraylist of walls
        for (Wall wall: walls) {
            wall.draw();
        }

        // Draw all the sinkholes in arraylist of sinkholes
        for (Sinkhole sinkhole: sinkholes) {
            sinkhole.draw();
        }
    }

    // Displays components of start screen
    private void runStartScreen(Input input) {
        titleMessage.drawString(GAME_TITLE, GAME_TITLE_POINT.x, GAME_TITLE_POINT.y);
        instructionMessage.drawString("PRESS SPACE TO START", INSTRUCTION_POINT1.x, INSTRUCTION_POINT1.y);
        instructionMessage.drawString("USE ARROW KEYS TO FIND GATE", INSTRUCTION_POINT2.x, INSTRUCTION_POINT2.y);
        if (input.isDown(Keys.SPACE)) {
            gameState = RUNNING;
        }
    }

    private void runGameScreen(Input input) {
        // Get key input and move if possible
        if (input.isDown(Keys.LEFT) && checkBorderCollision(LEFT) && checkWallCollision(LEFT)) {
            player.move(LEFT);
        }
        if (input.isDown(Keys.RIGHT) && checkBorderCollision(RIGHT) && checkWallCollision(RIGHT)) {
            player.move(RIGHT);
        }
        if (input.isDown(Keys.UP) && checkBorderCollision(UP) && checkWallCollision(UP)) {
            player.move(UP);
        }
        if (input.isDown(Keys.DOWN) && checkBorderCollision(DOWN) && checkWallCollision(DOWN)) {
            player.move(DOWN);
        }

        // Check sinkhole overlap
        checkSinkholeCollision();

        // Draw game elements
        BACKGROUND_IMAGE.drawFromTopLeft(0, 0);
        drawObstacles();
        drawHealthBar();
        player.draw();

        // Check win condition
        if (player.getPosition().x >= WIN_POINT.x && player.getPosition().y >= WIN_POINT.y) {
            gameState = WIN;
        }

        // Check lose condition
        if (player.getHealth() <= 0) {
            gameState = LOSE;
        }
    }

    // Performs a state update. Allows the game to exit when the escape key is pressed.
    @Override
    protected void update(Input input) {
        // Display start screen
        if (gameState == START) {
            runStartScreen(input);
        }

        // Display game
        if (gameState == RUNNING) {
            runGameScreen(input);
        }

        // If player has lost
        if (gameState == LOSE) {
            winMessage.drawString("GAME OVER!", LOSE_MESSAGE_POINT.x,LOSE_MESSAGE_POINT.y);
        }

        // If player has won
        if (gameState == WIN) {
            winMessage.drawString("CONGRATULATIONS!", WIN_MESSAGE_POINT.x,WIN_MESSAGE_POINT.y);
        }

        // Get key to close game
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }
}

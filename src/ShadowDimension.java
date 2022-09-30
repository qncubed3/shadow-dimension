import bagel.*;
import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Code for SWEN20003 Project 2, Semester 2, 2022
 *
 * Please enter your name below
 * Quan Nguyen
 */

public class ShadowDimension extends AbstractGame {

    // Game settings and constants
    private final static int PLAYER_SPEED = 2;
    private final int SINKHOLE_DAMAGE = 30;
    private static int WINDOW_WIDTH = 1024;
    private static int WINDOW_HEIGHT = 768;
    private final Image BACKGROUND_IMAGE_0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/background1.png");
    private static final int RED_HEALTH_MAX = 35;
    private static final int ORANGE_HEALTH_MAX = 65;
    private static final Point WIN_POINT = new Point(950, 670);
    private static final String LEVEL_0_FILE = "res/level0.csv";
    private static final String LEVEL_1_FILE = "res/level1.csv";
    private static final boolean TESTING_MODE = true;

    // Direction constants
    private static final Point UP = new Point(0, -1);
    private static final Point LEFT = new Point(-1, 0);
    private static final Point DOWN = new Point(0, 1);
    private static final Point RIGHT = new Point(1, 0);
    
    // Game state constants
    private static final int LOSE = -1;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int COMPLETE = 2;
    private static final int WIN = 3;
    private static final int LEVEL_0 = 0;
    private static final int LEVEL_1 = 1;

    // Font, text and colour settings
    private static final int TITLE_SIZE = 75;
    private static final int HEALTH_SIZE = 30;
    private static final int MESSAGE_SIZE = 40;
    private static final String FONT = "res/frostbite.ttf";
    private static final String GAME_TITLE = "SHADOW DIMENSION";
    private static final Colour RED = new Colour(1, 0, 0);
    private static final Colour ORANGE = new Colour(0.9f, 0.6f, 0f);
    private static final Colour GREEN = new Colour(0f, 0.8f, 0.2f);
    private final Font winMessage = new Font(FONT, TITLE_SIZE);
    private final Font titleMessage = new Font(FONT, TITLE_SIZE);
    private final Font instructionMessage = new Font(FONT, MESSAGE_SIZE);

    // Fixed positions of text
    private static final Point GAME_TITLE_POINT = new Point(260, 250);
    private static final Point WIN_MESSAGE_POINT = new Point(260, 380);
    private static final Point LOSE_MESSAGE_POINT = new Point(350, 380);
    private static final Point HEALTH_DISPLAY_POINT = new Point(20, 25);
    private static final Point INSTRUCTION_POINT_1 = new Point(350, 440);
    private static final Point INSTRUCTION_POINT_2 = new Point(300, 550);
    private static final Point INSTRUCTION_POINT_3 = new Point(350, 300);
    private static final Point INSTRUCTION_POINT_4 = new Point(375, 380);
    private static final Point INSTRUCTION_POINT_5 = new Point(360, 460);
    private static final Point INSTRUCTION_POINT_6 = new Point(290, 400);

    // Entities
    private static Player player;
    private static ArrayList<Entity> obstacles = new ArrayList<>();
    private static ArrayList<Enemy> enemies = new ArrayList<>();

    // Other constants and attributes
    private static final int COLUMN1 = 1;
    private static final int COLUMN2 = 2;
    private static final double PERCENT = 100.0;
    private static final int COMPLETE_TIME = 180;
    private static int levelCompleteDuration = 0;
    private static boolean level_0_read = false;
    private static boolean level_1_read = false;
    private static Rectangle gameBoundary;
    private static int level = LEVEL_0;
    private static int gameState;

    // The entry point for the program.
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    // Game constructor
    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }
    
    // Performs a state update. Allows the game to exit when the escape key is pressed.
    @Override
    protected void update(Input input) {
        
        readCSV();

        // Testing
        if (TESTING_MODE && input.isDown(Keys.W)) {
            level = LEVEL_1;
            level_1_read = false;
            gameState = COMPLETE;
        }

        if (gameState == COMPLETE) {
            titleMessage.drawString("LEVEL COMPLETE!", INSTRUCTION_POINT_6.x, INSTRUCTION_POINT_6.y);
            levelCompleteDuration++;
        }

        if (levelCompleteDuration >= COMPLETE_TIME) {
            gameState = START;
            levelCompleteDuration = 0;
        }

        // Display start screen
        if (gameState == START) {
            runStartScreen(input);
        }

        // Display game
        if (gameState == RUNNING) {
            runGameScreen(input);
        }

        // If player has won
        if (gameState == WIN) {
            winMessage.drawString("CONGRATULATIONS!", WIN_MESSAGE_POINT.x,WIN_MESSAGE_POINT.y);
        }

        // If player has lost
        if (gameState == LOSE) {
            winMessage.drawString("GAME OVER!", LOSE_MESSAGE_POINT.x,LOSE_MESSAGE_POINT.y);
        }

        // Get key to close game
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    // Reads data from provided level CSV file
    private static void readCSV() {
        if (level_0_read == false && level == LEVEL_0) {
            readLevel(LEVEL_0_FILE);
            level_0_read = true;
        } else if (level_1_read == false && level == LEVEL_1) {
            clearScene();
            readLevel(LEVEL_1_FILE);
            level_1_read = true;
        }
    }

    // Read level CSV file
    private static void readLevel(String levelFile) {

        ArrayList<Point> directions = new ArrayList<>(Arrays.asList(UP, RIGHT, DOWN, LEFT));
        Random random = new Random();

        // Buffer parameters
        Point bottomRight = new Point();
        Point topLeft = new Point();
        Boolean movable = false;
        String buffer = null;
        int xPosition;
        int yPosition;
        Demon demon;
        Navec navec;   
        
        try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {

            // Read through each line of CSV file and split into array of strings across commas
            while ((buffer = reader.readLine()) != null) {

                // Split line into cells 
                String cells[] = buffer.split(",");
                xPosition = Integer.parseInt(cells[COLUMN1]);
                yPosition = Integer.parseInt(cells[COLUMN2]);

                // Player
                if (cells[0].equals("Fae")|| cells[0].equals("Player")) {
                    player = new Player(xPosition, yPosition);

                // Wall
                } else if (cells[0].equals("Wall")) {
                    obstacles.add(new Wall(xPosition, yPosition));

                // Tree
                } else if (cells[0].equals("Tree")) {
                    obstacles.add(new Tree(xPosition, yPosition));

                // Sinkhole
                } else if (cells[0].equals("Sinkhole")) {
                    obstacles.add(new Sinkhole(xPosition, yPosition));

                // Demon
                } else if (cells[0].equals("Demon")) {
                    demon = new Demon(xPosition, yPosition);
                    movable = random.nextBoolean();
                    demon.setAgressive(movable);
                    if (movable) {
                        demon.setVelocity(makeVelocity(directions.get(random.nextInt(4)), 0.2 + 0.5 * random.nextDouble()));
                    }
                    enemies.add(demon);

                // Navec
                } else if (cells[0].equals("Navec")) {
                    navec = new Navec(xPosition, yPosition);
                    navec.setAgressive(true);
                    navec.setVelocity(makeVelocity(directions.get(random.nextInt(4)), 0.2 + 0.5 * random.nextDouble()));
                    enemies.add(navec);

                // Game boundary
                } else if (cells[0].equals("TopLeft")) {
                    // Get top left coordinate of window
                    topLeft = new Point(Integer.parseInt(cells[COLUMN1]), Integer.parseInt(cells[COLUMN2]));
                } else if (cells[0].equals("BottomRight")) {
                    // Get bottom right coordinate of window
                    bottomRight = new Point(Integer.parseInt(cells[COLUMN1]), Integer.parseInt(cells[COLUMN2]));
                }
            }

            gameBoundary = new Rectangle(topLeft, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Clear all entities
    private static void clearScene() {
        obstacles.removeAll(obstacles);
        enemies.removeAll(enemies);
    }

    // Draw level background
    private void drawBackground() {
        switch(level) {
            case LEVEL_0: 
                BACKGROUND_IMAGE_0.drawFromTopLeft(0, 0);
                break;
            case LEVEL_1:
                BACKGROUND_IMAGE_1.drawFromTopLeft(0, 0);
                break;
        }
    }

    // Display health bar
    public static void drawHealthBar(Entity entity, Point position, int size) {

        // Percentage calculation and formatting
        int healthPercentage = (int) Math.round(entity.getHealth() * PERCENT / entity.getMaxHealth());
        String healthDisplay = healthPercentage + "%";
        Font healthBar = new Font(FONT, size);

        // Colour control depending on percentage
        if (healthPercentage < RED_HEALTH_MAX) {
            healthBar.drawString(
                healthDisplay, position.x, position.y,
                new DrawOptions().setBlendColour(RED));
        } else if (healthPercentage < ORANGE_HEALTH_MAX) {
            healthBar.drawString(
                healthDisplay, position.x, position.y,
                new DrawOptions().setBlendColour(ORANGE));
        } else {
            healthBar.drawString(
                healthDisplay, position.x, position.y,
                new DrawOptions().setBlendColour(GREEN));
        }
    }

    // Draw game obstacles
    private void drawObstacles() {

        // Draw all the walls and trees in arraylist of obstacles
        for (Entity obstacle: obstacles) {
            obstacle.draw();
        }
    }

    // Check if player is within attack range of enemies
    private void checkEnemyRange() {
        for (Enemy enemy: enemies) {
            // If player in range, set enemy isAttacking to true
            enemy.inRange(player);
        }
    }

    // Check for damage inflicted amongst entities
    private void checkDamage() {

        // Interactions between player and enemies
        for (Enemy enemy: enemies) {
            if (enemy.getExists()) {

                // Check damage from enemy to player
                if (player.getBoundary().intersects(enemy.getFireBoundary()) && player.getInvincible() == false) {
                    if (enemy.getIsDamaging() == false) {
                        enemy.damagePlayer(player);
                        player.setInvincible(true);
                        printDamage(enemy, player);
                    } 
                } else {
                    enemy.setDamaging(false);
                }
                
                // Check damage from player to enemy
                if (player.getAttacking() && player.getBoundary().intersects(enemy.getBoundary()) && enemy.getInvincible() == false) {
                    if (player.getIsDamaging() == false) {
                        player.damageEnemy(enemy);
                        enemy.setInvincible(true);
                        printDamage(player, enemy);

                        // Check enemy health
                        if (enemy.getHealth() <= 0) {
                            enemy.setExists(false);
                        }
                    }
                }
            }
        }

        // Interactions between player and sinkholes
        checkSinkholeCollision();
    }

    // Check if player overlaps with sinkhole
    private void checkSinkholeCollision() {

        // Ignore sinkhole if player is invincible
        if (player.getInvincible()) {
            return;
        }
        
        // Find sinkholes in arraylist of obstacles
        for (Entity obstacle: obstacles) {
            if (obstacle instanceof Sinkhole && obstacle.getExists() && player.getBoundary().intersects(obstacle.getBoundary())) {

                // Damage player and remove sinkhole if overlap detected
                player.takeDamage(SINKHOLE_DAMAGE);
                printDamage(obstacle, player);
                obstacle.setExists(false);
            }
        }
    }

    // Update state of all enemies
    private void updateEnemies() {
        for (Enemy enemy: enemies) {
            enemy.updateState();
            if (enemy instanceof Navec && enemy.getExists() == false) {
                gameState = WIN;
            }
        }
    }

    // Update state of player
    private void updatePlayer() {
        player.updateState();
    }

    

    // Returns true if entity collides with obstacles or border in given direction
    private boolean checkCollisions(Entity entity, Point direction) {
        return checkBorderCollision(entity, direction) 
            || checkObstacleCollision(entity, direction);
    }

    // Returns true if entity is colliding with border
    public static boolean checkBorderCollision(Entity entity, Point direction) {
        // Check collision with left border
        if (direction.x < 0 && entity.getBoundary().left() > gameBoundary.left()) {
            return false;
            // Check collision with right border
        } else if (direction.x > 0 && entity.getBoundary().right() < gameBoundary.right() + player.getWidth()) {
            return false;
            // Check collision with upper border
        } else if (direction.y < 0 && entity.getBoundary().top() > gameBoundary.top()) {
            return false;
            // Check collision with lower border
        } else if (direction.y > 0 && entity.getBoundary().bottom() < gameBoundary.bottom() + player.getHeight()) {
            return false;
        } else {
            return true;
        }
    }

    // Returns true if movable entity will collide with an obstacle
    public static boolean checkObstacleCollision(Entity entity, Point direction) {

        for (Entity obstacle: obstacles) {

            // Allow player to collide with sinkhole
            if (entity instanceof Player && obstacle instanceof Sinkhole) {
                continue;
            }

            // Return true if current direction of entity will contact obstacle
            if (direction.x > 0 && entity.contactsEntity(obstacle, makeVelocity(RIGHT, PLAYER_SPEED))) {
                return true;
            } else if (direction.x < 0 && entity.contactsEntity(obstacle, makeVelocity(LEFT, PLAYER_SPEED))) {
                return true;
            } else if (direction.y < 0 && entity.contactsEntity(obstacle, makeVelocity(UP, PLAYER_SPEED))) {
                return true;
            } else if (direction.y > 0 && entity.contactsEntity(obstacle, makeVelocity(DOWN, PLAYER_SPEED))) {
                return true;
            }
        }
        return false;

    }

    // Displays components of start screen. Accepts player inputs.
    private void runStartScreen(Input input) {

        // Display text for level start screens
        if (level == LEVEL_0) {
            titleMessage.drawString(GAME_TITLE, GAME_TITLE_POINT.x, GAME_TITLE_POINT.y);
            instructionMessage.drawString("PRESS SPACE TO START", INSTRUCTION_POINT_1.x, INSTRUCTION_POINT_1.y);
            instructionMessage.drawString("USE ARROW KEYS TO FIND GATE", INSTRUCTION_POINT_2.x, INSTRUCTION_POINT_2.y);
        } else if (level == LEVEL_1) {
            instructionMessage.drawString("PRESS SPACE TO START", INSTRUCTION_POINT_3.x, INSTRUCTION_POINT_3.y);
            instructionMessage.drawString("PRESS A TO ATTACK", INSTRUCTION_POINT_4.x, INSTRUCTION_POINT_4.y);
            instructionMessage.drawString("DEFEAT NAVEC TO WIN", INSTRUCTION_POINT_5.x, INSTRUCTION_POINT_5.y);
        }
        
        // Run the game if space bar pressed
        if (input.isDown(Keys.SPACE)) {
            gameState = RUNNING;
        }
    }

    // Displays components of game. Accepts player inputs.
    private void runGameScreen(Input input) {

        // Get key input and move if possible
        if (input.isDown(Keys.LEFT) && !checkCollisions(player, LEFT)) {
            player.move(makeVelocity(LEFT, PLAYER_SPEED));
        }
        if (input.isDown(Keys.RIGHT) && !checkCollisions(player, RIGHT)) {
            player.move(makeVelocity(RIGHT, PLAYER_SPEED));
        }
        if (input.isDown(Keys.UP) && !checkCollisions(player, UP)) {
            player.move(makeVelocity(UP, PLAYER_SPEED));
        }
        if (input.isDown(Keys.DOWN) && !checkCollisions(player, DOWN)) {
            player.move(makeVelocity(DOWN, PLAYER_SPEED));
        }
        if (input.wasPressed(Keys.A)) {
            player.setAttacking(true);    
        } 

        // Keyboard inputs for level 1
        if (level == LEVEL_1) {
            if (input.wasPressed(Keys.L)) {
                Enemy.adjustTimescale(1);
            } 
            if (input.wasPressed(Keys.K)) {
                Enemy.adjustTimescale(-1);      
            } 
            
        }
        
        // Draw level scene
        drawBackground();
        drawObstacles();
        drawHealthBar(player, HEALTH_DISPLAY_POINT, HEALTH_SIZE);

        // Check interactions between entities
        checkEnemyRange();
        checkDamage();

        // Update moving entities
        updateEnemies();
        updatePlayer();

        // Check win conditions
        checkWinCondition();

        // Check lose condition
        if (player.getHealth() <= 0) {
            gameState = LOSE;
        }
    }

    // Check win conditions
    private void checkWinCondition() {
        if (level == LEVEL_0) {
            if (player.getPosition().x >= WIN_POINT.x && player.getPosition().y >= WIN_POINT.y) {
                gameState = COMPLETE;
                level = LEVEL_1;
            }
        }
    }

    // Print damage entity A inflicts on entity B
    private void printDamage(Entity A, Entity B) {
        System.out.printf("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                        A.getName(), A.getDamage(), B.getName(), B.getName(), B.getHealth(), B.getMaxHealth());
    }

    // Return vector given unit vector and speed
    private static Point makeVelocity(Point direction, double speed) {
        return new Point(direction.x * speed, direction.y * speed);
    }
}

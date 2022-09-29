import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.DrawOptions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
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
    private static final int PLAYER_HEALTH = 100;
    private static final int DEMON_HEALTH = 40;
    private static final int NAVEC_HEALTH = 80;
    private static final int PLAYER_DAMAGE = 20;
    private static final int DEMON_DAMAGE = 10;
    private static final int NAVEC_DAMAGE = 20;

    // Direction constants
    private static final Point LEFT = new Point(-1, 0);
    private static final Point RIGHT = new Point(1, 0);
    private static final Point UP = new Point(0, -1);
    private static final Point DOWN = new Point(0, 1);

    // Game state constants
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int LOSE = -1;
    private static final int WIN = 2;
    private static final int LEVEL_0 = 0;
    private static final int LEVEL_1 = 1;

    // Font, text and colour settings
    private static final String GAME_TITLE = "SHADOW DIMENSION";
    private static final String font = "res/frostbite.ttf";
    private static final int TITLE_SIZE = 75;
    private static final int MESSAGE_SIZE = 40;
    private static final int HEALTH_SIZE = 30;
    private final Font titleMessage = new Font(font, TITLE_SIZE);
    private final Font instructionMessage = new Font(font, MESSAGE_SIZE);
    private final Font winMessage = new Font(font, TITLE_SIZE);
    private static final Colour RED = new Colour(1, 0, 0);
    private static final Colour ORANGE = new Colour(0.9f, 0.6f, 0f);
    private static final Colour GREEN = new Colour(0f, 0.8f, 0.2f);

    // Fixed positions of text
    private static final Point WIN_MESSAGE_POINT = new Point(260, 380);
    private static final Point LOSE_MESSAGE_POINT = new Point(350, 380);
    private static final Point GAME_TITLE_POINT = new Point(260, 250);
    private static final Point INSTRUCTION_POINT_1 = new Point(350, 440);
    private static final Point INSTRUCTION_POINT_2 = new Point(300, 550);
    private static final Point INSTRUCTION_POINT_3 = new Point(350, 300);
    private static final Point INSTRUCTION_POINT_4 = new Point(375, 380);
    private static final Point INSTRUCTION_POINT_5 = new Point(360, 460);
    private static final Point HEALTH_DISPLAY_POINT = new Point(20, 25);

    // Entities
    private static Player player;
    private static ArrayList<Entity> obstacles = new ArrayList<>();
    private static ArrayList<Sinkhole> sinkholes = new ArrayList<>();
    private static ArrayList<Enemy> enemies = new ArrayList<>();

    // Other constants and attributes
    private static final int COLUMN1 = 1;
    private static final int COLUMN2 = 2;
    private static final double PERCENT = 100.0;
    private static Rectangle boundary;
    private static int gameState;
    private static int level = LEVEL_0;
    private static boolean level_0_read = false;
    private static boolean level_1_read = false;

    // Game constructor
    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    // The entry point for the program.
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    // Reads data from provided level CSV file
    private static void readCSV() {
        if (level_0_read == false && level == LEVEL_0) {
            readLevel0();
            level_0_read = true;
        } else if (level_1_read == false && level == LEVEL_1) {
            clearScene();
            readLevel1();
            level_1_read = true;
        }
    }

    // Read level 0
    private static void readLevel0() {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEVEL_0_FILE))) {
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
                    player = new Player("Fae", xPosition, yPosition, PLAYER_HEALTH, PLAYER_DAMAGE);
                    playerCount++;
                    // If a wall is read
                } else if (cells[0].equals("Wall")) {
                    // Get wall parameters and add wall to arraylist of walls
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[COLUMN2]);
                    obstacles.add(new Wall(xPosition, yPosition));
                    // If a sinkhole is read
                } else if (cells[0].equals("Sinkhole")) {
                    // Get sinkhole parameters and add sinkhole to arraylist of sinkholes
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[2]);
                    obstacles.add(new Sinkhole(xPosition, yPosition));
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

    // Read level 1
    private static void readLevel1() {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEVEL_1_FILE))) {
            // Declare buffer parameters
            String buffer = null;
            int playerCount = 0;
            int xPosition;
            int yPosition;

            ArrayList<Point> directions = new ArrayList<>(Arrays.asList(UP, RIGHT, DOWN, LEFT));
            Random random = new Random();

            Demon demon;
            Boolean movable = false;
            Navec navec;
            Point topLeft = new Point();
            Point bottomRight = new Point();
            // Read through each line of CSV file and split into array of strings across commas
            while ((buffer = reader.readLine()) != null) {
                String cells[] = buffer.split(",");
                // If player is read
                if (cells[0].equals(player.getName())) {
                    // Check that no more than 1 player is read
                    if (playerCount >= 1) {
                        System.out.println("Invalid level");
                        System.exit(1);
                    }
                    // Get player parameters and define player
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[COLUMN2]);
                    player = new Player("Fae", xPosition, yPosition, PLAYER_HEALTH, PLAYER_DAMAGE);
                    playerCount++;
                    // If a wall is read
                } else if (cells[0].equals("Tree")) {
                    // Get tree parameters and add wall to arraylist of walls
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[COLUMN2]);
                    obstacles.add(new Tree(xPosition, yPosition));
                    // If a sinkhole is read
                } else if (cells[0].equals("Sinkhole")) {
                    // Get sinkhole parameters and add sinkhole to arraylist of sinkholes
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[2]);
                    obstacles.add(new Sinkhole(xPosition, yPosition));
                } else if (cells[0].equals("Demon")) {
                    // Get sinkhole parameters and add sinkhole to arraylist of sinkholes
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[2]);
                    demon = new Demon(xPosition, yPosition);
                    movable = random.nextBoolean();
                    demon.setIsAgressive(movable);
                    if (movable) {
                        demon.setVelocity(makeVelocity(directions.get(random.nextInt(4)), 0.2 + 0.5 * random.nextDouble()));
                    }
                    enemies.add(demon);
                } else if (cells[0].equals("Navec")) {
                    // Get sinkhole parameters and add sinkhole to arraylist of sinkholes
                    xPosition = Integer.parseInt(cells[COLUMN1]);
                    yPosition = Integer.parseInt(cells[2]);
                    navec = new Navec(xPosition, yPosition);
                    navec.setIsAgressive(true);
                    navec.setVelocity(makeVelocity(directions.get(random.nextInt(4)), 0.2 + 0.5 * random.nextDouble()));
                    enemies.add(navec);
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

    private static Point makeVelocity(Point direction, double speed) {
        return new Point(direction.x * speed, direction.y * speed);
    }

    private void printDamage(Entity A, Entity B) {
        System.out.printf("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                        A.getName(), A.getDamage(), B.getName(), B.getName(), B.getHealth(), B.getMaxHealth());
    }

    private static void clearScene() {
        obstacles.removeAll(obstacles);
        sinkholes.removeAll(sinkholes);
        enemies.removeAll(enemies);
    }

    // Returns true if entity is colliding with border
    public static boolean checkBorderCollision(Entity entity, Point direction) {
        // Check collision with left border
        if (direction.x < 0 && entity.getBoundary().left() > boundary.left()) {
            return false;
            // Check collision with right border
        } else if (direction.x > 0 && entity.getBoundary().right() < boundary.right() + player.getWidth()) {
            return false;
            // Check collision with upper border
        } else if (direction.y < 0 && entity.getBoundary().top() > boundary.top()) {
            return false;
            // Check collision with lower border
        } else if (direction.y > 0 && entity.getBoundary().bottom() < boundary.bottom() + player.getHeight()) {
            return false;
        } else {
            return true;
        }
    }

    // Returns true if movable entity will collide with an obstacle
    public static boolean checkObstacleCollision(Entity entity, Point direction) {
        // Go to each wall
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

    private void checkSinkholeCollision() {
        for (Entity obstacle: obstacles) {
            if (obstacle instanceof Sinkhole && obstacle.getExists() && player.getBoundary().intersects(obstacle.getBoundary())) {
                player.takeDamage(SINKHOLE_DAMAGE);
                printDamage(obstacle, player);
                obstacle.setExists(false);
            }
        }
    }

    private void checkEnemyRange() {
        for (Enemy enemy: enemies) {
            enemy.inRange(player);
        }
    }

    private void checkDamage() {
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
                    enemy.setIsDamaging(false);
                }
                
                // Check damage from player to enemy
                if (player.getAttacking() && player.getBoundary().intersects(enemy.getBoundary()) && enemy.getInvincible() == false) {
                    if (player.getIsDamaging() == false) {
                        player.damageEnemy(enemy);
                        enemy.setInvincible(true);
                        printDamage(player, enemy);
                        if (enemy.getHealth() <= 0) {
                            enemy.setExists(false);
                        }
                    }
                }
            }
        }
    }

    // Display health bar
    public static void drawHealthBar(Entity entity, Point position, int size) {
        Font healthBar = new Font(font, size);
        int healthPercentage = (int) Math.round(entity.getHealth() * PERCENT / entity.getMaxHealth());
        String healthDisplay = healthPercentage + "%";
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

        // Draw all the sinkholes in arraylist of sinkholes
        for (Sinkhole sinkhole: sinkholes) {
            sinkhole.draw();
        }

    }

    private void updateEnemies() {
        for (Enemy enemy: enemies) {
            enemy.updateState();
            if (enemy instanceof Navec && enemy.getExists() == false) {
                gameState = WIN;
            }
        }
    }

    // Displays components of start screen
    private void runStartScreen(Input input) {
        if (level == LEVEL_0) {
            titleMessage.drawString(GAME_TITLE, GAME_TITLE_POINT.x, GAME_TITLE_POINT.y);
            instructionMessage.drawString("PRESS SPACE TO START", INSTRUCTION_POINT_1.x, INSTRUCTION_POINT_1.y);
            instructionMessage.drawString("USE ARROW KEYS TO FIND GATE", INSTRUCTION_POINT_2.x, INSTRUCTION_POINT_2.y);
        } else if (level == LEVEL_1) {
            instructionMessage.drawString("PRESS SPACE TO START", INSTRUCTION_POINT_3.x, INSTRUCTION_POINT_3.y);
            instructionMessage.drawString("PRESS A TO ATTACK", INSTRUCTION_POINT_4.x, INSTRUCTION_POINT_4.y);
            instructionMessage.drawString("DEFEAT NAVEC TO WIN", INSTRUCTION_POINT_5.x, INSTRUCTION_POINT_5.y);
        }
        
        if (input.isDown(Keys.SPACE)) {
            gameState = RUNNING;
        }
    }

    private void runGameScreen(Input input) {
        // Get key input and move if possible
        if (input.isDown(Keys.LEFT) && !checkBorderCollision(player, LEFT) && !checkObstacleCollision(player, LEFT)) {
            player.move(makeVelocity(LEFT, PLAYER_SPEED));
        }
        if (input.isDown(Keys.RIGHT) && !checkBorderCollision(player, RIGHT) && !checkObstacleCollision(player, RIGHT)) {
            player.move(makeVelocity(RIGHT, PLAYER_SPEED));
        }
        if (input.isDown(Keys.UP) && !checkBorderCollision(player, UP) && !checkObstacleCollision(player, UP)) {
            player.move(makeVelocity(UP, PLAYER_SPEED));
        }
        if (input.isDown(Keys.DOWN) && !checkBorderCollision(player, DOWN) && !checkObstacleCollision(player, DOWN)) {
            player.move(makeVelocity(DOWN, PLAYER_SPEED));
        }

        if (level == LEVEL_1) {
            if (input.wasPressed(Keys.L)) {
                Enemy.adjustTimescale(1);
            } 
            if (input.wasPressed(Keys.K)) {
                Enemy.adjustTimescale(-1);      
            } 
            if (input.wasPressed(Keys.A)) {
                player.setAttacking(true);    
            } 
        }
        
        

        // Check sinkhole overlap
        drawBackground();
        drawObstacles();
        drawHealthBar(player, HEALTH_DISPLAY_POINT, HEALTH_SIZE);
        checkEnemyRange();
        checkDamage();
        checkSinkholeCollision();
        updateEnemies();
        player.updateState();

        

        // Check win condition
        if (player.getPosition().x >= WIN_POINT.x && player.getPosition().y >= WIN_POINT.y) {
            if (level == LEVEL_0) {
                level = LEVEL_1;
                gameState = START;
            } else if (level == LEVEL_1) {
                gameState = WIN;
            }
        }

        // Check lose condition
        if (player.getHealth() <= 0) {
            gameState = LOSE;
        }
    }

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

    // Performs a state update. Allows the game to exit when the escape key is pressed.
    @Override
    protected void update(Input input) {

        readCSV();

        // Testing
        if (input.isDown(Keys.W)) {
            level = LEVEL_1;
            level_1_read = false;
            gameState = START;
        }


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

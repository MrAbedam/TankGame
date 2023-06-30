package ir.ac.kntu;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.cert.CertPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import static ir.ac.kntu.Wall.allWalls;
import static java.lang.Thread.sleep;

public class TankGame extends Application {

    public static ArrayList<Superpower> superPowers = new ArrayList<>();
    public static ArrayList<Tank> allTanks = new ArrayList<>();
    public static GameState gameState = GameState.RUNNING;
    public static PlayerTank p1;
    public static Eagle eagle;
    private Pane root = new Pane();
    private Scene scene;

    public static int storedHealth = 3;
    public static int mapSize = 500;
    public static int remainingTanks = 4;
    public static int playerScore = 0;  // Player's score


    public GameState getGameState() {
        return gameState;
    }

    private int currentLevel;

    public TankGame() {
        // Constructor logic goes here
    }

    public TankGame(int level) {
        this.currentLevel = level;
    }

    public void addMetalWall(double x, double y, Pane root) {
        MetalWall metalWall = new MetalWall(x, y);
        metalWall.addToPane(root);
    }

    public void addSimpleWall(int x, int y, Pane root) {
        SimpleWall simpleWall = new SimpleWall(x, y);
        simpleWall.addToPane(root);
    }

    public void addArmoredTank(int x, int y, Pane root) {
        ArmoredTank t1 = new ArmoredTank(x, y);
        root.getChildren().add(t1.getTankImageView());
    }

    public void addRegularTank(int x, int y, Pane root) {
        Tank t1 = new Tank(x, y);
        root.getChildren().add(t1.getTankImageView());
    }

    public void addLuckyTank(int x, int y, Pane root) {
        LuckyTank t1 = new LuckyTank(x, y);
        root.getChildren().add(t1.getTankImageView());
    }

    public void splitWall() {
        for (int i = 0; i < mapSize; i = i + 50) {
            addMetalWall(mapSize, i, root);
        }
    }

    public String healthToString() {
        return ("images/" + p1.getHealth() + ".png");
    }

    public void addHp() {
        ImageView hitPoint = new ImageView(new Image("images/HeartIcon.png"));
        ImageView hitPointAmount = new ImageView(new Image(healthToString()));
        hitPointAmount.setLayoutX(mapSize + 100);
        hitPointAmount.setLayoutY(100);
        hitPoint.setLayoutX(mapSize + 40);
        hitPoint.setLayoutY(100);
        root.getChildren().addAll(hitPointAmount, hitPoint);
    }

    public void addScore() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Current score:"));
        Text scoreDisplay = new Text("Current score: " + playerScore);
        scoreDisplay.setLayoutX(mapSize + 40);
        scoreDisplay.setLayoutY(300);
        scoreDisplay.setFill(Color.WHITE);
        double fontSize = 20;
        scoreDisplay.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(scoreDisplay);
    }

    public void addRemainingTanks() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Remaining Tanks:"));
        Text remainingTanksText = new Text("Remaining Tanks: " + remainingTanks);
        remainingTanksText.setLayoutX(mapSize + 40);
        remainingTanksText.setLayoutY(200);
        remainingTanksText.setFill(Color.WHITE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void addCurrentLevel() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Level:"));
        Text remainingTanksText = new Text("Level: " + currentLevel);
        remainingTanksText.setLayoutX(mapSize + 90);
        remainingTanksText.setLayoutY(400);
        remainingTanksText.setFill(Color.WHITE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void updateStatus() {
        addRemainingTanks();
        addHp();
        addScore();
        addCurrentLevel();
    }

    public void spawnAnimation() {
    }

    public void spawnTank() {
        if (remainingTanks >= 4 && allTanks.size() <= 4) {
            int xSpawn = giveSpawnPoint();
            int ySpawn = giveSpawnPoint();
            switch (remainingTanks % 3) {
                case 0 -> addArmoredTank(xSpawn, ySpawn, root);
                case 1 -> addRegularTank(xSpawn, ySpawn, root);
                case 2 -> addLuckyTank(xSpawn, ySpawn, root);
            }
        }
    }

    public int giveSpawnPoint() {
        return (new Random().nextInt(2) * (mapSize - 60));
    }

    public void gameSetup() {
        p1 = new PlayerTank(mapSize / 2 - 100, mapSize - 40);
        p1.setHealth(storedHealth);
        eagle = new Eagle((mapSize / 2), mapSize - 50);
        addMetalWall(mapSize / 2 + 50, mapSize - 50, root);
        addMetalWall(mapSize / 2 - 50, mapSize - 50, root);
        addMetalWall(mapSize / 2, mapSize - 150, root);
        addLuckyTank(0, 0, root);
        addRegularTank(mapSize - 50, mapSize - 50, root);
        addArmoredTank(0, mapSize - 50, root);
        addLuckyTank(mapSize - 50, 0, root);
        splitWall();
        root.getChildren().addAll(eagle.getImgView(), p1.getTankImageView());
    }


    private Button createLevelButton(int level) {
        Button levelButton = new Button("Level " + level);
        levelButton.setPrefWidth(200);
        levelButton.setOnAction(e -> startLevel(level));
        return levelButton;
    }

    @Override
    public void start(Stage primaryStage) {

        root = new Pane();
        scene = new Scene(root, mapSize + 250, mapSize, Color.BLACK);

        showMenu();
        gameState = GameState.MENU;
        scene.setOnKeyPressed(event -> {
            if (gameState == GameState.MENU) {
                // Handle level selection in the MENU state
                KeyCode keyCode = event.getCode();
                if (keyCode.isDigitKey()) {
                    int level = Integer.parseInt(keyCode.getName());
                    if (level >= 1 && level <= 10) {
                        startLevel(level);
                    }
                }
            } else if (gameState == GameState.RUNNING) {
                // Handle tank movement and shooting in the RUNNING state
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.LEFT) {
                    p1.moveLeft();
                } else if (keyCode == KeyCode.RIGHT) {
                    p1.moveRight();
                } else if (keyCode == KeyCode.UP) {
                    p1.moveUp();
                } else if (keyCode == KeyCode.DOWN) {
                    p1.moveDown();
                } else if (keyCode == KeyCode.SPACE) {
                    p1.shoot(root);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (GameState.RUNNING == getGameState()) {
                    spawnTank();
                    updateStatus();
                    updateBullets();
                    updateTanks();
                    updateHitsFromPlayer(p1);
                    updateHitsFromEnemies(p1);
                    updateSuperPower();
                    checkLevelCompletion();
                } else if (gameState == GameState.WIN) {
                    showWinPage();
                } else if (gameState == GameState.GAME_OVER) {
                    showGameOverPage();
                }
            }
        };

        gameLoop.start();
    }

    private void updateTanks() {
        Iterator<Tank> iterator = allTanks.iterator();
        while (iterator.hasNext()) {
            Tank tank = iterator.next();
            if (tank.getClass() != PlayerTank.class) {
                tank.move(root, p1);
                if (!(tank.getHealth() >= 1)) {
                    root.getChildren().remove(tank.getTankImageView());
                    iterator.remove();
                }
            }
        }
    }

    private void updateHitsFromPlayer(PlayerTank p1) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : p1.getBullets()) {
            bullet.move();

            for (Tank enemyTank : allTanks) {
                if (enemyTank == p1) continue;
                if (bullet.collidesWith(enemyTank)) {
                    enemyTank.hit(root);
                    bulletsToRemove.add(bullet);
                    break;
                }
            }

            if (bullet.isOffScreen(root)) {
                bulletsToRemove.add(bullet);
            }
        }

        // Remove bullets from root pane
        for (Bullet bullet : bulletsToRemove) {
            p1.removeBullet(bullet);
            root.getChildren().remove(bullet.getBulletImageView());
        }
    }

    private void updateHitsFromEnemies(PlayerTank p1) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Tank testTank : allTanks) {
            if (testTank == p1) continue;
            for (Bullet bullet : testTank.getBullets()) {
                bullet.move();
                if (bullet.collidesWith(p1)) {
                    p1.hit(root);
                    bulletsToRemove.add(bullet);
                    break;
                }
                if (bullet.collidesWith(eagle)) {
                    setGameState(GameState.GAME_OVER); // Set game state to "END" if the eagle is hit
                    bulletsToRemove.add(bullet);
                    break;
                }

                if (bullet.isOffScreen(root)) {
                    bulletsToRemove.add(bullet);
                }
            }
        }

        // Remove bullets from root pane
        for (Bullet bullet : bulletsToRemove) {
            bullet.removeBulletFromTank(root, bullet);
        }
    }

    private void updateBullets() {
        for (Tank testTank : allTanks) {
            Iterator<Bullet> bulletIterator = testTank.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.move();
                if (bullet.isOffScreen(root)) {
                    bulletIterator.remove();
                    root.getChildren().remove(bullet.getBulletImageView());
                }
                handleWallCollision(bullet, bulletIterator, root);
            }
        }
    }

    private void updateSuperPower() {
        if (superPowers.isEmpty()) return;
        Iterator<Superpower> superpowerIterator = superPowers.iterator();
        while (superpowerIterator.hasNext()) {
            Superpower testSuper = superpowerIterator.next();
            if (testSuper.collidesWith(p1)) {
                testSuper.collectSuperPower();
                superpowerIterator.remove(); // Safely remove the current element
            }
        }
    }


    public static void handleWallCollision(Bullet bullet, Iterator<Bullet> bulletIterator, Pane root) {
        Iterator<Wall> wallIterator = allWalls.iterator();
        while (wallIterator.hasNext()) {
            Wall testWall = wallIterator.next();
            if (bullet.collidesWith(testWall)) {
                if (bulletIterator == null) continue;
                if (testWall instanceof SimpleWall) {
                    testWall.handleBulletCollision(bullet, testWall, bulletIterator, wallIterator, root);
                }
                if (testWall instanceof MetalWall) {
                    testWall.handleBulletCollision(bullet, testWall, bulletIterator, wallIterator, root);
                }
            }
        }
    }

    private void showMenu() {
        // Create and display the menu screen
        Text menuText = new Text("Enter a level (1-10):");
        menuText.setLayoutX(mapSize / 2);
        menuText.setLayoutY(mapSize / 2);
        menuText.setFill(Color.WHITE);
        double fontSize = 20;
        menuText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        menuText.setFill(Color.WHITE);
        root.getChildren().add(menuText);
        System.out.println("AA");
    }

    private void startLevel(int level) {
        // Start a new level with the specified level number
        currentLevel = level;
        remainingTanks = 10 + (level - 1) * 4;
        allTanks.clear();
        gameState = GameState.RUNNING;
        root.getChildren().clear();
        gameSetup();
    }
//storedHealth = p1.getHealth();

    private void showWinPage() {
        // Clear the root pane
        root.getChildren().clear();
        storedHealth = p1.getHealth();
        if (currentLevel != 10) {
            Text winText = new Text("Congratulations! You won the level! You will go to the next level after 5 seconds");
            winText.setLayoutX(50);
            winText.setLayoutY(50);
            winText.setFill(Color.WHITE);
            winText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            root.getChildren().add(winText);
            //need delay over here
            startLevel(currentLevel + 1);
        } else {
            showEndGamePage(); // Show "You ended the game" message
        }
    }


    public void showEndGamePage() {
        Text endGameText = new Text("You have completed this game, WELL PLAYED.");
        endGameText.setLayoutX(50);
        endGameText.setLayoutY(mapSize / 2);
        endGameText.setFill(Color.WHITE);
        endGameText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 30));
        root.getChildren().add(endGameText);
    }

    private void showGameOverPage() {
        // Display the game over page
        root.getChildren().clear();
        Text gameOverText = new Text("Game Over! You lost the game.");
        gameOverText.setLayoutX(mapSize / 2);
        gameOverText.setLayoutY(mapSize / 2);
        gameOverText.setFill(Color.RED);
        double fontSize = 20;
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().add(gameOverText);
    }

    private void checkLevelCompletion() {
        // Check if all tanks are destroyed
        if (remainingTanks == 0) {
            gameState = GameState.WIN;
        }
    }


    public static void setGameState(GameState gameState) {
        TankGame.gameState = gameState;
    }

    public static void main(String[] args) {
        launch(args);
    }

    //to do : 1. read map from file, 2.fix menu click options 3. add highscore to menu, 4. add sign in to menu 5.add pause
}
/*
Mohammadreza Abedin Varamin
40120623
*/
package ir.ac.kntu.Engine;

import ir.ac.kntu.GameObjects.Bullet;
import ir.ac.kntu.GameObjects.PlayerTank;
import ir.ac.kntu.GameObjects.Superpower;
import ir.ac.kntu.GameObjects.Tank;
import ir.ac.kntu.EnumsAndStates.GameState;
import ir.ac.kntu.UI.LeaderboardWriter;
import ir.ac.kntu.UI.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static ir.ac.kntu.Engine.GlobalConstants.*;

import java.util.*;

import static ir.ac.kntu.UI.PopUpPages.*;
import static ir.ac.kntu.Engine.TankGameHelperClass.*;
import static ir.ac.kntu.GameObjects.Wall.allWalls;

public class TankGame extends Application {

    public static int currentLevel;

    public TankGame() {
    }

    public TankGame(int level) {
        this.currentLevel = level;
    }



    public static void splitWall() {
        for (int i = 0; i < mapSize; i = i + 50) {
            addMetalWall(mapSize, i);
        }
        for (int i = 0; i < mapSize + 150; i = i + 50) {
            addMetalWall(i, mapSize);
        }
    }

    public String healthToString() {
        if (p1 == null) {
            return "3";
        }
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
        scoreDisplay.setFill(Color.ORANGE);
        scoreDisplay.setLayoutX(mapSize + 40);
        scoreDisplay.setLayoutY(300);
        double fontSize = 20;
        scoreDisplay.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(scoreDisplay);
    }

    public void addRemainingTanks() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Remaining Tanks:"));
        Text remainingTanksText = new Text("Remaining Tanks: " + remainingTanks);
        remainingTanksText.setLayoutX(mapSize + 40);
        remainingTanksText.setLayoutY(200);
        remainingTanksText.setFill(Color.ORANGE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void addCurrentLevel() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Level:"));
        Text remainingTanksText = new Text("Level: " + currentLevel);
        remainingTanksText.setLayoutX(mapSize + 90);
        remainingTanksText.setLayoutY(400);
        remainingTanksText.setFill(Color.ORANGE);
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

    public void spawnTank() {
        if (remainingTanks >= 4 && allTanks.size() <= 4) {
            int xSpawn = giveSpawnPoint();
            switch (remainingTanks % 3) {
                case 0 -> {
                    addArmoredTank(xSpawn, 5, root);
                }
                case 1 -> {
                    addRegularTank(xSpawn, 5, root);
                }
                default -> {
                    addLuckyTank(xSpawn, 5, root);
                }
            }
        }
    }

    public int giveSpawnPoint() {
        return (new Random().nextInt(4) * (mapSize / 3 - 20));
    }

    public boolean doesUserExist(String userName) {
        for (Player p1 : leaderboard.getPlayers()) {
            if (p1.getName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void gameStateKeyHandle(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (gameState == GameState.MENU) {
                KeyCode keyCode = event.getCode();
                if (keyCode.isDigitKey()) {
                    int level = Integer.parseInt(keyCode.getName());
                    if (level >= 1 && level <= 10) {
                        startLevel(level);
                    }
                }
            } else if (gameState == GameState.RUNNING) {
                KeyCode keyCode = event.getCode();
                runningKeyCodeHandle(keyCode);
            } else if (gameState == GameState.PAUSED) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.P) {
                    resumeGame();
                }
            }
        });
    }

    public void runningKeyCodeHandle(KeyCode keyCode) {
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
        } else if (keyCode == KeyCode.P) {
            pauseGame();
        }
    }



    @Override
    public void start(Stage primaryStage) {
        makeMenu();
        leaderboard = LeaderboardWriter.readLeaderBoard();
        userNameInputWindow();
        if (doesUserExist(userName)) {
            showUserStats();
        }
        root = new Pane();
        Scene scene = new Scene(root, mapSize + 400, mapSize + 150, Color.BLACK);
        root.setStyle("-fx-background-color: #000000;");
        primaryStage.setScene(scene);
        readMapFromFile();
        showMenu();
        gameState = GameState.MENU;
        gameStateKeyHandle(scene);
        makeAnimationTimer(primaryStage);
        primaryStage.show();
    }

    public void allUpdates() {
        spawnTank();
        updateStatus();
        updateBullets();
        updateTanks();
        updateHitsFromPlayer(p1);
        updateHitsFromEnemies(p1);
        updateSuperPower();
        checkLevelCompletion();
    }

    public void makeAnimationTimer(Stage primaryStage) {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameState == GameState.RUNNING) {
                    allUpdates();
                } else if (gameState == GameState.WIN) {
                    showWinPage();
                } else if (gameState == GameState.GAME_OVER) {
                    stop();
                    showGameOverPage(primaryStage);
                    LeaderboardWriter.writeLeaderBoard(leaderboard);
                } else if (gameState == GameState.COMPLETED) {
                    root.getChildren().clear();
                    stop();
                    completeGamePage();
                    showLeaderboard(primaryStage);
                    LeaderboardWriter.writeLeaderBoard(leaderboard);
                }
            }
        };
        gameLoop.start();
    }



    private void pauseGame() {
        gameState = GameState.PAUSED;
    }

    private void resumeGame() {
        gameState = GameState.RUNNING;
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
                if (enemyTank == p1) {
                    continue;
                }
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
        for (Bullet bullet : bulletsToRemove) {
            p1.removeBullet(bullet);
            root.getChildren().remove(bullet.getBulletImageView());
        }
    }

    private void updateHitsFromEnemies(PlayerTank p1) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Tank testTank : allTanks) {
            if (testTank == p1) {
                continue;
            }
            for (Bullet bullet : testTank.getBullets()) {
                bullet.move();
                if (bullet.collidesWith(p1)) {
                    p1.hit(root);
                    bulletsToRemove.add(bullet);
                    break;
                }
                if (bullet.collidesWith(eagle)) {
                    setGameState(GameState.GAME_OVER);
                    bulletsToRemove.add(bullet);
                    break;
                }
                if (bullet.isOffScreen(root)) {
                    bulletsToRemove.add(bullet);
                }
            }
        }
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
        if (superPowers.isEmpty()) {
            return;
        }
        Iterator<Superpower> superpowerIterator = superPowers.iterator();
        while (superpowerIterator.hasNext()) {
            Superpower testSuper = superpowerIterator.next();
            if (testSuper.collidesWith(p1)) {
                testSuper.collectSuperPower();
                superpowerIterator.remove();
            }
        }
    }

    public static void handleLevelSelection(int level) {
        startLevel(level);
    }

    public static void startLevel(int level) {
        currentLevel = level;
        remainingTanks = 10 + (level - 1) * 4;
        allTanks.clear();
        allWalls.clear();
        gameState = GameState.RUNNING;
        root.getChildren().clear();
        gameSetup();
    }

    public static int getIn = 0;

    private void checkLevelCompletion() {
        if (remainingTanks == 0) {
            gameState = GameState.WIN;
        }
    }

    public static void setGameState(GameState gameState) {
        GlobalConstants.gameState = gameState;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void gameSetup() {
        makeMapFromFile();
        p1.setHealth(storedHealth);
        splitWall();
        Image pauseImage = new Image("images/Pause.png"); // Replace "pause.png" with the path to your image
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.setPreserveRatio(true);
        pauseImageView.setLayoutX(mapSize + 100);
        pauseImageView.setLayoutY(50);
        EventHandler<MouseEvent> pauseEventHandler = event -> {
            if (gameState == GameState.PAUSED) {
                setGameState(GameState.RUNNING);
            } else {
                setGameState(GameState.PAUSED);
            }
        };
        pauseImageView.setOnMouseClicked(pauseEventHandler);
        root.getChildren().addAll(eagle.getImgView(), p1.getTankImageView(), pauseImageView);
    }
}
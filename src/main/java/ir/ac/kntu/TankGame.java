package ir.ac.kntu;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

import static ir.ac.kntu.Wall.allWalls;

public class TankGame extends Application {

    public static ArrayList<Superpower> superPowers = new ArrayList<>();
    public static ArrayList<Tank> allTanks = new ArrayList<>();
    public static GameState gameState = GameState.RUNNING;
    private PlayerTank p1;
    private Pane root;

    public GameState getGameState() {
        return gameState;
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
        for (int i = 0; i < 600; i = i + 50) {
            addMetalWall(600, i, root);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();

        p1 = new PlayerTank(100, 400);
        root.getChildren().add(p1.getTankImageView());
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        addLuckyTank(0, 0, root);
        splitWall();

        addSimpleWall(100, 100, root);
        addSimpleWall(100, 150, root);
        addMetalWall(100, 200, root);
        addMetalWall(100, 250, root);
        addMetalWall(100, 300, root);
        addMetalWall(100, 350, root);


        scene.setOnKeyPressed(event -> {
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
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (GameState.RUNNING == getGameState()) {
                    updateBullets();
                    updateTanks();
                    updateHitsFromPlayer(p1);
                    updateHitsFromEnemies(p1);
                    updateSuperPower();
                } else {
                    System.out.println("You died");
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


    public static void setGameState(GameState gameState) {
        TankGame.gameState = gameState;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
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

public class TankGame extends Application {

    public static ArrayList<Bullet> freeBullets = new ArrayList<>();
    public static ArrayList<Tank> allTanks = new ArrayList<>();
    public static GameState gameState = GameState.RUNNING;
    private PlayerTank p1;
    private Pane root;

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();

        p1 = new PlayerTank(100, 200, 3, Direction.UP);
        Tank t1 = new Tank(10, 10, 1, Direction.RIGHT);
        Tank t2 = new Tank(100, 300, 1 ,Direction.RIGHT);
        Tank t3 = new Tank(150, 400, 1 ,Direction.RIGHT);
        root.getChildren().add(t1.getTankImageView());
        root.getChildren().add(p1.getTankImageView());
        root.getChildren().add(t3.getTankImageView());
        root.getChildren().add(t2.getTankImageView());

        Scene scene = new Scene(root, 800, 600, Color.BLACK);

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
                if (!(tank.getHealth()>=1)) {
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
            Iterator<Bullet> iterator = testTank.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                bullet.move();
                if (bullet.isOffScreen(root)) {
                    iterator.remove();
                    root.getChildren().remove(bullet.getBulletImageView());
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
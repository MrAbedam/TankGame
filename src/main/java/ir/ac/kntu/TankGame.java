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

        p1 = new PlayerTank(100,200,3,Direction.UP);
        Tank t1 = new Tank(10,10,1,Direction.RIGHT);
        root.getChildren().add(t1.getTankImageView());
        root.getChildren().add(p1.getTankImageView());

        Scene scene = new Scene(root, 800, 600,Color.BLACK);

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
                    //updateHits();
                }else {
                    System.out.println("Game ended");
                }
            }
        };

        gameLoop.start();
    }

    private void updateTanks() {
        for (Tank tank : allTanks) {
            if (tank.getClass()!= PlayerTank.class)
            tank.move(root,p1);
        }
    }

    private void updateBullets() {
        for (Tank testTank : allTanks){
            if (testTank.getBullets() == null) continue;
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



    public static void main(String[] args) {
        launch(args);
    }
}
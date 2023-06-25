package ir.ac.kntu;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Iterator;

public class TankGame extends Application {
    public GameState gameState = GameState.RUNNING;
    private Tank tank;
    private Pane root;

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();

        tank = new Tank();
        root.getChildren().add(tank.getTankImageView());

        Scene scene = new Scene(root, 800, 600,Color.BLACK);

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.LEFT) {
                tank.moveLeft();
            } else if (keyCode == KeyCode.RIGHT) {
                tank.moveRight();
            } else if (keyCode == KeyCode.UP) {
                tank.moveUp();
            } else if (keyCode == KeyCode.DOWN) {
                tank.moveDown();
            } else if (keyCode == KeyCode.SPACE) {
                tank.shoot(root);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (GameState.RUNNING == getGameState()) {
                    updateBullets();
                }else {
                    System.out.println("Game ended");
                }
            }
        };

        gameLoop.start();
    }

    private void updateBullets() {
        if (tank.getBullets() == null) return;
        Iterator<Bullet> iterator = tank.getBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.isOffScreen(root)) {
                iterator.remove();
                root.getChildren().remove(bullet.getBulletImageView());
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
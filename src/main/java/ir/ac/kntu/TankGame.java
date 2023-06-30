package ir.ac.kntu;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static ir.ac.kntu.Wall.allWalls;

public class TankGame extends Application {

    public static ArrayList<Superpower> superPowers = new ArrayList<>();
    public static ArrayList<Tank> allTanks = new ArrayList<>();
    public static GameState gameState = GameState.RUNNING;
    public static PlayerTank p1;
    public static Eagle eagle;
    private Pane root;

    public static int mapSize = 700;
    public static int remainingTanks = 12;

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
        for (int i = 0; i < mapSize; i = i + 50) {
            addMetalWall(mapSize, i, root);
        }
    }

    public String healthToString(){
        return ("images/"+p1.getHealth()+".png");
    }

    public void addHp(){
        ImageView hitPoint = new ImageView(new Image("images/HeartIcon.png"));
        ImageView hitPointAmount = new ImageView(new Image(healthToString()));
        hitPointAmount.setLayoutX(mapSize + 100);
        hitPointAmount.setLayoutY(100);
        hitPoint.setLayoutX(mapSize+40);
        hitPoint.setLayoutY(100);
        root.getChildren().addAll(hitPointAmount,hitPoint);
    }

    public void addRemainingTanks(){
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Remaining Tanks:"));
        Text remainingTanksText = new Text("Remaining Tanks: " + remainingTanks);
        remainingTanksText.setLayoutX(mapSize+40);
        remainingTanksText.setLayoutY(200);
        remainingTanksText.setFill(Color.WHITE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void updateStatus(){
        addRemainingTanks();
        addHp();
    }

    public void spawnAnimation(){}

    public void spawnTank(){
        if (remainingTanks >= 4 && allTanks.size() <= 4){
            int xSpawn = giveSpawnPoint();
            int ySpawn = giveSpawnPoint();
            switch (remainingTanks%3){
                case 0-> addArmoredTank(xSpawn,ySpawn,root);
                case 1-> addRegularTank(xSpawn,ySpawn,root);
                case 2-> addLuckyTank(xSpawn,ySpawn,root);
            }
        }
    }

    public int giveSpawnPoint(){
        return (new Random().nextInt(2)*(mapSize-60));
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        p1 = new PlayerTank(mapSize/2 - 80, mapSize -40);
        eagle = new Eagle((mapSize/2), mapSize-50);
        root.getChildren().addAll(eagle.getImgView(),p1.getTankImageView());
        Scene scene = new Scene(root, mapSize+250, mapSize, Color.BLACK);
        addLuckyTank(0,0,root);
        addRegularTank(mapSize-50,mapSize-50,root);
        addArmoredTank(0,mapSize-50,root);
        addLuckyTank(mapSize-50,0,root);
        splitWall();


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
                    spawnTank();
                    updateStatus();
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
                if (bullet.collidesWith(eagle)) {
                    setGameState(GameState.ENDED); // Set game state to "END" if the eagle is hit
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
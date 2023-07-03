package ir.ac.kntu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

import static ir.ac.kntu.TankGame.*;
import static ir.ac.kntu.Wall.*;

public class Tank {


    public static boolean isFreezeActive = false;

    public static double tankHeight = 40;

    public static double tankWidth = 40;

    private long lastShotTime = 0;

    private ImageView tankImageView;

    private double x;

    private double y;

    private int health;

    private Direction direction;

    private ArrayList<Bullet> bullets;

    private int value;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 1;
        this.direction = Direction.DOWN;
        this.bullets = new ArrayList<Bullet>();
        setTankImageView(new ImageView(new Image("images/RegularEnemyTankDown.png")));
        tankImageView.setLayoutY(y);
        tankImageView.setLayoutY(y);
        allTanks.add(this);
        this.value = getHealth() * 100;
    }

    public static boolean isIsFreezeActive() {
        return isFreezeActive;
    }

    public static void setIsFreezeActive(boolean isFreezeActive) {
        Tank.isFreezeActive = isFreezeActive;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void setTankImageView(ImageView tankImageView) {
        this.tankImageView = tankImageView;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ImageView getTankImageView() {
        return tankImageView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void removeBullet(Bullet bullet) {
        this.getBullets().remove(bullet);
    }

    public void hit(Pane root) {
        setHealth(getHealth() - p1.getBulletPower());
        if (this.getHealth() <= 0) {
            this.die(root);
            if (!this.getBullets().isEmpty()) {
                for (Bullet bullet : this.getBullets()) {
                    root.getChildren().remove(bullet.getBulletImageView());
                }
            }
        }
    }

    public static void showExplosion(String imageName, double time, double deathX, double deathY, Pane root) {
        ImageView explosionImageView = new ImageView(new Image("images/" + imageName + ".png"));
        explosionImageView.setLayoutX(deathX);
        explosionImageView.setLayoutY(deathY);
        root.getChildren().add(explosionImageView);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    explosionImageView.setVisible(true);
                }),
                new KeyFrame(Duration.seconds(time), event -> {
                    root.getChildren().remove(explosionImageView);
                })
        );
        timeline.play();
    }

    public static void gradualExplosion(double deathX, double deathY, Pane root) {
        showExplosion("SmallExplosion", 0.2, deathX, deathY, root);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {
                    showExplosion("MediumExplosion", 0.2, deathX, deathY, root);
                }),
                new KeyFrame(Duration.seconds(0.2), event -> {
                    showExplosion("BigExplosion", 0.2, deathX, deathY, root);
                })
        );
        timeline.play();
    }

    public void die(Pane root) {
        remainingTanks--;
        double deathX = this.getX();
        double deathY = this.getY();
        allTanks.remove(this);
        if (this instanceof ArmoredTank) {
            playerScore += 200;
        } else {
            playerScore += 100;
        }
        root.getChildren().remove(this.getTankImageView());
        gradualExplosion(deathX, deathY, root);
    }

    public void moveLeft() {
        if (cantMoveTo(getX() + 5, getY(), Direction.LEFT)) {
            return;
        }
        if (getX() - 0.5 < 0) {
            return;
        }
        setX(getX() - 0.5);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/RegularEnemyTankLeft.png"));
    }

    public void moveRight() {
        if (cantMoveTo(getX() + 5, getY(), Direction.RIGHT)) {
            return;
        }
        if (getX() + 0.5 > mapSize - 30) {
            return;
        }
        setDirection(Direction.RIGHT);
        setX(getX() + 0.5);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/RegularEnemyTankRight.png"));
    }

    public void moveUp() {
        if (cantMoveTo(getX() + 5, getY(), Direction.UP)) {
            return;
        }
        if (getY() - 0.5 < 0) {
            return;
        }
        setDirection(Direction.UP);
        setY(getY() - 0.5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/RegularEnemyTankUp.png"));
    }

    public void moveDown() {
        if (cantMoveTo(getX() + 5, getY(), Direction.DOWN)) {
            return;
        }
        if (getY() + 0.5 > mapSize - 30) {
            return;
        }
        setDirection(Direction.DOWN);
        setY(getY() + 0.5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/RegularEnemyTankDown.png"));
    }


    public void move(Pane root, PlayerTank playerTank) {
        if (isIsFreezeActive()) {
            return;
        }
        double xDiff = Math.abs(playerTank.getX() - getX());
        double yDiff = Math.abs(playerTank.getY() - getY());

        if (xDiff > mapSize / 2) {
            if (playerTank.getX() < getX()) {
                moveLeft();
            } else {
                moveRight();
            }
        } else if (yDiff > mapSize / 2) {
            if (playerTank.getY() < getY()) {
                moveUp();
            } else {
                moveDown();
            }
        } else {
            shoot(root);
        }

        tankImageView.setLayoutX(x);
        tankImageView.setLayoutY(y);
    }


    public void shoot(Pane root) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastShotTime;
        if (elapsedTime >= 1000) {
            Bullet bullet = new Bullet(this, (int) (getX() + getTankImageView().getImage().getWidth() / 2), getY(), getDirection());
            getBullets().add(bullet);
            root.getChildren().add(bullet.getBulletImageView());
            lastShotTime = currentTime;
        }
    }


    public boolean cantMoveTo(double x, double y, Direction direction) {
        double leftBoundary = x;
        double rightBoundary = x + tankWidth;
        double topBoundary = y;
        double bottomBoundary = y + tankHeight;
        for (Wall wall : allWalls) {
            double wallX = wall.getX();
            double wallY = wall.getY();
            double tankLayoutX = getTankImageView().getLayoutX();
            double tankLayoutY = getTankImageView().getLayoutY();
            switch (direction) {
                case LEFT:
                    if (rightBoundary > wallX && leftBoundary < wallX + wallWidth && bottomBoundary > wallY
                            && topBoundary < wallY + wallHeight) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (leftBoundary < wallX + wallWidth && rightBoundary > wallX && bottomBoundary > wallY
                            && topBoundary < wallY + wallHeight) {
                        return true;
                    }
                    break;
                case UP:
                    if (bottomBoundary > wallY && topBoundary < wallY + wallHeight && rightBoundary > wallX
                            && leftBoundary < wallX + wallWidth) {
                        return true;
                    }
                    break;
                default:
                    if (topBoundary < wallY + wallHeight && bottomBoundary > wallY && rightBoundary > wallX
                            && leftBoundary < wallX + wallWidth) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public void setFrozenImageView(Tank tank) {
        tank.setTankImageView(new ImageView(new Image("images/FrozenEnemy.png")));
    }

}
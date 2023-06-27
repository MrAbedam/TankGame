package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

import static ir.ac.kntu.TankGame.allTanks;
import static ir.ac.kntu.TankGame.gameState;

public class Tank {

    private long lastShotTime = 0;
    private ImageView tankImageView;
    private double x;
    private double y;
    private int health;
    private Direction direction;
    private ArrayList<Bullet> bullets;

    public Tank(int x, int y, int health, Direction direction) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.direction = direction;
        this.bullets = new ArrayList<Bullet>();
        setTankImageView(new ImageView(new Image("images/RegularEnemyTankUp.png")));
        tankImageView.setLayoutY(y);
        tankImageView.setLayoutY(y);
        allTanks.add(this);
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

    public void hit() {
        setHealth(getHealth() - 1);
        if (this.getHealth() == 0) {
            this.die();
        }
    }

    public void die() {
        double deathX = this.getX();
        double deathY = this.getY();
        allTanks.remove(this);
        System.out.println("1 tank died !!");
    }


    public void moveLeft() {
        if (getX() - 0.5 < 0) return;
        setX(getX() - 0.5);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/RegularEnemyTankLeft.png"));
    }

    public void moveRight() {
        if (getX() + 0.5 > 770) return;
        setDirection(Direction.RIGHT);
        setX(getX() + 0.5);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/RegularEnemyTankRight.png"));
    }

    public void moveUp() {
        if (getY() - 0.5 < 0) return;
        setDirection(Direction.UP);
        setY(getY() - 0.5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/RegularEnemyTankUp.png"));
    }

    public void moveDown() {
        if (getY() + 0.5 > 570) return;
        setDirection(Direction.DOWN);
        setY(getY() + 0.5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/RegularEnemyTankDown.png"));
    }


    public void move(Pane root, PlayerTank playerTank) {
        double xDiff = Math.abs(playerTank.getX() - getX());
        double yDiff = Math.abs(playerTank.getY() - getY());

        if (xDiff > 100) {
            if (playerTank.getX() < getX()) {
                moveLeft();
            } else {
                moveRight();
            }
        }
        else if (yDiff > 100) {
            if (playerTank.getY() < getY()) {
                moveUp();
            } else {
                moveDown();
            }
        }
        else {
            shoot(root);
        }

        tankImageView.setLayoutX(x);
        tankImageView.setLayoutY(y);
    }

    public void shoot(Pane root) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastShotTime;
        if (elapsedTime >= 1000) {
            Bullet bullet = new Bullet((int) (getX() + getTankImageView().getImage().getWidth() / 2), getY(), getDirection());
            getBullets().add(bullet);
            root.getChildren().add(bullet.getBulletImageView());
            lastShotTime = currentTime;
        }
    }


}
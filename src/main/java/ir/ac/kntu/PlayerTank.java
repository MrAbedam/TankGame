package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

import static ir.ac.kntu.TankGame.*;
import static ir.ac.kntu.Wall.*;

public class PlayerTank extends Tank {

    private static final int MAX_HEALTH = 3;

    private int bulletPower;

    public PlayerTank(int x, int y) {
        super(x, y);
        this.bulletPower = 1;
        setTankImageView(new ImageView(new Image("images/PlayerMoveDown.png")));
        getTankImageView().setLayoutX(x);
        getTankImageView().setLayoutY(y);
        setHealth(MAX_HEALTH);
        setDirection(Direction.DOWN);

    }

    @Override
    public void moveLeft() {
        if (cantMoveTo(getX() - 5, getY(), Direction.LEFT)) {
            return;
        }
        if (getX() - 5 < 0) {
            return;
        }
        setX(getX() - 5);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/PlayerMoveLeft.png"));
    }

    @Override
    public void moveRight() {
        if (cantMoveTo(getX() + 5, getY(), Direction.RIGHT)) {
            return;
        }
        if (getX() + 5 > mapSize - 30) {
            return;
        }
        setDirection(Direction.RIGHT);
        setX(getX() + 5);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/PlayerMoveRight.png"));
    }

    @Override
    public void moveUp() {
        if (cantMoveTo(getX(), getY() - 5, Direction.UP)) {
            return;
        }
        if (getY() - 5 < 0) {
            return;
        }
        setDirection(Direction.UP);
        setY(getY() - 5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/PlayerMoveUp.png"));
    }

    @Override
    public void moveDown() {
        if (cantMoveTo(getX(), getY() + 5, Direction.DOWN)) {
            return;
        }
        if (getY() + 5 > mapSize - 30) {
            return;
        }
        setDirection(Direction.DOWN);
        setY(getY() + 5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/PlayerMoveDown.png"));
    }

    public void shoot(Pane root) {
        Bullet bullet = new Bullet(this, (int) (getX() + getTankImageView().getImage().getWidth() / 2), getY(), getDirection());
        getBullets().add(bullet);
        root.getChildren().add(bullet.getBulletImageView());
    }

    public int getBulletPower() {
        return bulletPower;
    }

    public void setBulletPower(int bulletPower) {
        this.bulletPower = bulletPower;
    }

    @Override
    public void hit(Pane root) {
        setBulletPower(1);
        setHealth(getHealth() - 1);
        if (getHealth() <= 0) {
            for (Bullet bullet : this.getBullets()) {
                root.getChildren().remove(bullet.getBulletImageView());
            }
            System.out.println("oo im dead");
            root.getChildren().remove(this.getTankImageView());
            gameState = GameState.GAME_OVER;
            gradualExplosion(p1.getX(), p1.getY(), root);
        }

    }


}

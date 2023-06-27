package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

import static ir.ac.kntu.TankGame.gameState;

public class PlayerTank extends Tank {

    private static final int MAX_HEALTH = 3;

    public PlayerTank(int x, int y, int health, Direction direction) {
        super(x,y,health,direction);
        setTankImageView(new ImageView(new Image("images/PlayerMoveDown.png")));
        getTankImageView().setLayoutX(x);
        getTankImageView().setLayoutY(y);
        health = MAX_HEALTH;
        direction = Direction.UP;

    }

    @Override
    public void moveLeft() {
        if (getX() - 5 < 0)return;
        setX(getX()-5);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/PlayerMoveLeft.png"));
    }
    @Override
    public void moveRight() {
        if (getX() + 5 > 770)return;
        setDirection(Direction.RIGHT);
        setX(getX()+5);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/PlayerMoveRight.png"));
    }

    @Override
    public void moveUp() {
        if (getY() - 5 < 0)return;
        setDirection(Direction.UP);
        setY(getY()-5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/PlayerMoveUp.png"));
    }
    @Override
    public void moveDown() {
        if (getY() + 5 > 570 )return;
        setDirection(Direction.DOWN);
        setY(getY()+5);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/PlayerMoveDown.png"));
    }

    public void shoot(Pane root) {
        Bullet bullet = new Bullet((int) (getX() + getTankImageView().getImage().getWidth() / 2), getY(), getDirection());
        getBullets().add(bullet);
        root.getChildren().add(bullet.getBulletImageView());
    }

    @Override
    public void hit() {
        setHealth(getHealth()-1);
        if (getHealth() >= MAX_HEALTH) {
            System.out.println("oo im dead");
            gameState = GameState.ENDED;
        }
    }


}

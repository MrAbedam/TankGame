package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArmoredTank extends Tank {

    static final double WIDTH = 800;
    static final double HEIGHT = 600;


    public ArmoredTank(int x, int y) {
        super(x, y);
        setTankImageView(new ImageView(new Image("images/ArmoredEnemyTankDown.png")));
        getTankImageView().setLayoutX(x);
        getTankImageView().setLayoutY(y);
        setHealth(2);
        setDirection(Direction.DOWN);
    }

    @Override
    public void moveLeft() {
        if (cantMoveTo(getX() + 5, getY(), Direction.LEFT)) return;
        if (getX() - 0.75 < 0)return;
        setX(getX()-0.75);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/ArmoredEnemyTankLeft.png"));
    }
    @Override
    public void moveRight() {
        if (cantMoveTo(getX() + 5, getY(), Direction.RIGHT)) return;
        if (getX() + 0.75 > WIDTH -30)return;
        setDirection(Direction.RIGHT);
        setX(getX()+0.75);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/ArmoredEnemyTankRight.png"));
    }

    @Override
    public void moveUp() {
        if (cantMoveTo(getX() + 5, getY(), Direction.UP)) return;
        if (getY() - 0.75 < 0)return;
        setDirection(Direction.UP);
        setY(getY()-0.75);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/ArmoredEnemyTankUp.png"));
    }
    @Override
    public void moveDown() {
        if (cantMoveTo(getX() + 5, getY(), Direction.DOWN)) return;
        if (getY() + 0.75 > HEIGHT - 30 )return;
        setDirection(Direction.DOWN);
        setY(getY()+0.75);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/ArmoredEnemyTankDown.png"));
    }
}

package ir.ac.kntu;

import ir.ac.kntu.Tank;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet {
    private static final int SPEED = 5;
    private static final int BULLET_WIDTH = 10;
    private static final int BULLET_HEIGHT = 10;

    private ImageView bulletImageView;
    private int x;
    private int y;
    private Direction direction;

    public Bullet(int startX, int startY, Direction direction) {

        x = startX - (BULLET_WIDTH / 2);
        y = startY - BULLET_HEIGHT;
        this.direction = direction;
        switch (direction) {
            case DOWN -> bulletImageView = new ImageView(new Image("images/BulletMoveDown.png"));
            case UP -> bulletImageView = new ImageView(new Image("images/BulletMoveUp.png"));
            case LEFT -> bulletImageView = new ImageView(new Image("images/BulletMoveLeft.png"));
            case RIGHT -> bulletImageView = new ImageView(new Image("images/BulletMoveRight.png"));
        }
        bulletImageView.setLayoutX(x);
        bulletImageView.setLayoutY(y);
    }

    public ImageView getBulletImageView() {
        return bulletImageView;
    }

    public void move() {
        switch (direction) {
            case DOWN -> y += SPEED;
            case UP -> y -= SPEED;
            case LEFT -> x -= SPEED;
            case RIGHT -> x += SPEED;
        }
        bulletImageView.setLayoutY(y);
        bulletImageView.setLayoutX(x);

    }

    public boolean checkCollision(Tank tank) {
        if (bulletImageView.getBoundsInParent().intersects(tank.getTankImageView().getBoundsInParent())) {
            tank.hit();
            return true; // Collision
        }
        return false; // No Collision
    }

    public boolean isOffScreen(Pane pane) {
        return (y < -20 || y > pane.getHeight()+20 || x < -20 || x> pane.getWidth()+20);
    }
}

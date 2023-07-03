package ir.ac.kntu;

import ir.ac.kntu.Tank;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

import static ir.ac.kntu.TankGame.allTanks;

public class Bullet {

    private static final int SPEED = 5;

    private static final int BULLET_WIDTH = 10;

    private static final int BULLET_HEIGHT = 10;

    private Tank owner;

    private ImageView bulletImageView;

    private double x;

    private double y;

    private Direction direction;

    private boolean destroyed;

    public Bullet(Tank tank, double startX, double startY, Direction direction) {

        this.owner = tank;
        x = startX - (BULLET_WIDTH / 2);
        y = startY - BULLET_HEIGHT;
        this.direction = direction;
        switch (direction) {
            case DOWN -> bulletImageView = new ImageView(new Image("images/BulletMoveDown.png"));
            case UP -> bulletImageView = new ImageView(new Image("images/BulletMoveUp.png"));
            case LEFT -> bulletImageView = new ImageView(new Image("images/BulletMoveLeft.png"));
            default -> bulletImageView = new ImageView(new Image("images/BulletMoveRight.png"));
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
            default -> x += SPEED;
        }
        bulletImageView.setLayoutY(y);
        bulletImageView.setLayoutX(x);

    }


    public boolean isOffScreen(Pane pane) {
        return (y < -20 || y > pane.getHeight() + 20 || x < -20 || x > pane.getWidth() + 20);
    }

    public boolean collidesWith(Tank enemyTank) {
        ImageView tankImageView = enemyTank.getTankImageView();
        double tankX = tankImageView.getLayoutX();
        double tankY = tankImageView.getLayoutY();
        double tankWidth = tankImageView.getImage().getWidth();
        double tankHeight = tankImageView.getImage().getHeight();

        ImageView bulletImageView = getBulletImageView();
        double bulletX = bulletImageView.getLayoutX();
        double bulletY = bulletImageView.getLayoutY();
        double bulletWidth = bulletImageView.getImage().getWidth();
        double bulletHeight = bulletImageView.getImage().getHeight();

        // Check for collision
        if (bulletX < tankX + tankWidth &&
                bulletX + bulletWidth > tankX &&
                bulletY < tankY + tankHeight &&
                bulletY + bulletHeight > tankY) {
            return true; // Collision occurred
        }
        return false; // No collision
    }

    public Tank getOwner() {
        return owner;
    }

    public void setOwner(Tank owner) {
        this.owner = owner;
    }


    public static void removeBulletFromTank(Pane root, Bullet currentBullet) {
        for (Tank testTank : allTanks) {
            if (!testTank.getBullets().isEmpty()) {
                Iterator<Bullet> iterator = testTank.getBullets().iterator();
                while (iterator.hasNext()) {
                    Bullet testBullet = iterator.next();
                    if (testBullet == currentBullet) {
                        iterator.remove();
                        root.getChildren().remove(testBullet.getBulletImageView());
                    }
                }
            }
        }
    }

    public boolean collidesWith(Wall wall) {
        Bounds bulletBounds = bulletImageView.getBoundsInParent();
        Bounds wallBounds = wall.getWallImageView().getBoundsInParent();
        return bulletBounds.intersects(wallBounds);
    }

    public boolean collidesWith(Eagle eagle) {
        Bounds bulletBounds = bulletImageView.getBoundsInParent();
        Bounds eagleBounds = eagle.getImgView().getBoundsInParent();
        return bulletBounds.intersects(eagleBounds);
    }

    public void setDestroyed(boolean destroyed, Pane root) {
        this.destroyed = destroyed;
        if (destroyed) {
            root.getChildren().remove(bulletImageView);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


}

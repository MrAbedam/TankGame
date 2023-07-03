package ir.ac.kntu;

import javafx.scene.layout.Pane;

import java.util.Iterator;

import static ir.ac.kntu.Tank.gradualExplosion;

public class SimpleWall extends Wall {
    public SimpleWall(double x, double y) {
        super(x, y, "images/SimpleWall.png");
    }

    @Override
    public void handleBulletCollision(Bullet bullet, Wall testWall, Iterator<Bullet> bulletIterator, Iterator<Wall> wallIterator, Pane root) {
        if (!root.getChildren().contains(this.getWallImageView())) {
            return;
        }
        try {
            setDestroyed(true);  // Destroy the wall when hit by a bullet
            bullet.setDestroyed(true);
            bulletIterator.remove();
            root.getChildren().remove(bullet.getBulletImageView());
            wallIterator.remove();
            testWall.removeFromPane(root);
            gradualExplosion(testWall.getX(), testWall.getY(), root);
            allWalls.remove(this);
        } catch (IllegalStateException e) {

        }
    }
}

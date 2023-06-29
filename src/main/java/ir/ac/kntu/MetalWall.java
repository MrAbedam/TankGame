package ir.ac.kntu;

import javafx.scene.layout.Pane;

import java.util.Iterator;

public class MetalWall extends Wall {
    public MetalWall(double x, double y) {
        super(x, y, "images/MetalWall.png");
    }


    @Override
    public void handleBulletCollision(Bullet bullet, Wall testWall, Iterator<Bullet> bulletIterator, Iterator<Wall> wallIterator, Pane root) {
        bullet.setDestroyed(true);
        if (bulletIterator.hasNext()){
            bulletIterator.remove();
        }
        root.getChildren().remove(bullet.getBulletImageView());
    }
}

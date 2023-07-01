package ir.ac.kntu;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static ir.ac.kntu.TankGame.root;

public abstract class Wall {
    private ImageView wallImageView;
    private double x;
    private double y;
    private boolean destroyed;
    public static ArrayList<Wall> allWalls = new ArrayList<>();

    public static double wallHeight = 45;
    public static double wallWidth = 45;


    public Wall(double x, double y, String imagePath) {
        this.x = x;
        this.y = y;
        this.destroyed = false;
        this.wallImageView = new ImageView(new Image(imagePath));
        this.wallImageView.setLayoutX(x);
        this.wallImageView.setLayoutY(y);
        allWalls.add(this);
    }

    public boolean collidesWith(double x, double y) {
        if (x >= getX() && x < getX() + getWallImageView().getFitWidth() &&
                y >= getY() && y < getY() + getWallImageView().getFitHeight()) {
            return true;
        }
        return false;
    }

    public ImageView getWallImageView() {
        return wallImageView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public abstract void handleBulletCollision(Bullet bullet, Wall testWall, Iterator<Bullet> bulletIterator, Iterator<Wall> wallIterator,Pane root);

    public void addToPane() {
        root.getChildren().add(wallImageView);
    }

    public void removeFromPane(Pane root) {
        root.getChildren().remove(wallImageView);
    }
}

package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Tank {
    private static final int MAX_HEALTH = 3;

    private ImageView tankImageView;
    private int x;
    private int y;
    private int health;
    private Direction direction;
    private ArrayList<Bullet> bullets;


    public Tank() {
        tankImageView = new ImageView(new Image("images/PlayerMoveDown.png"));
        x = 0;
        y = 0;
        health = 3;
        direction = Direction.DOWN;
        bullets = new ArrayList<>();
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void moveLeft() {
        if (x - 5 < 0)return;
        x -= 5;
        setDirection(Direction.LEFT);
        tankImageView.setLayoutX(x);
        tankImageView.setImage(new Image("images/PlayerMoveLeft.png"));
    }

    public void moveRight() {
        if (x + 5 > 770)return;
        setDirection(Direction.RIGHT);
        x += 5;
        tankImageView.setLayoutX(x);
        tankImageView.setImage(new Image("images/PlayerMoveRight.png"));
    }

    public void moveUp() {
        if (y - 5 < 0)return;
        setDirection(Direction.UP);
        y -= 5;
        tankImageView.setLayoutY(y);
        tankImageView.setImage(new Image("images/PlayerMoveUp.png"));
    }

    public void moveDown() {
        if (y + 5 > 570 )return;
        setDirection(Direction.DOWN);
        y += 5;
        tankImageView.setLayoutY(y);
        tankImageView.setImage(new Image("images/PlayerMoveDown.png"));
    }

    public void shoot(Pane root) {
        Bullet bullet = new Bullet((int) (x + tankImageView.getImage().getWidth() / 2), y, this.direction);
        bullets.add(bullet);
        root.getChildren().add(bullet.getBulletImageView());
    }

    public void hit() {
        health++;
        if (health >= MAX_HEALTH) {
            System.out.println("oo im dead");
        }
    }

    public void removeBullet(Bullet bullet){
        bullets.remove(bullet);
    }
}

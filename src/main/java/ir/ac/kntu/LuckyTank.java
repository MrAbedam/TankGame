package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Random;

import static ir.ac.kntu.ArmoredTank.HEIGHT;
import static ir.ac.kntu.ArmoredTank.WIDTH;
import static ir.ac.kntu.TankGame.*;

public class LuckyTank extends Tank {

    private int value;

    public LuckyTank(int x, int y) {
        super(x,y);
        setTankImageView(new ImageView(new Image("images/LuckyEnemyTankDown.png")));
        getTankImageView().setLayoutX(x);
        getTankImageView().setLayoutY(y);
        setHealth(new Random().nextInt(2)+1); // 1 or 2
        this.value = getHealth()*100;
        setDirection(Direction.DOWN);
    }

    @Override
    public void die(Pane root) {
        remainingTanks--;
        double deathX = this.getX();
        double deathY = this.getY();
        allTanks.remove(this);
        playerScore += value;
        root.getChildren().remove(this.getTankImageView());
        gradualExplosion(deathX,deathY,root);
        Superpower superpower = new Superpower(deathX,deathY,"images/ArmoredEnemyTankDown.png",root);
    }

    public SuperPowerType giveRandomSuperPowerType(){
        Random rand = new Random();
        int n = rand.nextInt();
        switch (n%3){
            case 1: return SuperPowerType.FREEZE;
            case 2: return SuperPowerType.STAR;
            default: return SuperPowerType.EXTRA_LIFE;
        }
    }

    @Override
    public void moveLeft() {
        if (cantMoveTo(getX() + 5, getY(), Direction.LEFT)) return;
        if (getX() - 0.75 < 0)return;
        setX(getX()-0.75);
        setDirection(Direction.LEFT);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/LuckyEnemyTankLeft.png"));
    }
    @Override
    public void moveRight() {
        if (cantMoveTo(getX() + 5, getY(), Direction.RIGHT)) return;
        if (getX() + 0.75 > WIDTH -30)return;
        setDirection(Direction.RIGHT);
        setX(getX()+0.75);
        getTankImageView().setLayoutX(getX());
        getTankImageView().setImage(new Image("images/LuckyEnemyTankRight.png"));
    }

    @Override
    public void moveUp() {
        if (cantMoveTo(getX() + 5, getY(), Direction.UP)) return;
        if (getY() - 0.75 < 0)return;
        setDirection(Direction.UP);
        setY(getY()-0.75);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/LuckyEnemyTankUp.png"));
    }
    @Override
    public void moveDown() {
        if (cantMoveTo(getX() + 5, getY(), Direction.DOWN)) return;
        if (getY() + 0.75 > HEIGHT - 30 )return;
        setDirection(Direction.DOWN);
        setY(getY()+0.75);
        getTankImageView().setLayoutY(getY());
        getTankImageView().setImage(new Image("images/LuckyEnemyTankDown.png"));
    }

}

package ir.ac.kntu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

import static ir.ac.kntu.TankGame.superPowers;

public class Superpower {
    private ImageView superpowerImageView;
    private double x;
    private double y;
    private Timeline timer;
    private Pane root;
    private SuperPowerType superPowerType;


    public Superpower(double x, double y, String imagePath, Pane root) {
        this.x = x;
        this.y = y;
        SuperPowerType randomSP = giveRandomSuperPowerType();
        this.superpowerImageView = new ImageView(new Image("images/"+randomSP.name()+".png"));
        this.superpowerImageView.setLayoutX(x);
        this.superpowerImageView.setLayoutY(y);
        this.root = root;
        this.addToPane(root);
        this.superPowerType = randomSP;
        superPowers.add(this);
        timer = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            removeFromPane(root);
            if (superPowers.contains(this)){
                superPowers.remove(this);
            }
        }));
        timer.setCycleCount(1);
        timer.play();
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


    public SuperPowerType getSuperPowerType() {
        return superPowerType;
    }

    public void activateFreeze(){
        System.out.println("Frozen");
    }
    public void activeStar(){
        System.out.println("StarStruck");
    }
    public void activeExtraLife(){
        System.out.println("ExtraLife");
    }

    public void collectSuperPower(){
        switch(this.getSuperPowerType()){
            case FREEZE -> activateFreeze();
            case STAR -> activeStar();
            default -> activeExtraLife();
        }
        this.removeFromPane(root);
    }

    public ImageView getSuperpowerImageView() {
        return superpowerImageView;
    }

    public void setSuperpowerImageView(ImageView superpowerImageView) {
        this.superpowerImageView = superpowerImageView;
    }

    /*public boolean collidesWith(Tank enemyTank) {
        ImageView tankImageView = enemyTank.getTankImageView();
        double tankX = tankImageView.getLayoutX();
        double tankY = tankImageView.getLayoutY();
        double tankWidth = tankImageView.getImage().getWidth();
        double tankHeight = tankImageView.getImage().getHeight();

        ImageView superpowerImageView = getSuperpowerImageView();
        double superPowerX = superpowerImageView.getLayoutX();
        double superPowerY = superpowerImageView.getLayoutY();
        double superPowerWidth = superpowerImageView.getImage().getWidth();
        double superPowerHeight = superpowerImageView.getImage().getHeight();

        // Check for collision
        if (superPowerX < tankX + tankWidth &&
                superPowerX + superPowerWidth > tankX &&
                superPowerY < tankY + tankHeight &&
                superPowerY + superPowerHeight > tankY) {
            System.out.println("yesssss");
            return true; // Collision occurred
        }
        return false; // No collision
    }*/

    public boolean collidesWith(Tank tank) {
        Bounds superpowerImageViewBoundsInParent = superpowerImageView.getBoundsInParent();
        Bounds tankBounds = tank.getTankImageView().getBoundsInParent();
        return superpowerImageViewBoundsInParent.intersects(tankBounds);
    }

    public void handleCollision(PlayerTank playerTank) {
        // Implement the logic to handle the collision with the player tank here
    }

    public void addToPane(Pane root) {
        root.getChildren().add(superpowerImageView);
    }

    public void removeFromPane(Pane root) {
        root.getChildren().remove(superpowerImageView);
    }
}

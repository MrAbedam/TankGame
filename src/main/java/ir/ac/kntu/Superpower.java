package ir.ac.kntu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

import static ir.ac.kntu.TankGame.p1;
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
        this.superpowerImageView = new ImageView(new Image("images/" + randomSP.name() + ".png"));
        this.superpowerImageView.setLayoutX(x);
        this.superpowerImageView.setLayoutY(y);
        this.root = root;
        this.addToPane(root);
        this.superPowerType = randomSP;
        superPowers.add(this);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);  // Wait for 5 seconds
                Platform.runLater(() -> {
                    removeFromPane(root);
                    if (superPowers.contains(this)) {
                        superPowers.remove(this);
                    }
                });
            } catch (InterruptedException e) {
                // Handle interrupted exception
            }
        });
        thread.start();
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
        Tank.setIsFreezeActive(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    Tank.setIsFreezeActive(false);
                })
        );
        timeline.play();
    }

    public void activeStar(){
        p1.setBulletPower(2);
    }

    public void activeExtraLife(){
        p1.setHealth(p1.getHealth()+1);
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

    public boolean collidesWith(PlayerTank tank) {
        Bounds superpowerImageViewBoundsInParent = superpowerImageView.getBoundsInParent();
        Bounds tankBounds = tank.getTankImageView().getBoundsInParent();
        return superpowerImageViewBoundsInParent.intersects(tankBounds);
    }


    public void addToPane(Pane root) {
        root.getChildren().add(superpowerImageView);
    }

    public void removeFromPane(Pane root) {
        root.getChildren().remove(superpowerImageView);
    }
}

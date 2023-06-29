package ir.ac.kntu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

public class Superpower {
    private ImageView superpowerImageView;
    private double x;
    private double y;
    private Timeline timer;
    private Pane root;
    private SuperPowerType superPowerType;

    public Superpower(double x, double y, String imagePath,Pane root) {
        this.x = x;
        this.y = y;
        SuperPowerType randomSP = giveRandomSuperPowerType();
        this.superpowerImageView = new ImageView(new Image("images/"+randomSP.name()+".png"));
        this.superpowerImageView.setLayoutX(x);
        this.superpowerImageView.setLayoutY(y);
        this.root = root;
        this.addToPane(root);
        this.superPowerType = randomSP;
        timer = new Timeline(new KeyFrame(Duration.seconds(5), event -> removeFromPane(root)));
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

package ir.ac.kntu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreen extends Stage {
    private List<Player> leaderboard;


    public LeaderboardScreen(List<Player> leaderboard) {
        this.leaderboard = leaderboard;
        setTitle("Top 5 Players");
        setScene(createScene());
    }

    private Scene createScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        Label title = new Label("Top 5 Players:");
        root.getChildren().add(title);

        for (int i = 0; i < 5 && i < leaderboard.size(); i++) {
            Label playerLabel = new Label((i + 1) + ". " + leaderboard.get(i).getName() + " => " +leaderboard.get(i).getScore() );
            root.getChildren().add(playerLabel);
        }
        return new Scene(root, 300, 300, Color.BLACK);
    }
}

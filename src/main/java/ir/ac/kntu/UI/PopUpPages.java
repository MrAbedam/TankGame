package ir.ac.kntu.UI;

import ir.ac.kntu.EnumsAndStates.GameState;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Iterator;

import static ir.ac.kntu.Engine.GlobalConstants.*;
import static ir.ac.kntu.Engine.TankGame.*;

public class PopUpPages {

    public static void showWinPage() {
        if (currentLevel == 10) {
            gameState = GameState.COMPLETED;
            return;
        }
        root.getChildren().clear();
        storedHealth = p1.getHealth();
        if (currentLevel != 10 && getIn == 0) {
            getIn = 1;
            Pane winRoot = new Pane();
            winRoot.setStyle("-fx-background-color: #f88e55;");
            Stage winStage = new Stage();
            Scene winScene = new Scene(winRoot, 500, 500);
            winStage.setScene(winScene);
            winStage.show();
            Button winButton = new Button("Next level");
            winButton.setLayoutX(225);
            winButton.setLayoutY(400);
            winButton.setOnMouseClicked(event -> {
                getIn = 0;
                winStage.close();
                startLevel(currentLevel + 1);
            });
            winRoot.getChildren().addAll(makeLuckyImage(), makeArmoredImage(), makeRegularImage(), winButton, makeWinText(), makeTankKills());
            curArmored = 0;
            curLucky = 0;
            curRegular = 0;
        }
    }


    public static ImageView makeArmoredImage() {
        ImageView armoredImage = new ImageView(new Image("images/ArmoredEnemyTankUp.png"));
        armoredImage.setLayoutX(200);
        armoredImage.setLayoutY(130);
        return armoredImage;
    }

    public static ImageView makeLuckyImage() {
        ImageView regularImage = new ImageView(new Image("images/RegularEnemyTankUp.png"));
        regularImage.setLayoutX(280);
        regularImage.setLayoutY(130);
        return regularImage;
    }

    public static ImageView makeRegularImage() {
        ImageView luckyImage = new ImageView(new Image("images/LuckyEnemyTankUp.png"));
        luckyImage.setLayoutX(120);
        luckyImage.setLayoutY(130);
        return luckyImage;
    }

    public static Text makeTankKills() {
        Text tankKills = new Text("Lucky, Armored, Regular:" +
                " " + curLucky + ", " + curArmored + ", " + curRegular);
        tankKills.setFill(Color.BLUE);
        tankKills.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        tankKills.setLayoutX(100);
        tankKills.setLayoutY(100);
        return tankKills;
    }

    public static Text makeWinText() {
        Text winText = new Text("You won the level " + userName + " ! Score: " + playerScore);
        winText.setLayoutX(50);
        winText.setLayoutY(300);
        winText.setFill(Color.GREEN);
        winText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        winText.setTextAlignment(TextAlignment.CENTER);
        return winText;
    }


    public static void showGameOverPage(Stage primaryStage) {
        LeaderboardWriter.writeLeaderBoard(leaderboard);
        root.getChildren().clear();
        Image gameOverImage = new Image("images/GameOver.png"); // Replace "pause.png" with the path to your image
        ImageView gameOverImageView = new ImageView(gameOverImage);
        gameOverImageView.setPreserveRatio(true);
        gameOverImageView.setLayoutX(mapSize / 2);
        gameOverImageView.setLayoutY(50);
        gameOverImageView.setFitWidth(300);
        Text gameOverText = new Text("Game Over! You lost the game.");
        gameOverText.setLayoutX(mapSize / 2);
        gameOverText.setLayoutY(mapSize / 2);
        gameOverText.setFill(Color.RED);
        double fontSize = 20;
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(gameOverImageView, gameOverText);
        showLeaderboard(primaryStage);
    }

    public static void makeMenu(){
        Stage menuStage = new Stage();
        Pane menuRoot = new Pane();
        Scene menuScene = new Scene(menuRoot,500,400);

        menuStage.setScene(menuScene);

        Button submitButton = new Button("Start game");
        submitButton.setLayoutX(225);
        submitButton.setLayoutY(300);
        submitButton.setOnAction(event -> {
            menuStage.close();
        });
        ImageView backgroundImage = new ImageView(new Image("images/MenuPic.png"));
        menuRoot.getChildren().addAll(backgroundImage,submitButton);
        menuStage.setTitle("Menu");
        menuStage.showAndWait();
        menuRoot.getChildren().clear();
    }

    public static void showLeaderboard(Stage primaryStage) {
        Button button = new Button("See high scores");
        button.setLayoutX(mapSize / 2 + 100);
        button.setLayoutY(mapSize / 2 + 100);
        button.setOnAction(event -> {
            Player newPlayer = new Player(userName, playerScore);
            for (Player p1 : leaderboard.getPlayers()) {
                if (p1.getName().equals(userName)) {
                    newPlayer.setScore(Math.max(p1.getScore(), playerScore));
                    newPlayer.setNumberOfGames(p1.getNumberOfGames() + 1);
                }
            }
            Iterator<Player> iterator = leaderboard.getPlayers().iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (player.getName().equals(userName)) {
                    iterator.remove();
                }
            }
            leaderboard.addPlayer(newPlayer);
            leaderboard.showLeaderboard();
            LeaderboardScreen l1 = new LeaderboardScreen(leaderboard.getPlayers());
            primaryStage.setScene(l1.getScene());
        });
        root.getChildren().addAll(button);
    }

    public static void showMenu() {
        int numOfRectangles = 10;
        double rectangleSize = 50;
        double spacing = 10;
        double startX = (mapSize - (numOfRectangles * (rectangleSize + spacing))) / 2;
        double startY = (mapSize - rectangleSize) / 2;
        for (int i = 0; i < numOfRectangles; i++) {
            Rectangle rectangle = new Rectangle(100 + startX + i * (rectangleSize + spacing), startY, rectangleSize, rectangleSize);
            showLevelOption(rectangle, i);
        }
    }


    public static void completeGamePage() {
        Text endGameText = new Text("You have completed this game, WELL PLAYED.");
        endGameText.setLayoutX(50);
        endGameText.setLayoutY(mapSize / 2);
        endGameText.setFill(Color.GREEN);
        endGameText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 30));
        root.getChildren().add(endGameText);
    }

    public static void showLevelOption(Rectangle rectangle, int i) {
        rectangle.setFill(Color.YELLOW);
        Text levelText = new Text(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2, "Level " + String.valueOf(i + 1));
        levelText.setFill(Color.BLACK);
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        levelText.setTextAlignment(TextAlignment.CENTER);
        levelText.setTranslateX(-levelText.getBoundsInLocal().getWidth() / 2);
        levelText.setTranslateY(levelText.getBoundsInLocal().getHeight() / 4);
        int finalI = i;
        rectangle.setId(String.valueOf(i + 1));
        rectangle.setOnMouseClicked(event -> handleLevelSelection(finalI + 1));
        root.getChildren().addAll(rectangle, levelText);
    }

    public static void showUserStats() {
        Player curPlayer = findPlayerByName();
        Text name = new Text("Name: " + curPlayer.getName());
        Text gamesPlayed = new Text("Games played: " + curPlayer.getNumberOfGames());
        Text highScore = new Text("HighScore: " + curPlayer.getScore());
        name.setLayoutX(150);
        gamesPlayed.setLayoutX(150);
        highScore.setLayoutX(150);
        name.setLayoutY(100);
        gamesPlayed.setLayoutY(150);
        highScore.setLayoutY(200);
        Pane tempRoot = new Pane();
        Scene tempScene = new Scene(tempRoot, 300, 300);
        Stage tempStage = new Stage();
        Label label = new Label("User stats");
        Button submitButton = new Button("Start Game");
        submitButton.setLayoutX(150);
        submitButton.setLayoutY(250);
        submitButton.setOnAction(event -> {
            tempStage.close();
        });
        tempRoot.getChildren().addAll(label, submitButton, name, highScore, gamesPlayed);
        tempStage.setScene(tempScene);
        tempStage.setTitle("User stats");
        tempStage.showAndWait();
        tempRoot.getChildren().clear();
    }


    public static Player findPlayerByName() {
        for (Player player : leaderboard.getPlayers()) {
            if (player.getName().equals(userName)) {
                return player;
            }
        }
        return null;
    }

    public static void userNameInputWindow() {
        Stage secondaryStage = new Stage();
        VBox secondaryRoot = new VBox();
        Scene secondaryScene = new Scene(secondaryRoot, 300, 300, Color.BLACK);
        Label label = new Label("Enter your username:");
        TextField textField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            userName = textField.getText();
            secondaryStage.close();
        });
        secondaryRoot.getChildren().addAll(label, textField, submitButton);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setTitle("Username Input");
        secondaryStage.showAndWait();
        secondaryRoot.getChildren().clear();
    }

}

package ir.ac.kntu;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static ir.ac.kntu.Wall.allWalls;
import static java.lang.Thread.sleep;

public class TankGame extends Application {

    public static ArrayList<Superpower> superPowers = new ArrayList<>();

    public static ArrayList<Tank> allTanks = new ArrayList<>();

    public static GameState gameState = GameState.RUNNING;

    public static PlayerTank p1;

    public static Eagle eagle;

    public static Pane root = new Pane();

    public static int storedHealth = 3;

    public static int mapSize = 500;

    public static int remainingTanks = 4;

    public static int playerScore = 0;  // Player's score


    public static int curLucky = 0;

    public static int curArmored = 0;

    public static int curRegular = 0;

    static char[][] setupMap;

    static String userName;

    static Leaderboard leaderboard = new Leaderboard();


    public GameState getGameState() {
        return gameState;
    }

    private int currentLevel;

    public TankGame() {
    }

    public TankGame(int level) {
        this.currentLevel = level;
    }

    public void addMetalWall(double x, double y) {
        MetalWall metalWall = new MetalWall(x, y);
        metalWall.addToPane();
    }

    public void addSimpleWall(int x, int y) {
        SimpleWall simpleWall = new SimpleWall(x, y);
        simpleWall.addToPane();
    }

    public void addArmoredTank(int x, int y, Pane root) {
        ArmoredTank t1 = new ArmoredTank(x, y);
        curArmored++;
        root.getChildren().add(t1.getTankImageView());
    }

    public void addRegularTank(int x, int y, Pane root) {
        Tank t1 = new Tank(x, y);
        curRegular++;
        root.getChildren().add(t1.getTankImageView());
    }

    public void addLuckyTank(int x, int y, Pane root) {
        LuckyTank t1 = new LuckyTank(x, y);
        curLucky++;
        root.getChildren().add(t1.getTankImageView());
    }

    public void splitWall() {
        for (int i = 0; i < mapSize; i = i + 50) {
            addMetalWall(mapSize, i);
        }
        for (int i = 0; i < mapSize + 150; i = i + 50) {
            addMetalWall(i, mapSize);
        }
    }

    public String healthToString() {
        if (p1 == null) {
            return "3";
        }
        return ("images/" + p1.getHealth() + ".png");
    }

    public void addHp() {
        ImageView hitPoint = new ImageView(new Image("images/HeartIcon.png"));
        ImageView hitPointAmount = new ImageView(new Image(healthToString()));
        hitPointAmount.setLayoutX(mapSize + 100);
        hitPointAmount.setLayoutY(100);
        hitPoint.setLayoutX(mapSize + 40);
        hitPoint.setLayoutY(100);
        root.getChildren().addAll(hitPointAmount, hitPoint);
    }

    public void addScore() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Current score:"));
        Text scoreDisplay = new Text("Current score: " + playerScore);
        scoreDisplay.setFill(Color.ORANGE);
        scoreDisplay.setLayoutX(mapSize + 40);
        scoreDisplay.setLayoutY(300);
        double fontSize = 20;
        scoreDisplay.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(scoreDisplay);
    }

    public void addRemainingTanks() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Remaining Tanks:"));
        Text remainingTanksText = new Text("Remaining Tanks: " + remainingTanks);
        remainingTanksText.setLayoutX(mapSize + 40);
        remainingTanksText.setLayoutY(200);
        remainingTanksText.setFill(Color.ORANGE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void addCurrentLevel() {
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Level:"));
        Text remainingTanksText = new Text("Level: " + currentLevel);
        remainingTanksText.setLayoutX(mapSize + 90);
        remainingTanksText.setLayoutY(400);
        remainingTanksText.setFill(Color.ORANGE);
        double fontSize = 20;  // Adjust the font size as needed
        remainingTanksText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        root.getChildren().addAll(remainingTanksText);
    }

    public void updateStatus() {
        addRemainingTanks();
        addHp();
        addScore();
        addCurrentLevel();
    }


    public void spawnTank() {
        if (remainingTanks >= 4 && allTanks.size() <= 4) {
            int xSpawn = giveSpawnPoint();
            switch (remainingTanks % 3) {
                case 0 -> {
                    addArmoredTank(xSpawn, 15, root);
                }
                case 1 -> {
                    addRegularTank(xSpawn, 15, root);
                }
                default -> {
                    addLuckyTank(xSpawn, 15, root);
                }
            }
        }
    }

    public int giveSpawnPoint() {
        return (new Random().nextInt(4) * (mapSize / 3 - 20));
    }


    public void userNameInputWindow() {
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


    public boolean doesUserExist(String userName) {
        for (Player p1 : leaderboard.getPlayers()) {
            if (p1.getName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void showUserStats(String userName) {
        Player curPlayer = null;
        for (Player player : leaderboard.getPlayers()) {
            if (player.getName().equals(userName)) {
                curPlayer = player;
            }
        }
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

    @Override
    public void start(Stage primaryStage) {
        leaderboard = LeaderboardWriter.readLeaderBoard();
        userNameInputWindow();
        if (doesUserExist(userName)){
            showUserStats(userName);
        }
        //userName = "mmdabed";
        root = new Pane();
        Scene scene = new Scene(root, mapSize + 400, mapSize + 150, Color.BLACK);
        root.setStyle("-fx-background-color: #000000;");
        primaryStage.setScene(scene);
        readMapFromFile();
        showMenu();
        gameState = GameState.MENU;
        scene.setOnKeyPressed(event -> {
            if (gameState == GameState.MENU) {
                // Handle level selection in the MENU state
                KeyCode keyCode = event.getCode();
                if (keyCode.isDigitKey()) {
                    int level = Integer.parseInt(keyCode.getName());
                    if (level >= 1 && level <= 10) {
                        startLevel(level);
                    }
                }
            } else if (gameState == GameState.RUNNING) {
                // Handle tank movement and shooting in the RUNNING state
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.LEFT) {
                    p1.moveLeft();
                } else if (keyCode == KeyCode.RIGHT) {
                    p1.moveRight();
                } else if (keyCode == KeyCode.UP) {
                    p1.moveUp();
                } else if (keyCode == KeyCode.DOWN) {
                    p1.moveDown();
                } else if (keyCode == KeyCode.SPACE) {
                    p1.shoot(root);
                } else if (keyCode == KeyCode.P) {
                    pauseGame();
                }
            } else if (gameState == GameState.PAUSED) {
                // Handle resuming the game from PAUSED state
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.P) {
                    resumeGame();
                }
            }
        });

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameState == GameState.RUNNING) {
                    spawnTank();
                    updateStatus();
                    updateBullets();
                    updateTanks();
                    updateHitsFromPlayer(p1);
                    updateHitsFromEnemies(p1);
                    updateSuperPower();
                    checkLevelCompletion();
                } else if (gameState == GameState.WIN) {
                    showWinPage();
                } else if (gameState == GameState.GAME_OVER) {
                    stop();
                    showGameOverPage(primaryStage);
                    LeaderboardWriter.writeLeaderBoard(leaderboard);
                } else if (gameState == GameState.COMPLETED) {
                    root.getChildren().clear();
                    stop();
                    completeGamePage();
                    showLeaderboard(primaryStage);
                    LeaderboardWriter.writeLeaderBoard(leaderboard);
                }
            }
        };

        gameLoop.start();

        primaryStage.show();
    }


    public void showLeaderboard(Stage primaryStage) {
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
            System.out.println(newPlayer.getNumberOfGames());
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

    private void pauseGame() {
        gameState = GameState.PAUSED;
    }

    private void resumeGame() {
        gameState = GameState.RUNNING;
    }


    private void updateTanks() {
        Iterator<Tank> iterator = allTanks.iterator();
        while (iterator.hasNext()) {
            Tank tank = iterator.next();
            if (tank.getClass() != PlayerTank.class) {
                tank.move(root, p1);
                if (!(tank.getHealth() >= 1)) {
                    root.getChildren().remove(tank.getTankImageView());
                    iterator.remove();
                }
            }
        }
    }

    private void updateHitsFromPlayer(PlayerTank p1) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : p1.getBullets()) {
            bullet.move();

            for (Tank enemyTank : allTanks) {
                if (enemyTank == p1) {
                    continue;
                }
                if (bullet.collidesWith(enemyTank)) {
                    enemyTank.hit(root);
                    bulletsToRemove.add(bullet);
                    break;
                }
            }

            if (bullet.isOffScreen(root)) {
                bulletsToRemove.add(bullet);
            }
        }

        // Remove bullets from root pane
        for (Bullet bullet : bulletsToRemove) {
            p1.removeBullet(bullet);
            root.getChildren().remove(bullet.getBulletImageView());
        }
    }

    private void updateHitsFromEnemies(PlayerTank p1) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Tank testTank : allTanks) {
            if (testTank == p1) {
                continue;
            }
            for (Bullet bullet : testTank.getBullets()) {
                bullet.move();
                if (bullet.collidesWith(p1)) {
                    p1.hit(root);
                    bulletsToRemove.add(bullet);
                    break;
                }
                if (bullet.collidesWith(eagle)) {
                    setGameState(GameState.GAME_OVER); // Set game state to "END" if the eagle is hit
                    bulletsToRemove.add(bullet);
                    break;
                }

                if (bullet.isOffScreen(root)) {
                    bulletsToRemove.add(bullet);
                }
            }
        }

        // Remove bullets from root pane
        for (Bullet bullet : bulletsToRemove) {
            bullet.removeBulletFromTank(root, bullet);
        }
    }

    private void updateBullets() {
        for (Tank testTank : allTanks) {
            Iterator<Bullet> bulletIterator = testTank.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.move();
                if (bullet.isOffScreen(root)) {
                    bulletIterator.remove();
                    root.getChildren().remove(bullet.getBulletImageView());
                }
                handleWallCollision(bullet, bulletIterator, root);
            }
        }
    }

    private void updateSuperPower() {
        if (superPowers.isEmpty()) {
            return;
        }
        Iterator<Superpower> superpowerIterator = superPowers.iterator();
        while (superpowerIterator.hasNext()) {
            Superpower testSuper = superpowerIterator.next();
            if (testSuper.collidesWith(p1)) {
                testSuper.collectSuperPower();
                superpowerIterator.remove(); // Safely remove the current element
            }
        }
    }


    public static void handleWallCollision(Bullet bullet, Iterator<Bullet> bulletIterator, Pane root) {
        Iterator<Wall> wallIterator = allWalls.iterator();
        while (wallIterator.hasNext()) {
            Wall testWall = wallIterator.next();
            if (bullet.collidesWith(testWall)) {
                if (bulletIterator == null) {
                    continue;
                }
                if (testWall instanceof SimpleWall) {
                    testWall.handleBulletCollision(bullet, testWall, bulletIterator, wallIterator, root);
                }
                if (testWall instanceof MetalWall) {
                    testWall.handleBulletCollision(bullet, testWall, bulletIterator, wallIterator, root);
                }
            }
        }
    }

    private void showMenu() {
        // Create and display the menu screen
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

    private void showLevelOption(Rectangle rectangle, int i) {

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

    private void handleLevelSelection(int level) {
        // Handle the level selection here
        System.out.println("Selected Level: " + level);
        // Enter the selected level
        startLevel(level);
    }

    private void startLevel(int level) {
        // Start a new level with the specified level number
        currentLevel = level;
        remainingTanks = 10 + (level - 1) * 4;
        allTanks.clear();
        allWalls.clear();
        gameState = GameState.RUNNING;
        root.getChildren().clear();
        gameSetup();
    }

    public static int getIn = 0;

    private void showWinPage() {
        // Clear the root pane
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
            Scene winScene = new Scene(winRoot,500,500);
            winStage.setScene(winScene);
            winStage.show();
            Text winText = new Text("You won the level "+userName+" ! Score: " + playerScore);
            Text tankKills = new Text("Lucky, Armored, Regular:" +
                    " " + curLucky + ", " + curArmored + ", " + curRegular);

            tankKills.setFill(Color.BLUE);
            tankKills.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            tankKills.setLayoutX(100);
            tankKills.setLayoutY(100);

            curArmored = 0;
            curLucky = 0;
            curRegular = 0;
            winText.setLayoutX(50);
            winText.setLayoutY(300);
            winText.setFill(Color.GREEN);
            winText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            winText.setTextAlignment(TextAlignment.CENTER);

            Button winButton = new Button("Next level");
            winButton.setLayoutX(225);
            winButton.setLayoutY(400);

            ImageView luckyImage = new ImageView(new Image("images/LuckyEnemyTankUp.png"));
            ImageView armoredImage = new ImageView(new Image("images/ArmoredEnemyTankUp.png"));
            ImageView regularImage = new ImageView(new Image("images/RegularEnemyTankUp.png"));

            luckyImage.setLayoutX(120);
            armoredImage.setLayoutX(200);
            regularImage.setLayoutX(280);

            luckyImage.setLayoutY(130);
            armoredImage.setLayoutY(130);
            regularImage.setLayoutY(130);
            winButton.setOnMouseClicked(event -> {
                getIn = 0;
                winStage.close();
                startLevel(currentLevel + 1);
            });
            winRoot.getChildren().addAll(luckyImage,armoredImage,regularImage,winButton,winText,tankKills);
        }
    }


    public void completeGamePage() {
        Text endGameText = new Text("You have completed this game, WELL PLAYED.");
        endGameText.setLayoutX(50);
        endGameText.setLayoutY(mapSize / 2);
        endGameText.setFill(Color.GREEN);
        endGameText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 30));
        root.getChildren().add(endGameText);
    }

    private void showGameOverPage(Stage primaryStage) {
        // Display the game over page
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


    private void checkLevelCompletion() {
        // Check if all tanks are destroyed
        if (remainingTanks == 0) {
            gameState = GameState.WIN;
        }
    }


    public static void setGameState(GameState gameState) {
        TankGame.gameState = gameState;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void makeMapFromFile() {
        mapSize = setupMap.length * 50;
        for (int i = 0; i < setupMap.length; i++) {
            for (int j = 0; j < setupMap[i].length; j++) {
                int spawnX = i * 50;
                int spawnY = j * 50;
                switch (setupMap[j][i]) {
                    case 'P' -> p1 = new PlayerTank(spawnX, spawnY);
                    case 'O' -> addRegularTank(spawnX, spawnY, root);
                    case 'A' -> addArmoredTank(spawnX, spawnY, root);
                    case 'F' -> eagle = new Eagle(spawnX, spawnY);
                    case 'M' -> addMetalWall(spawnX, spawnY);
                    case 'B' -> addSimpleWall(spawnX, spawnY);
                    case 'C' -> addLuckyTank(spawnX, spawnY, root);
                    default -> {}
                }
            }
        }
    }


    public void gameSetup() {

        makeMapFromFile();
        p1.setHealth(storedHealth);
        splitWall();
        Image pauseImage = new Image("images/Pause.png"); // Replace "pause.png" with the path to your image
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.setPreserveRatio(true);
        pauseImageView.setLayoutX(mapSize + 100);
        pauseImageView.setLayoutY(50);
        EventHandler<MouseEvent> pauseEventHandler = event -> {
            if (gameState == GameState.PAUSED) {
                setGameState(GameState.RUNNING);
            } else {
                setGameState(GameState.PAUSED);
            }
        };
        pauseImageView.setOnMouseClicked(pauseEventHandler);
        root.getChildren().addAll(eagle.getImgView(), p1.getTankImageView(), pauseImageView);
    }


    public static void readMapFromFile() {
        try {
            Scanner fileScanner = new Scanner(new File("C:\\Users\\intech\\Desktop\\poj4\\project4\\map.txt"));

            ArrayList<String> lines = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }

            int rows = lines.size();
            int columns = lines.get(0).length();

            setupMap = new char[rows][columns];

            // Fill the setup map array
            for (int i = 0; i < rows; i++) {
                String curLine = lines.get(i);
                for (int j = 0; j < columns; j++) {
                    setupMap[i][j] = curLine.charAt(j);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //to do : 1. read map from file, 3. add highscore to menu, 4. add sign in to menu
}
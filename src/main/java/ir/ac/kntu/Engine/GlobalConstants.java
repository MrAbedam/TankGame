package ir.ac.kntu.Engine;

import ir.ac.kntu.GameObjects.Eagle;
import ir.ac.kntu.GameObjects.PlayerTank;
import ir.ac.kntu.GameObjects.Superpower;
import ir.ac.kntu.GameObjects.Tank;
import ir.ac.kntu.EnumsAndStates.GameState;
import ir.ac.kntu.UI.Leaderboard;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class GlobalConstants {

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

    public static String userName;

    public static Leaderboard leaderboard = new Leaderboard();


}

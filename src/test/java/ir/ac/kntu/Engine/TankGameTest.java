package ir.ac.kntu.Engine;

import ir.ac.kntu.UI.Leaderboard;
import ir.ac.kntu.UI.Player;
import junit.framework.TestCase;
import org.junit.Test;

public class TankGameTest extends TestCase {
    private TankGame tankGame;

    public void setup() {
        tankGame = new TankGame();
    }

    public void testGiveSpawnPoint() {
        setup();
        int spawnPoint = tankGame.giveSpawnPoint();
        assertFalse(spawnPoint<0);
    }

    public void testDoesUserExist() {
        setup();
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addPlayer(new Player("Mmdk",100));
        boolean exists = tankGame.doesUserExist("Mmdk");
        assertTrue(exists);
    }



}
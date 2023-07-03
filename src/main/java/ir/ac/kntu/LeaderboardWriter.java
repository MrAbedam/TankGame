package ir.ac.kntu;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LeaderboardWriter {
    public static void writeLeaderboardToFile(Leaderboard leaderboard) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt"))) {
            for (Player player : leaderboard.getPlayers()) {
                writer.write(player.getName() + "," + player.getScore() +","+player.getNumberOfGames()+ System.lineSeparator());
            }
            System.out.println("Leaderboard data written to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing leaderboard data to file: " + e.getMessage());
        }
    }
}

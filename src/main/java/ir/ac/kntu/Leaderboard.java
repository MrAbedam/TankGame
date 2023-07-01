package ir.ac.kntu;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static ir.ac.kntu.LeaderboardWriter.writeLeaderboardToFile;
import static ir.ac.kntu.TankGame.leaderboard;

public class Leaderboard {
    private ArrayList<Player> players;

    public Leaderboard() {
        players = new ArrayList<>();
        loadLeaderboard();
    }

    public void addPlayer(Player player) {
        players.add(player);
        sortPlayers();
        writeLeaderboardToFile(leaderboard); // Save the updated leaderboard data
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void sortPlayers() {
        // Sort players based on their scores in descending order
        Collections.sort(players, Comparator.comparingInt(Player::getScore).reversed());
    }

    public void showLeaderboard() {
        // Display the leaderboard
        System.out.println("Leaderboard:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getName() + " - " + player.getScore());
        }
    }

    private void loadLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into player name and score
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    Player player = new Player(name, score);
                    players.add(player);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load leaderboard data: " + e.getMessage());
        }
    }
}
package ir.ac.kntu;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static ir.ac.kntu.TankGame.leaderboard;

public class Leaderboard implements Serializable {
    private ArrayList<Player> players;

    public Leaderboard() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
        sortPlayers();
        LeaderboardWriter.writeLeaderBoard(leaderboard); // Save the updated leaderboard data
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
}
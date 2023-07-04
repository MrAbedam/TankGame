package ir.ac.kntu.UI;


import ir.ac.kntu.Engine.GlobalConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard implements Serializable {
    public ArrayList<Player> players;

    public Leaderboard() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
        sortPlayers();
        LeaderboardWriter.writeLeaderBoard(GlobalConstants.leaderboard); // Save the updated leaderboard data
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void sortPlayers() {
        Collections.sort(players, Comparator.comparingInt(Player::getScore).reversed());
    }

    public void showLeaderboard() {
        System.out.println("Leaderboard:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getName() + " - " + player.getScore());
        }
    }
}
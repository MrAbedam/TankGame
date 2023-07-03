package ir.ac.kntu;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int score;
    private int numberOfGames;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
        this.numberOfGames = 1;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
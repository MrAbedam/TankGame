package ir.ac.kntu.UI;

import java.io.*;

import ir.ac.kntu.GameObjects.PlayerTank;
import ir.ac.kntu.UI.Leaderboard;

public class LeaderboardWriter implements Serializable{
    public static void writeLeaderBoard(Leaderboard leaderboard){
        File file = new File("leaderboard.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(leaderboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Leaderboard readLeaderBoard() {
        Leaderboard leaderboard = null;
        File file = new File("leaderboard.txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            leaderboard = (Leaderboard) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return leaderboard;
    }


}

package ir.ac.kntu.Engine;

import ir.ac.kntu.GameObjects.*;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static ir.ac.kntu.Engine.GlobalConstants.*;
import static ir.ac.kntu.Engine.GlobalConstants.root;
import static ir.ac.kntu.GameObjects.Wall.allWalls;

public class TankGameHelperClass {

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

    public static void makeMapFromFile() {
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
                    default -> {
                    }
                }
            }
        }
    }

    public static void addMetalWall(double x, double y) {
        MetalWall metalWall = new MetalWall(x, y);
        metalWall.addToPane();
    }

    public static void addSimpleWall(int x, int y) {
        SimpleWall simpleWall = new SimpleWall(x, y);
        simpleWall.addToPane();
    }

    public static void addArmoredTank(int x, int y, Pane root) {
        ArmoredTank t1 = new ArmoredTank(x, y);
        curArmored++;
        root.getChildren().add(t1.getTankImageView());
    }

    public static void addRegularTank(int x, int y, Pane root) {
        Tank t1 = new Tank(x, y);
        curRegular++;
        root.getChildren().add(t1.getTankImageView());
    }

    public static void addLuckyTank(int x, int y, Pane root) {
        LuckyTank t1 = new LuckyTank(x, y);
        curLucky++;
        root.getChildren().add(t1.getTankImageView());
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

}

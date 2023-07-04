package ir.ac.kntu.GameObjects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import junit.framework.TestCase;

import ir.ac.kntu.EnumsAndStates.GameState;
import javafx.scene.layout.Pane;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTankTest {

    @Test
    public void testHit() {
        Stage testaStage = new Stage();
        Pane testaRoot = new Pane();
        Scene testaScene = new Scene(testaRoot);
        PlayerTank playerTank = new PlayerTank(100, 100);

        playerTank.hit(testaRoot);
        assertEquals(1, playerTank.getBulletPower());


        assertEquals(2, playerTank.getHealth());


        playerTank.hit(testaRoot);
        playerTank.hit(testaRoot);
        assertEquals(0, testaRoot.getChildren().size());

    }

}






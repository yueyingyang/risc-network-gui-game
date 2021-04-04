package edu.duke.ece651.risc.shared.checker;


import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.SoldierEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SoldierRuleCheckerTest {
    @Test
    public void test_checkMyRule() {
        SoldierEntry upgrade1 = new SoldierEntry("1", "0", "2", 2, "player1");
        PlayerInfo player1 = new PlayerInfo("player1", 1, 20, 1000);
        Checker checker = new SoldierRuleChecker(null);
        V1MapFactory factory = new V1MapFactory();
        List<String> namelist = new ArrayList<>();
        namelist.add("player1");
        namelist.add("player2");
        GameMap map = factory.createMap(namelist, 2);
        Army army1 = new BasicArmy("player1", 2);
        map.getTerritory("1").setMyArmy(army1);
        // check tech level is not enough case
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade1, map, player1));
        PlayerInfo player2 = new PlayerInfo("2", 5, 20, 100);
        Army army2 = new BasicArmy("player1", 4);
        map.getTerritory("2").setMyArmy(army2);
        SoldierEntry upgrade2 = new SoldierEntry("1", "0", "3", 4, "1");
        // not enough tech resource case
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade2, map, player2));
        SoldierEntry upgrade3 = new SoldierEntry("1", "0", "2", 4, "1");
        // correct case
        assertDoesNotThrow(() -> checker.checkAction(upgrade3, map, player2));
    }
}
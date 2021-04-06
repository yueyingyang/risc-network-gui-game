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
    public void test_checkMyRule(){
        Checker checker=new SoldierRuleChecker(null);
        PlayerInfo player1 = new PlayerInfo("player1", 1, 20, 1000);
        PlayerInfo player2 = new PlayerInfo("player2", 5, 20, 100);
        V1MapFactory factory = new V1MapFactory();
        List<String> namelist = new ArrayList<>();
        namelist.add("player1");
        namelist.add("player2");
        GameMap map = factory.createMap(namelist, 2);
        Army army1 = new BasicArmy("player1", 2);
        map.getTerritory("1").setMyArmy(army1);
        Army army2 = new BasicArmy("player1", 4);
        map.getTerritory("2").setMyArmy(army2);
        // check tech level is not enough case
        SoldierEntry upgrade1=new SoldierEntry("1","0","2",2,"player1");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade1, map, player1));
        // not enough tech resource case
        SoldierEntry upgrade2 = new SoldierEntry("1", "0", "3", 4, "player1");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade2, map, player1));
        // correct case
        SoldierEntry upgrade3 = new SoldierEntry("2", "0", "2", 4, "player2");
        assertDoesNotThrow(() -> checker.checkAction(upgrade3, map, player2));
        // check toType less than fromType case
        SoldierEntry upgrade4 = new SoldierEntry("2","2","0",2,"player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade4, map, player2));
        // check negative soldier number
        SoldierEntry upgrade5 = new SoldierEntry("1","2","0",-2,"player1");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade5, map, player1));
        // check not the owner case
        SoldierEntry upgrade6 = new SoldierEntry("1","2","0",-2,"player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade6, map, player1));
        // check not enough soldier case
        SoldierEntry upgrade7 = new SoldierEntry("1","2","0",-2,"player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade7, map, player1));
        // check illegal from type
        SoldierEntry upgrade8 = new SoldierEntry("2", "-1", "2", 4, "player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade8, map, player2));
        // check illegal to type
        SoldierEntry upgrade9 = new SoldierEntry("2", "0", "7", 4, "player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade9, map, player2));
    }
}
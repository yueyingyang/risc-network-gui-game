package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.entry.ProdEntry;
import edu.duke.ece651.risc.shared.entry.SpyEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpyRuleCheckerTest {
    @Test
    public void test_checkMyRule(){
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        PlayerInfo info1 = new PlayerInfo("LiLei", 6, 280, 180);
        PlayerInfo info2 = new PlayerInfo("HanMeiMei",6,100,30);

        ActionEntry entry0 = new PlaceEntry("0", 8, "LiLei");
        ActionEntry entry1 = new PlaceEntry("2",5,"HanMeiMei");
        Territory terr0=myMap.getTerritory("0");
        terr0.addSoldiersToArmy(2,"1");
        //check upgrade spy other player's territory case
        ActionEntry entry2 = new SpyEntry("2", 3, "LiLei");
        //check not enough soldier case
        ActionEntry entry3 = new SpyEntry("0", 10, "LiLei");
        // check not enough tech resource case
        ActionEntry entry4 = new SpyEntry("2", 3, "HanMeiMei");
        // check normal case
        ActionEntry entry5 = new SpyEntry("0",5,"LiLei");

        entry0.apply(myMap, info1);
        entry1.apply(myMap, info2);

        Checker checker = new SpyRuleChecker(null);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry2,myMap,info1));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry3,myMap,info1));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry4,myMap,info2));
        assertDoesNotThrow(()->checker.checkAction(entry5,myMap,info1));
    }
}

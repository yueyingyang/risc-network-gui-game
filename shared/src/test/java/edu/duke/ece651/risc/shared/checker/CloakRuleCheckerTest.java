package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.CloakEntry;
import edu.duke.ece651.risc.shared.entry.CloakingTechEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloakRuleCheckerTest {
    @Test
    public void test_checkMyRUle(){
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "LiLei";
        String name1 = "HanMeiMei";
        String name2 = "WangBin";
        List<String> names = Arrays.asList(name0, name1, name2);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        PlayerInfo myInfo0 = new PlayerInfo(name0, 3, 280, 110);

        ActionEntry entry0 = new CloakingTechEntry(name0);
        entry0.apply(myMap, myInfo0);
        Checker checker = new ClockRuleChecker(null);
        // check invalid territory name
        ActionEntry entry1 = new CloakEntry("9", name0);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry1,myMap,myInfo0));
        // check other player's territory case
        ActionEntry entry2 = new CloakEntry("4", name0);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry2,myMap,myInfo0));
        // check cost not enough case
        ActionEntry entry3 = new CloakEntry("0",name0);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry3,myMap,myInfo0));
        // check normal case
        PlayerInfo myInfo2=new PlayerInfo(name0, 3, 280, 120);
        ActionEntry entry4 = new CloakEntry("0",name0);
        assertDoesNotThrow(()->checker.checkAction(entry4,myMap,myInfo2));
    }
}

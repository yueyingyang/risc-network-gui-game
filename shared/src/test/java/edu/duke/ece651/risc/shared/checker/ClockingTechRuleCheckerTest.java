package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.CloakingTechEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClockingTechRuleCheckerTest {
    @Test
    public void test_checkMyRule(){
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "LiLei";
        String name1 = "HanMeiMei";
        String name2 = "WangBin";
        List<String> names = Arrays.asList(name0, name1, name2);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        PlayerInfo myInfo0 = new PlayerInfo(name0, 3, 280, 1000);
        PlayerInfo myInfo1 = new PlayerInfo(name1, 2, 280, 1000);
        PlayerInfo myInfo2 = new PlayerInfo(name2, 3, 280, 10);

        Checker checker = new CloakingTechRuleChecker(null);
        // normal case
        ActionEntry entry0 = new CloakingTechEntry(name0);
        assertDoesNotThrow(()->checker.checkAction(entry0,myMap,myInfo0));
        // check tech level not enough case
        ActionEntry entry1 = new CloakingTechEntry(name1);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry1,myMap,myInfo1));
        ActionEntry entry2 = new CloakingTechEntry(name2);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry2,myMap,myInfo2));
    }
}

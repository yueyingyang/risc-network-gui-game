package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.entry.SpyEntry;
import edu.duke.ece651.risc.shared.entry.SpyMoveEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpyMoveRuleCheckerTest{
    @Test
    public void test_checkMyRule(){
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "LiLei";
        String name1 = "HanMeiMei";
        List<String> names = Arrays.asList(name0, name1);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        Territory terr1 = myMap.getTerritory("1");
        Territory terr2 = myMap.getTerritory("2");
        Territory terr3 = myMap.getTerritory("3");

        PlayerInfo myInfo = new PlayerInfo(name0, 1, 280, 1000);

        ActionEntry entry0 = new PlaceEntry("0", 12, name0);
        ActionEntry entry1 = new SpyEntry("0", 6, name0);

        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);

        Checker checker=new SpyMoveRuleChecker(null);
        // check non-adjacent territory
        ActionEntry entry3=new SpyMoveEntry("0","2",2,"LiLei");
        // check not enough soldier
        ActionEntry entry4=new SpyMoveEntry("0","1",8,"LiLei");
        // check invalid territory name
        ActionEntry entry5=new SpyMoveEntry("4","3",1,"LiLei");
        ActionEntry entry6=new SpyMoveEntry("0","-1",1,"LiLei");
        // check normal case
        ActionEntry entry7=new SpyMoveEntry("0","3",2,"LiLei");

        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry3,myMap,myInfo));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry4,myMap,myInfo));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry5,myMap,myInfo));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry6,myMap,myInfo));
        assertDoesNotThrow(()->checker.checkAction(entry7,myMap,myInfo));

    }
}

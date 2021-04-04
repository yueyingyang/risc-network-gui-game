package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.V1MapFactory;
import edu.duke.ece651.risc.shared.entry.TechEntry;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TechRuleCheckerTest {
    @Test
    public void test_checkMyRule(){
        TechEntry upgrade1=new TechEntry("1");
        PlayerInfo player1=new PlayerInfo("1",2,50,50);
        Checker checker=new TechRuleChecker(null);
        List<String> namelist=new ArrayList<>();
        namelist.add("1");
        namelist.add("2");
        V1MapFactory factory=new V1MapFactory();
        GameMap gamemap= factory.createMap(namelist,3);
        // check no enough tech resource case
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade1,gamemap,player1));
        TechEntry upgrade2=new TechEntry("2");
        PlayerInfo player2=new PlayerInfo("2",1,50,250);
        assertDoesNotThrow(() -> checker.checkAction(upgrade2,gamemap,player2));
        // check correct case
        upgrade2.apply(gamemap,player2);
        // check upgrade twice in one turn case
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(upgrade2,gamemap,player2));
    }
}

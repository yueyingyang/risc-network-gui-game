package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.MissileEntry;
import edu.duke.ece651.risc.shared.entry.ProdEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MissileRuleCheckerTest {
    @Test
    public void test_checkMyRule(){
        Checker checker=new ProdUseChecker(new MissileRuleChecker(null));
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("player1");
        nameList.add("player2");
        GameMap m=f.createMap(nameList,3);
        PlayerInfo myInfo1 = new PlayerInfo("player1", 6, 500, 420);
        PlayerInfo myInfo2 = new PlayerInfo("player2", 6, 500, 420);
        ActionEntry prod=new ProdEntry(Constant.missile,1);
        prod.apply(m,myInfo1);
        // test player name not consistent case
        ActionEntry entry1=new MissileEntry("4");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry1,m,myInfo2));
        // test territory name invalid case
        ActionEntry entry2=new MissileEntry("6");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry2,m,myInfo1));
        //test normal case
        ActionEntry entry3=new MissileEntry("4");
        assertDoesNotThrow(()->checker.checkAction(entry3,m,myInfo1));
        entry3.apply(m,myInfo1);
        // test missile is not enough case
        ActionEntry entry4=new MissileEntry("4");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry4,m,myInfo1));
    }
}

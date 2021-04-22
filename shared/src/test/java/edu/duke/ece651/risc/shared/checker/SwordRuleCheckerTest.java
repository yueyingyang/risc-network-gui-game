package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.ProdEntry;
import edu.duke.ece651.risc.shared.entry.ShieldEntry;
import edu.duke.ece651.risc.shared.entry.SwordEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SwordRuleCheckerTest {
    @Test
    public void test_applyMyRule(){
        Checker checker=new ProdUseChecker(new SwordRuleChecker(null));
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("player1");
        nameList.add("player2");
        GameMap m=f.createMap(nameList,2);
        PlayerInfo info1=new PlayerInfo("player1",5,100,100);
        PlayerInfo info2=new PlayerInfo("player2",5,100,100);
        ActionEntry prod=new ProdEntry(Constant.sword,1);
        prod.apply(m,info2);
        // test enemy's territory case
        ActionEntry entry1=new SwordEntry("2");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry1,m,info2));
        // test invalid territory name case
        ActionEntry entry2=new SwordEntry("4");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry2,m,info2));
        // test normal case
        ActionEntry entry3=new SwordEntry("1");
        assertDoesNotThrow(()->checker.checkAction(entry3,m,info2));
        entry3.apply(m,info2);
        // test not enough shield case
        ActionEntry entry4=new ShieldEntry("2");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry4,m,info1));
        ActionEntry entry5=new ShieldEntry("1");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry5,m,info2));
    }
}

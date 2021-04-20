package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.MissileRuleChecker;
import org.apache.tomcat.util.bcel.Const;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MissileEntryTest {
    @Test
    public void test_getter(){
        ActionEntry entry=new MissileEntry("1","player1");
        assertEquals("1",entry.getToName());
        assertEquals("player1", entry.getPlayerName());
    }

    @Test
    public void test_apply(){
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("player1");
        nameList.add("player2");
        GameMap m=f.createMap(nameList,3);
        ActionEntry entry=new MissileEntry("4","player1");
        PlayerInfo myInfo = new PlayerInfo("player1", 6, 500, 420);
        ActionEntry prod=new ProdEntry(Constant.missile,1);
        prod.apply(m,myInfo);
        Territory terr1=m.getTerritory("4");
        terr1.setMyArmy(new Army("player2",2,"2"));
        entry.apply(m,myInfo);
        assertEquals(terr1.getNumSoldiersInArmy(),0);
    }
}

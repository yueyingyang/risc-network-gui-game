package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SwordEntryTest {
    @Test
    public void test_apply(){
        ActionEntry entry1=new SwordEntry("2");
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("player1");
        nameList.add("player2");
        GameMap m=f.createMap(nameList,2);
        PlayerInfo info=new PlayerInfo("player1",5,100,100);
        ActionEntry prod=new ProdEntry(Constant.sword,1);
        prod.apply(m,info);
        Territory terr=m.getTerritory("2");
        assertEquals(terr.getAttackerBonus("player1"),0);
        entry1.apply(m,info);
        assertEquals(terr.getAttackerBonus("player1"),Constant.SWORD_BONUS);
    }
}

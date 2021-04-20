package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProdEntryTest {
    @Test
    public void test_apply(){
        ActionEntry action=new ProdEntry("SHIP",2);
        assertEquals(action.getNumProds(),2);
        assertEquals(action.getProdName(), Constant.ship);
        PlayerInfo myInfo=new PlayerInfo("LiLei",5,300,100);
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("LiLei");
        nameList.add("Tom");
        GameMap m=f.createMap(nameList,2);
        action.apply(m,myInfo);
        assertEquals(myInfo.getProdCount(Constant.ship),2);
        assertEquals(myInfo.getFoodResource(),200);
    }
}

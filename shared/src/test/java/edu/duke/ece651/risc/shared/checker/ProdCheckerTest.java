package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.V2MapFactory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.ProdEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProdCheckerTest {
    @Test
    public void test_checkMyRule(){
        ActionEntry action1=new ProdEntry("Missile",2);
        ActionEntry action2=new ProdEntry("MISSILE",-1);
        ActionEntry action3=new ProdEntry("SHIP",50);
        ActionEntry action4=new ProdEntry("SHIP",2);
        PlayerInfo myInfo=new PlayerInfo("LiLei",5,300,100);
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("LiLei");
        nameList.add("Tom");
        GameMap m=f.createMap(nameList,2);
        Checker checker=new ProdChecker(null);
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(action1,m,myInfo));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(action2,m,myInfo));
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(action3,m,myInfo));
        assertDoesNotThrow(()->checker.checkAction(action4,m,myInfo));
    }
}

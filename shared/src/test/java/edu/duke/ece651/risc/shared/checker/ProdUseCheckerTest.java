package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.V2MapFactory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.MissileEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProdUseCheckerTest {
    @Test
    public void test_checkMyRule(){
        AbstractMapFactory f=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("player1");
        nameList.add("player2");
        GameMap m=f.createMap(nameList,2);
        Checker checker=new ProdUseChecker(null);
        PlayerInfo info=new PlayerInfo("player1");
        ActionEntry entry1=new MissileEntry("5");
        ActionEntry entry2=new MissileEntry("2");
        assertThrows(IllegalArgumentException.class,()->checker.checkAction(entry1,m,info));
        assertDoesNotThrow(()->checker.checkAction(entry2,m,info));
    }
}

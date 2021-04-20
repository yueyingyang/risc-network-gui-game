package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Army;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.FancyMoveEntry;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FancyClientCheckerTest {
    @Test
    public void test_checkMyRule(){
        GameMap map1=createTestMap();
        Checker checker=new FancyClientChecker(null);
        PlayerInfo info1=new PlayerInfo("player1",2,1000,10);
        // check not enough army case
        ActionEntry move1=new FancyMoveEntry("1","3",1,"1", "player1");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move1,map1,info1));
        ActionEntry move2=new FancyMoveEntry("1","3",1,"0", "player1");
        assertDoesNotThrow(()->checker.checkAction(move2,map1,info1));
    }

    private GameMap createTestMap(){
        Territory t1=new Territory("1",5,0,0);
        Territory t2=new Territory("2",5,0,0);
        Territory t3=new Territory("3",5,0,0);
        Territory t4=new Territory("4",5,0,0);
        Territory t5=new Territory("5",5,0,0);
        Territory t6=new Territory("6",5,0,0);
        Territory t7=new Territory("7",5,0,0);
        Territory t8=new Territory("8",5,0,0);

        Map<String,Territory> territoryFinder=new HashMap<>();
        territoryFinder.put("1",t1);
        t1.setOwnerName("player1");
        territoryFinder.put("2",t2);
        t2.setOwnerName("player1");
        territoryFinder.put("3",t3);
        t3.setOwnerName("player1");
        territoryFinder.put("4",t4);
        t4.setOwnerName("player2");
        territoryFinder.put("5",t5);
        t5.setOwnerName("player2");
        territoryFinder.put("6",t6);
        t6.setOwnerName("player2");
        territoryFinder.put("7",t7);
        t7.setOwnerName("player1");
        territoryFinder.put("8",t8);
        t8.setOwnerName("player2");

        List<List<String>> connections=new ArrayList<>();
        for(int i=0;i<territoryFinder.size();i++){
            String name1=""+(i+1);
            String name2=""+((i+1)%territoryFinder.size()+1);
            connections.add(Arrays.asList(name1,name2));
        }

        for(String territoryName:territoryFinder.keySet()){
            Territory t=territoryFinder.get(territoryName);
            t.setMyArmy(new Army(t.getOwnerName(), 3));
        }

        return new GameMap(connections,territoryFinder);
    }
}

package edu.duke.ece651.risc.shared.checker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.duke.ece651.risc.shared.BasicArmy;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.ClientChecker;
import edu.duke.ece651.risc.shared.checker.FancyMoveRuleChecker;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.FancyMoveEntry;
import edu.duke.ece651.risc.shared.entry.MoveEntry;
import org.junit.jupiter.api.Test;

public class FancyMoveRuleCheckerTest {
    @Test
    public void test_checkMyRule() {
        GameMap map1=createTestMap();
        Checker checker=new FancyMoveRuleChecker(null);
        PlayerInfo info1=new PlayerInfo("player1",2,1000,10);
        PlayerInfo info2=new PlayerInfo("player2",2,10,10);
        // check move to other player's territory
        ActionEntry move1=new FancyMoveEntry("3","4",1,"0", "player1");
        // check move to itself
        ActionEntry move2=new FancyMoveEntry("1", "1", 2,"0", "player1");
        // check no path case
        ActionEntry move3=new FancyMoveEntry("1","7",1,"0", "player1");
        // normal case
        ActionEntry move4=new FancyMoveEntry("1","3",1,"0", "player1");
        // check not enough food resource case
        ActionEntry move5=new FancyMoveEntry("4","5",2,"0","player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move1,map1,info1));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move2,map1,info1));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move3,map1,info1));
        checker.checkAction(move4,map1,info1);
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move5,map1,info2));
    }

    @Test
    public void test_combinedRule(){
        GameMap map1=createTestMap();
        Checker checker=new ClientChecker(new FancyMoveRuleChecker(null));
        PlayerInfo info1=new PlayerInfo("player1",2,1000,10);
        PlayerInfo info2=new PlayerInfo("player2",2,10,10);
        // check move to other player's territory
        ActionEntry move1=new FancyMoveEntry("4", "3", 2, "0","player2");
        // check too many units
        ActionEntry move2=new FancyMoveEntry("2", "3", 7, "0","player1");
        // check illegal territory name
        ActionEntry move3=new FancyMoveEntry("a", "3", 7, "0","player1");
        // normal case
        ActionEntry move4=new FancyMoveEntry("1","3",1,"0", "player1");
        // check negative unit
        ActionEntry move5=new FancyMoveEntry("1","3",-1,"0","player1");
        // check move other player's unit
        ActionEntry move6=new FancyMoveEntry("5","6",1,"0","player1");
        // check not enough food resource case
        ActionEntry move7=new FancyMoveEntry("4","5",2,"0","player2");
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move1,map1,info2));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move2,map1,info1));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move3,map1,info1));
        checker.checkAction(move4,map1,info1);
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move5,map1,info1));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move6,map1,info1));
        assertThrows(IllegalArgumentException.class, () -> checker.checkAction(move7,map1,info2));
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
            t.setMyArmy(new BasicArmy(t.getOwnerName(), 3));
        }

        return new GameMap(connections,territoryFinder);
    }

}

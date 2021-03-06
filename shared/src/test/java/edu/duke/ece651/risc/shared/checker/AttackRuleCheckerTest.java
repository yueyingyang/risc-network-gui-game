package edu.duke.ece651.risc.shared.checker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.duke.ece651.risc.shared.Army;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.AttackEntry;
import org.junit.jupiter.api.Test;

public class AttackRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
    GameMap map1=createTestMap();
    Checker checker=new AttackRuleChecker(null);
    PlayerInfo info1=new PlayerInfo("player1");
    // check not adjacent case
    ActionEntry attack1=new AttackEntry("1", "4",  2,"player1");
    // check same owner case
    ActionEntry attack2=new AttackEntry("1","2",1, "player1");
    // check normal case
    ActionEntry attack3=new AttackEntry("3","4",1, "player1");
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack1,map1,info1));
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack2,map1,info1));
    checker.checkAction(attack3,map1,info1);
  }

  @Test
  public void test_combinedRule(){
    GameMap map1=createTestMap();
    Checker checker=new ClientChecker(new AttackRuleChecker(null));
    PlayerInfo info1=new PlayerInfo("player1");
    PlayerInfo info2=new PlayerInfo("player2");
    // check same owner case
    ActionEntry attack1=new AttackEntry("1", "2", 2, "player1");
    // check not enough arm available case
    ActionEntry attack2=new AttackEntry("4", "3", 7, "player2");
    //check no such territory case
    ActionEntry attack3=new AttackEntry("a", "3", 7, "player1");
    // check normal case
    ActionEntry attack4=new AttackEntry("3","4",1, "player1");
    // check negative unit
    ActionEntry attack5=new AttackEntry("3","4",-1, "player1");
    // check attack from other player's territoy
    ActionEntry attack6=new AttackEntry("3","4",1, "player2");
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack1,map1,info1));
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack2,map1,info2));
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack3,map1,info1));
    checker.checkAction(attack4,map1,info1);
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack5,map1,info1));
    assertThrows(IllegalArgumentException.class, () -> checker.checkAction(attack6,map1,info2));
  }

  private GameMap createTestMap(){
    Territory t1=new Territory("1");
    Territory t2=new Territory("2");
    Territory t3=new Territory("3");
    Territory t4=new Territory("4");
    Territory t5=new Territory("5");
    Territory t6=new Territory("6");
    Territory t7=new Territory("7");
    Territory t8=new Territory("8");

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

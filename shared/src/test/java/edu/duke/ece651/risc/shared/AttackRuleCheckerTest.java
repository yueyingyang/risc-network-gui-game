package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AttackRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
    GameMap map1=createTestMap();
    Checker checker=new AttackRuleChecker(null);
    ActionEntry attack1=new AttackEntry("1", "3", 2);
    checker.checkAction(attack1,map1);

  }

  @Test
  public void test_conbinedRule(){
    GameMap map1=createTestMap();
    Checker checker=new AttackRuleChecker(new ClientChecker(null));
    ActionEntry attack1=new AttackEntry("1", "3", 2);
    checker.checkAction(attack1,map1);
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
      String name2=""+((i+2)%territoryFinder.size()+1);
      connections.add(Arrays.asList(name1,name2));
    }
    
    for(String territoryName:territoryFinder.keySet()){
      Territory t=territoryFinder.get(territoryName);
      t.setMyArmy(new BasicArmy(t.getOwnerName(), 3));
    }

    return new GameMap(connections,territoryFinder);
  }

}

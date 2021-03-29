package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.V1MapFactory;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GameControllerTest {

  @Autowired
  private GameController controller;

//  @Test
//  void test_getTerrUnitList(){
//    List<TerrUnitList> list = controller.getTerrUnitList(createMap(), "test", 3);
////    option 0-3 included
//    assertEquals(4, list.get(0).getTerrUnitList().size());
//  }

  //  mock a map
  private GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    List<String> players = Arrays.asList("test", "p2");
    GameMap map = v1f.createMap(players, 2);
    return map;
  }
}
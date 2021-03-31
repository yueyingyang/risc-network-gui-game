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

  @Test
  void test_getTerrUnitList() {

  }

}
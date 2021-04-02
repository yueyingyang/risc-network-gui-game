package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.V1MapFactory;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
class GameControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private PlayerSocketMap playerMapping;

  @MockBean
  private UtilService util;

  ClientSocket cs = Mockito.mock(ClientSocket.class);
  private final JSONSerializer jsonSerializer = new JSONSerializer();

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void test_place() throws Exception {
//    given(playerMapping.getSocket("test")).willReturn(cs);
////    Recv 1. empty game map, 2. total unit number, 3. mapview.toString(false)
//    String mapStr = "{\"territoryFinder\":{\"0\":{\"name\":\"0\",\"ownerName\":\"p2\",\"myArmy\":null,\"neighbours\":[{\"name\":\"1\",\"ownerName\":\"p2\",\"myArmy\":null,\"neighbours\":[\"0\",{\"name\":\"2\",\"ownerName\":\"test\",\"myArmy\":null,\"neighbours\":[\"1\",{\"name\":\"3\",\"ownerName\":\"test\",\"myArmy\":null,\"neighbours\":[\"0\",\"2\"],\"attackerBuffer\":{}}],\"attackerBuffer\":{}}],\"attackerBuffer\":{}},\"3\"],\"attackerBuffer\":{}},\"1\":\"1\",\"2\":\"2\",\"3\":\"3\"}}";
//    String emptyView = "[{\"name\":\"0\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"1\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"2\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"},{\"name\":\"3\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"}]";
//    given(cs.recvMessage()).willReturn(mapStr)
//            .willReturn("1")
//            .willReturn(emptyView);
//
//    GameMap map = (GameMap) jsonSerializer.deserialize(mapStr, GameMap.class);
//    given(util.createTerrUnitList(map, "test")).willReturn(new TerrUnitList(Arrays.asList(new TerrUnit("p2", 0),
//            new TerrUnit("p2", 0),
//            new TerrUnit("test", 0),
//            new TerrUnit("test", 0))));
//    UtilService real = new UtilService();
//    given(util.deNodeList(emptyView)).willReturn(real.deNodeList(emptyView));
//    mvc.perform(MockMvcRequestBuilders
//            .get("/game/place"))
//            .andExpect(model().attribute("graphData", is(equalTo(real.deNodeList(emptyView)))))
//            .andExpect(model().attribute("wrapper", is(equalTo(real.createTerrUnitList(map, "test")))))
//            .andExpect(model().attribute("units", is(6)))
//            .andExpect(status().isOk());
  }

}
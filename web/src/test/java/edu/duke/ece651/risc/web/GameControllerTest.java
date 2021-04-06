package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
@ContextConfiguration
class GameControllerTest {
  @Autowired
  private MockMvc mvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private PlayerSocketMap playerMapping;

  @MockBean
  private UtilService util;

  private final JSONSerializer jsonSerializer = new JSONSerializer();

  public static RequestPostProcessor mockUser() {
    return user("user1").password("user1Pass").roles("USER");
  }
  @Before
  public void setup() {
    mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
  }

  ClientSocket cs = Mockito.mock(ClientSocket.class);

  @Test
  @Disabled
  void test_place() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
//    Recv 1. empty game map, 2. total unit number, 3. mapview.toString(false)
    String mapStr = "{\"territoryFinder\":{\"0\":{\"name\":\"0\",\"ownerName\":\"p2\",\"myArmy\":null,\"neighbours\":[{\"name\":\"1\",\"ownerName\":\"p2\",\"myArmy\":null,\"neighbours\":[\"0\",{\"name\":\"2\",\"ownerName\":\"test\",\"myArmy\":null,\"neighbours\":[\"1\",{\"name\":\"3\",\"ownerName\":\"test\",\"myArmy\":null,\"neighbours\":[\"0\",\"2\"],\"attackerBuffer\":{}}],\"attackerBuffer\":{}}],\"attackerBuffer\":{}},\"3\"],\"attackerBuffer\":{}},\"1\":\"1\",\"2\":\"2\",\"3\":\"3\"}}";
    String emptyView = "[{\"name\":\"0\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"1\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"2\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"},{\"name\":\"3\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"}]";
    given(cs.recvMessage()).willReturn(mapStr)
            .willReturn("1")
            .willReturn(emptyView);

    GameMap map = (GameMap) jsonSerializer.deserialize(mapStr, GameMap.class);

    UtilService real = new UtilService();
    TerrUnitList tul = real.createTerrUnitList(map, "test");
    Map<String, List<ObjectNode>> lon = real.deNodeList(emptyView);
    given(util.deNodeList(any())).willReturn(lon);
    given(util.createTerrUnitList(any(), any())).willReturn(tul);
    mvc.perform(MockMvcRequestBuilders
            .get("/game/place").with(mockUser()).with(csrf()))
            .andExpect(model().attribute("graphData", is(lon)))
            .andExpect(model().attribute("wrapper", is(tul)))
            .andExpect(model().attribute("units", is(1)))
            .andExpect(status().isOk());
  }

}
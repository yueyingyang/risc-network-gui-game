package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.V1MapFactory;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
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

  ClientSocket cs = Mockito.mock(ClientSocket.class);

  @Test
  void test_place() throws Exception {
//    given(playerMapping.getSocket("test")).willReturn(cs);
////    Recv 1. empty game map, 2. total unit number, 3. mapview.toString(false)
//    given(cs.recvMessage()).willReturn("").willReturn("1").willReturn("");
//    mvc.perform(MockMvcRequestBuilders
//            .get("/place"))
//            .andExpect(status().isOk())
//            .andExpect(model().attribute("graphData", is(equalTo())))
//            .andExpect(model().attribute("wrapper", is(equalTo())));
  }

}
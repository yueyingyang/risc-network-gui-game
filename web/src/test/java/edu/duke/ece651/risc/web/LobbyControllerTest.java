package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.game.GameInfo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LobbyController.class)
class LobbyControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private PlayerSocketMap playerMapping;

  ClientSocket cs = Mockito.mock(ClientSocket.class);

  @Test
  void test_enter_lobby() throws Exception {
    List<GameInfo> gameList = new ArrayList<>();
    JSONSerializer s = new JSONSerializer();
    gameList.add(new GameInfo(0, Arrays.asList("p1", "p2")));
    String listJSON = s.getOm().writerFor(new TypeReference<List<GameInfo>>() {
    }).writeValueAsString(gameList);
    given(cs.recvMessage()).willReturn(listJSON).willReturn(listJSON);
    given(playerMapping.getOneTimeSocket()).willReturn(cs);
    mvc.perform(MockMvcRequestBuilders
            .get("/lobby?name=p1"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("allJoinedGames", is(equalTo(gameList))))
            .andExpect(model().attribute("allOpenGames", is(equalTo(gameList))));
  }

//  Didn't figure out post req test yet, expected 200 but return 302
//  @Test
//  void test_start() throws Exception {
//    given(playerMapping.getSocket("test")).willReturn(cs);
////    Mockito.spy(cs).sendMessage(any());
//    mvc.perform(MockMvcRequestBuilders
//            .post("/start").param("size", "2"))
//            .andExpect(status().isOk());
//}
}
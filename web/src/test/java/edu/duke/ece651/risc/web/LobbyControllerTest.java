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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LobbyController.class)
class LobbyControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private PlayerSocketMap playerMapping;

  @Test
  void test_enter_lobby() throws Exception {
    ClientSocket cs = Mockito.mock(ClientSocket.class);
    List<GameInfo> gameList = new ArrayList<>();
    JSONSerializer s = new JSONSerializer();
    String listJSON = s.getOm().writerFor(new TypeReference<List<GameInfo>>() {
    }).writeValueAsString(gameList);
    gameList.add(new GameInfo(0, Arrays.asList("p1", "p2")));
    given(cs.recvMessage()).willReturn(listJSON).willReturn(listJSON);
    given(playerMapping.getOneTimeSocket()).willReturn(cs);
    mvc.perform(MockMvcRequestBuilders
            .get("/lobby?name=p1"))
            .andExpect(status().isOk());
  }
}
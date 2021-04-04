package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.game.GameInfo;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(LobbyController.class)
@ContextConfiguration
class LobbyControllerTest {
  @Autowired
  private WebApplicationContext context;

  @MockBean
  private PlayerSocketMap playerMapping;

  @Autowired
  private MockMvc mvc;

  ClientSocket cs = Mockito.mock(ClientSocket.class);

  @Before
  public void setup() {
    mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
  }

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
            .get("/lobby").with(mockUser()).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attribute("allJoinedGames", is(equalTo(gameList))))
            .andExpect(model().attribute("allOpenGames", is(equalTo(gameList))));
  }

  public static RequestPostProcessor mockUser() {
    return user("user1").password("user1Pass").roles("USER");
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
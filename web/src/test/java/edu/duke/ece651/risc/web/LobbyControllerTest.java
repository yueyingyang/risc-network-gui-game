package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

  public static RequestPostProcessor mockUser() {
    return user("user1").password("user1Pass").roles("USER");
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

  @Test
  void test_start() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    mvc.perform(MockMvcRequestBuilders
            .post("/start").param("size", "2").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("waiting"));
    Mockito.spy(cs).sendMessage("{\"type\":\"start\",\"name\":\"test\",\"gameSize\":\"2\"}");
  }

  @Test
  void test_join_succeed() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    given(cs.recvMessage()).willReturn(Constant.SUCCESS_NUMBER_CHOOSED);
    mvc.perform(MockMvcRequestBuilders
            .get("/join").param("id", "0").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("waiting"));
    Mockito.spy(cs).sendMessage("{\"type\":\"join\",\"name\":\"test\",\"gameID\":\"0\"}");
  }

  @Test
  void test_join_fail() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    given(cs.recvMessage()).willReturn("cannot join");
    mvc.perform(MockMvcRequestBuilders
            .get("/join").param("id", "0").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("back_lobby"));
    Mockito.spy(cs).sendMessage("{\"type\":\"join\",\"name\":\"test\",\"gameID\":\"0\"}");
  }

  @Test
  void test_rejoin_succeed() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    given(cs.recvMessage()).willReturn("can rejoin");
    mvc.perform(MockMvcRequestBuilders
            .get("/rejoin").param("id", "0").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("/game/play"));
    Mockito.spy(cs).sendMessage("{\"type\":\"rejoin\",\"name\":\"test\",\"gameID\":\"0\"}");
  }

  @Test
  void test_rejoin_fail() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    given(cs.recvMessage()).willReturn("cannot join");
    mvc.perform(MockMvcRequestBuilders
            .get("/join").param("id", "0").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("back_lobby"));
    Mockito.spy(cs).sendMessage("{\"type\":\"rejoin\",\"name\":\"test\",\"gameID\":\"0\"}");
  }

  @Test
  void test_waiting_room() throws Exception {
    mvc.perform(MockMvcRequestBuilders
            .get("/waiting").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(status().isOk());
  }

  @Test
  void test_exit_game() throws Exception {
    given(playerMapping.getSocket("user1")).willReturn(cs);
    mvc.perform(MockMvcRequestBuilders
            .get("/exit_game").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("back_lobby"));
    Mockito.spy(cs).sendMessage(Constant.DISCONNECT_GAME);
  }

  @Test
  void test_back_lobby() throws Exception {
    mvc.perform(MockMvcRequestBuilders
            .get("/back_lobby").with(mockUser()).with(csrf()))
            // will redirect to waiting room
            .andExpect(redirectedUrl("lobby"));
    Mockito.spy(playerMapping).removeUser("test");
  }
}
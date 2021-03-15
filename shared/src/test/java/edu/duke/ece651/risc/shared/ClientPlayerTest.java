package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientPlayerTest {
  ByteArrayOutputStream serverOut;
  ByteArrayOutputStream userOut;

  // run before each test to initialize outputstream
  @BeforeEach
  private void initEach() {
    serverOut = new ByteArrayOutputStream();
    userOut = new ByteArrayOutputStream();
  }

  @Test
  void test_constructor() throws IOException {
    ClientPlayer p = createClientPlayer("Hello Server", serverOut, "UserInput", userOut);
    assertEquals("Hello Server", p.recvMessage());
    p.sendMessage("Hello Client 1");
    assertEquals("Hello Client 1\n", serverOut.toString());
    assertEquals("UserInput", p.readFromUser());
    p.display("Display it on the client side program");
    assertEquals("Display it on the client side program\n", userOut.toString());
  }

  @Test
  void test_type_until_correct() throws IOException {
    ClientPlayer p = createClientPlayer("read from server\nIt's correct", serverOut, "read from user\n ohh\n", userOut);
    p.typeUntilCorrect("It's correct");
    assertEquals("read from server\n" +
            "It's correct\n", userOut.toString());
  }

  @Test
  void test_login_game_no_available_game() throws IOException {
    String serverIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED;
    String userIn = "3\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(serverIn + "\n", userOut.toString());
  }

  @Test
  void test_login_game_join_existed_game() throws IOException {
    String serverIn = Constant.ASK_START_NEW_OR_JOIN + "\n" + Constant.SUCCESS_ACTION_CHOOSED + "\n" + "fake list of games" + "\n" + Constant.SUCCESS_NUMBER_CHOOSED;
    String userIn = "j\n1\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(serverIn + "\n", userOut.toString());
  }

  private ClientPlayer createClientPlayer(String serverIn, ByteArrayOutputStream serverOut, String userIn, ByteArrayOutputStream userOut) {
    return new ClientPlayer(new BufferedReader(new StringReader(serverIn)),
            new PrintWriter(serverOut, true),
            new BufferedReader(new StringReader(userIn)),
            new PrintStream(userOut));
  }

  @Test
  void test_get_name() {
    ClientPlayer p = createClientPlayer("",
            serverOut,
            "",
            userOut);
    p.setName("test_player");
    assertEquals("test_player", p.getName());
  }

  @Test
  void test_recv_map() throws JsonProcessingException {

    ClientPlayer p = createClientPlayer("",
            serverOut,
            "",
            userOut);
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    Iterable<Territory> ts = map.getPlayerTerritories("player1");
    Territory t1 = ts.iterator().next();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    List<ActionEntry> placement = new ArrayList<>();
    placement.add(new PlaceEntry("0", 1));
    placement.add(new PlaceEntry("1", 1));
    placement.add(new PlaceEntry("2", 1));
    placement.add(new PlaceEntry("3", 1));
//    List<PlaceEntry> listaction = objectMapper.readValue(pjson, new TypeReference<>(){});
    for (ActionEntry pe : placement) {
      pe.apply(map, null);
    }
    String mapjson = objectMapper.writeValueAsString(map);


    GameMap mafter = objectMapper.readValue(mapjson, GameMap.class);
//    map.getAllPlayerTerritories();
//    assertEquals("", new MapView(map).display());

  }
}
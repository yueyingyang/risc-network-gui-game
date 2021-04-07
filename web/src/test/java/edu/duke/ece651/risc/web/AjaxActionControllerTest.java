package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.duke.ece651.risc.web.GameControllerTest.mockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(AjaxActionController.class)
@WebAppConfiguration
class AjaxActionControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private PlayerSocketMap playerMapping;

  @MockBean
  private UtilService util;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @BeforeEach
  public void mock() throws IOException {
    given(playerMapping.getSocket("user1")).willReturn(cs);
//    Recv 1. empty game map, 2. total unit number, 3. mapview.toString(false)
    given(cs.recvMessage()).willReturn("validation result")
            .willReturn("mapview");
    Map<String, List<ObjectNode>> lon = new HashMap<>();
    given(util.deNodeList(any())).willReturn(lon);
  }

  ClientSocket cs = Mockito.mock(ClientSocket.class);
  String resStr = "{\"valRes\":\"validation result\",\"winnerInfo\":null,\"graphData\":{},\"win\":false,\"playerInfo\":null}";

  @Test
  void test_attack() throws Exception {

    String inputJson = "{\"fromName\":\"0\",\"toName\":\"3\",\"fromType\":\"0\",\"soldierNum\":\"1\"}";
    MvcResult res = mvc.perform(MockMvcRequestBuilders
            .post("/attack").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(mockUser()).with(csrf()))
            .andReturn();
    assertEquals(resStr, res.getResponse().getContentAsString());
  }

  @Test
  void test_move() throws Exception {
    String inputJson = "{\"fromName\":\"0\",\"toName\":\"1\",\"fromType\":\"0\",\"soldierNum\":\"1\"}";
    MvcResult res = mvc.perform(MockMvcRequestBuilders
            .post("/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(mockUser()).with(csrf()))
            .andReturn();
    assertEquals(resStr, res.getResponse().getContentAsString());
  }

  @Test
  void test_upgrade_soldier() throws Exception {
    String inputJson = "{\"toName\":\"0\",\"fromType\":\"0\",\"toType\":\"1\",\"soldierNum\":\"1\"}";
    MvcResult res = mvc.perform(MockMvcRequestBuilders
            .post("/soldier").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(mockUser()).with(csrf()))
            .andReturn();
    assertEquals(resStr, res.getResponse().getContentAsString());
  }

  @Test
  void test_upgrade_tech() throws Exception {
    String inputJson = "";
    MvcResult res = mvc.perform(MockMvcRequestBuilders
            .post("/tech").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(mockUser()).with(csrf()))
            .andReturn();
//    commit only return null as body
    assertEquals("", res.getResponse().getContentAsString());
  }
}
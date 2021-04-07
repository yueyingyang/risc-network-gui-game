package edu.duke.ece651.risc.web.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActionAjaxResBodyTest {
  @Test
  void test_getter_setter() {
    ActionAjaxResBody res = new ActionAjaxResBody();
    String valRes = "action validation result";
    String winnerInfo = "winner info";
    Map<String, List<ObjectNode>> graphData = new HashMap<>();
    res.setValRes(valRes);
    res.setWinnerInfo(winnerInfo);
    res.setGraphData(graphData);
    res.setWin(true);
    assertEquals(winnerInfo, res.getWinnerInfo());
    assertEquals(graphData, res.getGraphData());
    assertEquals(valRes, res.getValRes());
    assertTrue(res.isWin());
  }
}
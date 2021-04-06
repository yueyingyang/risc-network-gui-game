package edu.duke.ece651.risc.web.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

/**
 * A class used as response body in action submission ajax call
 * <p>
 * valRes is the validation result.
 * graphData is the List<ObjectNode> to display map.
 */
public class ActionAjaxResBody {

  String valRes;
  String winnerInfo;
  Map<String, List<ObjectNode>> graphData;
  boolean win;

  String playerInfo;

  public boolean isWin() {
    return win;
  }

  public String getValRes() {
    return valRes;
  }

  public Map<String, List<ObjectNode>> getGraphData() {
    return graphData;
  }

  public void setValRes(String valRes) {
    this.valRes = valRes;
  }

  public void setGraphData(Map<String, List<ObjectNode>> graphData) {
    this.graphData = graphData;
  }

  public void setWin(boolean win) {
    this.win = win;
  }

  public String getWinnerInfo() {
    return winnerInfo;
  }

  public void setWinnerInfo(String winnerInfo) {
    this.winnerInfo = winnerInfo;
  }

  public String getPlayerInfo() {
    return playerInfo;
  }

  public void setPlayerInfo(String playerInfo) {
    this.playerInfo = playerInfo;
  }

  public ActionAjaxResBody() {
  }
}

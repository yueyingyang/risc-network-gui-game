package edu.duke.ece651.risc.web.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * A class used as response body in action submission ajax call
 *
 * valRes is the validation result.
 * graphData is the List<ObjectNode> to display map.
 */
public class ActionAjaxResBody {
  public void setValRes(String valRes) {
    this.valRes = valRes;
  }

  public void setGraphData(List<ObjectNode> graphData) {
    this.graphData = graphData;
  }

  String valRes;

  public String getValRes() {
    return valRes;
  }

  public List<ObjectNode> getGraphData() {
    return graphData;
  }

  List<ObjectNode> graphData;

  public ActionAjaxResBody() {
  }
}

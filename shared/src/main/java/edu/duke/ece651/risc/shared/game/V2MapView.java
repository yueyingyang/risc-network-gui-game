package edu.duke.ece651.risc.shared.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@JsonIgnoreProperties({ "jsonSerializer"})
public class V2MapView {
  private GameMap map;
  private JSONSerializer jsonSerializer;
  private Map<String, String> playerColorMap;
  private PlayerInfo playerInfo;

  private int radius;
  private Point middle;
  private double angleOffset;
  private double angleStart;
  

  public V2MapView(){this.jsonSerializer = new JSONSerializer();}

  public V2MapView(GameMap map, List<ServerPlayer> players, PlayerInfo playerInfo, Map<String, String> playerColorMap) {
    this.map = map;
    this.jsonSerializer = new JSONSerializer();
    this.playerInfo = playerInfo;
    this.playerColorMap = playerColorMap;

    int height = 400;
    int width = 400;
    this.radius = 30;
    this.middle = new Point(width / 2, height / 2);
    this.angleOffset = 360.0 / map.getAllTerritories().size();
    this.angleStart = 0;
  }

    /**
    * Helper function to make a point
    *
    * @param middle
    * @param angle
    * @param radius
    * @return
    */
  private Point calPoint(Point middle, double angle, int radius) {
    double radians = Math.toRadians(angle);
    int x = (int) (middle.x + radius * Math.cos(radians));
    int y = (int) (middle.y - radius * Math.sin(radians));
    return new Point(x, y);
  }


    /**
    * LINKS part in map view
    *
    * @return a list of pair of (source, target)
    */
  protected List<ObjectNode> createTerrEdge() {
    //JSONSerializer jsonSerializer = new JSONSerializer();
    List<ObjectNode> graphData = new ArrayList<>();
    for (Territory t : map.getAllTerritories()) {
      for (Territory neigh : t.getNeighbours()) {
        ObjectNode o = jsonSerializer.getOm().createObjectNode();
        o.put("source", t.getName());
        o.put("target", neigh.getName());
        graphData.add(o);
      }
    }
    return graphData;
  }


  /**
   * Convert GameMap to the JSON NODE to display map
   *
   * @return the MAP display info in JSON Node
   */
  // protected List<ObjectNode> createTerrNode(boolean full) {
  //   JSONSerializer jsonSerializer = new JSONSerializer();
  //   int height = 400;
  //   int width = 400;
  //   int radius = 30;
  //   Point middle = new Point(width / 2, height / 2);
  //   double angleOffset = 360.0 / map.getAllTerritories().size();
  //   double angleStart = 0;
  //   List<ObjectNode> graphData = new ArrayList<>();
  //   for (Territory t : map.getAllTerritories()) {
  //     Point point = calPoint(middle, angleStart, radius);
  //     ObjectNode o = jsonSerializer.getOm().createObjectNode();
  //     o.put("name", t.getName());
  //     o.put("owner", t.getOwnerName());
  //     o.put("value", t.getSize()); // hardcoded, need to change to t.size()
  //     o.put("color", playerColorMap.get(t.getOwnerName()));
  //     if (full) {
  //       o.put("foodProd", t.getFoodProd());
  //       o.put("techProd", t.getTechProd());
  //       for (int i = 0; i <= 6; i++) {
  //         o.put("unit" + i, t.getNumSoldiersInArmy(Integer.toString(i)));//t.getNumSoldiersInArmy(""+i)
  //       }
  //     }
  //     o.put("x", point.x);
  //     o.put("y", point.y);
  //     angleStart -= angleOffset;
  //     graphData.add(o);
  //   }
  //   return graphData;
  // }

  /**
   * DATA part in map view
   *
   * @param full is false if it's for the placement phase map
   * @return a list of territory node
   */
  protected List<ObjectNode> createTerrNode(boolean full) {
    //JSONSerializer jsonSerializer = new JSONSerializer();
    List<ObjectNode> graphData = new ArrayList<>();
    for (Territory t : map.getAllTerritories()) {
      ObjectNode o = jsonSerializer.getOm().createObjectNode();
      // 1. name and size are required
      o.put("name", t.getName());
      o.put("value", t.getSize());
      o.put("spy", t.getNumSpies(playerInfo.getName()));
      if (t.isVisible(playerInfo)) {
        // 2.1 show the realtime information
        fillTerrInfo(o, t, playerColorMap.get(t.getOwnerName()), full);
      } else if (playerInfo.getPrevSeenTerr(t.getName()) != null) {
        // 2.2 old information shown as gray
        fillTerrInfo(o, playerInfo.getPrevSeenTerr(t.getName()), "#808080", full);
      } else {
        // 2.3.= outline only
        o.put("isOutline", true);
        o.put("color", "#FFF");
      }
      // 3. position info
      putPos(calPoint(middle, angleStart, radius), o);
      angleStart -= angleOffset;
      graphData.add(o);
    }
    return graphData;
  }

  /**
   * Serialize object node to json string
   *
   * @param fullInfo is false if display the empty map (only owner and size...)
   *                 is true if display the full map
   * @return string is JSON string
   */
  public String toString(boolean fullInfo) {
    //JSONSerializer jsonSerializer = new JSONSerializer();
    HashMap<String, List<ObjectNode>> data = new HashMap<>();
    data.put("data", createTerrNode(fullInfo));
    data.put("links", createTerrEdge());
    data.put("playerInfo", createPlayerInfoNode());
    return jsonSerializer.serialize(data);
  }

  private List<ObjectNode> createPlayerInfoNode() {
    //JSONSerializer jsonSerializer = new JSONSerializer();
    List<ObjectNode> list = new ArrayList<>();
    ObjectNode o = jsonSerializer.getOm().createObjectNode();
    o.put("name", playerInfo.getName());
    o.put("techLevel", playerInfo.getTechLevel());
    o.put("foodRes", playerInfo.getFoodResource());
    o.put("techRes", playerInfo.getTechResource());
    o.put("isRequested", playerInfo.isRequested());
    list.add(o);
    return list;
  }


  /**
   * Helper method to fill in information into object node
   *
   * @param o     is object node to be filled in
   * @param t     is the territory to copy over
   * @param color is the color to fill in
   * @param full  is false during the placement phase, is true otherwise
   */
  private void fillTerrInfo(ObjectNode o, Territory t, String color, Boolean full) {
    o.put("owner", t.getOwnerName());
    o.put("color", color);
    if (full) {
      o.put("foodProd", t.getFoodProd());
      o.put("techProd", t.getTechProd());
      for (String s : GameUtil.getTypeList()) {
        o.put("unit" + s, t.getNumSoldiersInArmy(s));
      }
    }
  }

  private void putPos(Point point, ObjectNode o) {
    o.put("x", point.x);
    o.put("y", point.y);
  }
}




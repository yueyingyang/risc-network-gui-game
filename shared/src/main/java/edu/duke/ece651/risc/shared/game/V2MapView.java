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

  public V2MapView() {
    this.jsonSerializer = new JSONSerializer();
  }

  public V2MapView(GameMap map, List<ServerPlayer> players, PlayerInfo playerInfo, Map<String, String> playerColorMap) {
    this.map = map;
    this.jsonSerializer = new JSONSerializer();
    this.playerInfo = playerInfo;
    this.playerColorMap = playerColorMap;
    // position related fields
    int height = 400;
    int width = 400;
    this.radius = 30;
    this.middle = new Point(width / 2, height / 2);
    this.angleOffset = 360.0 / map.getAllTerritories().size();
    this.angleStart = 0;
  }

  /**
   * DATA part in map view
   *
   * @param full is false if it's for the placement phase map
   * @return a list of territory node
   */
  protected List<ObjectNode> createTerrNode(boolean full) {
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
        fillTerrInfo(o, playerInfo.getPrevSeenTerr(t.getName()), GameUtil.toHexStr(Color.gray), full);
      } else {
        // 2.3.= outline only
        o.put("isOutline", true);
        o.put("color", GameUtil.toHexStr(Color.white));
      }
      // 3. position info
      putPos(calPoint(middle, angleStart, radius), o);
      angleStart -= angleOffset;
      graphData.add(o);
    }
    return graphData;
  }

  /**
   * LINKS part in map view
   *
   * @return a list of pair of (source, target)
   */
  protected List<ObjectNode> createTerrEdge() {
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

  private List<ObjectNode> createPlayerInfoNode() {
    List<ObjectNode> list = new ArrayList<>();
    ObjectNode o = jsonSerializer.getOm().createObjectNode();
    o.put("name", playerInfo.getName());
    o.put("techLevel", playerInfo.getTechLevel());
    o.put("foodRes", playerInfo.getFoodResource());
    o.put("techRes", playerInfo.getTechResource());
    o.put("isRequested", playerInfo.isRequested());
    o.put("isResCloak", playerInfo.hasCloakingTech());
    for (String s : Constant.prodCost.keySet()) {
      o.put(s.toLowerCase(Locale.ROOT), playerInfo.getProdCount(s));
    }
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

  /**
   * Serialize object node to json string
   *
   * @param fullInfo is false if display the empty map (only owner and size...)
   *                 is true if display the full map
   * @return string is JSON string
   */
  public String toString(boolean fullInfo) {
    HashMap<String, List<ObjectNode>> data = new HashMap<>();
    data.put("data", createTerrNode(fullInfo));
    data.put("links", createTerrEdge());
    data.put("playerInfo", createPlayerInfoNode());
    data.put("myTerr", getTerrNameList(true));
    data.put("enemyTerr", getTerrNameList(false));
    return jsonSerializer.serialize(data);
  }

  /**
   * Wrap 2 lists for territory name dropdown
   *
   * @param mine is true then return my terr list
   * @return a list of object node, which just want to align with current mapview structure
   */
  private List<ObjectNode> getTerrNameList(boolean mine) {
    List<ObjectNode> list = new ArrayList<>();
    ObjectNode o = jsonSerializer.getOm().createObjectNode();
    if (mine) {
      // get my territory list
      for (Territory t : map.getPlayerTerritories(playerInfo.getName())) {
        o.put(t.getName(), "");
      }
    } else {
      // get all other enemy's territory names
      for (Territory t : map.getAllTerritories()) {
        if (!t.getOwnerName().equals(playerInfo.getName())) {
          o.put(t.getName(), "");
        }
      }
    }
    list.add(o);
    return list;
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
   * Helper function to fill in position info
   *
   * @param point
   * @param o
   */
  private void putPos(Point point, ObjectNode o) {
    o.put("x", point.x);
    o.put("y", point.y);
  }
}


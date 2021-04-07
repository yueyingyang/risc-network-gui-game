package edu.duke.ece651.risc.shared.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class V2MapView {
  private final GameMap map;
  private final JSONSerializer jsonSerializer;
  private final Map<String, String> playerColorMap;
  private final PlayerInfo playerInfo;

  public V2MapView(GameMap map, List<ServerPlayer> players, PlayerInfo playerInfo) {
    this.map = map;
    this.jsonSerializer = new JSONSerializer();
    this.playerInfo = playerInfo;
    this.playerColorMap = new HashMap<>();
    for (ServerPlayer p : players) {
      playerColorMap.put(p.getName(), "#" + Integer.toHexString(p.getColor().getRGB()).substring(2).toUpperCase(Locale.ROOT));
    }
  }

  private Point calPoint(Point middle, double angle, int radius) {
    double radians = Math.toRadians(angle);
    int x = (int) (middle.x + radius * Math.cos(radians));
    int y = (int) (middle.y - radius * Math.sin(radians));
    return new Point(x, y);
  }

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

  /**
   * Convert GameMap to the JSON NODE to display map
   *
   * @return the MAP display info in JSON Node
   */
  protected List<ObjectNode> createTerrNode(boolean full) {
    int height = 400;
    int width = 400;
    int radius = 30;
    Point middle = new Point(width / 2, height / 2);
    double angleOffset = 360.0 / map.getAllTerritories().size();
    double angleStart = 0;
    List<ObjectNode> graphData = new ArrayList<>();
    for (Territory t : map.getAllTerritories()) {
      Point point = calPoint(middle, angleStart, radius);
      ObjectNode o = jsonSerializer.getOm().createObjectNode();
      o.put("name", t.getName());
      o.put("owner", t.getOwnerName());
      o.put("value", t.getSize()); // hardcoded, need to change to t.size()
      o.put("color", playerColorMap.get(t.getOwnerName()));
      if (full) {
        o.put("foodProd", t.getFoodProd());
        o.put("techProd", t.getTechProd());
        for (int i = 0; i <= 6; i++) {
          o.put("unit" + i, t.getNumSoldiersInArmy(Integer.toString(i)));//t.getNumSoldiersInArmy(""+i)
        }
      }
      o.put("x", point.x);
      o.put("y", point.y);
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
    HashMap<String, List<ObjectNode>> data = new HashMap<>();
    data.put("data", createTerrNode(fullInfo));
    data.put("links", createTerrEdge());
    data.put("playerInfo", createPlayerInfoNode());
    return jsonSerializer.serialize(data);
  }

  private List<ObjectNode> createPlayerInfoNode() {
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
}


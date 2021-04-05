package edu.duke.ece651.risc.shared.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.Territory;

import java.util.*;
import java.util.List;

public class V2MapView {
  private final GameMap map;
  private final JSONSerializer jsonSerializer;
  private final Map<String, String> playerColorMap;

  public V2MapView(GameMap map, List<ServerPlayer> players) {
    this.map = map;
    this.jsonSerializer = new JSONSerializer();
    this.playerColorMap = new HashMap<>();
    for (ServerPlayer p : players) {
      playerColorMap.put(p.getName(), "#" + Integer.toHexString(p.getColor().getRGB()).substring(2).toUpperCase(Locale.ROOT));
    }
  }

  /**
   * Convert GameMap to the JSON NODE to display map
   *
   * @return the MAP display info in JSON Node
   */
  protected List<ObjectNode> createTerrNode(boolean full) {
    List<ObjectNode> graphData = new ArrayList<>();
    for (Territory t : map.getAllTerritories()) {
      ObjectNode o = jsonSerializer.getOm().createObjectNode();
      o.put("name", t.getName());
      o.put("owner", t.getOwnerName());
      o.put("value", t.getSize()); // hardcoded, need to change to t.size()
      o.put("color", playerColorMap.get(t.getOwnerName()));
      if (full) {
        //o.put("units", t.getNumSoldiersInArmy());
        o.put("foodProd", 0);
        o.put("techProd", 0);
        
        for(int i=0;i<=6;i++){
          o.put("unit"+i,i);//t.getNumSoldiersInArmy(""+i)
        }
        
      }
      graphData.add(o);
    }
    return graphData;
  }

  /**
   * Serialize object node to json string
   *
   * @param fullInfo is false if display the empty map (only owner and size...)
   *                 is true if display the full map
   *
   * @return string is JSON string
   */
  public String toString(boolean fullInfo) {
    try {
      return jsonSerializer.serializeList(createTerrNode(fullInfo), ObjectNode.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}


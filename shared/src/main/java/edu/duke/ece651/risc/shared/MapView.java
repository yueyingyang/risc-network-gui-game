package edu.duke.ece651.risc.shared;

import java.util.*;

public class MapView {
    GameMap toView;

    public MapView(GameMap toView) {
        this.toView = toView;
    }

    /**
     * MapView includes player's territories and neighbors with it
     * <p>
     * Formatted:
     * <p>
     * Green player:
     * -------------
     * 10 units in Narnia (next to: Elantris, Midkemia)
     * 12 units in Midkemia (next to: Narnia, Elantris, Scadrial, Oz)
     * 8 units in Oz (next to: Midkemia, Scadrial, Mordor, Gondor)
     *
     * @return a informational string
     */
    public String display() {
      return toDisplay(true);
    }

    /**
     * Only territory names and neighbors, without unit
     *
     * @return string
     */
    public String displayMapShape() {
      return toDisplay(false);
    }

    /**
     * Generate header string as format:
     * <p>
     * Green player:
     * -------------
     *
     * @param name is the name of the player
     * @return the table header
     */
    private String printHeader(String name) {
        return name + " player:\n" + "-------------\n";
    }

    /**
     * Generate neighbor string as format:
     * (next to: Roshar, Scadrial, Midkemia, Narnia)
     *
     * @param neighbours is the neighbors to display
     * @return the formatted string
     */
    private String printNeighbours(Iterable<Territory> neighbours) {
        // Extract all the names from Iterable of Territory, for joining them together
        List<String> territoriesName = new ArrayList<>();
        for (Territory t : neighbours) {
            territoriesName.add(t.getName());
        }
        return " (next to: " +
                String.join(", ", territoriesName) +
                ")";
    }

    private String toDisplay(boolean wUnitInfo){
      StringBuilder toDisplay = new StringBuilder();
      Map<String, Set<Territory>> playerTerritories = toView.getAllPlayerTerritories();

      // Iterate all players
      for (Map.Entry<String, Set<Territory>> player : playerTerritories.entrySet()) {
        // append header
        toDisplay.append(printHeader(player.getKey()));
        for (Territory t : player.getValue()) {
          // append # of territories
          if(wUnitInfo){
            toDisplay.append(t.getNumSoldiersInArmy()).append(" units in ");
          }
          toDisplay.append(t.getName());
          // append joined neighbors
          Set<Territory> validNeigh = t.getNeighbours();
          validNeigh.remove(t);
          toDisplay.append(printNeighbours(validNeigh));
          toDisplay.append(System.getProperty("line.separator"));
        }
        toDisplay.append(System.getProperty("line.separator"));
      }
      return toDisplay.toString();
    }
}


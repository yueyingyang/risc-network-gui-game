package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;

public class SpyMoveEntry extends BasicEntry {
    public SpyMoveEntry(String fromName, String toName, int numSoldiers,
                        String playerName, String fromType) {
        super(fromName, toName, numSoldiers, playerName, fromType, null);
    }

    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSpies(playerName, numSoldiers);
        toTerr.bufferSpies(playerName, numSoldiers);
    }
}

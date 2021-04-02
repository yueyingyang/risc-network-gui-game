package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.V2MapView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the class for multi thread used in handling incoming actions from players
 */
public class OneTurnThread extends Thread {
    private GameMap mapLocal;
    private GameMap gameMap;
    private Player player;
    // for playerInfo field
    private final List<ServerPlayer> playerList;

    /**
     * constructor
     *
     * @param g the gameMap of the game
     * @param p the player
     */
    public OneTurnThread(GameMap g, Player p, List<ServerPlayer> playerList) {
        this.gameMap = g;
        this.player = p;
        // Each player keep a local copy of game map, to update during one turn
        this.mapLocal = (GameMap) new JSONSerializer().clone(g, GameMap.class);
        // need to generate player info table later
        this.playerList = playerList;
    }

    /**
     * make changes to the map according to the players action
     * gameMap should be synchronized to avoid conflicts
     *
     * @param a is the player's action
     */
    private void applyMovement(ActionEntry a) {
        synchronized (gameMap) {
            try {
                a.apply(gameMap);
                // also apply on local copy
                a.apply(mapLocal);
                player.sendMessage(Constant.VALID_ACTION);
            } catch (Exception e) {
                player.sendMessage(e.getMessage());
            }
        }
        // send the updated map view
        player.sendMessage(new V2MapView(mapLocal, playerList).toString());
    }

    /**
     * recv the actions from the player until COMMIT
     */
    public void run() {
        JSONSerializer js = new JSONSerializer();
        //player.sendObject(gameMap);
        while (true) {
            try {
                String s = player.recvMessage();
                //check if the player has done with his orders
                if (s.equals(Constant.ORDER_COMMIT)) {
                    break;
                }
                ActionEntry action = (ActionEntry) js.deserialize(s, ActionEntry.class);
                applyMovement(action);
            } catch (IOException e) {
                System.out.println("receive message exception!\n" + e.getMessage());
            }
        }
    }
}










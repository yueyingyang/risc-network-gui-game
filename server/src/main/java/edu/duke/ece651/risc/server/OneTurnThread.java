package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.V2MapView;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * This is the class for multi thread used in handling incoming actions from players
 */
public class OneTurnThread implements Runnable {

    private GameMap mapLocal;
    private GameMap gameMap;
    private ServerPlayer player;
    // for playerInfo field
    private final List<ServerPlayer> playerList;
    PlayerInfo playerInfo;
    CyclicBarrier barrier;
    int gameID;
    private Map<String, String> playerColorMap;

    /**
     * the constructor of oneTurnThread
     * @param g
     * @param p
     * @param playerList
     * @param playerInfo
     * @param barrier
     * @param gameID
     */
    public OneTurnThread(GameMap g, ServerPlayer p, List<ServerPlayer> playerList, PlayerInfo playerInfo, CyclicBarrier barrier, int gameID) {
        this.gameMap = g;
        this.player = p;
        // Each player keep a local copy of game map, to update during one turn
        this.mapLocal = (GameMap) new JSONSerializer().clone(g, GameMap.class);
        // need to generate player info table later
        this.playerList = playerList;
        this.playerInfo = playerInfo;
        this.barrier = barrier;
        this.gameID = gameID;
        this.playerColorMap = new HashMap<>();
    }

    /**
     * the constroctor that add playerColorMap
     * @param g
     * @param p
     * @param playerList
     * @param playerInfo
     * @param barrier
     * @param gameID
     * @param playerColorMap
     */
    public OneTurnThread(GameMap g, ServerPlayer p, List<ServerPlayer> playerList, PlayerInfo playerInfo, CyclicBarrier barrier, int gameID, Map<String, String> playerColorMap) {
        this(g, p, playerList, playerInfo, barrier, gameID);
        this.playerColorMap = playerColorMap;
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
                JSONSerializer js = new JSONSerializer();
                PlayerInfo pi = (PlayerInfo)js.clone(playerInfo, PlayerInfo.class);
                // PlayerInfo pi = new PlayerInfo(playerInfo.getName(), playerInfo.getTechLevel(), playerInfo.getFoodResource(), playerInfo.getTechResource());
                a.apply(gameMap, playerInfo);
                // also apply on the local copy
                a.apply(mapLocal, pi);
                player.sendMessage(Constant.VALID_ACTION);
            } catch (Exception e) {
                player.sendMessage(e.getMessage());
            }
        }
        // send the updated map view
        player.sendMessage(new V2MapView(mapLocal, playerList, playerInfo, playerColorMap).toString(true));
    }

    /**
     * recv the actions from the player until COMMIT
     */
    public void run() {
        JSONSerializer js = new JSONSerializer();
        if (player.getCurrentGame() != gameID) {           
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        
        while (true) {
            try {
                String s = player.recvMessage();
                if (s == null) {
                    if (player.getCurrentGame() == gameID) {
                        player.setCurrentGameID(-1);
                    }
                    break;
                }
                //check if the player has done with his orders
                if (s.equals(Constant.ORDER_COMMIT)) {
                    break;
                }
                ActionEntry action = (ActionEntry) js.deserialize(s, ActionEntry.class);
                applyMovement(action);
            } catch (IOException e) {
                System.out.println("receive message exception!\n" + e.getMessage());
                break;
            }
        }
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}










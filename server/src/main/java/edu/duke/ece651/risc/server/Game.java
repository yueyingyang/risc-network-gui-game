
package edu.duke.ece651.risc.server;


import static edu.duke.ece651.risc.shared.Constant.COLORS;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.type.TypeReference;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.V2MapFactory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.V2MapView;


import java.util.stream.Collectors;


/**
 * Game class is responsible for the one game's play
 */
@JsonIgnoreProperties({ "threadPool"})
public class Game {
    private int gameID;
    private int playerNum;
    private ArrayList<ServerPlayer> players;
    private ArrayList<ServerPlayer> stillInPlayers;//players still didn't lose
    private ArrayList<ServerPlayer> stillWatchPlayers;//players stillIn with those who want to watch after losing
    private Map<String, PlayerInfo> allplayerInfo;
    private Map<String, V2MapView> allMapViews;
    private GameMap gameMap;
    //private Random myRandom;
    public Boolean isComplete;
    private ExecutorService threadPool;
    private Map<String, String> playerColorMap;


    public Game(){}

    /**
     * the construtor of the game
     *
     * @param playerNum is the required number of players in this game
     */
    public Game(int playerNum, int gameID) {
        this.gameID = gameID;
        this.playerNum = playerNum;
        this.players = new ArrayList<>();
        this.stillInPlayers = new ArrayList<>();
        this.stillWatchPlayers = new ArrayList<>();
        this.allplayerInfo = new HashMap<>();
        this.allMapViews = new HashMap<>();
        this.playerColorMap = new HashMap<>();
        //this.myRandom = new Random();
        this.isComplete = false;
        this.threadPool = Executors.newFixedThreadPool(5);
    }

    /**
     * get playerInfo by the player's name
     *
     * @param name
     * @return
     */
    @JsonIgnore
    public PlayerInfo getPlayerInfoByName(String name) {
        return allplayerInfo.get(name);
    }

    /**
     * get the current gameID
     *
     * @return the current gameID
     */
    public Integer getGameID() {
        return this.gameID;
    }

    /**
     * get all players' name in game
     *
     * @return the list of playerName
     */
    @JsonIgnore
    public List<String> getAllPlayers() {
        ArrayList<String> res = new ArrayList<String>();
        for (Player p : players) {
            res.add(p.getName());
        }
        return res;
    }

    /**
     * try to add one player to the game
     *
     * @return null if the player is successfully added into the game
     */
    public String addPlayer(ServerPlayer player) {
        if (this.isGameFull()) {
            // error information
            return "This game is full, please select another game from the available list.";
        }
        this.players.add(player);
        // assign a color to player based on the idx of the player in the list
        playerColorMap.put(player.getName(), toHexStr(Color.decode(COLORS[players.size() - 1])));
        return null;
    }

    /**
     * check if the game's player number limit has been reached
     *
     * @return true if no more players can be accepted
     * false if can accpet more players
     */
    public synchronized Boolean isGameFull() {
        return (this.players.size() == this.playerNum);
    }

    /**
     * get the required number of players in the game
     *
     * @return the number of players that can start a game
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * get the number of players currently waiting in game to start
     *
     * @return the num of players participated in the game in total
     */
    @JsonIgnore
    public int getPLayerInGameNum() {
        return this.players.size();
    }

    /**
     * check if a player exist in a game or not
     *
     * @param playerName is the name of the player
     * @return
     */
    public Boolean IsPlayerExist(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the map pf this game
     *
     * @return the map of this game
     */
    @JsonIgnore
    public GameMap getMap() {
        return this.gameMap;
    }


    /**
     * create the game map when all players are added into the game, i.e the game room is full
     *
     * @param territoriesPerPlayer
     * @return
     */
    public void makeMap(int territoriesPerPlayer) {
        V2MapFactory factory = new V2MapFactory();
        ArrayList<String> nameList = new ArrayList<String>();
        for (Player player : players) {
            nameList.add(player.getName());
        }
        this.gameMap = factory.createMap(nameList, territoriesPerPlayer);
    }

    /**
     * send object to all players in the player list
     *
     * @param o the object to send
     * @param p the player list
     */
    public void sendObjectToAll(Object o, ArrayList<ServerPlayer> p) {
        for (Player player : p) {
            try {
                player.sendObject(o);
            } catch (Exception e) {
            }
        }
    }

    /**
     * send string to all players in the player list
     *
     * @param s the message to send
     * @param p the player list
     */
    public void sendStringToAll(String s, ArrayList<ServerPlayer> p) {
        for (Player player : p) {
            try {
                player.sendMessage(s);
            } catch (Exception e) {
            }
        }
    }

    /**
     * place soldiers on the map according to the all players' input
     */
    public void placementPhase() throws IOException {
        JSONSerializer js = new JSONSerializer();
        for (Player player : players) {
            String s = player.recvMessage();
            Collection<ActionEntry> placements = js.getOm().readValue(s, new TypeReference<Collection<ActionEntry>>() {
            });
            for (ActionEntry placement : placements) {
                try {
                    placement.apply(gameMap, null);
                } catch (Exception e) {

                    System.out.println(e.getMessage());//should never reach here
                }
            }
        }
    }

    /**
     * This method will create a thread for each player to receive their actions
     * the move action and the move part in attack will be done immediately
     */
    public void receiveAndApplyMoves(ArrayList<ServerPlayer> connectePlayers) throws BrokenBarrierException, InterruptedException{
        //ArrayList<OneTurnThread> threads = new ArrayList<>();
        List<ServerPlayer> temp = connectePlayers.stream().filter(it -> stillInPlayers.contains(it)).collect(Collectors.toList());
        CyclicBarrier barrier = new CyclicBarrier(temp.size()+1);
        for (ServerPlayer player : temp) {
            threadPool.execute(new OneTurnThread(gameMap, player, players, allplayerInfo.get(player.getName()), barrier, gameID, playerColorMap));
        }
        barrier.await();
    }

    /**
     * do attacks according to the attack buffer
     *
     * @return the combat result string
     */
    public String doAttacks() {
        StringBuilder sb = new StringBuilder("");
        for (Territory t : gameMap.getAllTerritories()) {
            //resolve combats and create combat results
            sb.append(t.resolveCombat(new Random()));
        }
        return sb.toString();
    }

    /**
     * add one soldier to all territories after one turn
     */
    public void addSoldiersToAll(ArrayList<ServerPlayer> connectedPlayers) {
        HashSet<String> connectedNames = new HashSet<>();
        for (Player p : connectedPlayers) {
            connectedNames.add(p.getName());
        }
        for (Territory t : gameMap.getAllTerritories()) {
            if (connectedNames.contains(t.getOwnerName())) {
                t.addSoldiersToArmy(1);
            }
        }
    }

    /**
     * upgrade the tech level if requested
     */
    public void effectTechForStillIn(ArrayList<ServerPlayer> connectedPlayers) {
        for (ServerPlayer p : connectedPlayers) {
            allplayerInfo.get(p.getName()).effectTech();
        }
    }

    ArrayList<ServerPlayer> sendMap_GetConnectedPlayers() {
        //send map to players in the stillWatch list
        ArrayList<ServerPlayer> connectedPlayers = new ArrayList<>();
        for (ServerPlayer p : stillWatchPlayers) {
            if (p.getCurrentGame() == gameID) {
                p.sendMessage(allMapViews.get(p.getName()).toString(true));
                connectedPlayers.add(p);
            }
        }
        return connectedPlayers;
    }

    /**
     * all players play one turn
     *
     * @throws IOException
     */
    public void playOneTurn(ArrayList<ServerPlayer> connectedPlayers) throws IOException, BrokenBarrierException, InterruptedException {
        //create a thread for each player to type their actions until receive commit
        //for inactive players, do nothing, just like they drectly type in Commit
        receiveAndApplyMoves(connectedPlayers);
        //resolve all combats and send combat results to players still watch the game
        String combatResult = doAttacks();
        effectTechForStillIn(connectedPlayers);
        for (ServerPlayer p : connectedPlayers) {
            if (p.getCurrentGame() == gameID) p.sendObject(combatResult);
        }
    }

    /**
     * update the stillIn and stillWatch players lists after each round
     *
     * @throws IOException
     */
    public void updatePlayerLists(ArrayList<ServerPlayer> connectedPlayers) throws IOException {
        //update the stillWatch players list and the stillIn players list
        ArrayList<ServerPlayer> temp = new ArrayList<ServerPlayer>(stillInPlayers);
        ArrayList<ServerPlayer> losers = new ArrayList<>();
        for (ServerPlayer player : temp) {
            //if lost the game, the player can only watch or disconnect
            if (checkLost(player)) {
                //we will only send lose game info to who has just lost the game
                try {
                    player.sendMessage(Constant.LOSE_GAME);
                } catch (Exception e) {
                }
                //remove the lost player from stillIn
                stillInPlayers.remove(player);
                losers.add(player);
            }
            //for those who didn't lose, tell them to continue
            else {
                if(player.getCurrentGame().equals(this.gameID) && connectedPlayers.contains(player)){
                    try{player.sendMessage(Constant.CONTINUE_PLAYING);}catch(Exception e){}
                }
            }
        }

        // should end game as only winner left in the room
        if (stillInPlayers.size() == 1) {
            return;
        }

        for (ServerPlayer player : losers) {
            if (player.getCurrentGame() == gameID) {
                player.sendMessage(Constant.CONTINUE_PLAYING);
                //if receive disconnect, rmv from the watch game list
                String msg = player.recvMessage();
                if (msg != null && msg.equals(Constant.DISCONNECT_GAME)) {
                    stillWatchPlayers.remove(player);
                    player.closeSocket();
                }
            }
        }

    }

    /**
     * check whether a particular player has lost all his/her territories
     *
     * @param player
     * @return false if still has territories
     */
    Boolean checkLost(Player player) {
        if (!gameMap.getPlayerTerritories(player.getName()).iterator().hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * @return false if still needs more fights
     */
    Boolean checkWin() {
        if (this.gameMap.getAllPlayerTerritories().size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * close all sockets when the game ends
     */
    public void endGame() throws IOException {
        for (ServerPlayer p : stillWatchPlayers) {
            if (p.getCurrentGame() == gameID) {
                p.closeSocket();
            }
        }
    }

    /**
     * add resources to all territories
     */
    public void addResourcesToConnected(ArrayList<ServerPlayer> connectedPlayers) {
        for (ServerPlayer p : connectedPlayers) {
            Iterable<Territory> myTerrs = gameMap.getPlayerTerritories(p.getName());
            allplayerInfo.get(p.getName()).addResource(myTerrs);
        }
    }

    /**
     * send gameMap to all players and receive their placement using thread pool
     *
     * @param soldierNum is the num of soldier that one player own's
     */
    public void sendAndPlace(int soldierNum) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier barrier = new CyclicBarrier(players.size() + 1);
        for (ServerPlayer p : players) {
            threadPool.execute(new PlacementThread(soldierNum, this.gameMap, allMapViews.get(p.getName()), p, barrier));
        }
        barrier.await();
    }

    public synchronized void updateGamesCollection(){
    String s = serializer.serialize(this);
    
    Document document = new Document("gameID", g.getGameID()).append("description", s);
    gamesCollection.insertOne(document);
  }


    /**
     * after the game room's required number of people is reached, run the game
     *
     * @throws IOException
     */
    public void runGame(int TerritoryPerPlayer, int totalSoldiers, MongoCollection<Document> gamesCollection) throws IOException, InterruptedException, BrokenBarrierException {
        //copy players list for stillIn and stillWatch
        stillInPlayers = new ArrayList<>(players);
        stillWatchPlayers = new ArrayList<>(players);
        for (ServerPlayer p : players) {
            PlayerInfo pi = new PlayerInfo(p.getName());
            allplayerInfo.put(p.getName(), pi);
        }
        makeMap(TerritoryPerPlayer);
        for (ServerPlayer p : players) {
            V2MapView view = new V2MapView(this.gameMap, players, allplayerInfo.get(p.getName()), playerColorMap);
            allMapViews.put(p.getName(), view);
        }
        addResourcesToConnected(stillInPlayers);
        sendAndPlace(totalSoldiers);
        while (!Thread.currentThread().isInterrupted()) {
            ArrayList<ServerPlayer> connectedPlayers = sendMap_GetConnectedPlayers();
            //multi thread in this function to handle simultaneous input
            playOneTurn(connectedPlayers);
            //update stillIn and stillWatch players list
            updatePlayerLists(connectedPlayers);
            //add 1 soldier to all territories at the end of one turn;
            addSoldiersToAll(connectedPlayers);
            //add resources for all players
            addResourcesToConnected(connectedPlayers);
            //check if the game is over
            if (checkWin() == true) {
                this.isComplete = true;
                String winner = this.gameMap.getAllPlayerTerritories().keySet().iterator().next();
                sendStringToAll(Constant.GAME_OVER, stillWatchPlayers);
                sendStringToAll("The winner is " + winner, stillWatchPlayers);
                break;
            }
        }
        //close sockets
        endGame();
    }

    /**
     * Convert color object to hex string
     *
     * @param c is the color to convert
     * @return hex string
     */
    private String toHexStr(Color c) {
        return '#' + Integer.toHexString(c.getRGB()).substring(2).toUpperCase(Locale.ROOT);
    }

}



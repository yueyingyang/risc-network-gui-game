
package edu.duke.ece651.risc.server;
import java.util.*;
import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

import java.util.Collection;

/**
 * Game class is responsible for the one game's play
 */
public class Game {
    private int gameID;
    private int playerNum;
    private ArrayList<ServerPlayer> players;
    private ArrayList<ServerPlayer> stillInPlayers;//players still didn't lose
    private ArrayList<ServerPlayer> stillWatchPlayers;//players stillIn with those who want to watch after losing
    private GameMap gameMap;
    private Random myRandom;
    private int randomSeed;


    /**
     * the construtor of the game
     *
     * @param playerNum is the required number of players in this game
     */
    public Game(int playerNum,int gameID,int r) {
        this.gameID = gameID;
        this.playerNum = playerNum;
        this.players = new ArrayList<>();
        this.stillInPlayers = new ArrayList<>();
        this.stillWatchPlayers = new ArrayList<>();
        this.randomSeed = r;
        this.myRandom = new Random(randomSeed);
    }

    public Integer getGameID(){
        return this.gameID;
    }

    public List<String> getAllPlayers(){
        ArrayList<String> res = new ArrayList<String>();
        for(Player p:players){
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
        return null;
    }

    /**
     * check if the game's player number limit has been reached
     *
     * @return true if no more players can be accepted
     * false if can accpet more players
     */
    public Boolean isGameFull() {
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
    public int getPLayerInGameNum() {
        return this.players.size();
    }

    /**
     * check if a player exist in a game or not
     * @param playerName is the name of the player
     * @return
     */
    public Boolean IsPlayerExist(String playerName){
        for(Player p:players){
            if(p.getName().equals(playerName)){
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
        V1MapFactory factory = new V1MapFactory();
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
            try{player.sendObject(o);}catch(Exception e){}        
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
            try{player.sendMessage(s);}catch(Exception e){} 
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
    public void receiveAndApplyMoves() {
        ArrayList<OneTurnThread> threads = new ArrayList<>();
        for (ServerPlayer player : stillInPlayers) {
            //if the player is still active in this game
            if(player.getCurrentGame().equals(gameID)){
                OneTurnThread thread = new OneTurnThread(gameMap, player);
                threads.add(thread);
                thread.start();
            }
        }
        for (OneTurnThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("catch interruptException!\n" + e.getMessage());
            }
        }
    }

    /**
     * do attacks according to the attack buffer
     *
     * @return the combat result string
     */
    public String doAttacks() {
        StringBuilder sb = new StringBuilder("The combat results are:\n");
        for (Territory t : gameMap.getAllTerritories()) {
            //resolve combats and create combat results
            sb.append(t.resolveCombat(myRandom));
        }
        return sb.toString();
    }

    /**
     * add one soldier to all territories after one turn
     */
    public void addSoldiersToAll() {
        for (Territory t : gameMap.getAllTerritories()) {
            t.addSoldiersToArmy(1);
        }
    }

    /**
     * all players play one turn
     *
     * @throws IOException
     */
    public void playOneTurn() throws IOException {
        //send map to players in the stillWatch list
        for (Player p : stillWatchPlayers) {
            try{p.sendObject(gameMap);}catch(Exception e){}
        }
        //create a thread for each player to type their actions until receive commit
        //for inactive players, do nothing, just like they drectly type in Commit
        receiveAndApplyMoves();
        //resolve all combats and send combat results to players still watch the game
        String combatResult = doAttacks();
        sendObjectToAll(combatResult, stillWatchPlayers);
    }

    /**
     * update the stillIn and stillWatch players lists after each round
     *
     * @throws IOException
     */
    public void updatePlayerLists() throws IOException {
        //update the stillWatch players list and the stillIn players list
        ArrayList<ServerPlayer> temp = new ArrayList<ServerPlayer>(stillInPlayers);
        ArrayList<ServerPlayer> losers = new ArrayList<>();
        for (ServerPlayer player : temp) {
            //if lost the game, the player can only watch or disconnect
            if (checkLost(player)) {
                //we will only send lose game info to who has just lost the game
                try{player.sendMessage(Constant.LOSE_GAME);}catch(Exception e){}               
                //remove the lost player from stillIn
                stillInPlayers.remove(player);
                losers.add(player);
            }
            //for those who didn't lose, tell them to continue
            else {
                try{player.sendMessage(Constant.CONTINUE_PLAYING);}catch(Exception e){}               
            }
        }

        // should end game as only winner left in the room
        if(stillInPlayers.size() == 1){
            return;
        }

        for(ServerPlayer player : losers){
            if(player.getCurrentGame()==gameID){
                player.sendMessage(Constant.CONTINUE_PLAYING);
                //if receive disconnect, rmv from the watch game list
                if (player.recvMessage().equals(Constant.DISCONNECT_GAME)) {
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
    public void endGame() throws IOException{
        for (ServerPlayer p : stillWatchPlayers) {
            if(p.getCurrentGame()==gameID){
                p.closeSocket();
                p.setCurrentGameID(2);
            }
        }
    }

    /**
     * after the game room's required number of people is reached, run the game
     *
     * @throws IOException
     */
    public void runGame(int TerritoryPerPlayer, int totalSoldiers) throws IOException {
        //copy players list for stillIn and stillWatch
        stillInPlayers = new ArrayList<>(players);
        stillWatchPlayers = new ArrayList<>(players);
        makeMap(TerritoryPerPlayer);
        sendObjectToAll(this.gameMap, players);
        sendStringToAll(String.valueOf(totalSoldiers), players);
        placementPhase();
        while (true) {
            //multi thread in this function to handle simultaneous input
            playOneTurn();
            //update stillIn and stillWatch players list
            updatePlayerLists();
            //add 1 soldier to all territories at the end of one turn;
            addSoldiersToAll();
            //check if the game is over
            if (checkWin() == true) {
                String winner = this.gameMap.getAllPlayerTerritories().keySet().iterator().next();
                sendStringToAll(Constant.GAME_OVER, stillWatchPlayers);
                sendStringToAll("The winner is " + winner, stillWatchPlayers);
                break;
            }
        }
        //close sockets
        endGame();
    }

}



package edu.duke.ece651.risc.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.*;

import java.util.Collection;

/**
 * Game class is responsible for the one game's play
 */
public class Game {
    private final int playerNum;
    private final ArrayList<ServerPlayer> players;
    private ArrayList<ServerPlayer> stillInPlayers;//players still didn't lose
    private ArrayList<ServerPlayer> stillWatchPlayers;//players stillIn with those who want to watch after losing
    private final HashSet<String> colorSet;
    private GameMap gameMap;
    private Random myRandom;

    /**
     * the construtor of the game
     *
     * @param playerNum is the required number of players in this game
     */
    public Game(int playerNum) {
        this.playerNum = playerNum;
        this.players = new ArrayList<>();
        this.stillInPlayers = new ArrayList<>();
        this.stillWatchPlayers = new ArrayList<>();
        this.colorSet = new HashSet<>();
        makeColors();
        this.myRandom = new Random(1);
    }

    /**
     * make a color set at the beginning of the game
     */
    private void makeColors() {
        this.colorSet.add("Red");
        this.colorSet.add("Orange");
        this.colorSet.add("Green");
        this.colorSet.add("Blue");
        this.colorSet.add("Purple");
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
        // add successfully
        String playerColor = colorSet.iterator().next();
        player.setName(playerColor);
        colorSet.remove(playerColor);
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
        return (this.players.size() >= this.playerNum);
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
     * after creating the map, assign territorities to players
     * each player will have territoriesPerPlayer territories
     *
     * @param territoriesPerPlayer
     */
    public void assignTerritories(int territoriesPerPlayer) {
        ArrayList<Territory> territories = this.getMap().getAllTerritories();
        for (int i = 0; i < players.size(); i++) {
            String playerName = players.get(i).getName();
            for (int j = 0; j < territoriesPerPlayer; j++) {
                territories.get(i * territoriesPerPlayer + j).setOwnerName(playerName);
            }
        }
    }


    /**
     * send object to all players in the player list
     *
     * @param o the object to send
     * @param p the player list
     */
    public void sendObjectToAll(Object o, ArrayList<ServerPlayer> p) {
        for (Player player : p) {
            player.sendObject(o);
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
            player.sendMessage(s);
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
                    placement.apply(gameMap);
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
        for (Player player : stillInPlayers) {
            OneTurnThread thread = new OneTurnThread(gameMap, player);
            threads.add(thread);
            thread.start();
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
            p.sendObject(gameMap);
        }
        //create a thread for each player to type their actions until receive commit
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
                player.sendMessage(Constant.LOSE_GAME);
                //remove the lost player from stillIn
                stillInPlayers.remove(player);
                losers.add(player);
            }
            //for those who didn't lose, tell them to continue
            else {
                player.sendMessage(Constant.CONTINUE_PLAYING);
            }
        }

        // should end game as only winner left in the room
        if(stillInPlayers.size() == 1){
            for(Player p : losers){
                p.sendMessage("Game ends");
            }
            return;
        }

        for(ServerPlayer player : losers){
          player.sendMessage(Constant.CONTINUE_PLAYING);
          //if receive disconnect, rmv from the watch game list
          if (player.recvMessage().equals(Constant.DISCONNECT_GAME)) {
            stillWatchPlayers.remove(player);
            player.closeSocket();
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
    public void endGame() {
        for (ServerPlayer p : stillWatchPlayers) {
            p.closeSocket();
        }
    }

    /**
     * after the game room's required number of people is reached, run the game
     *
     * @throws IOException
     */
    public void runGame() throws IOException {
        if (players.size() == playerNum) {
            //copy players list for stillIn and stillWatch
            stillInPlayers = new ArrayList<>(players);
            stillWatchPlayers = new ArrayList<>(players);
            int TerritoryPerPlayer = 1;//assume that one player has three territories
            int totalSoldiers = 1;//assume that each player have 12 soldiers in total
            makeMap(TerritoryPerPlayer);
            assignTerritories(TerritoryPerPlayer);
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

}



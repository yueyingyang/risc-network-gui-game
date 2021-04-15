/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.*;


public class App {
  public ArrayList<Game> games;
  private final ServerSocket hostSocket;
  private final PrintStream output;
  private Map<String, ServerPlayer> players;
  private Map<String, actionHandler> actionHandlerMap;
  private ExecutorService threadPool;

  /**
   * the constructor of App build the socket based on the port number initialize
   * the games list
   */
  public App(ServerSocket s, PrintStream out) {
    games = new ArrayList<>();
    this.output = out;
    this.hostSocket = s;
    this.players = new HashMap<>();
    this.actionHandlerMap = new HashMap<>();
    this.threadPool = Executors.newFixedThreadPool(10);
    actionHandlerMap.put(Constant.GET_GAMELIST, this::sendGameList);
    actionHandlerMap.put(Constant.STARTGAME, this::startNewGame);
    actionHandlerMap.put(Constant.JOINGAME, this::joinAndRun);
    actionHandlerMap.put(Constant.REJOINGAME, this::rejoinGame);
  }

  /**
   * All steps of the server side program
   */
  public void run() throws IOException{
    this.acceptPlayers(this.hostSocket);
    this.hostSocket.close();
  }

  /**
   * @return the list of available games for a player to join
   * should exclude the games that the player is already in
   */
  private List<Game> getAvailableGames(String playerName) {
    List<Game> res = new ArrayList<>();
    for (Game g : this.games) {
      if (!g.isGameFull() && g.IsPlayerExist(playerName).equals(false) && g.isComplete==false) {
        res.add(g);
      }
    }
    return res;
  }

  /**
   * @param playerName is the name of the player
   * @return the list of games that the player is in
   */
  private List<Game> getPlayerGame(String playerName) {
    List<Game> res = new ArrayList<>();
    for (Game g : this.games) {
      if (g.IsPlayerExist(playerName).equals(true)) {
        res.add(g);
      }
    }
    return res;
  }

  /**
   * this function send all open games and the gamesthat the player participated to the player
   *
   * @param player
   * @param playerName
   */
  public void sendGameList(ServerPlayer player, JsonNode rootNode, ArrayList<Game> games) {
    player.sendMessage(allGameList(rootNode.path("name").textValue()));
  }

  /**
   * @param playerName is the player's name
   */
  public String allGameList(String playerName) {
    List<GameInfo> allOpen = new ArrayList<>();
    for (Game g : this.getAvailableGames(playerName)) {
      allOpen.add(new GameInfo(g.getGameID(), g.getAllPlayers()));
    }
    List<GameInfo> allJoined = new ArrayList<>();
    for (Game g : this.getPlayerGame(playerName)) {
        if(g.isComplete==false){
          allJoined.add(new GameInfo(g.getGameID(), g.getAllPlayers()));
        }   
    }
    String res = null;
    try {
      res = new JSONSerializer().getOm().writeValueAsString(allOpen) + "\n" + new JSONSerializer().getOm().writeValueAsString(allJoined);
    } catch (JsonProcessingException ignored) {
    }
    return res;
  }


  /**
   * start a new game for a user
   *
   * @param player is the player needs to login
   * @throws IOException if R/W exception
   */
  public Game startNewGame(ServerPlayer player, JsonNode rootNode, ArrayList<Game> games) {
    int gameID = games.size();
    Game newGame = new Game(Integer.parseInt(rootNode.path("gameSize").textValue()), gameID);
    player.setCurrentGameID(gameID);
    games.add(newGame);
    // a new game should always add a player successfully
    // new game should assert newGame.addPlayer(player) == null;
    newGame.addPlayer(player);
    return newGame;
  }

  /**
   * let the player join into an existing game
   *
   * @param player is the player needs to login
   * @throws IOException if R/W exception
   */
  public Game joinExistingGame(ServerPlayer player, JsonNode rootNode){
    // wait util the user give a valid game number
    int gameID = Integer.parseInt(rootNode.path("gameID").textValue());
    while (true) {
      String tryAddPlayerErrorMsg = games.get(gameID).addPlayer(player);
      if (tryAddPlayerErrorMsg == null) {
        player.setCurrentGameID(gameID);
        player.sendMessage(Constant.SUCCESS_NUMBER_CHOOSED);
        return games.get(gameID);
      } else {
        // in multi-thread env,
        // it's possible to have another user fill in the selected game before current user
        // not figure out a good way to test it tho
        player.sendMessage(tryAddPlayerErrorMsg);
      }
    }
  }

  /**
   * join into the game and if the game has enough players, run it
   * @param player is the player that choose join game
   * @param rootNode
   * @throws IOException
   */
  public void joinAndRun(ServerPlayer player, JsonNode rootNode, ArrayList<Game> games){
    Game g = this.joinExistingGame(player, rootNode);
    if (g.isGameFull()) {
      Thread t = new Thread(() -> {
        try {
          g.runGame(2, 6);
        } catch (Exception e) {
          System.out.println("Exception catched when running the game!" + e.getMessage());
        }
      });
      t.start();
    }
  }

  /**
   * This function will let the player reconnect to the game
   *
   * @param player
   * @param n is the JSON Node recv from server
   */
  public void rejoinGame(ServerPlayer player, JsonNode n, ArrayList<Game> games) {
    Integer currentGameID = Integer.parseInt(n.path("gameID").textValue());
    if(games.get(currentGameID).checkWin().equals(true)){
      player.sendMessage(Constant.CANNOT_REJOINGAME);
      player.sendMessage(Constant.CANNOT_REJOINGAME_WIN);
      return;
    }
    if(games.get(currentGameID).checkLost(player).equals(true)){
      player.sendMessage(Constant.CANNOT_REJOINGAME);
      player.sendMessage(Constant.CANNOT_REJOINGAME_LOSE);
      return;
    }
    player.sendMessage(Constant.CAN_REJOINGAME);
    player.setCurrentGameID(currentGameID);
  }


  /**
   * This function will create a server player if he/she doesn't exist in the server side
   * or return the player from the player list if he/she has already exist
   * need to update the inputstream and outputstream
   *
   * @param playerName   is the name of the player
   * @param in           is the player's inputstream
   * @param out          is the player's outputstream
   * @param clientSocket is the player's socket
   * @return the unique serverplayer
   */
  public ServerPlayer createOrUpdatePlayer(String playerName, BufferedReader in, PrintWriter out, Socket clientSocket) {
    ServerPlayer player = null;
    if (!players.containsKey(playerName)) {
      player = new ServerPlayer(in, out, clientSocket);
      players.put(playerName, player);
      player.setName(playerName);
    } else {
      player = players.get(playerName);
      //update the player's inputstream and outputstream
      player.setInOut(in, out);
      player.setSocket(clientSocket);
    }
    return player;
  }


  /**
   * continuously accept connections and initialize players the player will be
   * asked whether he/she want to start a new game or join a game
   *
   * @param ss is the server socket for accepting connection
   */
  public void acceptPlayers(ServerSocket ss) {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        // accept a new connection and create a new player based on that
        Socket clientSocket = ss.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(in.readLine());//use readTree when not knowing the exact type of object
        String actionType = rootNode.path("type").textValue();
        String playerName = rootNode.path("name").textValue();
        ServerPlayer player = createOrUpdatePlayer(playerName, in, out, clientSocket);
        threadPool.execute(() -> {
          if (actionHandlerMap.containsKey(actionType)) {
              actionHandlerMap.get(actionType).apply(player, rootNode, this.games);        
        }
      });
        // if (actionHandlerMap.containsKey(actionType)) {
        //   actionHandlerMap.get(actionType).apply(player, rootNode);
        // }
      } catch (Exception e) {
        this.output.println(e.getMessage());
      }
    }
  }

  /**
   * The main function to run
   *
   * @param args is command line args which is [] for this program
   */
  public static void main(String[] args) throws IOException{
    App app = new App(new ServerSocket(4444), System.out);
    app.run();
  }

}

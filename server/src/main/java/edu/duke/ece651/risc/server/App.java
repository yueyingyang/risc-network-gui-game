/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.server;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.ServerPlayer;

public class App {
  ArrayList<Game> games;
  HostSocket hostSocket;
  BufferedReader input;

  /**
   * the constructor of App
   * build the socket based on the portnumber
   * initialize the games list
   */
  public App() {
    input = new BufferedReader(new InputStreamReader(System.in));
    games = new ArrayList<Game>();
    int portNumber = 4444;
    this.hostSocket = new HostSocket(portNumber);
  }

  private ArrayList<Game> getAvailablGames(){
    ArrayList<Game> res = new ArrayList<Game>(); 
    for (int i = 0; i < this.games.size(); i++) {
        if (this.games.get(i).isGameFull() == false) {
            res.add(this.games.get(i));
        }
      }
      return res;
  }

  /**
   * start a new game for a user
   * @param player
   * @throws IOException
   */
  public void startNewGame(Player player) throws IOException {
    player.sendMessage("Hi ^_^, We will create a new game for you. How many players do you want to have in your Game"
        + games.size() + "?");
        String s = player.recvMessage();
    while(true){
        Boolean isValid = true;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9' || s.charAt(i)<='0'){
                player.sendMessage("Please input a number between 1-9.");
                isValid = false;
            }
        }
        if(isValid==true){
            player.sendMessage("Success!");
            break;
        }
        else{
            s = player.recvMessage();
        }
    }
   
    int playerNum = Integer.parseInt(s);
    Game newGame = new Game(playerNum);   
    if (newGame.addPlayer(player).equals("")) {
      System.out.println(1);
      player.sendMessage(player.getName()); // send name to client player
      this.games.add(newGame);
    }
  }

  /**
   * let the player join into an existing game
   * @param player
   * @throws IOException
   */
  public void joinExistingGame(Player player) throws IOException {
    // send the available games to user to choose from
    StringBuilder sb = new StringBuilder("The available games here are: ");
    ArrayList<Game> availableGames = this.getAvailablGames();
    for (int i = 0; i < availableGames.size(); i++) {
        sb.append(Integer.toString(games.indexOf(availableGames.get(i))));
        sb.append(" ");
    }
    String availables = sb.toString();
    player.sendMessage(availables);
    // wait util the user give a valid game number
    while(true){
        int chosenGame = Integer.parseInt(player.recvMessage());
        if(chosenGame>=this.games.size()){
            player.sendMessage("You should only chose from the available list!");
            continue;
        }
        String msg = games.get(chosenGame).addPlayer(player);
        if (msg.equals("")) {
            player.sendMessage("Success!");
            player.sendMessage(player.getName()); // send name to client player
            break;
        }
        else{
            player.sendMessage(msg); 
        }
    }
    
  }

  /**
   * continously accept connections and initialize players
   * the player will be asked whether he/she want to start a new game or join a game
   * @throws IOException
   */
  public void acceptPlayers() throws IOException {
    while (true) {
      try {
        // accpet a new connection and create a new player based on that
        Socket clientSocket = this.hostSocket.getSocket().accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Player player = new ServerPlayer(in, out);
        // let the player choose whether to join a game or start a new one
        // when there are existing games
        if (this.getAvailablGames().size() > 0) {
            out.println("Hi, Do you want to start a new game(type s) or join an existing game(type j)?");
          while(true){           
            String action = in.readLine();
            if (action.equals("s")) {
                startNewGame(player);
                break;
            } else if (action.equals("j")) {
                joinExistingGame(player);
                break;
            }
            else{
                player.sendMessage("You should only input s/j");
            }
          }
          
        } else { // when there's no existing games  
          out.println("Hi, there's no available game in the system, so we will start a game for you.");
          startNewGame(player);
        }
      } catch (IOException e) {
        System.out.println("Exception caught when listening for a connection");
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * The main function to run
   * @param args
   */
  public static void main(String[] args) throws IOException {
    App myapp = new App();

    myapp.acceptPlayers();

    // close socket
    myapp.hostSocket.closeSocket();
  }

}

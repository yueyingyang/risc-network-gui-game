package edu.duke.ece651.risc.shared;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * ClientPlayer: Used on the client-side game
 */
public class ClientPlayer extends Player {
  /**
   * User's input and output stream
   */
  protected final BufferedReader userIn;
  protected final PrintStream userOut;

  /**
   * @param in      is the reader to game server.
   * @param out     is the writer to game server. PrintWriter is good for
   *                transmitting STRING.
   * @param userIn  is the user input stream.
   * @param userOut is the user output stream.
   */
  public ClientPlayer(BufferedReader in, PrintWriter out, BufferedReader userIn, PrintStream userOut) {
    super(in, out);
    this.userIn = userIn;
    this.userOut = userOut;
  }

  /**
   * @param s the string for the client to display
   */
  public void display(String s) {
    this.userOut.println(s);
  }

  /**
   * @return string read from stdin
   * @throws IOException if read/write exception
   */
  public String readFromUser() throws IOException {
    return this.userIn.readLine();
  }

  /**
   * Error handling for user's input:
   * continue to readinput until receive correctMsg
   * display correctMsg and go to next step after receive expected correctMsg
   *
   * @param correctMsg is the correct signal
   * @throws IOException if read/write exception
   */
  public void typeUntilCorrect(String correctMsg) throws IOException {
    sendMessage(readFromUser());
    String msg = recvMessage();
    while (!msg.equals(correctMsg)) {
      display(msg);
      sendMessage(readFromUser());
      msg = recvMessage();
    }
    display(msg);
  }

  public void loginGame() throws IOException {
    // ask the player whether she/he wants to start a name game or join a game
    // different prompt when there's no available games to join
    String str = this.recvMessage();
    this.display(str);
    if (!str.equals(Constant.NO_GAME_AVAILABLE_INFO)) {
      // the user send s or j from stdin
      this.typeUntilCorrect(Constant.SUCCESS_ACTION_CHOOSED);
      // user type in how many player do you want/the available games list
    }
    this.display(this.recvMessage());
    this.typeUntilCorrect(Constant.SUCCESS_NUMBER_CHOOSED);
    String name = this.recvMessage();
    this.setName(name);
  }
}

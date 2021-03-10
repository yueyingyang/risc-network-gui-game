package edu.duke.ece651.risc.shared;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;

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
     * @param out     is the writer to game server. PrintWriter is good for transmitting STRING.
     * @param userIn  is the user input stream.
     * @param userOut is the user output stream.
     */
    public ClientPlayer(BufferedReader in, PrintWriter out, BufferedReader userIn, PrintStream userOut) {
        super(in, out);
        this.userIn = userIn;
        this.userOut = userOut;
    }
}

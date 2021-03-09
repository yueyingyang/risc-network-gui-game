package edu.duke.ece651.risc.shared;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * ServerPlayer: Used on the server-side game
 *
 * Could add more features based on server-side needs
 */
public class ServerPlayer extends Player {
    public ServerPlayer(BufferedReader in, PrintWriter out) {
        super(in, out);
    }
}

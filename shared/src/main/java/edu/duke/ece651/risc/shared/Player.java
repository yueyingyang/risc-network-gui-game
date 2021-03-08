package edu.duke.ece651.risc.shared;

import java.io.*;

public abstract class Player {
    // IO stream with game server
    protected BufferedReader in;
    protected PrintWriter out;

    public Player(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String recvMessage() throws IOException {
        return in.readLine();
    }
}

package edu.duke.ece651.risc.shared;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


public class TextPlayer extends Player{
    public TextPlayer(BufferedReader in, PrintStream out) {
        super(in, out);
    }
}

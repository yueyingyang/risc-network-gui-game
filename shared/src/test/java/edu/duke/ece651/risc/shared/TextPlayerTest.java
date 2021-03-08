package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TextPlayerTest {
    @Test
    void test_main() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("Hello Client\n"));
        Player p = new TextPlayer(in, new PrintStream(bytes, true));
        assertEquals("Hello Client", p.recvMessage());
        p.sendMessage("Hello Server");
        assertEquals("Hello Server\n", bytes.toString());
    }
}
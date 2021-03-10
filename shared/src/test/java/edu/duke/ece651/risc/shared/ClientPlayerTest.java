package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientPlayerTest {
    @Test
    void test_constructor() throws IOException {
        BufferedReader in = new BufferedReader(new StringReader("Hello Server\n"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader userIn = new BufferedReader(new StringReader("UserInput\n"));
        ByteArrayOutputStream userOut = new ByteArrayOutputStream();
        Player p = new ClientPlayer(in, new PrintWriter(bytes, true), userIn, new PrintStream(userOut));
        assertEquals("Hello Server", p.recvMessage());
        p.sendMessage("Hello Client 1");
        assertEquals("Hello Client 1\n", bytes.toString());
    }
}
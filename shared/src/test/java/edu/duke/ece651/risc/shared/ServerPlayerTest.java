package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ServerPlayerTest {
    @Test
    void test_constructor() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("Hello Server\n"));
        Player p = new ServerPlayer(in, new PrintWriter(bytes, true));
        assertEquals("Hello Server", p.recvMessage());
        p.sendMessage("Hello Client 1");
        assertEquals("Hello Client 1\n", bytes.toString());
    }
}
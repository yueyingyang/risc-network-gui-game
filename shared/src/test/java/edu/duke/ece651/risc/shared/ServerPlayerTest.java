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

    @Test
    void test_read_action_type() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("a\ns\n"));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true));
        p.readActionType();
        String expected = Constant.ASK_START_NEW_OR_JOIN + "\n" +
                Constant.NOR_JOIN_START + "\n" +
                Constant.SUCCESS_ACTION_CHOOSED + "\n";
        assertEquals(expected, bytes.toString());
    }

    @Test
    void test_read_game_size() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("a\n10\n5\n"));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true));
        p.readGameSize();
        String expected = Constant.ASK_HOW_MANY_PLAYERS + "\n" +
                Constant.INVALID_NUMBER + "\n" +
                Constant.INVALID_NUMBER + "\n" +
                Constant.SUCCESS_NUMBER_CHOOSED + "\n";
        assertEquals(expected, bytes.toString());
    }
}
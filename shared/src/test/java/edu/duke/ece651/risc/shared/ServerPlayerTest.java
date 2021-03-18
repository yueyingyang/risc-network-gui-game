package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class ServerPlayerTest {
    @Test
    void test_constructor() throws IOException {
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("Hello Server\n"));
        Player p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        assertEquals("Hello Server", p.recvMessage());
        p.sendMessage("Hello Client 1");
        assertEquals("Hello Client 1\n", bytes.toString());
    }

    @Test
    void test_read_action_type() throws IOException {
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("a\ns\n"));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        p.readActionType();
        String expected = Constant.ASK_START_NEW_OR_JOIN + "\n" +
                Constant.NOR_JOIN_START + "\n" +
                Constant.SUCCESS_ACTION_CHOOSED + "\n";
        assertEquals(expected, bytes.toString());
    }

    @Test
    void test_read_game_size() throws IOException {
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("a\n10\n5\n"));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        p.readGameSize();
        String expected = Constant.ASK_HOW_MANY_PLAYERS + "\n" +
                Constant.INVALID_NUMBER + "\n" +
                Constant.INVALID_NUMBER + "\n" +
                Constant.SUCCESS_NUMBER_CHOOSED + "\n";
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void test_closeSocket(){
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader("a\n10\n5\n"));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        assertDoesNotThrow(()->{p.closeSocket();});
    }

    @Test
    public void test_sendObject(){
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        p.sendObject("Hi");
        assertEquals("\"Hi\"\n",bytes.toString());
    }
}
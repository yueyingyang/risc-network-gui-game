package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.awt.*;
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

    @Test
    public void test_setGetPlayerInfo() {
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        p.setName("Red");
        p.setPlayerInfo();
        assertEquals("Red", p.getPlayerInfo().getName());
        assertEquals(1, p.getPlayerInfo().getTechLevel());
    }

    @Test
    public void test_setGetCurrentGameID(){
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        p.setCurrentGameID(0);
        assertEquals(0, p.getCurrentGame());
    }

    @Test
    public void test_setGetColor(){
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        Color c = new Color(1,1,1);
        p.setColor(c);
        assertEquals(c, p.getColor());
    }

    @Test
    public void test_setInOut() throws IOException{
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
        BufferedReader in1 = new BufferedReader(new StringReader("123"));
        p.setInOut(in1,  new PrintWriter(bytes1, true));
        assertEquals("123", p.recvMessage());
    }

    @Test
    public void test_setSocket(){
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(""));
        ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
        Socket s1 = new Socket();
        p.setSocket(s1);
        assertDoesNotThrow(()->{p.closeSocket();});
    }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void test_client_socket_init() throws IOException {
        new Thread(() -> {
            try {
                TestServer server = new TestServer(4444);
                server.sendMessage("1");
                assertEquals("test message from Player1", server.recvMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        App.main(new String[0]);
    }
}

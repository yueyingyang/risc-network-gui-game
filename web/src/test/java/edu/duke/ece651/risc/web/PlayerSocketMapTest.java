package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.ClientSocket;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.ServerSocket;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"spring.application.server.hostname=localhost", "spring.application.server.port=6666"})
@SpringBootTest
class PlayerSocketMapTest {
  @Autowired
  private PlayerSocketMap playerSocketMap;

  @Test
  void test() throws IOException, InterruptedException {
    ServerSocket ss = new ServerSocket(6666);
    Thread t = new Thread(() -> {
      try {
        while (Thread.currentThread().isInterrupted()) {
          ss.accept();
        }
      } catch (NullPointerException | IOException ignored) {
      }
    });
    t.start();
    Thread.sleep(1000);
    ClientSocket c1 = playerSocketMap.getSocket("test1");
    assertSame(c1, playerSocketMap.getSocket("test1"));
    assertThat(playerSocketMap.getOneTimeSocket(), instanceOf(ClientSocket.class));
    t.interrupt();
    t.join();
  }

}
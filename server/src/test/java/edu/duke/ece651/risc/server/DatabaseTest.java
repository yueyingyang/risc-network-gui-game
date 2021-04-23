

package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import edu.duke.ece651.risc.shared.ServerPlayer;

public class DatabaseTest {

  @Test
  public void test_Database() {
      Database db = new Database();
      ServerPlayer sp = new ServerPlayer();
      sp.setName("test");
      Game g = new Game(2,0);
      db.insertPlayersCollection(sp);
      db.insertGamesCollection(g);
      ArrayList<Game> gl = db.recoverGameList();
      ArrayList<ServerPlayer> spl = db.recoverPlayerList();
      assertEquals(1, gl.size());
      assertEquals(1, spl.size());
  }


   

    
}


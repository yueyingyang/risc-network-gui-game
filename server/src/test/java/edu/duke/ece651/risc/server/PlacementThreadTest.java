package edu.duke.ece651.risc.server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.game.V2MapView;

public class PlacementThreadTest {
    @Test
    public void test_PlacementThreadRun()throws InterruptedException, JsonProcessingException{
        ActionEntry p0 = new PlaceEntry("0",0, "test");
        Collection<ActionEntry> ca = new ArrayList<ActionEntry>();
        ca.add(p0);
        JSONSerializer js = new JSONSerializer(); 
        Game game = new Game(2,0);        
        Socket s = new Socket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ServerPlayer sp = new ServerPlayer(new BufferedReader((new StringReader(js.getOm().writerFor(new TypeReference<List<ActionEntry>>() {}).writeValueAsString(ca)))),
            new PrintWriter(bytes, true),s);
        System.out.println(js.getOm().writerFor(new TypeReference<List<ActionEntry>>() {}).writeValueAsString(ca));
        sp.setCurrentGameID(0);
        sp.setName("test");
        game.addPlayer(sp);
        ArrayList<ServerPlayer> pl = new ArrayList<>();
        pl.add(sp);
        Map<String,String> colorMap = new HashMap<>();
        colorMap.put("test","Red");
        PlayerInfo pi = new PlayerInfo("test",0,0,0);
        game.makeMap(2);       
        V2MapView mv = new V2MapView(game.getMap(),pl,pi,colorMap);
        CyclicBarrier barrier = new CyclicBarrier(1);
        
        PlacementThread pth = new PlacementThread(6,game.getMap(),mv,sp,barrier);
        Thread t = new Thread(pth);
        t.start();
        // wait for "acceptPlayers" finishing
        Thread.sleep(2000);
        t.join();
        assertEquals(0, game.getMap().getTerritory("0").getNumSoldiersInArmy());
        
    }


}


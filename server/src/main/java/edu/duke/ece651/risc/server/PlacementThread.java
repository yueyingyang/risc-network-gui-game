package edu.duke.ece651.risc.server;

import java.util.Collection;
import java.util.concurrent.CyclicBarrier;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.V2MapView;
import edu.duke.ece651.risc.shared.PlayerInfo;

public class PlacementThread implements Runnable{
    private Integer totalSoldiers;
    private GameMap gameMap;
    private V2MapView view;
    private ServerPlayer p;
    private CyclicBarrier barrier;

    public PlacementThread(Integer soldierNum,GameMap gameMap,V2MapView view,ServerPlayer p, CyclicBarrier barrier){
        this.totalSoldiers = soldierNum;
        this.gameMap = gameMap;
        this.view = view;
        this.p = p;
        this.barrier = barrier;
    }

    public void run(){
        while(true){
            JSONSerializer js = new JSONSerializer();
            p.sendObject(this.gameMap);
            p.sendMessage(String.valueOf(totalSoldiers));
            p.sendMessage(view.toString(false));
            try{
                String s = p.recvMessage();
                Collection<ActionEntry> placements = js.getOm().readValue(s, new TypeReference<Collection<ActionEntry>>() {
                });
                int sum = 0;
                for (ActionEntry placement : placements){
                    sum += placement.getNumSoldiers();
                }
                if(sum>totalSoldiers){
                    p.sendMessage("invalid");
                    continue;
                }
                for (ActionEntry placement : placements) {
                    placement.apply(gameMap, null);
                }
                p.sendMessage("valid");
                break;         
            }catch(IOException e){
                System.out.println("Exception catched when recving message form player");
                e.printStackTrace();
            }       
        }
        try{
            barrier.await();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}

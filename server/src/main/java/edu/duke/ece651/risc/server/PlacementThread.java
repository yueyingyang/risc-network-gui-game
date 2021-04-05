package edu.duke.ece651.risc.server;

import java.util.Collection;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.V2MapView;

public class PlacementThread extends Thread{
    private Integer totalSoldiers;
    private GameMap gameMap;
    private V2MapView view;
    private ServerPlayer p;

    public PlacementThread(Integer soldierNum,GameMap gameMap,V2MapView view,ServerPlayer p){
        this.totalSoldiers = soldierNum;
        this.gameMap = gameMap;
        this.view = view;
        this.p = p;
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
            }       
        }

    }
    
}

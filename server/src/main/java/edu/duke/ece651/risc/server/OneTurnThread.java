package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.io.IOException;

/**
 * This is the class for multi thread used in handling incoming actions from players
 */
public class OneTurnThread extends Thread{
    private GameMap gameMap;
    private Player player;
    private Checker myChecker;


    /**
     * constructor
     * @param g the gameMap of the game
     * @param p the player
     */
    public OneTurnThread(GameMap g,Player p) {
        this.gameMap=g;
        this.player=p;
        this.myChecker = null;
    }

    /**
     * make changes to the map according to the players action
     * gameMap should be synchronized to avoid conflicts
     * @param a is the player's action
     */
    private void applyMovement(ActionEntry a){
        synchronized(gameMap){
            try{
                a.apply(gameMap, myChecker);
                player.sendMessage(Constant.VALID_ACTION);
            }catch(Exception e){
                player.sendMessage(Constant.INVALID_ACTION);
            }
        }
    }
    
    /**
     * recv the actions from the player until COMMIT
     */
    public void run(){
        JSONSerializer js=new JSONSerializer();
        player.sendObject(gameMap);
        while(true){
            try{
                String s = player.recvMessage();
                //check if the player has done with his orders
                if(s.equals(Constant.ORDER_COMMIT)){break;}
                ActionEntry action = (ActionEntry)js.deserialize(s, ActionEntry.class); 
                applyMovement(action);
            }catch(IOException e){
                System.out.println("receive message exception!\n"+e.getMessage());
            }          
        }
    }
}

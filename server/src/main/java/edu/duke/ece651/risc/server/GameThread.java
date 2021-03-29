package edu.duke.ece651.risc.server;

public class GameThread extends Thread{
    private Game game;

    public GameThread(Game g){
        this.game = g;
    }

    public void run(){
        try{
            this.game.runGame(2,6);
        }catch(Exception e){
            System.out.println("Exception catched when running the game!"+e.getMessage());
        }
        
    }
}

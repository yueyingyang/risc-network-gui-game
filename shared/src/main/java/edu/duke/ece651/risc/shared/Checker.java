package edu.duke.ece651.risc.shared;

public abstract class Checker {
  private final Checker next;

  public Checker(Checker next){
    this.next = next;
  }

  protected abstract void checkMyRule(Territory start, Territory end, GameMap map);
  
  public void checkAction(Territory start, Territory end, GameMap map){
    checkMyRule(start,end,map);
    if(next!=null){
      next.checkAction(start,end,map);
    }
  }
}
package edu.duke.ece651.risc.shared;

public abstract class Checker {
  private final Checker next;

  public Checker(Checker next){
    this.next = next;
  }

  protected abstract void checkMyRule(ActionEntry action, GameMap map);
  
  public void checkAction(ActionEntry action, GameMap map){
    checkMyRule(action,map);
    if(next!=null){
      next.checkAction(action,map);
    }
  }
}
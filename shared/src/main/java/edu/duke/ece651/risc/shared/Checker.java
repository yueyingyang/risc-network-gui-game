package edu.duke.ece651.risc.shared;

public abstract class Checker {
  private final Checker next;

  public Checker(Checker next){
    this.next = next;
  }

<<<<<<< HEAD
  protected abstract void checkMyRule(Territory start, Territory end, GameMap map);
  
  public void checkAction(Territory start, Territory end, GameMap map){
    checkMyRule(start,end,map);
    if(next!=null){
      next.checkAction(start,end,map);
=======
  protected abstract void checkMyRule(ActionEntry action, GameMap map);
  
  public void checkAction(ActionEntry action, GameMap map){
    checkMyRule(action,map);
    if(next!=null){
      next.checkAction(action,map);
>>>>>>> 124e13702c668145b5e1205eab27ca4f9d76880f
    }
  }
}
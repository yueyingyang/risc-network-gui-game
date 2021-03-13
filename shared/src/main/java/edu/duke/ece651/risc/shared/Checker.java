package edu.duke.ece651.risc.shared;

public abstract class Checker {
  private final Checker next;

  public Checker(Checker next){
    this.next=next;
  }

  protected abstract String checkMyRule();
  
  /*
  public String checkPlacement(Ship<T> theShip, Board<T> theBoard){
    //if we fail our own rule: stop the placement is not legal
    String message=checkMyRule(theShip, theBoard);
    if(message!=null){
      return message;
    }
    //otherwise, ask the rest of the chain
    if(next!=null){
      return next.checkPlacement(theShip,theBoard);
    }
    //if there are no more rules, then the placement is legal
    return null;
  }
  */

  
}
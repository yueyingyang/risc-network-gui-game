package edu.duke.ece651.risc.web.model;

/**
 * A class for wrapping user input
 */
public class UserActionInput {
  String toName;
  String fromName;
  int soldierNum;
  String soldierType;

  public void setToName(String toName) {
    this.toName = toName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public void setSoldierNum(String soldierNum) {
    this.soldierNum = Integer.parseInt(soldierNum);
  }

  public void setSoldierType(String soldierType) {
    this.soldierType = soldierType;
  }


  public String getToName() {
    return toName;
  }

  public String getFromName() {
    return fromName;
  }

  public int getSoldierNum() {
    return soldierNum;
  }

  public String getSoldierType() {
    return soldierType;
  }

}

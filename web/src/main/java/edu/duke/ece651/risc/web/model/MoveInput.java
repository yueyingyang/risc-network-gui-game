package edu.duke.ece651.risc.web.model;

/**
 * A class for wrapping user input
 */
public class MoveInput {
  String toName;
  String fromName;

  public void setToName(String toName) {
    this.toName = toName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public void setSoldierNum(String soldierNum) {
    this.soldierNum = soldierNum;
  }

  public void setSoldierType(String soldierType) {
    this.soldierType = soldierType;
  }

  String soldierNum;

  public String getToName() {
    return toName;
  }

  public String getFromName() {
    return fromName;
  }

  public String getSoldierNum() {
    return soldierNum;
  }

  public String getSoldierType() {
    return soldierType;
  }

  String soldierType;
}

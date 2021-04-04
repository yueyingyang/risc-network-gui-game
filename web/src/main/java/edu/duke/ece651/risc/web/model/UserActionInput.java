package edu.duke.ece651.risc.web.model;

/**
 * A class for wrapping user input
 */
public class UserActionInput {
  String fromName;
  String toName;
  int soldierNum;
  String fromType;
  String toType;
  

  public void setToName(String toName) {
    this.toName = toName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public void setSoldierNum(String soldierNum) {
    this.soldierNum = Integer.parseInt(soldierNum);
  }

  public void setFromType(String fromType) {
    this.fromType = fromType;
  }

  public void setToType(String toType) {
    this.toType = toType;
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

  public String getFromType() {
    return fromType;
  }

  public String getToType() {
    return toType;
  }
}

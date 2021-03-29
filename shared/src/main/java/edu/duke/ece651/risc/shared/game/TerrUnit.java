package edu.duke.ece651.risc.shared.game;

/**
 * territory name and unit
 * for spring view to submit placement info
 */
public class TerrUnit {
  public String getTerrName() {
    return terrName;
  }

  public int getUnit() {
    return unit;
  }

  public void setTerrName(String terrName) {
    this.terrName = terrName;
  }

  public void setUnit(int unit) {
    this.unit = unit;
  }

  private String terrName;
  private int unit;

  public TerrUnit(String terrName, int unit) {
    this.terrName = terrName;
    this.unit = unit;
  }

  public TerrUnit() {
  }
}

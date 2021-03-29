package edu.duke.ece651.risc.shared.game;

import java.util.List;

/**
 * A list of territory units
 */
public class TerrUnitList {
  public TerrUnitList(List<TerrUnit> terrUnitList) {
    this.terrUnitList = terrUnitList;
  }

  public void setTerrUnitList(List<TerrUnit> terrUnitList) {
    this.terrUnitList = terrUnitList;
  }

  public List<TerrUnit> getTerrUnitList() {
    return terrUnitList;
  }

  private List<TerrUnit> terrUnitList;
}

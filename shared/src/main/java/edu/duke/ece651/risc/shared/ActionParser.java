package edu.duke.ece651.risc.shared;

public class ActionParser {
  ActionEntry makePlaceEntry(String input) {
    String[] s = input.split("\\s+");
    if (s.length != 2) {
      throw new IllegalArgumentException("Input format should be <territoryName unit>");
    }
    Integer unit = parseUnitNumber(s[1]);
    return new PlaceEntry(s[0], unit);
  }

  ActionEntry makeAttackEntry(String input) {
    String[] s = input.split("\\s+");
    if (s.length != 3) {
      throw new IllegalArgumentException("Input format should be <type from to unit>");
    }
    Integer unit = parseUnitNumber(s[2]);
    return new AttackEntry(s[0], s[1], unit);
  }

  public ActionEntry makeMoveEntry(String input) {
    String[] s = input.split("\\s+");
    Integer unit = parseUnitNumber(s[2]);
    return new MoveEntry(s[0], s[1], unit);
  }

  private Integer parseUnitNumber(String s) {
    int unit;
    try {
      unit = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Unit number should be an integer:" + e.getMessage());
    }
    return unit;
  }

}

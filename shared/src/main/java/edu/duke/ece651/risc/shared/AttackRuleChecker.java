package edu.duke.ece651.risc.shared;


public class AttackRuleChecker extends Checker {
    public AttackRuleChecker(Checker next) {
        super(next);
    }

    /**
     * check 2 rules:
     * 1. the "from territory" should be adjacent to the "to territory"
     * 2. the "from territory" and "to territory" should belong to different owners.
     */
    public void checkMyRule(ActionEntry action, GameMap map) {
        Territory start = map.getTerritory(action.getFromName());
        Territory end = map.getTerritory(action.getToName());
        if (!start.isAdjacent(end)) {
            throw new IllegalArgumentException("expected attacked territory is adjacent to this territory!");
        }
        if (start.belongToSameOwner(end)) {
            throw new IllegalArgumentException("The attacked territory should be owned by other players!");
        }
    }
}
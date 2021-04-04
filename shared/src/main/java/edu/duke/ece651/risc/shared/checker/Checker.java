package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public abstract class Checker {
    private final Checker next;

    public Checker(Checker next) {
        this.next = next;
    }

    protected abstract void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo);

    public void checkAction(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        checkMyRule(action, map, myInfo);
        if (next != null) {
            next.checkAction(action, map,myInfo);
        }
    }
}
package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.ProdUseChecker;
import edu.duke.ece651.risc.shared.checker.ShieldRuleChecker;
import edu.duke.ece651.risc.shared.checker.SwordRuleChecker;

public class ShieldEntry extends BasicEntry{
    /**
     * Construct a basic entry
     *
     * @param toName      is the name of the to-territory
     */
    public ShieldEntry(String toName) {
        super(null, toName, 0, null, null, null);
    }

    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker checker=new ProdUseChecker(new ShieldRuleChecker(null));
        checker.checkAction(this,myMap,myInfo);
        myInfo.consumeProd(Constant.shield);
        Territory terr=myMap.getTerritory(this.toName);
        terr.setUseShield(myInfo.getName());
    }
}

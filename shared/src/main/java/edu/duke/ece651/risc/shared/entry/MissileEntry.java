package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.MissileRuleChecker;
import edu.duke.ece651.risc.shared.checker.ProdUseChecker;

import java.beans.ConstructorProperties;
import java.util.ArrayList;

public class MissileEntry extends BasicEntry{
    /**
     * create a missile entry object
     *
     * @param toName      the attacked territory name
     */
    @ConstructorProperties({"toName"})
    public MissileEntry(String toName){
        super(null,toName,0,null,null,null);
    }


    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker myChecker=new ProdUseChecker(new MissileRuleChecker(null));
        myChecker.checkAction(this,myMap,myInfo);
        myInfo.consumeProd(Constant.missile);
        Territory toTerr=myMap.getTerritory(toName);
        toTerr.applyMissile(myInfo.getName());
    }
}

package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.MissileRuleChecker;

import java.beans.ConstructorProperties;
import java.util.ArrayList;

public class MissileEntry extends BasicEntry{
    /**
     * create a missile entry object
     *
     * @param toName      the attacked territory name
     * @param playerName  the attacking player's name
     */
    @ConstructorProperties({"toName","playerName"})
    public MissileEntry(String toName, String playerName){
        super(null,toName,0,playerName,null,null);
    }


    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker myChecker=new MissileRuleChecker(null);
        myChecker.checkAction(this,myMap,myInfo);
        myInfo.consumeProd(Constant.missile);
        Territory toTerr=myMap.getTerritory(toName);
        toTerr.setMyArmy(new Army(toTerr.getOwnerName(),new ArrayList<>()));
    }
}

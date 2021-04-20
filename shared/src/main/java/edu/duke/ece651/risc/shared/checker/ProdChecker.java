package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class ProdChecker extends Checker{
    public ProdChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(!Constant.prodCost.containsKey(action.getProdName())){
            throw new IllegalArgumentException("Invalid prod name!");
        }
        if(action.getNumProds()<=0){
            throw new IllegalArgumentException("The count must be positive!");
        }
        if(action.getNumProds()* Constant.prodCost.get(action.getProdName())>myInfo.getFoodResource()){
            throw new IllegalArgumentException("The food resource is not enough!");
        }
    }
}

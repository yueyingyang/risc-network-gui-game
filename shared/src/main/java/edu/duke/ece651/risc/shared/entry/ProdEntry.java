package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.ProdChecker;

public class ProdEntry extends BasicEntry{
    /**
     * construct a prod entry
     *
     * @param prodName the name of prod
     * @param numProds the number rof prods to be added
     */
    public ProdEntry(String prodName, int numProds) {
        super(null,null,0,null,null,null);
        this.prodName=prodName;
        this.numProds=numProds;
    }

    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker checker=new ProdChecker(null);
        checker.checkAction(this,myMap,myInfo);
        if(prodName.equals(Constant.ship)){
            myInfo.addShipCount(numProds);
        }else if(prodName.equals(Constant.missile)){
            myInfo.addMissileCount(numProds);
        }else if(prodName.equals(Constant.shield)){
            myInfo.addShieldCount(numProds);
        }else{
            myInfo.addSwordCount(numProds);
        }
    }
}

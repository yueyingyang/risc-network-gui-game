package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInfoTest {

    @Test
    public void test_getName() {
        PlayerInfo myInfo = new PlayerInfo("LiLei");
        assertEquals("LiLei", myInfo.getName());
    }

    @Test
    public void test_addResource() {
        Territory terr0 = new Territory("NANJING", 5, 30, 22);
        Territory terr1 = new Territory("SHANGHAI", 8, 15, 18);
        List<Territory> myTerrs = Arrays.asList(terr0, terr1);
        PlayerInfo myInfo = new PlayerInfo("LiLei");
        myInfo.addResource(myTerrs);
        assertEquals(45, myInfo.getFoodResource());
        assertEquals(40, myInfo.getTechResource());
    }

    @Test
    public void test_upgradeTech() {
        PlayerInfo myInfo = new PlayerInfo("LiLei", 3, 200, 400);
        assertFalse(myInfo.isRequested());
        myInfo.takeTech();
        assertTrue(myInfo.isRequested());
        assertEquals(3, myInfo.getTechLevel());
        assertEquals(275, myInfo.getTechResource());
        myInfo.effectTech();
        assertEquals(4, myInfo.getTechLevel());
        assertFalse(myInfo.isRequested());
    }

    @Test
    public void test_seeTerr() {
        String name0 = "Green";
        String name1 = "Purple";
        String name2 = "Blue";
        Territory terr0 = new Territory("0");
        terr0.setOwnerName(name0);
        terr0.addSoldiersToArmy(4);
        terr0.addSpies(name0, 2);
        terr0.addSpies(name1, 3);

        PlayerInfo myInfo = new PlayerInfo(name0);
        myInfo.seeTerr(terr0);
        terr0.removeSoldiersFromArmy(3);
        terr0.addSpies(name0, 4);

        assertEquals(1, terr0.getNumSoldiersInArmy());
        assertEquals(6, terr0.getNumSpies(name0));
        assertEquals(3, terr0.getNumSpies(name1));
        assertEquals(0, myInfo.getProdCount(Constant.ship));
        assertEquals(0,myInfo.getProdCount(Constant.missile));


        Territory terr1 = myInfo.getPrevSeenTerr("0");
        assertEquals(4, terr1.getNumSoldiersInArmy());
        assertEquals(2, terr1.getNumSpies(name0));
        assertEquals(3, terr1.getNumSpies(name1));

        assertNull(myInfo.getPrevSeenTerr("1"));
    }

    @Test
    public void test_adder(){
        PlayerInfo myInfo = new PlayerInfo("Green",5,1000,100);
        myInfo.addProd(Constant.ship,2);
        assertEquals(myInfo.getProdCount(Constant.ship),2);
        myInfo.addProd(Constant.missile,1);
        assertEquals(myInfo.getProdCount(Constant.missile),1);
        myInfo.addProd(Constant.shield,2);
        assertEquals(myInfo.getProdCount(Constant.shield),2);
        myInfo.addProd(Constant.sword,2);
        assertEquals(myInfo.getProdCount(Constant.sword),2);
    }

}
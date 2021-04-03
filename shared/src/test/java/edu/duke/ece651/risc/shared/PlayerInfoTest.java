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
        assertTrue(myInfo.canUpgradeTech());
        myInfo.upgradeTech();
        assertEquals(275, myInfo.getTechResource());
        assertFalse(myInfo.canUpgradeTech());
    }

}
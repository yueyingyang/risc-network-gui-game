package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    public void test_Constructor() {
        Territory terr = new Territory("NANJING");
        assertEquals("NANJING", terr.getName());

        Territory terr1 = new Territory("NANJING", 10, 180, 200);
        assertEquals(10, terr1.getSize());
    }

    @Test
    public void test_equals() {
        Territory terr0 = new Territory("NANJING");
        Territory terr1 = new Territory("BEIJING");
        Territory terr2 = new Territory("NANJING");
        assertEquals(terr0, terr2);
        assertNotEquals(terr0, terr1);
        assertNotEquals(terr1, "BEIJING");
    }

    @Test
    public void test_hashCode() {
        Territory terr0 = new Territory("HANGZHOU");
        Territory terr1 = new Territory("SHANGHAI");
        Territory terr2 = new Territory("HANGZHOU");
        assertEquals(terr0.hashCode(), terr2.hashCode());
        assertNotEquals(terr0.hashCode(), terr1.hashCode());
    }

    @Test
    public void test_addNeighbour_isAdjacent() {
        Territory terr0 = new Territory("NANJING");
        Territory terr1 = new Territory("SHANGHAI");
        Territory terr2 = new Territory("HUNAN");
        terr0.addNeighbour(terr1);
        terr1.addNeighbour(terr0);
        assertTrue(terr0.isAdjacent(terr1));
        assertTrue(terr1.isAdjacent(terr0));
        assertFalse(terr0.isAdjacent(terr2));
    }

    @Test
    public void test_getOwnerName_setOwnerName() {
        Territory terr = new Territory("NANJING");
        terr.setOwnerName("Green");
        assertEquals("Green", terr.getOwnerName());
    }

    @Test
    public void test_add_remove() {
        Territory terr = new Territory("NANJING");
        Army myArmy = new BasicArmy("HanMeiMei", 3);
        terr.setMyArmy(myArmy);
        assertEquals(3, terr.getNumSoldiersInArmy());
        terr.addSoldiersToArmy(4);
        assertEquals(7, terr.getNumSoldiersInArmy());
        terr.removeSoldiersFromArmy(2);
        assertEquals(5, terr.getNumSoldiersInArmy());
    }

    @Test
    public void test_bufferAttacker() {
        Territory terr = new Territory("NANJING");
        Army myArmy = new BasicArmy("LiLei", 5);
        terr.setMyArmy(myArmy);

        Army attacker0 = new BasicArmy("HanMeiMei", 4);
        Army attacker1 = new BasicArmy("Kitty", 6);
        Army attacker2 = new BasicArmy("HanMeiMei", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            terr.bufferAttacker(attacker);
        }

        assertEquals(6, terr.getNumSoldiersInAttacker("Kitty"));
        assertEquals(12, terr.getNumSoldiersInAttacker("HanMeiMei"));
    }

    @Test
    public void test_resolveCombat() {
        Territory terr = new Territory("1");
        terr.setOwnerName("Purple");
        Army myArmy = new BasicArmy("Purple", 5);
        terr.setMyArmy(myArmy);

        // no combat on territory 1
        String ans2 = terr.resolveCombat(new Random(0));
        assertEquals("", ans2);
        assertEquals("Purple", terr.getOwnerName());

        // first combat on territory 1
        Army attacker0 = new BasicArmy("Blue", 4);
        Army attacker1 = new BasicArmy("Green", 6);
        Army attacker2 = new BasicArmy("Blue", 8);
        addAttackers(terr, attacker0, attacker1, attacker2);

        String ans = terr.resolveCombat(new Random(0));
        String expect = "On territory 1:\n" +
                "Purple player(5 soldiers) defends Blue player(12 soldiers). Blue player wins.\n" +
                "Blue player(10 soldiers) defends Green player(6 soldiers). Blue player wins.\n";
        assertEquals(expect, ans);
        assertEquals(-1, terr.getNumSoldiersInAttacker("Green"));

        // second combat on territory 1
        Army attacker3 = new BasicArmy("Yellow", 4);
        Army attacker4 = new BasicArmy("Orange", 10);
        addAttackers(terr, attacker3, attacker4);
        String ans1 = terr.resolveCombat(new Random(0));
        String expect1 = "On territory 1:\n" +
                "Blue player(2 soldiers) defends Yellow player(4 soldiers). Yellow player wins.\n" +
                "Yellow player(4 soldiers) defends Orange player(10 soldiers). Orange player wins.\n";
        assertEquals(expect1, ans1);
    }

    private void addAttackers(Territory terr, Army... attackers) {
        for (Army attacker : attackers) {
            terr.bufferAttacker(attacker);
        }
    }

    @Test
    public void test_belongToSameOwner() {
        Territory terr0 = new Territory("0");
        Territory terr1 = new Territory("1");
        Territory terr2 = new Territory("2");
        terr0.setOwnerName("LiLei");
        terr1.setOwnerName("HanMeiMei");
        terr2.setOwnerName("LiLei");
        assertTrue(terr0.belongToSameOwner(terr2));
        assertFalse(terr1.belongToSameOwner(terr0));
    }

    @Test
    public void test_upgradeArmy() {
        Territory terr0 = new Territory("0");
        Army army = new BasicArmy("HanMeiMei", 5);
        terr0.setOwnerName("HanMeiMei");
        terr0.setMyArmy(army);
        PlayerInfo myInfo = new PlayerInfo("HanMeiMei", 6, 180, 120);
        terr0.upgradeArmy("0", "4", 2, myInfo);
        assertEquals(3, terr0.getNumSoldiersInArmy("0"));
        assertEquals(2, terr0.getNumSoldiersInArmy("4"));
        assertEquals(70, myInfo.getFoodResource());
    }

    @Test
    public void test_add_remove_with_type() {
        Territory terr = new Territory("NANJING");
        Army army = new BasicArmy("LiLei", 5);
        terr.setMyArmy(army);
        assertEquals(5, terr.getNumSoldiersInArmy("0"));

        terr.addSoldiersToArmy(3, "1" );
        assertEquals(3, terr.getNumSoldiersInArmy("1"));
        terr.addSoldiersToArmy(6, "4");
        assertEquals(6, terr.getNumSoldiersInArmy("4"));

        terr.removeSoldiersFromArmy(2, "4");
        assertEquals(4, terr.getNumSoldiersInArmy("4"));
        terr.removeSoldiersFromArmy(2, "1");
        assertEquals(1, terr.getNumSoldiersInArmy("1"));
    }

}
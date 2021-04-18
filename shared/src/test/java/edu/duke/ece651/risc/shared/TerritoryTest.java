package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        Set<Territory> exptNeighs = new HashSet<>();
        exptNeighs.add(terr1);
        assertEquals(exptNeighs, terr0.getNeighbours());
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
        Army myArmy = new Army("HanMeiMei", 3);
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
        Army myArmy = new Army("LiLei", 5);
        terr.setMyArmy(myArmy);

        Army attacker0 = new Army("HanMeiMei", 4);
        Army attacker1 = new Army("Kitty", 6);
        Army attacker2 = new Army("HanMeiMei", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            terr.bufferAttacker(attacker);
        }

        assertEquals(6, terr.getNumSoldiersInAttacker("Kitty"));
        assertEquals(12, terr.getNumSoldiersInAttacker("HanMeiMei"));
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
    public void test_add_remove_with_type() {
        Territory terr = new Territory("NANJING");
        assertEquals(0, terr.getNumSoldiersInArmy());
        assertEquals(0, terr.getNumSoldiersInArmy("1"));
        Army army = new Army("LiLei", 5);
        terr.setMyArmy(army);
        assertEquals(5, terr.getNumSoldiersInArmy("0"));

        terr.addSoldiersToArmy(3, "1");
        assertEquals(3, terr.getNumSoldiersInArmy("1"));
        terr.addSoldiersToArmy(6, "4");
        assertEquals(6, terr.getNumSoldiersInArmy("4"));

        terr.removeSoldiersFromArmy(2, "4");
        assertEquals(4, terr.getNumSoldiersInArmy("4"));
        terr.removeSoldiersFromArmy(2, "1");
        assertEquals(1, terr.getNumSoldiersInArmy("1"));
    }

    @Test
    public void test_resolveCombat() {
        Territory terr0 = new Territory("1");
        terr0.setOwnerName("Purple");
        Army army0 = new Army("Purple", 1);
        army0.addSoldiers(2, "3");
        army0.addSoldiers(3, "2");
        terr0.setMyArmy(army0);
        Random myRandom = new Random(0);

        Army army1 = new Army("Green", 2, "4");
        army1.addSoldiers(3, "2");
        Army army2 = new Army("Green", 1, "3");
        army2.addSoldiers(1, "4");
        terr0.bufferAttacker(army1);
        terr0.bufferAttacker(army2);
        assertEquals(3, terr0.getNumSoldiersInAttacker("Green", "4"));
        assertEquals(3, terr0.getNumSoldiersInAttacker("Green", "2"));
        assertEquals(1, terr0.getNumSoldiersInAttacker("Green", "3"));

        String ans0 = terr0.resolveCombat(myRandom);
        String expt0 = "On territory 1:\n" +
                "Defender: Purple player(1 type-0 soldiers, 3 type-2 soldiers, 2 type-3 soldiers)\n" +
                "Attacker: Green player(3 type-2 soldiers, 1 type-3 soldiers, 3 type-4 soldiers)\n" +
                "Green player wins.\n";
        assertEquals(expt0, ans0);

        // second combat on territory0
        Army army3 = new Army("Blue", 2, "5");
        army3.addSoldiers(1, "3");
        Army army4 = new Army("Orange", 4, "2");
        terr0.bufferAttacker(army3);
        terr0.bufferAttacker(army4);
        String ans1 = terr0.resolveCombat(myRandom);
        String expt1 = "On territory 1:\n" +
                "Defender: Green player(1 type-4 soldiers)\n" +
                "Attacker: Orange player(4 type-2 soldiers)\n" +
                "Orange player wins.\n" +
                "Defender: Orange player(3 type-2 soldiers)\n" +
                "Attacker: Blue player(1 type-3 soldiers, 2 type-5 soldiers)\n" +
                "Blue player wins.\n";
        assertEquals(expt1, ans1);

        // no combat
        String ans2 = terr0.resolveCombat(myRandom);
        assertEquals("", ans2);
        assertEquals(0, terr0.getNumSoldiersInAttacker("Purple"));
        assertEquals(0, terr0.getNumSoldiersInAttacker("Purple", "1"));
    }

    @Test
    public void test_getters() {
        Territory terr = new Territory("NANJING", 10, 15, 20);
        assertEquals(10, terr.getSize());
        assertEquals(15, terr.getFoodProd());
        assertEquals(20, terr.getTechProd());
    }

    @Test
    public void test_cloaking() {
        Territory terr = new Territory("NANJING");
        terr.addCloaking();
        assertEquals(3, terr.getCloaking());
        terr.useCloaking();
        assertEquals(2, terr.getCloaking());
        terr.useCloaking();
        assertEquals(1, terr.getCloaking());
        terr.useCloaking();
        assertEquals(0, terr.getCloaking());
        terr.useCloaking();
        assertEquals(0, terr.getCloaking());
    }

    @Test
    public void test_mySpies() {
        Territory terr = new Territory("NANJING");
        assertEquals(0, terr.getNumSpies());
        terr.addMySpies(2);
        assertEquals(2, terr.getNumSpies());
        terr.addMySpies(3);
        assertEquals(5, terr.getNumSpies());
        terr.removeMySpies(4);
        assertEquals(1, terr.getNumSpies());
    }

    @Test
    public void test_bufferMySpies() {
        Territory terr = new Territory("NANJING");

        // both null, remove
        terr.removeMySpies(2);
        verify_bufferMySpies(terr, 0, 0);

        // both null, buffer
        terr.bufferMySpies(2);
        verify_bufferMySpies(terr, 0, 2);

        // mySpies = null, temp not null, buffer
        terr.bufferMySpies(3);
        verify_bufferMySpies(terr, 0, 5);

        // mySpies = null, temp not null, add
        terr.addMySpies(3);
        verify_bufferMySpies(terr, 3, 8);

        // mySpies = null, temp not null, remove
        terr.removeMySpies(2);
        verify_bufferMySpies(terr, 1, 6);

        // not equal, buffer
        terr.bufferMySpies(4);
        verify_bufferMySpies(terr, 1, 10);

        // not equal, add
        terr.addMySpies(6);
        verify_bufferMySpies(terr, 7, 16);

        // not equal, remove
        terr.removeMySpies(4);
        verify_bufferMySpies(terr, 3, 12);

        // sync mySpies with tempSpies
        terr.syncMySpies();
        verify_bufferMySpies(terr, 12, 12);

        // equal add
        terr.addMySpies(3);
        verify_bufferMySpies(terr, 15, 15);

        // equal remove
        terr.removeMySpies(6);
        verify_bufferMySpies(terr, 9, 9);

        // equal buffer
        terr.bufferMySpies(4);
        verify_bufferMySpies(terr, 9, 13);

        Territory terr1 = new Territory("SHANGHAI");

        // both null, add
        terr1.addMySpies(3);
        verify_bufferMySpies(terr1, 3, 3);
    }

    private void verify_bufferMySpies(Territory terr, int expect1, int expect2) {
        assertEquals(expect1, terr.getNumSpies());
        assertEquals(expect2, terr.getLatestNumSpies());
    }


    @Test
    public void test_enemySpies() {
        Territory terr = new Territory("NANJING");

        assertEquals(0, terr.getNumEnemySpies("LiLei"));
        terr.addEnemySpies(new Army("LiLei", 2));
        assertEquals(2, terr.getNumEnemySpies("LiLei"));
        terr.addEnemySpies(new Army("LiLei", 4));
        assertEquals(6, terr.getNumEnemySpies("LiLei"));
        terr.addEnemySpies(new Army("HanMeiMei", 3));
        assertEquals(3, terr.getNumEnemySpies("HanMeiMei"));

        terr.removeEnemySpies("LiLei", 5);
        assertEquals(1, terr.getNumEnemySpies("LiLei"));
    }

    @Test
    public void test_bufferEnemySpies() {
        Territory terr = new Territory("NANJING");
        String name1 = "LiLei";
        String name2 = "HanMeiMei";

        // both null, remove
        terr.removeEnemySpies(name1, 2);
        verify_bufferEnemySpies(terr, name1, 0, 0);

        // both null, buffer
        terr.bufferEnemySpies(new Army(name1, 3));
        verify_bufferEnemySpies(terr, name1, 0, 3);

        // both null, add
        terr.addEnemySpies(new Army(name2, 6));
        verify_bufferEnemySpies(terr, name2, 6, 6);

        // one null, one not null, buffer
        terr.bufferEnemySpies(new Army(name1, 2));
        verify_bufferEnemySpies(terr, name1, 0, 5);

        // one null, one not null, remove
        terr.removeEnemySpies(name1, 4);
        verify_bufferEnemySpies(terr, name1, 0, 1);

        // one null, one not null, add
        terr.addEnemySpies(new Army(name1, 5));
        verify_bufferEnemySpies(terr, name1, 5, 6);

        // not equal, remove
        terr.removeEnemySpies(name1, 2);
        verify_bufferEnemySpies(terr, name1, 3, 4);

        // not equal, buffer
        terr.bufferEnemySpies(new Army(name1, 3));
        verify_bufferEnemySpies(terr, name1, 3, 7);

        // not equal, add
        terr.addEnemySpies(new Army(name1, 2));
        verify_bufferEnemySpies(terr, name1, 5, 9);

        // sync buffer
        terr.syncBuffer();
        verify_bufferEnemySpies(terr, name1, 9, 9);
        verify_bufferEnemySpies(terr, name2, 6, 6);

        // equal add
        terr.addEnemySpies(new Army(name1, 2));
        verify_bufferEnemySpies(terr, name1, 11, 11);

        // equal remove
        terr.removeEnemySpies(name1, 3);
        verify_bufferEnemySpies(terr, name1, 8, 8);

        // equal buffer
        terr.bufferEnemySpies(new Army(name1, 4));
        verify_bufferEnemySpies(terr, name1, 8, 12);
    }

    protected void verify_bufferEnemySpies(Territory terr, String name, int expect1, int expect2) {
        assertEquals(expect1, terr.getNumEnemySpies(name));
        assertEquals(expect2, terr.getLatestNumEnemySpies(name));
    }

}
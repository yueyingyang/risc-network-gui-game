package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.*;

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
    }

    @Test
    public void test_resolveCombatWithProd() {
        Random myRandom = new Random(0);
        Territory terr1 = new Territory("1");
        terr1.setOwnerName("Green");
        terr1.setMyArmy(new Army("Green", 3));
        Army army5 = new Army("Yellow", 2);
        Army army6 = new Army("Blue", 2);
        terr1.bufferAttacker(army5);
        terr1.bufferAttacker(army6);
        terr1.applyMissile("Yellow");
        terr1.setUseShield("Green");
        terr1.setUseSword("Yellow");
        terr1.addUseShip("Orange");
        String expect = "On territory 1:\n" +
                "Received missile(s) from Yellow player.\n" +
                "Green player use shield.\n" +
                "Yellow player use sword.\n" +
                "Orange player use ship.\n" +
                "Defender: Green player(0 soldiers)\n" +
                "Attacker: Yellow player(2 type-0 soldiers)\n" +
                "Yellow player wins.\n" +
                "Defender: Yellow player(2 type-0 soldiers)\n" +
                "Attacker: Blue player(2 type-0 soldiers)\n" +
                "Yellow player wins.\n";
        String ans = terr1.resolveCombat(myRandom);
        assertEquals(expect, ans);

        // no combat
        String ans2 = terr1.resolveCombat(myRandom);
        assertEquals("", ans2);

        // use ship
        terr1.addUseShip("Green");
        System.out.println(terr1.resolveCombat(myRandom));

    }

    @Test
    public void test_resolveMissile(){
        Territory terr0 = new Territory("1");
        terr0.setOwnerName("Purple");
        Army army0 = new Army("Purple", 1);
        army0.addSoldiers(2, "3");
        army0.addSoldiers(3, "2");
        terr0.setMyArmy(army0);
        String ans0 = terr0.resolveMissile();
        assertEquals("", ans0);

        // combat with missile
        terr0.applyMissile("Blue");
        terr0.applyMissile("Yellow");
        String ans3 = terr0.resolveMissile();
        assertEquals("Received missile(s) from Blue, Yellow player.\n", ans3);
    }

    @Test
    public void test_displayShieldInfo() {
        Territory terr0 = new Territory("1");
        String ans0 = terr0.displayShieldInfo();
        assertEquals("", ans0);
        terr0.setOwnerName("Purple");
        terr0.setUseShield("Purple");
        String ans1 = terr0.displayShieldInfo();
        assertEquals("Purple player use shield.\n", ans1);

    }

    @Test
    public void test_displaySwordInfo() {
        Territory terr0 = new Territory("0");
        String ans0 = terr0.displaySwordInfo();
        assertEquals("", ans0);
        terr0.setUseSword("Blue");
        terr0.setUseSword("Yellow");
        terr0.setUseSword("Green");
        terr0.setUseSword("Red");
        String ans1 = terr0.displaySwordInfo();
        assertEquals("Blue, Green, Red, Yellow player use sword.\n", ans1);
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
        // first turn
        terr.bufferCloaking();
        assertEquals(3, terr.getTempCloaking());
        assertEquals(0, terr.getCloaking());

        terr.consumeCloaking();
        assertEquals(0, terr.getCloaking());

        terr.effectCloaking();
        assertEquals(3, terr.getCloaking());
        assertEquals(0, terr.getTempCloaking());

        // second turn
        terr.bufferCloaking();
        terr.bufferCloaking();
        terr.consumeCloaking();
        assertEquals(6, terr.getTempCloaking());
        assertEquals(2, terr.getCloaking());

        terr.effectCloaking();
        assertEquals(8, terr.getCloaking());
        assertEquals(0, terr.getTempCloaking());

    }

    @Test
    public void test_spies() {
        Territory terr = new Territory("0");
        String name0 = "Green";
        String name1 = "Yellow";
        String name2 = "Blue";

        // round one
        terr.addSpies(name0, 3);
        test_spies(terr, name0, 3, 0);
        terr.addSpies(name0, 2);
        test_spies(terr, name0, 5, 0);
        terr.bufferSpies(name0, 4);
        test_spies(terr, name0, 5, 4);

        terr.bufferSpies(name1, 6);
        test_spies(terr, name1, 0, 6);
        terr.addSpies(name1,2);
        test_spies(terr, name1, 2, 6);

        terr.bufferSpies(name2, 2);
        test_spies(terr, name2, 0, 2);

        terr.effectSpyMove();
        test_spies(terr, name0, 9, 0);
        test_spies(terr, name1, 8, 0);
        test_spies(terr, name2, 2, 0);

        // round 2
        terr.bufferSpies(name0, 3);
        test_spies(terr, name0, 9, 3);
        terr.removeSpies(name0, 2);
        test_spies(terr, name0, 7, 3);
        terr.effectSpyMove();
        test_spies(terr, name0, 10, 0);
    }

    private void test_spies(Territory terr, String name, int expect1, int expect2) {
        assertEquals(expect1, terr.getNumSpies(name));
        assertEquals(expect2, terr.getBufferedNumSpies(name));
    }

    @Test
    public void test_copy() {
        String name0 = "Green";
        String name1 = "Purple";
        String name2 = "Blue";
        String name3 = "Orange";

        Territory terr0 = new Territory("0");
        terr0.setOwnerName(name0);
        terr0.addSoldiersToArmy(4);
        terr0.addSpies(name0, 2);
        terr0.addSpies(name1, 3);
        Territory terr1 = new Territory(terr0);

        terr0.removeSoldiersFromArmy(3);
        terr0.addSpies(name1, 6);

        assertEquals(1, terr0.getNumSoldiersInArmy());
        assertEquals(2, terr0.getNumSpies(name0));
        assertEquals(9, terr0.getNumSpies(name1));

        assertEquals(4, terr1.getNumSoldiersInArmy());
        assertEquals(2, terr1.getNumSpies(name0));
        assertEquals(3, terr1.getNumSpies(name1));

        Territory terr2 = new Territory("1");
        terr2.setOwnerName(name2);
        Territory terr3 = new Territory(terr2);
        terr2.setOwnerName(name3);
        assertEquals(terr3.getOwnerName(), name2);
        assertEquals(terr2.getOwnerName(), name3);
    }


    @Test
    public void test_isAdjacentEnemy() {
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "Green";
        String name1 = "Yellow";
        String name2 = "Blue";
        List<String> names = Arrays.asList(name0, name1, name2);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        assertFalse(terr0.isAdjacentEnemy(name1));
        assertTrue(terr0.isAdjacentEnemy(name2));
        assertFalse(terr0.isAdjacentEnemy(name0));
    }

    @Test
    public void test_isVisible() {
        String name0 = "Green";
        String name1 = "Yellow";
        String name2 = "Blue";
        PlayerInfo myInfo0 = new PlayerInfo(name0);
        PlayerInfo myInfo1 = new PlayerInfo(name1);
        PlayerInfo myInfo2 = new PlayerInfo(name2);

        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList(name0, name1, name2);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");

        // my territory
        assertTrue(terr0.isVisible(myInfo0));
        assertNotNull(myInfo0.getPrevSeenTerr("0"));

        // adjacent
        assertFalse(terr0.isVisible(myInfo1));
        // not adjacent, no spy
        assertTrue(terr0.isVisible(myInfo2));

        terr0.addSpies(name1, 1);
        // not adjacent, has spy
        assertTrue(terr0.isVisible(myInfo1));

        terr0.bufferCloaking();
        terr0.effectCloaking();
        // my territory
        assertTrue(terr0.isVisible(myInfo0));
        // adjacent, hide, no spy
        assertTrue(terr0.isVisible(myInfo1));
        // not adjacent, hide, has spy
        assertFalse(terr0.isVisible(myInfo2));

        terr0.addSpies(name2, 1);
        // adjacent, hide, has spy
        assertTrue(terr0.isVisible(myInfo1));
    }

    @Test
    public void test_prod(){
        Territory terr = new Territory("NANJING", 10, 15, 20);
        terr.setUseSword("Blue");
        terr.setUseShield("Red");
        assertEquals(terr.getAttackerBonus("Blue"),Constant.SWORD_BONUS);
        assertEquals(terr.getAttackerBonus("Red"),0);
        assertEquals(terr.getDefenderBonus("Blue"),Constant.SHIELD_BONUS);
        assertEquals(terr.getDefenderBonus("Red"),Constant.SHIELD_BONUS);
        assertEquals(terr.getDefenderBonus("Green"),0);
    }

}
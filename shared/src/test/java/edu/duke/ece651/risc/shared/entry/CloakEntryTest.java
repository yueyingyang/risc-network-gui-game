package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloakEntryTest {

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "LiLei";
        String name1 = "HanMeiMei";
        List<String> names = Arrays.asList(name0, name1);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        PlayerInfo myInfo = new PlayerInfo(name0, 3, 280, 1000);

        assertFalse(myInfo.hasCloakingTech());
        ActionEntry entry0 = new CloakingTechEntry(name0);
        entry0.apply(myMap, myInfo);
        assertTrue(myInfo.hasCloakingTech());
        assertEquals(900, myInfo.getTechResource());

        ActionEntry entry1 = new CloakEntry("0", name0);
        entry1.apply(myMap, myInfo);
        assertEquals(880, myInfo.getTechResource());
        assertEquals(0, terr0.getCloaking());
        terr0.effectCloaking();
        assertEquals(3, terr0.getCloaking());

    }

}
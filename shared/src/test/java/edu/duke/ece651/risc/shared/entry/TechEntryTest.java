package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.V1MapFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TechEntryTest {

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        PlayerInfo myInfo = new PlayerInfo("LiLei", 1, 280, 1000);

        test_apply(myMap, myInfo, 2, 950);
        test_apply(myMap, myInfo, 3, 875);
        test_apply(myMap, myInfo, 4, 750);
        test_apply(myMap, myInfo, 5, 550);
        test_apply(myMap, myInfo, 6, 250);
    }

    private void test_apply(GameMap myMap, PlayerInfo myInfo, int expLevel, int expTech) {
        ActionEntry entry = new TechEntry("LiLei");
        entry.apply(myMap, myInfo);
        assertEquals(expLevel, myInfo.getTechLevel());
        assertEquals(expTech, myInfo.getTechResource());
    }

}
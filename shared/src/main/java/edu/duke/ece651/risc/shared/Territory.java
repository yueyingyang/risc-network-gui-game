package edu.duke.ece651.risc.shared;

import java.util.Map;
import java.util.Set;

/**
 * An class represents a territory
 */
public class Territory {
    private String name;
    private Army defender;
    private Set<String> neighbours;
    private Map<String, Army> attackerBuffer;  // ownerName


}

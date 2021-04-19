package edu.duke.ece651.risc.web.model;

import edu.duke.ece651.risc.shared.entry.ActionEntry;

/**
 * Used for tool entry creation: e.g. Cloaking, Shield, Sword, Missile ...
 */
@FunctionalInterface
public interface ToolCreation {
  ActionEntry apply(String terrName, String userName);
}

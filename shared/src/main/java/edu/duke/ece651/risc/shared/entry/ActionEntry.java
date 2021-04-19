package edu.duke.ece651.risc.shared.entry;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;

/**
 * An interface represent an action entry
 *
 * Annotation added for jackson subtype deserialization: https://www.baeldung.com/jackson-inheritance
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlaceEntry.class, name = "place"),
        @JsonSubTypes.Type(value = AttackEntry.class, name = "attack"),
        @JsonSubTypes.Type(value = MoveEntry.class, name = "move"),
        @JsonSubTypes.Type(value = FancyMoveEntry.class, name = "fancyMove"),
        @JsonSubTypes.Type(value = FancyAttackEntry.class, name = "fancyAttack"),
        @JsonSubTypes.Type(value = SoldierEntry.class, name = "soldier"),
        @JsonSubTypes.Type(value = TechEntry.class, name = "tech"),
        @JsonSubTypes.Type(value = CloakEntry.class, name = "cloak"),
        @JsonSubTypes.Type(value = CloakingTechEntry.class, name = "cloakingTech"),
        @JsonSubTypes.Type(value = SpyEntry.class, name = "spy"),
        @JsonSubTypes.Type(value = SpyMoveEntry.class, name = "spyMove"),
        @JsonSubTypes.Type(value = MissileEntry.class, name = "missile")
})
public interface ActionEntry {

    /**
     * Apply the action on the action entry
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    public void apply(GameMap myMap, PlayerInfo myInfo);

    /**
     * Get the name of the from-territory
     *
     * @return the name of the from-territory or null if the field does not exist
     */
    public String getFromName();

    /**
     * Get the name of the to-territory
     *
     * @return the name of the to-territory or null if the field does not exist
     */
    public String getToName();

    /**
     * Get the number of soldiers
     *
     * @return the number of soldiers or -1 if the field does not exist
     */
    public int getNumSoldiers();

    /**
     * Get the name of the player
     *
     * @return the name of the player
     */
    public String getPlayerName();

    /**
     * Get the current type of the soldier
     *
     * @return the current type of the soldier or null if the field does not exist
     */
    public String getFromType();

    /**
     * Get the type of the soldier after upgrade
     *
     * @return the type of the soldier after upgrade or null if the field does not exist
     */
    public String getToType();


}

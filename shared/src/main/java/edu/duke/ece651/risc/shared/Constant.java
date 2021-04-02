package edu.duke.ece651.risc.shared;

import java.util.HashMap;
import java.util.Map;

/**
 * The messages send between the server and player at the beginning
 */
public class Constant {
    public static final String NO_GAME_AVAILABLE_INFO = "Hi, there's no available game in the system, so we will start a game for you.";
    public static final String ASK_HOW_MANY_PLAYERS = "Hi ^_^, We will create a new game for you. How many players do you want to have in your Game?";
    public static final String ASK_START_NEW_OR_JOIN = "Hi, Do you want to start a new game(type s) or join an existing game(type j)?";
    public static final String SUCCESS_ACTION_CHOOSED = "Successfully choose an action!";
    public static final String SUCCESS_NUMBER_CHOOSED = "You are in a Game now!";
    public static final String OUT_OF_RANGE_CHOICE = "You should only chose from the available list!";
    public static final String INVALID_NUMBER = "You should type a valid number!";
    public static final String IMPROPER_NUMBER = "Please input a number between 2-5!";
    public static final String AVAILABLE_LIST_INFO = "The available games you can choose from are: ";
    public static final String NOR_JOIN_START = "You should only input s/j!";
    public static final String ORDER_COMMIT = "order commit";
    public static final String LOSE_GAME = "You have lost all your territories.";
    public static final String DISCONNECT_GAME = "The client want to exit this game and disconnect.";
    public static final String WATCH_GAME = "The client want to stay in the game to watch.";
    public static final String CONTINUE_PLAYING = "You can continue playing.";
    public static final String GAME_OVER = "Game over ~";
    public static final String VALID_ACTION = "The action is valid.";
    public static final String INVALID_ACTION = "The action cannot be applied.";
    public static final String GET_GAMELIST = "getGameList";
    public static final String STARTGAME = "start";
    public static final String JOINGAME = "join";
    public static final String REJOINGAME = "rejoin";

    /**
     * The bonus of each type of soldier
     */
    public static final Map<String, Integer> bonus = new HashMap<>();

    static {
        bonus.put("0", 0);
        bonus.put("1", 1);
        bonus.put("2", 3);
        bonus.put("3", 5);
        bonus.put("4", 8);
        bonus.put("5", 11);
        bonus.put("6", 15);
    }

    /**
     * The cost to upgrade the tech level by one from current level
     * The key in hte map is the current level
     */
    public static final Map<Integer, Integer> techCost = new HashMap<>();

    static {
        techCost.put(1, 50);
        techCost.put(2, 75);
        techCost.put(3, 125);
        techCost.put(4, 200);
        techCost.put(5, 300);
    }

    /**
     * The total cost to upgrade soldier from type 0 to the target type
     * The key is the target type
     */
    public static final Map<String, Integer> soldierCost = new HashMap<>();

    static {
        soldierCost.put("0", 0);
        soldierCost.put("1", 3);
        soldierCost.put("2", 11);
        soldierCost.put("3", 30);
        soldierCost.put("4", 55);
        soldierCost.put("5", 90);
        soldierCost.put("6", 140);
    }
}


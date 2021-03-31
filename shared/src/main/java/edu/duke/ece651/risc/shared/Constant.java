package edu.duke.ece651.risc.shared;

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
}


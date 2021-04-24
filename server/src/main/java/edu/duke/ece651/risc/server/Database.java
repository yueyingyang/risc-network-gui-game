
package edu.duke.ece651.risc.server;
import java.util.ArrayList;

import javax.swing.plaf.TreeUI;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.ServerPlayer;

/**
 * This class is used for connecting the database and providing some interface of the DB used in App
 */
public class Database {
    //the flag to refrsh the database
    private Boolean refresh = true; 
    private volatile MongoClient mongoClient;
    private volatile MongoDatabase mongoDatabase;
    private volatile MongoCollection<Document> playersCollection;// only need players names
    private volatile MongoCollection<Document> gamesCollection;
    private JSONSerializer js;

    /**
     * The constructor of the database
     */
    public Database(){
        //ignore some annoying log of MongoDB
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("org.mongodb.driver").setLevel(Level.ERROR);
        //instantiate a MongoClient object without any parameters to connect to a MongoDB instance 
        //running on localhost on port 27017
        this.mongoClient = new MongoClient("vcm-18515.vm.duke.edu" , 27017);
        //create or get database mydb
        this.mongoDatabase = mongoClient.getDatabase("mydb");
        System.out.println("Connect to database successfully");

        // if need refresh, drop the collections in the database
        if(this.refresh==true){
            mongoDatabase.getCollection("players").drop();
            System.out.println("drop players collection!");
            mongoDatabase.getCollection("games").drop();
            System.out.println("drop games collection!");
        }

        //create or get players collection
        this.playersCollection = mongoDatabase.getCollection("players");
        System.out.println("Choose players collection successfully");
        //create or get games collection
        this.gamesCollection = mongoDatabase.getCollection("games");
        System.out.println("Choose games collection successfully");
        this.js = new JSONSerializer();
    }

    /**
     * get the games collection
     */
    public MongoCollection<Document> getGamesCollection(){
      return this.gamesCollection;
    }

    /**
     * get the players collection
     */
    /*public MongoCollection<Document> getPlayersCollection(){
        return this.playersCollection;
    }*/

    /**
     * insert a player into the player collection
     */
    public synchronized void insertPlayersCollection(ServerPlayer player){
        Document document = new Document("playerName", player.getName());
        this.playersCollection.insertOne(document);
    }

    /**
     * insert a game into the games collection
     */
    public synchronized void insertGamesCollection(Game g){
        String s = js.serialize(g);
        Document document = new Document("gameID", g.getGameID()).append("description", s);
        gamesCollection.insertOne(document);
    }

    /**
     * update a particular game in the games collection
     */
    public synchronized void updateGamesCollection(Game g){
        String s = js.serialize(g);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("description", s); 
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument); 
        gamesCollection.updateOne(Filters.eq("gameID", g.getGameID()), updateObject);
    }

    /**
     * recover the player List from the database
     */
    public ArrayList<ServerPlayer> recoverPlayerList(){
        ArrayList<ServerPlayer> playerList = new ArrayList<>();
        FindIterable<Document> findIterable = playersCollection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){
            ServerPlayer sp = new ServerPlayer(null,null,null);
            sp.setName((String)mongoCursor.next().get("playerName"));
            sp.setCurrentGameID(-1);
            playerList.add(sp);
        }
        return playerList;
    }

    /**
     * recover the game list from the database
     */
    public ArrayList<Game> recoverGameList(){
        ArrayList<Game> res = new ArrayList<>();
        FindIterable<Document> findIterable1 = gamesCollection.find();  
        MongoCursor<Document> mongoCursor1= findIterable1.iterator();  
        while(mongoCursor1.hasNext()){
            String gameString = (String)mongoCursor1.next().get("description");
            Game g = (Game)js.deserialize(gameString, Game.class);
            res.add(g);
        }
        return res;
    }
   
}

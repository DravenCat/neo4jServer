package ca.utoronto.utm.mcs;

import java.util.ArrayList;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
public class Neo4jDAO {
	private final Driver driver;
	
	
    /**
     * Class constructor
     */
	
	public Neo4jDAO(Driver driver) {
	this.driver = driver;
	}
	
	
	 /**
     * This method will close the database driver
     */
	
	public void close() throws Exception {
		driver.close();
	}
	
	
    /**
     * This method will check whether the actor exists in the database
     * @param actorId The unique actorId of the actor
     * 
     * @return boolean True if the actor exists in the database 
     */
	
	public boolean searchActor(String actorId) {
		try (Session session = driver.session()) {
			Query query = new Query("MATCH (actor {actorid: $actorId}) RETURN actor");
			Result result = session.run(query.withParameters(Values.parameters("actorId", actorId)));
			return result.hasNext();
		}
	}
	
	
	/**
     * This method will add an actor in the database
     * @param name The name of the actor
     * @param actorId The unique actorId of the actor
     * 
     * @return 400 If the actor exists in the database
     *         200 If successfully added
     *         500 If saving or network error exists
     */
	
	public int addActor(String name, String actorId){		
		if (searchActor(actorId)) {
			return 400;
		}
		try (Session session = driver.session()) {
			Query query = new Query("CREATE (a:actor {name: $name, actorid: $actorId})");
			session.run(query.withParameters(Values.parameters("name", name, "actorId", actorId)));
			return 200;
		} catch(Exception e) {
			return 500;
		}
	}
	
	
    /**
     * This method will check whether the movie exists in the database
     * @param movieId The unique movieId of the movie
     * 
     * @return boolean True if the movie exists in the database 
     */
	
	public boolean searchMovie(String movieId) {
		try (Session session = driver.session()) {
			Query query = new Query("MATCH (movie {movieid: $movieId}) RETURN movie");
			Result result = session.run(query.withParameters(Values.parameters("movieId", movieId)));
			return result.hasNext();
		}
	}
	
	
	/**
     * This method will add an movie in the database
     * @param name The name of the movie
     * @param movieId The unique movieId of the movie
     * 
     * @return 400 If the movie exists in the database
     *         200 If successfully added
     *         500 If saving or network error exists
     */
	
	public int addMovie(String name, String movieId) {
		if (searchMovie(movieId)) {
			return 400;
		}
		try (Session session = driver.session()) {
			Query query = new Query("CREATE (m:movie {name: $name, movieid: $movieId})");
			session.run(query.withParameters(Values.parameters("name", name, "movieId", movieId)));
			return 200;
		} catch(Exception e) {
			return 500;
		}
	}
	
	
	/**
     * This method will check whether the actor has the relation ship with the movie
     * @param actorId The unique actorId of the actor
     * @param movieId The unique movieId of the movie
     * 
     * @return boolean True if the actor has the relationship with the movie
     * 
     * @exception e Exception if any session error occurred
     */

	public boolean hasRelationShip(String actorId, String movieId) throws Exception{
		try (Session session = driver.session()) {
			Query query = new Query(
					"MATCH (a:actor {actorid: $actorId}), "
					+ "(m:movie {movieid: $movieId}), "
					+ "rs=(a)-[]-(m) "
					+ "RETURN count(rs)");
			Result result = session.run(query.withParameters(
					Values.parameters("actorId", actorId, "movieId", movieId)));
			int numberOfRelationships = result.next().get(0).asInt();
			if (numberOfRelationships == 0) {
				return false;
			}
			return true;
		}
	}
	
	
	/**
     * This method will add an actor->movie relationship in the database
     * @param actorId The unique actorId of the actor
     * @param movieId The unique movieId of the movie
     * 
     * @return 400 If the movie exists in the database
     *         200 If successfully added
     *         500 If saving or network error exists
     *         
     * @exception e Exception if any session error occurred
     */
	public int addRelationShip(String actorId, String movieId) throws Exception {
		//check if the request is well formatted
	    if(!searchActor(actorId)) {
          return 404;
        }
        if(!searchMovie(movieId)) {
          return 404;
        }
		if (hasRelationShip(actorId, movieId)) {
			return 400;
		}
		try (Session session = driver.session()) {
			Query query = new Query("MATCH (a:actor {actorid: $actorId}), "
					+ "(m:movie {movieid: $movieId}) "
					+ "CREATE (a)-[r: ACTED_IN]->(m)");
			session.run(query.withParameters(Values.parameters("actorId", actorId, "movieId", movieId)));
			return 200;
		} catch(Exception e) {
			return 500;
		}
	}
	
	
	/**
     * This method will get all the movies that the actor has acted in
     * @param actorId The unique actorId of the actor
     * 
     * @return movies An arrayList that contains all the movieId that the actor has acted in
     *                Empty if the actor has not acted in any movie yet
     *                Null if the actor does not exist
     *                
     * @exception e Exception if any session error occurred
     */
	
	public ArrayList<String> getActor(String actorId) throws Exception{
		if (!searchActor(actorId)) {
			return null;
		}
		try (Session session = driver.session()) {
			ArrayList<String> movies = new ArrayList<String>();
			//query the database and add the result into an array list
			Query query = new Query("MATCH (a:actor {actorid: $actorId})"
					+ "--(m:movie) "
					+ "RETURN m.movieid as movieid");
			Result result = session.run(query.withParameters(Values.parameters("actorId", actorId)));
			
			for (Record record: result.list()) {
				movies.add(record.get("movieid").asString());
			}
			return movies;
		}
	}
		

    /**
     * This method will return the actor's with the actorId in the database
     * @param actorId The unique actorId of the actor
     * 
     * @return name The name of the actor
     *              Null if the actor does not exist
     *              
     * @exception e Exception if any session error occurred
     */
	
	public String findActor(String actorId) throws Exception{
		try (Session session = driver.session()) {
			Query query = new Query("MATCH (actor {actorid: $actorId}) RETURN actor.name AS name");
			Result result = session.run(query.withParameters(Values.parameters("actorId", actorId)));
			if (!result.hasNext()) {
				return null;
			}
			return result.next().get("name").asString();
		}
	}
	
	
    /**
     * This method computes the bacon number of the actor with the actorId in the database
     * @param actorId The unique actorId of the actor
     * 
     * @return baconNumber The bacon number of the actor
     *                     0 if the actor is bacon himself
     *                     -1 if the actor has not acted in any movie with bacon
     *                     
     * @exception e Exception if any session error occurred
     */
	public int computeBaconNumber(String actorId) throws Exception{
		if (actorId.equals("nm0000102")) {
			return 0;
		}
		
		try (Session session = driver.session()) {
			//construct "KNOWS" relationship between actors who have acted in the same movie
			Query queryKnows = new Query("MATCH (a1:actor)-[r1:ACTED_IN]->()<-[r2:ACTED_IN]-(a2:actor) "
					+ "MERGE (a1)-[k:KNOWS]-(a2)");
			session.run(queryKnows);
			
			//compute the number of actors on the baconPath
			Query queryNumber = new Query("MATCH (a1:actor {actorid: $actorId}), "
					+ "(KevinB:actor {actorid: \"nm0000102\"}), "
					+ "path = shortestPath((a1)-[:KNOWS*]-(KevinB)) "
					+ "RETURN LENGTH(path)");
			Result result = session.run(queryNumber.withParameters(Values.parameters("actorId", actorId)));
			if (!result.hasNext()) {
				return -1;
			}
			return result.next().get(0).asInt();
		}
	}
	
	
    /**
     * This method computes the bacon path of actors of the actor with the actorId in the database
     * @param actorId The unique actorId of the actor
     * 
     * @return baconPath The bacon path of actors without movieId
     *                   "nm0000102" if the actor is Bacon himself
     *                   Empty if the actor has not acted with Bacon
     *                   
     * @exception e Exception if any session error occurred                
     */
	public ArrayList<String> computeBaconPath(String actorId) throws Exception {
		ArrayList<String> baconPath = new ArrayList<String>();
		if (actorId.equals("nm0000102")) {
	        baconPath.add("nm0000102");
			return baconPath;
		}
		
		try (Session session = driver.session()) {
			//construct "KNOWS" relationship between actors who have acted in the same movie
			Query queryKnows = new Query("MATCH (a1:actor)-[r1:ACTED_IN]->()<-[r2:ACTED_IN]-(a2:actor)"
					+ "MERGE (a1)-[k:KNOWS]-(a2)");
			session.run(queryKnows);
			
			//find all the actors on the baconPath
			Query queryPath = new Query("MATCH (a1:actor {actorid: $actorId}), "
					+ "(KevinB:actor {actorid: \"nm0000102\"}), "
					+ "path = shortestPath((a1)-[:KNOWS*]-(KevinB)) "
					+ "UNWIND (nodes(path)) AS a "
					+ "RETURN a.actorid AS actorID");
			Result result = session.run(queryPath.withParameters(Values.parameters("actorId", actorId)));
			
			if (!result.hasNext()) {
				return baconPath;
			}
			
            for (Record record: result.list()) {
				baconPath.add(record.get("actorID").asString());
			}
		    return baconPath;
		}
	}
}


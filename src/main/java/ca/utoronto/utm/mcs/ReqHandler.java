package ca.utoronto.utm.mcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

public class ReqHandler implements HttpHandler {

    // TODO Complete This Class
    
    private Neo4jDAO neo4jDAO;
    
    
    /**
     * Class constructor
     * 
     * @param neo4jDAO
     */
    @Inject
    public ReqHandler(Neo4jDAO neo4jDAO) {
      this.neo4jDAO=neo4jDAO;
    }
    
    /**
     * This function is taken from Utils.java, since it could not be
     * resolved from the package
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String convert(InputStream inputStream) throws IOException {
      
      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
          return br.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
    
    
    /**
     * This method will handle request based on "PUT" or "GET" requests
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body=ReqHandler.convert(exchange.getRequestBody());
        //Check request type
        if(exchange.getRequestMethod().equals("GET")) {
          this.handleGet(exchange,body);
        }
        
        else if(exchange.getRequestMethod().equals("PUT")) {
          this.handlePut(exchange,body);    
        }
        else {
          exchange.sendResponseHeaders(405, -1);
          exchange.close();
        }
    }
        
    
    /**
     * This method will deal with "PUT" request.
     * 
     * @param exchange HttpExchange
     * @param body String request body
     * @throws IOException
     */
    private void handlePut(HttpExchange exchange,String body) throws IOException {
      try {
        JSONObject deserialized=new JSONObject(body);
        String path=exchange.getRequestURI().getPath();
        //check which api is used
        if(path.equals("/api/v1/addActor")) 
          this.addActor(exchange,deserialized);
        else if(path.equals("/api/v1/addMovie")) 
          this.addMovie(exchange,deserialized);
        else if(path.equals("/api/v1/addRelationship")) 
          this.addRelationship(exchange, deserialized); 
        //no api is found response status 400
        else exchange.sendResponseHeaders(400, -1);
        exchange.close();
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1); 
        exchange.close();
      }
    }
    
    
    /**
     * This method will deal with "GET" request.
     * 
     * @param exchange HttpExchange
     * @param body String request body
     * @throws IOException
     */
    private void handleGet(HttpExchange exchange,String body) throws IOException {
      try {
        JSONObject deserialized=new JSONObject(body);
        String path=exchange.getRequestURI().getPath();
        //check which api is used
        if(path.equals("/api/v1/getActor"))
          this.getActor(exchange,deserialized);
        else if(path.equals("/api/v1/hasRelationship")) 
          this.hasRelationship(exchange,deserialized);
        else if(path.equals("/api/v1/computeBaconNumber")) 
          this.computeBaconNumber(exchange,deserialized);
        else if(path.equals("/api/v1/computeBaconPath")) 
          this.computeBaconPath(exchange,deserialized);
        //no api is found response status 400
        else exchange.sendResponseHeaders(400, -1);
        exchange.close();
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();        
      }
   }


    /**
     * This method is for handling "/api/v1/addActor".
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void addActor(HttpExchange exchange,JSONObject deserialized) throws IOException {
     try {
       String actorId,name;
       //check the request body
       if(deserialized.has("name") && deserialized.has("actorId")) {
         actorId=deserialized.getString("actorId");
         name=deserialized.getString("name");
         //run addActor in the Neo4jDAO
         int status = this.neo4jDAO.addActor(name, actorId); 
         exchange.sendResponseHeaders(status, -1);
       }
       else 
         //incorrect request body response status 400
         exchange.sendResponseHeaders(400, -1);
       exchange.close();
       return;
     }
     catch(Exception e) {
       //server error response status 500
       exchange.sendResponseHeaders(500, -1);
       exchange.close();
     }
    }
    
    
    /**
     * This method is for handling "/api/v1/addMovie".
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void addMovie(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        String movieId,name;
        //check request body
        if(deserialized.has("name") && deserialized.has("movieId")) {
          movieId=deserialized.getString("movieId");
          name=deserialized.getString("name");
          //run addMovie in Neo4jDAO
          int status=this.neo4jDAO.addMovie(name, movieId);  
          exchange.sendResponseHeaders(status, -1);
        }
        else
          //incorrect request body response status 400
          exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }
      catch(Exception e) {
        //server error reponse status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }  
    }
    
    
    /**
     * This method is for handling "/api/v1/addRelationship".
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void addRelationship(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        String actorId,movieId;
        //check the request body
        if(deserialized.has("actorId") && deserialized.has("movieId")) {
          movieId=deserialized.getString("movieId");
          actorId=deserialized.getString("actorId");
          //run addRelationShip method in Neo4jDAO
          int status=this.neo4jDAO.addRelationShip(actorId, movieId); 
          exchange.sendResponseHeaders(status, -1);
        }
        else 
          //incorrect request body response status 400
          exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }  
    }

    
    /**
     * This method is for "/api/v1/getActor"
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void getActor(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        
        String actorId,name;
        //check request body
        if(deserialized.has("actorId")) {
          actorId=deserialized.getString("actorId");
          //check whether the actor exists
          if(this.neo4jDAO.searchActor(actorId)) {
            //run getActor method in the Neo4jDAO and covert it to array of string
            ArrayList<String>movieList=this.neo4jDAO.getActor(actorId);
            String movieArray[] = new String[movieList.size()];
            movieArray = movieList.toArray(movieArray);
            
            //get the actor name
            name = this.neo4jDAO.findActor(actorId);
            
            //Create response body
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json; charset=utf-8");
            
            JSONObject response=new JSONObject();
            response.put("actorId", actorId);
            response.put("name", name);
            response.put("movies", movieArray);
            
            String output=response.toString();
            exchange.sendResponseHeaders(200, output.length());
            OutputStream responseBody=exchange.getResponseBody();
            responseBody.write(output.getBytes());
            responseBody.close();
            return;
          }
          //actor does not exist response status 404
          else exchange.sendResponseHeaders(404, -1);         
        }
        //incorrect request body response status 400
        else exchange.sendResponseHeaders(400, -1);
        exchange.close();
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }
    }
    
    
    /**
     * This method is for "/api/v1/hasRelationship"
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException 
     */
    private void hasRelationship(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        String actorId, movieId;
        //check request body
        if(deserialized.has("actorId") && deserialized.has("movieId")) {
          actorId=deserialized.getString("actorId");
          movieId=deserialized.getString("movieId");
          //check if the actor and movie exists (if not response status 404)
          if(!this.neo4jDAO.searchActor(actorId)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          
          if(!this.neo4jDAO.searchMovie(movieId)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          //run hasRelationShip method in Neo4jDAO
          boolean target = this.neo4jDAO.hasRelationShip(actorId, movieId);
          //create response body
          Headers responseHeaders = exchange.getResponseHeaders();
          responseHeaders.set("Content-Type", "application/json; charset=utf-8");
          
          JSONObject response = new JSONObject();
          response.put("actorId", actorId);
          response.put("movieId", movieId);
          response.put("hasRelationship", target);
          
          String output=response.toString();
          exchange.sendResponseHeaders(200, output.length());
          OutputStream responseBody=exchange.getResponseBody();
          responseBody.write(output.getBytes());
          responseBody.close();
          return;      
        }
        else 
          //incorrect request body response status 400
          exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }     
    }
    
    
    /**
     * This method is for "/api/v1/computeBaconNumber"
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void computeBaconNumber(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        String actorId;
        //check request body
        if(deserialized.has("actorId")) {
          actorId=deserialized.getString("actorId");
          //check whether the searchActor and Bacon exist (if not response status 404)
          if(!this.neo4jDAO.searchActor(actorId)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          
          if(!this.neo4jDAO.searchActor("nm0000102")) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          //run computeBaconNumber in Neo4jDAO
          int baconNumber=this.neo4jDAO.computeBaconNumber(actorId);
          //if no path response status 404
          if(baconNumber==-1) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          //create response body
          Headers responseHeaders = exchange.getResponseHeaders();
          responseHeaders.set("Content-Type", "application/json; charset=utf-8");
          
          JSONObject response = new JSONObject();
          response.put("baconNumber", baconNumber);
          
          String output=response.toString();
          exchange.sendResponseHeaders(200, output.length());
          OutputStream responseBody=exchange.getResponseBody();
          responseBody.write(output.getBytes());
          responseBody.close();
          return;      
        }
        else 
          //incorrect request body response status 400
          exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }
    }
    
    
    /**
     * This method is for "/api/v1/computeBaconPath"
     * 
     * @param exchange HttpExchange
     * @param deserialized JSONObject request body
     * @throws IOException
     */
    private void computeBaconPath(HttpExchange exchange, JSONObject deserialized) throws IOException {
      try {
        String actorId;
        //Check request body
        if(deserialized.has("actorId")) {
          actorId=deserialized.getString("actorId");
          //check whether the actor and Bacon exist (if not response status 404)
          if(!this.neo4jDAO.searchActor(actorId)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          
          if(!this.neo4jDAO.searchActor("nm0000102")) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          
          //run computeBaconPath in Neo4jDAO
          ArrayList<String> baconPathList=this.neo4jDAO.computeBaconPath(actorId);
          //empty return repsonse status 404
          if(baconPathList.isEmpty()) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
          }
          
          // find the BaconPath given actors' list
          String baconPathArray[]=new String[baconPathList.size()];
          baconPathArray=baconPathList.toArray(baconPathArray);
          String movie[]=new String[baconPathList.size()-1];
          
          for(int i=0;i<baconPathArray.length-1;i++) {
            ArrayList<String> actor1_movie=this.neo4jDAO.getActor(baconPathArray[i]);
            ArrayList<String> actor2_movie=this.neo4jDAO.getActor(baconPathArray[i+1]);
            for(String movie1:actor1_movie) {
              for(String movie2:actor2_movie) {
                if(movie1.equals(movie2)){
                  movie[i]=movie1;
                  break;
                }
              }
            }
          }
          
          int actor_num=baconPathArray.length;
          int movie_num=movie.length;
          int actor_index=0,movie_index=0;
          String baconPath[]=new String[actor_num+movie_num];
          for(int j=0;j<actor_num+movie_num;j++) {
            if(j%2==0) {
              baconPath[j]=baconPathArray[actor_index];
              actor_index++;
            }
            else {
              baconPath[j]=movie[movie_index];
              movie_index++;
            }
          }
          //create response body
          Headers responseHeaders = exchange.getResponseHeaders();
          responseHeaders.set("Content-Type", "application/json; charset=utf-8");
          
          
          JSONObject response=new JSONObject();
          response.put("baconPath", baconPath);

          String output=response.toString();
          exchange.sendResponseHeaders(200, output.length());
          OutputStream responseBody=exchange.getResponseBody();
          responseBody.write(output.getBytes());
          responseBody.close();
          return;
        }
        else
          //incorrect request body response status 400
          exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }
      catch(Exception e) {
        //server error response status 500
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }
    }
    
}
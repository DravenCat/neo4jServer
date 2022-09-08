package ca.utoronto.utm.mcs;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;

public class App
{
    static int port = 8080;

    public static void main(String[] args) throws IOException
    {
        // TODO Create Your Server Context Here, There Should Only Be One Context
        //create Server
        ServerComponent serverComponent = DaggerServerComponent.create();
        Server server = serverComponent.buildServer();
        //Check HttpServer create successfully 
        if(server.checkServer()) {
          System.out.print("No server avaliable");
          throw new IOException();
        }
        //Get HttpServer
        HttpServer httpServer=server.getHttpServer();
       
        //Create ReqHandler
        ReqHandlerComponent reqComponent = DaggerReqHandlerComponent.create();
        ReqHandler reqHandler = reqComponent.buildHandler();
        
        
        //create content
        httpServer.createContext("/api/v1",reqHandler);
        //start server
        server.startServer();
    	System.out.printf("Server started on port %d\n", port);
    }
}

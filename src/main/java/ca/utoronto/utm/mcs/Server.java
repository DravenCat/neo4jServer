package ca.utoronto.utm.mcs;

import javax.inject.Inject;
import com.sun.net.httpserver.HttpServer;

public class Server {
    // TODO Complete This Class
  private HttpServer httpServer;
  
  /**
   * Class constructor
   * 
   * @param httpServer
   */
  @Inject
  public Server(HttpServer httpServer) { 
    this.httpServer = httpServer;
  }
  
  
  /**
   * This method checks whether HttpServer is successfully created.
   * 
   * @return Boolean
   */
  public Boolean checkServer() {
    if(this.httpServer==null) {
      return true;
    }
    return false;
  }
  
  
  /**
   * This method will get the class member httpserver.
   * 
   * @return HttpServer
   */
  public HttpServer getHttpServer() {
    return this.httpServer;
  }
  
  
  /**
   * This method will start the httpserver
   */
  public void startServer() {
    this.httpServer.start();
    return;
  }
}

package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerModule {
    // TODO Complete This Module
  @Provides
  public HttpServer provideHttpServer(){
      try {
        return HttpServer.create(new InetSocketAddress(App.port), 0);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        return null;
      }
  }

  
}

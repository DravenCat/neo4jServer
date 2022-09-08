package ca.utoronto.utm.mcs;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class ReqHandlerModule {
    // TODO Complete This Module
  @Provides
  public Neo4jDAO provideNeo4jDAO() {
    return new Neo4jDAO(GraphDatabase.driver(
        "bolt://localhost:7687", AuthTokens.basic("neo4j","1234" )));
  }
  
}

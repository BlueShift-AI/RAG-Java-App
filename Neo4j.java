package RAGCON;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class Neo4j {
    // Connect to Neo4j
    public static Driver connect(String uri, String user, String password) {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        return driver;
    }

    // Check the status of Neo4j
    public static boolean checkStatus(Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("RETURN 1");
            return result.hasNext();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Disconnect from Neo4j
    public static void disconnect(Driver driver) {
        driver.close();
    }
}

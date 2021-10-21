package reimburstment.services;;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {

    /*public Connection establishConnection() throws SQLException {

        //registering our JDBC driver in the classpath
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "p4ssw0rd";
        return DriverManager.getConnection(url, username, password);
    }*/

    public Connection establishConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String password = "welcome123";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

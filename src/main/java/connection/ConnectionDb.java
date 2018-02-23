package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {
    private static final java.lang.String URL = "jdbc:mysql://localhost:3306/javastudy?useSSL=false";
    private static final java.lang.String USERNAME = "root";
    private static final java.lang.String PASSWORD = "12345";
    private static ConnectionDb instance;
    private Connection connection;

    private ConnectionDb() {
        connection = getConnection();
    }

    public static synchronized ConnectionDb getInstance(){
        if (instance == null){
            instance = new ConnectionDb();
        }
        return instance;
    }

    public Connection getConnection(){
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDbConnected(){
        try {
            if (connection != null)
                return !connection.isClosed();
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

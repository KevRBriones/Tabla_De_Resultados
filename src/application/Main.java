package application;
	
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/formula1";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
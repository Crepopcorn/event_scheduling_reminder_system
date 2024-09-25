import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/event_scheduler";  // Change DB name if needed
    private static final String USER = "root";  // DB username
    private static final String PASSWORD = "Mypwd_3016";  // DB password

    // Get a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        }

        // Return connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:/Users/tanishqpadwal/Desktop/Helpdesk/MyHelpdeskDB.db"; // Update this path

    /**
     * Connect to the SQLite database
     * @return a Connection object to the database
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Execute a SELECT (query) SQL command
     * @param sql the SELECT SQL query
     * @return a ResultSet containing the results
     */
    public static ResultSet executeQuery(String sql) {
        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute an INSERT, UPDATE, or DELETE (update) SQL command
     * @param sql the SQL command to execute
     * @param parameters parameters for the prepared statement
     * @return true if the operation was successful, false otherwise
     */
    public static boolean executeUpdate(String sql, Object[] parameters) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }
}

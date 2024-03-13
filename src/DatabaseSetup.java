import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    private String url;

    public DatabaseSetup(String dbName) {
        this.url = "jdbc:sqlite:" + dbName;
    }

    /**
     * Connect to the SQLite database
     * @return the Connection object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Initialize database schema
     */
    public void initialize() {
        // SQL statement for creating a new Users table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (\n"
                + " UserID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " Name TEXT NOT NULL,\n"
                + " Email TEXT NOT NULL UNIQUE,\n"
                + " Role TEXT NOT NULL,\n"
                + " Password TEXT NOT NULL\n"
                + ");";

        // Adjusted SQL statement for creating a new ServiceRequests table
        String sqlServiceRequests = "CREATE TABLE IF NOT EXISTS ServiceRequests ("
                + " RequestID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " Problem TEXT NOT NULL,"
                + " Severity TEXT NOT NULL,"
                + " SubmittedBy INTEGER NOT NULL,"
                + " Description TEXT NOT NULL,"
                + " Priority TEXT,"
                + " AssignedTo INTEGER DEFAULT NULL,"
                + " Status TEXT DEFAULT 'Pending',"
                + " Comment TEXT,"
                + " Timestamp DATETIME CURRENT_TIMESTAMP,"
                + " ResolutionDate DATETIME DATETIME,"
                + " EstimatedResolutionDate DATETIME"
                + ");";





        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // Create the Users table
            stmt.execute(sqlUsers);
            // Create the ServiceRequests table with adjusted schema
            stmt.execute(sqlServiceRequests);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseSetup dbSetup = new DatabaseSetup("MyHelpdeskDB.db");
        dbSetup.initialize();
    }
}

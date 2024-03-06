import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int userID;
    private String name;
    private String email;
    private String role;
    private String password; // Consider hashing the password for security
    private boolean loggedIn;

    // Constructor
    public User(String name, String email, String role, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.loggedIn = false;
    }

    // Getters and setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    // Registration method
    public static String registerUser(String name, String email, String role, String password) {
        String sql = "INSERT INTO Users(Name, Email, Role, Password) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, role);
            pstmt.setString(4, password); // Hash the password for security
            pstmt.executeUpdate();
            return "Registration Successful";
        } catch (SQLException e) {
            return "Email already exists";
        }
    }

    // Login method
    public static boolean loginUser(String email, String password) {
        String sql = "SELECT Password FROM Users WHERE Email = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("Password"); // Compare hashed passwords for security
                if (storedPassword.equals(password)) { // This is a placeholder comparison, implement password hashing
                    // Set user as logged in (this logic should be expanded based on your application's requirements)
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}

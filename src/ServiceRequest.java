import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequest {
    private int requestID;
    private String problem;
    private String description;
    private int submittedBy;
    private Integer assignedTo; // This can be null if not yet assigned
    private String status;

    // Constructor to populate object from a ResultSet
    public ServiceRequest(ResultSet rs) throws SQLException {
        this.requestID = rs.getInt("RequestID");
        this.problem = rs.getString("Problem");
        this.description = rs.getString("Description");
        this.submittedBy = rs.getInt("SubmittedBy");
        this.assignedTo = (Integer) rs.getObject("AssignedTo");
        this.status = rs.getString("Status");
    }

    // Method to submit a new service request
    public static boolean submitRequest(int userID, String problem, String description) {
        String sql = "INSERT INTO ServiceRequests(Problem, Description, SubmittedBy, Status) VALUES(?,?,?, 'Submitted')";
        return DatabaseHelper.executeUpdate(sql, new Object[]{problem, description, userID});
    }

    // Method to view service requests by status
    public static List<ServiceRequest> viewRequestsByStatus(String status) {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM ServiceRequests WHERE Status = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(new ServiceRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching requests by status: " + e.getMessage());
        }
        return requests;
    }

    // Method to update the status of a service request
    public static boolean updateRequestStatus(int requestID, String newStatus) {
        String sql = "UPDATE ServiceRequests SET Status = ? WHERE RequestID = ?";
        return DatabaseHelper.executeUpdate(sql, new Object[]{newStatus, requestID});
    }

    // Getters and setters for all class fields
    public int getRequestID() { return requestID; }
    public String getProblem() { return problem; }
    public String getDescription() { return description; }
    public int getSubmittedBy() { return submittedBy; }
    public Integer getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }

    // You can add additional getters, setters, and other methods as needed.
}

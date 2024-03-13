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

    // Constructors, getters, and setters as needed

    // Submit a new service request
    public static boolean submitRequest(int userID, String problem, String description) {
        String sql = "INSERT INTO ServiceRequests(Problem, Description, SubmittedBy, Status) VALUES(?,?,?,?)";
        return DatabaseHelper.executeUpdate(sql, new Object[]{problem, description, userID, "Submitted"});
    }

    // View service requests, differentiating based on user role
    public static List<ServiceRequest> viewRequests(int userID, String userRole) {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql;

        if ("Helpdesk".equals(userRole) || "Manager".equals(userRole)) {
            sql = "SELECT * FROM ServiceRequests";
        } else {
            sql = "SELECT * FROM ServiceRequests WHERE SubmittedBy = ?";
        }

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!"Helpdesk".equals(userRole) && !"Manager".equals(userRole)) {
                pstmt.setInt(1, userID);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ServiceRequest sr = new ServiceRequest();
                sr.requestID = rs.getInt("RequestID");
                sr.problem = rs.getString("Problem");
                sr.description = rs.getString("Description");
                sr.submittedBy = rs.getInt("SubmittedBy");
                sr.assignedTo = (Integer) rs.getObject("AssignedTo"); // Handle possible null value
                sr.status = rs.getString("Status");
                requests.add(sr);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return requests;
    }

    // Assign a service request to a helpdesk user
    public static boolean assignRequest(int requestID, int helpdeskUserID) {
        String sql = "UPDATE ServiceRequests SET AssignedTo = ?, Status = 'InProgress' WHERE RequestID = ? AND Status = 'Submitted'";
        return DatabaseHelper.executeUpdate(sql, new Object[]{helpdeskUserID, requestID});
    }

    // Update the status of a service request
    public static boolean updateRequestStatus(int requestID, String newStatus, int userID, String userRole) {
        String sql = "UPDATE ServiceRequests SET Status = ? WHERE RequestID = ?";
        if ("Manager".equals(userRole)) {
            return DatabaseHelper.executeUpdate(sql, new Object[]{newStatus, requestID});
        } else {
            sql += " AND AssignedTo = ?";
            return DatabaseHelper.executeUpdate(sql, new Object[]{newStatus, requestID, userID});
        }

    }

    // Additional methods as needed for comments, etc.
}

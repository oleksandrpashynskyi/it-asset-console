package pashynskyi.INTERFACE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pashynskyi.DBSPT.DBConnection;

public interface Queryable {

    private static String getStyle() {
        return "<link rel='stylesheet' type='text/css' href='styles.css'>";
    }

    public static ResultSet conductLogin(String userID, String pw) {
        Connection conn = DBConnection.getFullConnection();
        try {
            String sql = "SELECT * FROM TechLogin WHERE TechID = ? AND Password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, pw);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String queryTechnicians(String techid) {
        StringBuilder html = new StringBuilder();
        html.append(getStyle()); 
        
        try (Connection conn = DBConnection.getFullConnection()) {
            if (conn == null) return "<h3>Database Connection Failed</h3>";

            String techSql = "SELECT * FROM Techs WHERE TechID = ?";
            PreparedStatement techStmt = conn.prepareStatement(techSql);
            techStmt.setString(1, techid);
            ResultSet rs = techStmt.executeQuery();

            if (rs.next()) {
                html.append("<table>");
                html.append("<tr><th>Tech ID</th><td>").append(rs.getString("TechID")).append("</td></tr>");
                html.append("<tr><th>Tech Name</th><td>").append(rs.getString("FullName")).append("</td></tr>");
                html.append("<tr><th>Tech Role</th><td>").append(rs.getString("TechRole")).append("</td></tr>");
                html.append("</table>");
            } else {
                return "<div class='container'><h3>No Technician found with ID: " + techid + "</h3></div>";
            }

            String compSql = "SELECT * FROM Workstations WHERE AssignedTech = ?";
            PreparedStatement compStmt = conn.prepareStatement(compSql);
            compStmt.setString(1, techid);
            ResultSet rsComp = compStmt.executeQuery();

            boolean found = false;
            while (rsComp.next()) {
                found = true;
                html.append("<table>");
                html.append("<tr><th>Computer ID</th><td>").append(rsComp.getString("CmptrID")).append("</td></tr>");
                html.append("<tr><th>Computer OS</th><td>").append(rsComp.getString("CmptrOS")).append("</td></tr>");
                html.append("<tr><th>Located On Floor</th><td>").append(rsComp.getInt("CmptrLocation")).append("</td></tr>");
                html.append("<tr><th>Status</th><td>").append(rsComp.getString("CmptrStatus")).append("</td></tr>");
                html.append("</table>");
            }
            if (!found) html.append("<p>No workstations assigned.</p>");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

    public static String queryUsers(String userid) {
        StringBuilder html = new StringBuilder();
        html.append(getStyle());
        
        try (Connection conn = DBConnection.getFullConnection()) {
             if (conn == null) return "<h3>Database Connection Failed</h3>";

            // 1. User Info Table
            String userSql = "SELECT * FROM Users WHERE UserID = ?";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, userid);
            ResultSet rs = userStmt.executeQuery();

            if (rs.next()) {
                html.append("<table>");
                html.append("<tr><th>User ID</th><td>").append(rs.getString("UserID")).append("</td></tr>");
                html.append("<tr><th>User Name</th><td>").append(rs.getString("FullName")).append("</td></tr>");
                html.append("<tr><th>User Role</th><td>").append(rs.getString("UserRole")).append("</td></tr>");
                html.append("</table>");
            } else {
                return "<div class='container'><h3>No User found with ID: " + userid + "</h3></div>";
            }

            String compSql = "SELECT * FROM Workstations WHERE AssignedUser = ?";
            PreparedStatement compStmt = conn.prepareStatement(compSql);
            compStmt.setString(1, userid);
            ResultSet rsComp = compStmt.executeQuery();

            while (rsComp.next()) {
                html.append("<table>");
                html.append("<tr><th>Computer ID</th><td>").append(rsComp.getString("CmptrID")).append("</td></tr>");
                html.append("<tr><th>Computer OS</th><td>").append(rsComp.getString("CmptrOS")).append("</td></tr>");
                html.append("<tr><th>Located On Floor</th><td>").append(rsComp.getInt("CmptrLocation")).append("</td></tr>");
                html.append("<tr><th>Status</th><td>").append(rsComp.getString("CmptrStatus")).append("</td></tr>");
                html.append("<tr><th>Technician Assigned</th><td>").append(rsComp.getString("AssignedTech")).append("</td></tr>");
                html.append("</table>");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

    public static String queryComputers(String cmptrid) {
        StringBuilder html = new StringBuilder();
        html.append(getStyle());
        
        try (Connection conn = DBConnection.getFullConnection()) {
             if (conn == null) return "<h3>Database Connection Failed</h3>";

            String sql = "SELECT * FROM Workstations WHERE CmptrID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cmptrid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                html.append("<table>");
                html.append("<tr><th>Computer ID</th><td>").append(rs.getString("CmptrID")).append("</td></tr>");
                html.append("<tr><th>Computer OS</th><td>").append(rs.getString("CmptrOS")).append("</td></tr>");
                html.append("<tr><th>Located On Floor</th><td>").append(rs.getInt("CmptrLocation")).append("</td></tr>");
                html.append("<tr><th>Status</th><td>").append(rs.getString("CmptrStatus")).append("</td></tr>");
                html.append("<tr><th>Technician Assigned</th><td>").append(rs.getString("AssignedTech")).append("</td></tr>");
                html.append("</table>");
            } else {
                return "<div class='container'><h3>No Workstation found with ID: " + cmptrid + "</h3></div>";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html.toString();
    }
}
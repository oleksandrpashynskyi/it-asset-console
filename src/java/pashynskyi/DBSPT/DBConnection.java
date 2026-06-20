package pashynskyi.DBSPT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    
    private static final String DBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";  // Update if your SQL password is different
    private static final String DB_PASS = "root";  // Update if your SQL password is different
    private static final String DB_NAME = "itassetdb";
    
    private static Connection connectToServe() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DBC_URL, DB_USER, DB_PASS);
    }
    
    public static Connection getFullConnection() {
        try {
            Connection conn = connectToServe();
            Statement state = conn.createStatement();
            state.execute("USE " + DB_NAME); // Switch to itassetdb
            state.close();
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
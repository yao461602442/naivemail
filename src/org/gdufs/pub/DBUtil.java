package org.gdufs.pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	public static Connection getConnection() {
		Connection conn = null;
		try {
			// Á¬½ÓSQLiteµÄJDBC
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:naivemail.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn) { 
        if (conn == null) 
            return; 
        try { 
            conn.close(); 
        } catch (SQLException e) { 
            System.out.println("ctcjz.DBUtils: Cannot close connection."); 
        } 
    } 
    public static void close(Statement stmt) { 
        try { 
            if (stmt != null) { 
                stmt.close(); 
            } 
        } catch (SQLException e) { 
            System.out.println("ajax.DBUtils: Cannot close statement."); 
        } 
    } 
    public static void close(ResultSet rs) { 
        try { 
            if (rs != null) { 
                rs.close(); 
            } 
        } catch (SQLException e) { 
            System.out.println("ctcjz.DBUtils: Cannot close resultset."); 
        } 
    } 
}

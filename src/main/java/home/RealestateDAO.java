package home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RealestateDAO {
	private final String url = "jdbc:postgresql://localhost:5432/realestate?useSSL=false";
	private final String user = "katsuyaharada";
	private final String password = "tkznemou19";

	public Connection getConnection() {
		Connection conn = null;
		
		try {
			// JDBCドライバのロード
			Class.forName("org.postgresql.Driver");
			
			// データベースへ接続
			conn = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	// Connection型変数が持つデータベースとJDBCリソースの解放
	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// PreparedStatement型変数が持つデータベースとJDBCリソースの解放
	public void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ResultSet型変数が持つデータベースとJDBCリソースの解放
	public void close(ResultSet rset) {
		if (rset != null) {
			try {
				rset.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

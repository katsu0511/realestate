package home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager extends RealestateDAO {
	// ユーザー名とパスワードを受け取り、登録ユーザー一覧に一致したものがあるか検索
	public UserDTO getLoginUser(String userName, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM cmpn WHERE usernm=? AND passwd=?";
		UserDTO user = null;
		
		try {
			// データベース接続情報取得
			conn = getConnection();
			
			// SELECT文の登録と実行
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			rset = pstmt.executeQuery();
			
			// 検索結果があれば
			if (rset.next()) {
				user = new UserDTO();
				user.setUserName(rset.getString(5));
				user.setPassword(rset.getString(6));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		
		return user;
	}
}

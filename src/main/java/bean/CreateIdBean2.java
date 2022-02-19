package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import home.RealestateDAO;

public class CreateIdBean2 {
	public String getCreatedId(String table, String id, String cmpnid) {
		// 接続情報
		RealestateDAO db = new RealestateDAO();
		Connection conn = null;
		
		// SQL情報管理
		PreparedStatement pstmt1 = null;
		ResultSet rset1 = null;
		
		// 情報管理
		String last_id = null;
		String created_id = null;
		
		try {
			// データベース接続情報取得
			conn = db.getConnection();
			
			// 最後に登録されたidを取得
			String sql1 = "SELECT " + id + " FROM " + table + " WHERE cmpnid=? ORDER BY " + id + " DESC LIMIT 1";
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, cmpnid);
			rset1 = pstmt1.executeQuery();
			
			// 年月の取得
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			String yearStr = String.valueOf(year);
			String yearLast2Digit = yearStr.substring(2,4);
		    int month = calendar.get(Calendar.MONTH) + 1;
		    String month_hex = Integer.toHexString(month).toUpperCase();
		    
		    // 検索結果があれば
			if (rset1.next()) {
				last_id = rset1.getString(1);
			} else {
				last_id = "2110000000";
			}
			
			String lastid_year = last_id.substring(0,2);
			String lastid_month = last_id.substring(2,3);
			String lastid_num1 = last_id.substring(3,10);
			int lastid_num2 = Integer.parseInt(lastid_num1);
			String newid_num = String.format("%07d", lastid_num2 + 1);
			String start_id = "0000001";
			
			 // created_idの生成
			if (!lastid_year.equals(yearLast2Digit)) {
				created_id = yearLast2Digit + month_hex + start_id;
			} else if (!lastid_month.equals(month_hex)) {
				created_id = lastid_year + month_hex + start_id;
			} else if (lastid_year.equals(yearLast2Digit) && lastid_month.equals(month_hex)) {
				created_id = lastid_year + lastid_month + newid_num;
		    }
			
			created_id += "_" + cmpnid;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt1.close();
			} catch (SQLException e) { }
			
			try {
				conn.close();
			} catch (SQLException e) {  }
		}
		
		return created_id;
	}
}

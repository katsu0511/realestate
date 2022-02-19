package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import home.RealestateDAO;

public class CreateIdBean {
	public String getCreatedId(String table, String initial, String cmpnid) {
		// 接続情報
		RealestateDAO db = new RealestateDAO();
		Connection conn = null;
		
		// SQL情報管理
		PreparedStatement pstmt1 = null;
		ResultSet rset1 = null;
		
		// 情報管理
		String last_id = null;
		String id = null;
		
		try {
			// データベース接続情報取得
			conn = db.getConnection();
			
			// 最後に登録されたidを取得
			String addSql = cmpnid.equals("") ? "" : " WHERE cmpnid='" + cmpnid + "'";
			String sql1 = "SELECT " + table + "id FROM " + table + addSql + " ORDER BY rgdttm DESC LIMIT 1";
			pstmt1 = conn.prepareStatement(sql1);
			rset1 = pstmt1.executeQuery();
			
			// 年月の取得
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR) - 2021;
			if (year > 15) {// yearが16以上にならないように設定
				int a = (year - 16) / 16;
				year = year - 16 * (a + 1);
			}
			String year_hex = Integer.toHexString(year);
		    int month = calendar.get(Calendar.MONTH) + 1;
		    String month_hex = Integer.toHexString(month).toUpperCase();
		    
		    // 検索結果があれば
			if (rset1.next()) {
				last_id = rset1.getString(1);
			} else {
				last_id = initial + "110000";
			}
			
			String lastid_year = last_id.substring(1,2);
			String lastid_month = last_id.substring(2,3);
			String lastid_num1 = last_id.substring(3,7);
			int lastid_num2 = Integer.parseInt(lastid_num1);
			String newid_num = String.format("%04d", lastid_num2 + 1);
			String start_num = "0001";
		    
		    // idの生成
			if (!lastid_year.equals(year_hex)) {
				id = initial + year_hex + month_hex + start_num;
			} else if (!lastid_month.equals(month_hex)) {
				id = initial + lastid_year + month_hex + start_num;
			} else if (lastid_year.equals(year_hex) && lastid_month.equals(month_hex)) {
				id = initial + lastid_year + lastid_month + newid_num;
		    }
			
			id = cmpnid.equals("") ? id : id + "_" + cmpnid;
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
		
		return id;
	}
}

package edit;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import home.RealestateDAO;

/**
 * Servlet implementation class EditTenantServlet
 */
@WebServlet("/edit/tenant")
public class EditTenantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditTenantServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userName") == null || session.getAttribute("password") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			// 文字化け対策
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			// 接続情報
			RealestateDAO db = new RealestateDAO();
			Connection conn = null;
			
			// SQL
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			PreparedStatement pstmt7 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			ResultSet rset6 = null;
			ResultSet rset7 = null;
			boolean data_exists = false;
			boolean current_rsdc_exists = false;
			ArrayList<String> rsdcids = new ArrayList<String>();
			
			// tnnt_idの取得
			String tnnt_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// tnnt情報を取得
				String sql1 = "SELECT * FROM tnnt WHERE tnntid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, tnnt_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> tenant = new ArrayList<>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 33; i++){
						tenant.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("tenant", tenant);
				
				// 担当者情報を取得
				String sql2 = "SELECT rppsid,rppsnm FROM rpps WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, cmpn_id);
				rset2 = pstmt2.executeQuery();
				ArrayList<Map<String, String>> rppns = new ArrayList<Map<String, String>>();
				
				while (rset2.next()) {
					Map<String, String> rppn = new HashMap<>();
					rppn.put("rppsid", rset2.getString(1));
					rppn.put("rppsnm", rset2.getString(2));
					rppns.add(rppn);
				}
				
				request.setAttribute("rppns", rppns);
				
				// 物件情報を取得
				String sql3 = "SELECT rsdcid FROM rsdc WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, cmpn_id);
				rset3 = pstmt3.executeQuery();
				ArrayList<Map<String, String>> rsdcs = new ArrayList<Map<String, String>>();
				
				while (rset3.next()) {
					rsdcids.add(rset3.getString(1));
				}
				
				for (String rsdcid: rsdcids) {
					String sql4 = "SELECT roomid FROM room WHERE cmpnid=? AND rsdcid=? AND CNTRCT='f'";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, cmpn_id);
					pstmt4.setString(2, rsdcid);
					rset4 = pstmt4.executeQuery();
					
					if (rset4.next()) {
						String sql5 = "SELECT rsdcnm FROM rsdc WHERE cmpnid=? AND rsdcid=?";
						pstmt5 = conn.prepareStatement(sql5);
						pstmt5.setString(1, cmpn_id);
						pstmt5.setString(2, rsdcid);
						rset5 = pstmt5.executeQuery();
						
						if (rset5.next()) {
							Map<String, String> rsdc = new HashMap<>();
							rsdc.put("rsdcid", rsdcid);
							rsdc.put("rsdcnm", rset5.getString(1));
							rsdcs.add(rsdc);
						}
					}
				}
				
				for (Map<String, String> rsdc: rsdcs) {
					if (rsdc.get("rsdcid").equals(tenant.get(3))) {
						current_rsdc_exists = true;
					}
				}
				
				if (!current_rsdc_exists) {
					String sql6 = "SELECT rsdcnm FROM rsdc WHERE cmpnid=? AND rsdcid=?";
					pstmt6 = conn.prepareStatement(sql6);
					pstmt6.setString(1, cmpn_id);
					pstmt6.setString(2, tenant.get(3));
					rset6 = pstmt6.executeQuery();
					
					if (rset6.next()) {
						Map<String, String> rsdc = new HashMap<>();
						rsdc.put("rsdcid", tenant.get(3));
						rsdc.put("rsdcnm", rset6.getString(1));
						rsdcs.add(rsdc);
					}
				}
				
				request.setAttribute("rsdcs", rsdcs);
				
				// 部屋情報を取得
				String sql7 = "SELECT roomid,roomnu FROM room WHERE rsdcid=("
						    +     "SELECT rsdcid FROM tnnt WHERE tnntid=?"
						    + ") "
						    + "AND cmpnid=? AND actvfg='t' "
						    + "ORDER BY roomnu ASC";
				pstmt7 = conn.prepareStatement(sql7);
				pstmt7.setString(1, tnnt_id);
				pstmt7.setString(2, cmpn_id);
				rset7 = pstmt7.executeQuery();
				ArrayList<Map<String, String>> rooms = new ArrayList<Map<String, String>>();
				
				while (rset7.next()) {
					Map<String, String> room = new HashMap<>();
					room.put("roomid", rset7.getString(1));
					room.put("roomnu", rset7.getString(2));
					rooms.add(room);
				}
				
				request.setAttribute("rooms", rooms);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					pstmt2.close();
				} catch (SQLException e) { }
				
				try {
					pstmt3.close();
				} catch (SQLException e) { }
				
				try {
					if (rsdcids != null) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (rset4.next()) {
						pstmt5.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (!current_rsdc_exists && rset6.next()) {
						pstmt6.close();
					}
				} catch (SQLException e) { }
				
				try {
					pstmt7.close();
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (data_exists) {
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_tenant.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/tenant");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userName") == null || session.getAttribute("password") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			// 文字化け対策
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			// 接続情報
			RealestateDAO db = new RealestateDAO();
			Connection conn = null;
			
			// SQL
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			
			// 送信情報の取得
			String tnnt_id = request.getParameter("tnnt_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rp_prsn = request.getParameter("rp_prsn");
			String cont_rsd = request.getParameter("cont_rsd");
			String cont_room = request.getParameter("cont_room");
			String cont_date = request.getParameter("cont_date");
			String tnnt_name = request.getParameter("tnnt_name").replace("　", " ");
			String tnnt_kana = request.getParameter("tnnt_kana").replace("　", " ");
			String zip1 = request.getParameter("zip1");
			String zip2 = request.getParameter("zip2");
			String address1 = request.getParameter("address1");
			String address2 = request.getParameter("address2");
			String address3 = request.getParameter("address3");
			String address4 = request.getParameter("address4");
			String number1 = request.getParameter("number1");
			String number2 = request.getParameter("number2");
			String email = request.getParameter("email");
			String birthday = request.getParameter("birthday");
			String bank = request.getParameter("bank");
			String bank_branch = request.getParameter("bank_branch");
			String account_type = request.getParameter("account_type");
			String account_number = request.getParameter("account_number");
			String account_name = request.getParameter("account_name");
			String wkplc_name = request.getParameter("wkplc_name").replace("　", " ");
			String wkplc_kana = request.getParameter("wkplc_kana").replace("　", " ");
			String wkplc_zip1 = request.getParameter("wkplc_zip1");
			String wkplc_zip2 = request.getParameter("wkplc_zip2");
			String wkplc_zip = wkplc_zip1.equals("") || wkplc_zip2.equals("") ? "" : wkplc_zip1 + "-" + wkplc_zip2;
			String wkplc_address1 = request.getParameter("wkplc_address1");
			String wkplc_address2 = request.getParameter("wkplc_address2");
			String wkplc_address3 = request.getParameter("wkplc_address3");
			String wkplc_address4 = request.getParameter("wkplc_address4");
			String actv_flg = request.getParameter("actv_flg");
			Boolean actvflg = Boolean.valueOf(actv_flg);
			String comment = request.getParameter("comment");
			String room_bfo_change = request.getParameter("room_bfo_change");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// SQL実行
				String sql1 = "UPDATE tnnt SET rppsid=?,rsdcid=?,roomid=?,contdt=?,tnntnm=?,tnntnf=?,zipcod=?,statnm=?,citynm=?,strno1=?,strno2=?,phone1=?,phone2=?,emladr=?,brthdy=?,banknm=?,bankbr=?,acntty=?,acntnu=?,acntnm=?,wkpcnm=?,wkpcnf=?,wkpzip=?,wkpsta=?,wkpcit=?,wkpst1=?,wkpst2=?,actvfg=?,commnt=?,uddttm=current_timestamp WHERE tnntid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rp_prsn);
				pstmt1.setString(2, cont_rsd);
				pstmt1.setString(3, cont_room);
				pstmt1.setString(4, cont_date);
				pstmt1.setString(5, tnnt_name);
				pstmt1.setString(6, tnnt_kana);
				pstmt1.setString(7, zip1 + '-' + zip2);
				pstmt1.setString(8, address1);
				pstmt1.setString(9, address2);
				pstmt1.setString(10, address3);
				pstmt1.setString(11, address4);
				pstmt1.setString(12, number1);
				pstmt1.setString(13, number2);
				pstmt1.setString(14, email);
				pstmt1.setString(15, birthday);
				pstmt1.setString(16, bank);
				pstmt1.setString(17, bank_branch);
				pstmt1.setString(18, account_type);
				pstmt1.setString(19, account_number);
				pstmt1.setString(20, account_name);
				pstmt1.setString(21, wkplc_name);
				pstmt1.setString(22, wkplc_kana);
				pstmt1.setString(23, wkplc_zip);
				pstmt1.setString(24, wkplc_address1);
				pstmt1.setString(25, wkplc_address2);
				pstmt1.setString(26, wkplc_address3);
				pstmt1.setString(27, wkplc_address4);
				pstmt1.setBoolean(28, actvflg);
				pstmt1.setString(29, comment);
				pstmt1.setString(30, tnnt_id);
				pstmt1.setString(31, cmpn_id);
				int edited1 = pstmt1.executeUpdate();
				int edited2 = 0;
				int edited3 = 0;
				
				// 契約中の部屋が変わっている場合
				if (!room_bfo_change.equals(cont_room)) {
					// room_bfo_changeのroomのcntrctをfalseに変更
					String sql2 = "UPDATE room SET cntrct=? WHERE roomid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setBoolean(1, false);
					pstmt2.setString(2, room_bfo_change);
					pstmt2.setString(3, cmpn_id);
					edited2 = pstmt2.executeUpdate();
					
					// roomidのroomのcntrctをtrueに変更
					String sql3 = "UPDATE room SET cntrct=? WHERE roomid=? AND cmpnid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setBoolean(1, true);
					pstmt3.setString(2, cont_room);
					pstmt3.setString(3, cmpn_id);
					edited3 = pstmt3.executeUpdate();
				}
				
				// 変更できていない場合はロールバック
				// 契約中の部屋が変わっている場合
				if (!room_bfo_change.equals(cont_room)) {
					if (!room_bfo_change.equals("") && !cont_room.equals("")) {
						if (edited1 > 0 && edited2 > 0 && edited3 > 0) {
							conn.commit();
						} else {
							conn.rollback();
						}
					} else if (!room_bfo_change.equals("") && cont_room.equals("")) {
						if (edited1 > 0 && edited2 > 0) {
							conn.commit();
						} else {
							conn.rollback();
						}
					} else if (room_bfo_change.equals("") && !cont_room.equals("")) {
						if (edited1 > 0 && edited3 > 0) {
							conn.commit();
						} else {
							conn.rollback();
						}
					}
				} else {
					if (edited1 > 0) {
						conn.commit();
					} else {
						conn.rollback();
					}
				}
				
				// エンコード
				tnnt_id = URLEncoder.encode(tnnt_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rp_prsn = URLEncoder.encode(rp_prsn, "UTF-8");
				cont_rsd = URLEncoder.encode(cont_rsd, "UTF-8");
				cont_room = URLEncoder.encode(cont_room, "UTF-8");
				cont_date = URLEncoder.encode(cont_date, "UTF-8");
				tnnt_name = URLEncoder.encode(tnnt_name, "UTF-8");
				tnnt_kana = URLEncoder.encode(tnnt_kana, "UTF-8");
				zip1 = URLEncoder.encode(zip1, "UTF-8");
				zip2 = URLEncoder.encode(zip2, "UTF-8");
				address1 = URLEncoder.encode(address1, "UTF-8");
				address2 = URLEncoder.encode(address2, "UTF-8");
				address3 = URLEncoder.encode(address3, "UTF-8");
				address4 = URLEncoder.encode(address4, "UTF-8");
				number1 = URLEncoder.encode(number1, "UTF-8");
				number2 = URLEncoder.encode(number2, "UTF-8");
				email = URLEncoder.encode(email, "UTF-8");
				birthday = URLEncoder.encode(birthday, "UTF-8");
				bank = URLEncoder.encode(bank, "UTF-8");
				bank_branch = URLEncoder.encode(bank_branch, "UTF-8");
				account_type = URLEncoder.encode(account_type, "UTF-8");
				account_number = URLEncoder.encode(account_number, "UTF-8");
				account_name = URLEncoder.encode(account_name, "UTF-8");
				wkplc_name = URLEncoder.encode(wkplc_name, "UTF-8");
				wkplc_kana = URLEncoder.encode(wkplc_kana, "UTF-8");
				wkplc_zip1 = URLEncoder.encode(wkplc_zip1, "UTF-8");
				wkplc_zip2 = URLEncoder.encode(wkplc_zip2, "UTF-8");
				wkplc_address1 = URLEncoder.encode(wkplc_address1, "UTF-8");
				wkplc_address2 = URLEncoder.encode(wkplc_address2, "UTF-8");
				wkplc_address3 = URLEncoder.encode(wkplc_address3, "UTF-8");
				wkplc_address4 = URLEncoder.encode(wkplc_address4, "UTF-8");
				actv_flg = URLEncoder.encode(actv_flg, "UTF-8");
				comment = URLEncoder.encode(comment, "UTF-8");
				
				// オートコミットをオン
				conn.setAutoCommit(true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					if (!room_bfo_change.equals(cont_room)) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (!room_bfo_change.equals(cont_room)) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			String url = request.getContextPath() + "/show/tenant?id=" + tnnt_id;
			response.sendRedirect(url);
		}
	}

}

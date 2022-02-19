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
 * Servlet implementation class EditRoomServlet
 */
@WebServlet("/edit/room")
public class EditRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRoomServlet() {
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
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			boolean data_exists = false;
			
			// room_idの取得
			String room_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// room情報を取得
				String sql1 = "SELECT * FROM room WHERE roomid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, room_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> room = new ArrayList<>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 15; i++){
						room.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("room", room);
				
				// rsdcnmの取得
				String sql2 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=("
				            +     "SELECT rsdcid FROM room WHERE roomid=?"
				            + ") "
				            + "AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, room_id);
				pstmt2.setString(2, cmpn_id);
				rset2 = pstmt2.executeQuery();
				String rsdcnm = null;
				
				if (rset2.next()) {
					rsdcnm = rset2.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("rsdcnm", rsdcnm);
				
				// 部屋登録で使われている追加費用項目のaditid,adehvlをadehから取得
				String sql3 = "SELECT aditid,adehvl FROM adeh WHERE cmpnid=? AND roomid=? ORDER BY aditid ASC";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, cmpn_id);
				pstmt3.setString(2, room_id);
				rset3 = pstmt3.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				while (rset3.next()) {
					// aditの情報を取得
					String sql4 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, rset3.getString(1));
					pstmt4.setString(2, cmpn_id);
					rset4 = pstmt4.executeQuery();
					
					if (rset4.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset4.getString(1));
						adit.put("itemnm", rset4.getString(3));
						adit.put("itemid", rset4.getString(4));
						adit.put("maxlng", rset4.getString(5));
						adit.put("spclng", rset4.getString(6));
						adit.put("commnt", rset4.getString(7));
						adit.put("adehvl", rset3.getString(2));
						adits.add(adit);
					}
				}
				
				request.setAttribute("adits", adits);
				
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
					if (rset3.next()) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (data_exists) {
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_room.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/room");
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
			PreparedStatement pstmt4 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			int row = 0;
			int su = 0;
			
			// 送信情報の取得
			String room_id = request.getParameter("room_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rent = request.getParameter("rent");
			String mng_fee = request.getParameter("mng_fee");
			String deposit = request.getParameter("deposit");
			String honorarium = request.getParameter("honorarium");
			String brokerage = request.getParameter("brokerage");
			String key_fee = request.getParameter("key_fee");
			String actv_flg = request.getParameter("actv_flg");
			Boolean actvfg = Boolean.valueOf(actv_flg);
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 部屋登録で使われている追加費用項目のadehid,aditidをadehから取得
				String sql1 = "SELECT adehid,aditid FROM adeh WHERE cmpnid=? AND roomid=? ORDER BY adehid ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, room_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				while (rset1.next()) {
					// itemidを取得
					String sql2 = "SELECT itemid FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(2));
					pstmt2.setString(2, cmpn_id);
					rset2 = pstmt2.executeQuery();
					
					if (rset2.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("adehid", rset1.getString(1));
						adit.put("itemid", rset2.getString(1));
						adits.add(adit);
					}
				}
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// roomに保存
				String sql3 = "UPDATE room SET rentfe=?,mngfee=?,depsit=?,hnrrum=?,brkrag=?,keyfee=?,actvfg=?,commnt=?,uddttm=current_timestamp WHERE roomid=? AND cmpnid=?";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, rent);
				pstmt3.setString(2, mng_fee);
				pstmt3.setString(3, deposit);
				pstmt3.setString(4, honorarium);
				pstmt3.setString(5, brokerage);
				pstmt3.setString(6, key_fee);
				pstmt3.setBoolean(7, actvfg);
				pstmt3.setString(8, comment);
				pstmt3.setString(9, room_id);
				pstmt3.setString(10, cmpn_id);
				int edited1 = pstmt3.executeUpdate();
				
				int edited2 = 0;
				for (int i = 0; i < adits.size(); i++) {
					edited2 = 0;
					String adehid = adits.get(i).get("adehid");
					String adehvl = request.getParameter(adits.get(i).get("itemid"));
					
					// adehに保存
					String sql4 = "UPDATE adeh SET adehvl=?,uddttm=current_timestamp WHERE adehid=? AND cmpnid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, adehvl);
					pstmt4.setString(2, adehid);
					pstmt4.setString(3, cmpn_id);
					edited2 = pstmt4.executeUpdate();
					row += edited2;
					su++;
				}
				
				// 保存できていない場合はロールバック
				if (edited1 == 1 && row == adits.size()) {
					conn.commit();
				} else {
					conn.rollback();
				}
				
				// オートコミットをオン
				conn.setAutoCommit(true);
				
				// エンコード
				room_id = URLEncoder.encode(room_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rent = URLEncoder.encode(rent, "UTF-8");
				mng_fee = URLEncoder.encode(mng_fee, "UTF-8");
				deposit = URLEncoder.encode(deposit, "UTF-8");
				honorarium = URLEncoder.encode(honorarium, "UTF-8");
				brokerage = URLEncoder.encode(brokerage, "UTF-8");
				key_fee = URLEncoder.encode(key_fee, "UTF-8");
				actv_flg = URLEncoder.encode(actv_flg, "UTF-8");
				comment = URLEncoder.encode(comment, "UTF-8");
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					if (rset1.next()) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					pstmt3.close();
				} catch (SQLException e) { }
				
				try {
					if (su > 0) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			String url = request.getContextPath() + "/show/room?id=" + room_id;
			response.sendRedirect(url);
		}
	}

}

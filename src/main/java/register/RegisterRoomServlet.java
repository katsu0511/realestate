package register;

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

import bean.CreateIdBean2;
import home.RealestateDAO;

/**
 * Servlet implementation class RegisterRoomServlet
 */
@WebServlet("/register/room")
public class RegisterRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterRoomServlet() {
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
			
			// SQL情報管理
			PreparedStatement pstmt1 = null;
			ResultSet rset1 = null;
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 物件情報を取得
				String sql1 = "SELECT rsdcid,rsdcnm FROM rsdc WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> rsdcs = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					Map<String, String> rsdc = new HashMap<>();
					rsdc.put("rsdcid", rset1.getString(1));
					rsdc.put("rsdcnm", rset1.getString(2));
					rsdcs.add(rsdc);
				}
				
				request.setAttribute("rsdcs", rsdcs);
				
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
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_room.jsp").forward(request, response);
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
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			PreparedStatement pstmt7 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			int row1 = 0;
			int row2 = 0;
			
			// 送信情報の取得
			String room_id = request.getParameter("room_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rsd_id = request.getParameter("rsd_id");
			String room_nu = request.getParameter("room_nu");
			String rent = request.getParameter("rent");
			String mng_fee = request.getParameter("mng_fee");
			String deposit = request.getParameter("deposit");
			String honorarium = request.getParameter("honorarium");
			String brokerage = request.getParameter("brokerage");
			String key_fee = request.getParameter("key_fee");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rsd_idとroom_nuが同じものが既に登録されているか確認
				String sql1 = "SELECT * FROM room WHERE cmpnid=? AND rsdcid=? AND roomnu=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsd_id);
				pstmt1.setString(3, room_nu);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					row1++;
				}
				
				// 物件登録で使われている追加費用項目のaditid,adebvlをadebから取得
				String sql2 = "SELECT aditid,adebvl FROM adeb WHERE cmpnid=? AND rsdcid=? ORDER BY aditid ASC";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, cmpn_id);
				pstmt2.setString(2, rsd_id);
				rset2 = pstmt2.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				while (rset2.next()) {
					// aditの情報を取得
					String sql3 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, rset2.getString(1));
					pstmt3.setString(2, cmpn_id);
					rset3 = pstmt3.executeQuery();
					
					if (rset3.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset3.getString(1));
						adit.put("itemnm", rset3.getString(3));
						adit.put("itemid", rset3.getString(4));
						adit.put("maxlng", rset3.getString(5));
						adit.put("spclng", rset3.getString(6));
						adit.put("commnt", rset3.getString(7));
						adit.put("adebvl", rset2.getString(2));
						adits.add(adit);
					}
				}
				
				if (row1 > 0) {
					// rsd_idとroom_nuが同じものが既に登録されている場合
					// rsdcnmを取得
					String sql4 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=? AND cmpnid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, rsd_id);
					pstmt4.setString(2, cmpn_id);
					rset4 = pstmt4.executeQuery();
					String rsdcnm = null;
					
					if (rset4.next()) {
						rsdcnm = rset4.getString(1);
					}
					
					// 物件情報を取得
					String sql5 = "SELECT rsdcid,rsdcnm FROM rsdc WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
					pstmt5 = conn.prepareStatement(sql5);
					pstmt5.setString(1, cmpn_id);
					rset5 = pstmt5.executeQuery();
					ArrayList<Map<String, String>> rsdcs = new ArrayList<Map<String, String>>();
					
					while (rset5.next()) {
						Map<String, String> rsdc = new HashMap<>();
						rsdc.put("rsdcid", rset5.getString(1));
						rsdc.put("rsdcnm", rset5.getString(2));
						rsdcs.add(rsdc);
					}
					
					String errorMessage = "「" + rsdcnm + "」の" + room_nu + "号室はすでに登録されています";
					String[] room = {room_id,rsd_id,room_nu,rent,mng_fee,deposit,honorarium,brokerage,key_fee,comment};
					request.setAttribute("errorMessage", errorMessage);
					request.setAttribute("room", room);
					request.setAttribute("rsdcs", rsdcs);
					request.setAttribute("adits", adits);
				} else {
					// オートコミットをオフ
					conn.setAutoCommit(false);
					
					// roomに保存
					String sql6 = "INSERT INTO room(roomid,cmpnid,rsdcid,roomnu,rentfe,mngfee,depsit,hnrrum,brkrag,keyfee,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
					pstmt6 = conn.prepareStatement(sql6);
					pstmt6.setString(1, room_id);
					pstmt6.setString(2, cmpn_id);
					pstmt6.setString(3, rsd_id);
					pstmt6.setString(4, room_nu);
					pstmt6.setString(5, rent);
					pstmt6.setString(6, mng_fee);
					pstmt6.setString(7, deposit);
					pstmt6.setString(8, honorarium);
					pstmt6.setString(9, brokerage);
					pstmt6.setString(10, key_fee);
					pstmt6.setString(11, comment);
					int edited1 = pstmt6.executeUpdate();
					
					// 保存できていない場合はロールバック
					if (edited1 == 1) {
						conn.commit();
					} else {
						conn.rollback();
					}
					
					// オートコミットをオン
					conn.setAutoCommit(true);
					
					for (int i = 0; i < adits.size(); i++) {
						String aditid = adits.get(i).get("aditid");
						String adehvl = request.getParameter(adits.get(i).get("itemid"));
						
						// adehidを生成
						CreateIdBean2 cib2 = new CreateIdBean2();
						String adehid = cib2.getCreatedId("adeh", "adehid", cmpn_id);
						
						// adehに保存
						String sql7 = "INSERT INTO adeh(adehid,cmpnid,roomid,aditid,adehvl) VALUES(?,?,?,?,?)";
						pstmt7 = conn.prepareStatement(sql7);
						pstmt7.setString(1, adehid);
						pstmt7.setString(2, cmpn_id);
						pstmt7.setString(3, room_id);
						pstmt7.setString(4, aditid);
						pstmt7.setString(5, adehvl);
						pstmt7.executeUpdate();
						row2++;
					}
					
					// エンコード
					room_id = URLEncoder.encode(room_id, "UTF-8");
					cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
					rsd_id = URLEncoder.encode(rsd_id, "UTF-8");
					room_nu = URLEncoder.encode(room_nu, "UTF-8");
					rent = URLEncoder.encode(rent, "UTF-8");
					mng_fee = URLEncoder.encode(mng_fee, "UTF-8");
					deposit = URLEncoder.encode(deposit, "UTF-8");
					honorarium = URLEncoder.encode(honorarium, "UTF-8");
					brokerage = URLEncoder.encode(brokerage, "UTF-8");
					key_fee = URLEncoder.encode(key_fee, "UTF-8");
					comment = URLEncoder.encode(comment, "UTF-8");
				}
				
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
					if (rset2.next()) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 > 0) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 > 0) {
						pstmt5.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0) {
						pstmt6.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0  && row2 > 0) {
						pstmt7.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (row1 > 0) {
				request.getRequestDispatcher("/WEB-INF/app/register/register_room.jsp").forward(request, response);
			} else if (row1 == 0) {
				if (request.getParameter("submit").equals("登録終了")) {
					response.sendRedirect(request.getContextPath() + "/index/room");
				} else {
					response.sendRedirect(request.getContextPath() + "/register/room");
				}
			}
		}
	}

}

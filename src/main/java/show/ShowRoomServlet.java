package show;

import java.io.IOException;
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
 * Servlet implementation class ShowRoomServlet
 */
@WebServlet("/show/room")
public class ShowRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowRoomServlet() {
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
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			boolean data_exists = false;
			
			// roomidの取得
			String roomid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// roomの情報取得
				String sql1 = "SELECT * FROM room WHERE roomid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, roomid);
				pstmt1.setString(2, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> room = new ArrayList<String>();
				
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
				pstmt2.setString(1, roomid);
				pstmt2.setString(2, cmpnid);
				rset2 = pstmt2.executeQuery();
				String rsdcnm = null;
				
				if (rset2.next()) {
					rsdcnm = rset2.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("rsdcnm", rsdcnm);
				
				// aditid,adehvlの取得
				String sql3 = "SELECT aditid,adehvl FROM adeh WHERE cmpnid=? AND roomid=? ORDER BY adehid ASC";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, cmpnid);
				pstmt3.setString(2, roomid);
				rset3 = pstmt3.executeQuery();
				ArrayList<Map<String, String>> adehs = new ArrayList<Map<String, String>>();
				
				while (rset3.next()) {
					// itemnmの取得
					String sql4 = "SELECT itemnm FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, rset3.getString(1));
					pstmt4.setString(2, cmpnid);
					rset4 = pstmt4.executeQuery();
					
					if (rset4.next()) {
						Map<String, String> adeh = new HashMap<>();
						adeh.put("itemnm", rset4.getString(1));
						adeh.put("adehvl", rset3.getString(2));
						adehs.add(adeh);
					}
				}
				
				request.setAttribute("adehs", adehs);
				
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
				request.getRequestDispatcher("/WEB-INF/app/show/show_room.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/room");
			}
		}
	}

}

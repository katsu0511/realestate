package cite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import home.RealestateDAO;

/**
 * Servlet implementation class CiteRoomServlet
 */
@WebServlet("/cite/room")
public class CiteRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CiteRoomServlet() {
        super();
        // TODO Auto-generated constructor stub
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
			
			// SQL情報管理
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			
			// 送信情報の取得
			String rsdc_id = request.getParameter("id");
			String current_rsdc = request.getParameter("current_rsdc");
			String current_room = request.getParameter("current_room");
			
			// cmpn_idの情報取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// roomnuの取得
				String sql1 = "SELECT roomid,roomnu FROM room WHERE cmpnid=? AND rsdcid=? AND cntrct='f' AND actvfg='t' ORDER BY roomnu ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsdc_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> roomnu = new ArrayList<>();
				
				while (rset1.next()) {
					roomnu.add(rset1.getString(1) + ' ' + rset1.getString(2));
				}
				
				if (rsdc_id.equals(current_rsdc)) {
					String sql2 = "SELECT roomnu FROM room WHERE cmpnid=? AND roomid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, cmpn_id);
					pstmt2.setString(2, current_room);
					rset2 = pstmt2.executeQuery();
					
					if (rset2.next()) {
						roomnu.add(current_room + ' ' + rset2.getString(1));
					}
				}
				
				// レスポンス用JSON文字列生成
				String resData = "{\"roomnu\":\"" + roomnu + "\"}";

				// レスポンス処理
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf8");
				PrintWriter out = response.getWriter();
				out.println(resData);
				
			} catch(Exception err) {
				((HttpServletResponse) request).sendError(HttpServletResponse.SC_BAD_REQUEST);
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					if (rsdc_id.equals(current_rsdc)) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
		}
	}

}

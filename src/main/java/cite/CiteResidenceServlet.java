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
 * Servlet implementation class CiteResidenceServlet
 */
@WebServlet("/cite/residence")
public class CiteResidenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CiteResidenceServlet() {
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
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			
			// rsdc_idの取得
			String rsdc_id = request.getParameter("id");
			
			// cmpn_idの情報取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// room_idを作成
				String sql1 = "SELECT roomid FROM room WHERE cmpnid=? AND rsdcid=? ORDER BY roomid DESC LIMIT 1";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsdc_id);
				rset1 = pstmt1.executeQuery();
				String last_room_id = null;
				
				if (rset1.next()) {
					last_room_id = rset1.getString(1);
				} else {
					last_room_id = rsdc_id.substring(0, 7) + "00000_" + cmpn_id;
				}
				
				String num1 = last_room_id.substring(7,12);
				int num2 = Integer.parseInt(num1);
				String num3 = String.format("%05d", num2 + 1);
				String room_id = rsdc_id.substring(0, 7) + num3 + "_" + cmpn_id;
				
				// rsdcの情報取得
				String sql2 = "SELECT * FROM rsdc WHERE rsdcid=? AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rsdc_id);
				pstmt2.setString(2, cmpn_id);
				rset2 = pstmt2.executeQuery();
				ArrayList<String> rsdc = new ArrayList<String>();
				
				while (rset2.next()) {
					for (int i = 1; i <= 19; i++){
						rsdc.add(rset2.getString(i));
					}
				}
				
				// レスポンス用JSON文字列生成
				String resData =
					"{\"roomid\":\"" + room_id +
					"\",\"rentfe\":\"" + rsdc.get(9) +
					"\",\"mngfee\":\"" + rsdc.get(10) +
					"\",\"depsit\":\"" + rsdc.get(11) +
					"\",\"hnrrum\":\"" + rsdc.get(12) +
					"\",\"brkrag\":\"" + rsdc.get(13) +
					"\",\"keyfee\":\"" + rsdc.get(14) +
					"\"}";

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
					pstmt2.close();
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
		}
	}

}

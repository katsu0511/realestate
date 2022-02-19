package cite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import home.RealestateDAO;

/**
 * Servlet implementation class CiteTenantForPayment
 */
@WebServlet("/cite/tenant_for_payment")
public class CiteTenantForPayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CiteTenantForPayment() {
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
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			
			// tnnt_idの取得
			String tnnt_id = request.getParameter("id");
			
			// cmpn_idの情報取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// tnntの情報の取得
				String sql1 = "SELECT rsdcid,roomid FROM tnnt WHERE tnntid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, tnnt_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				String rsdcid = null;
				String rsdcnm = null;
				String roomid = null;
				String roomnu = null;
				
				if (rset1.next()) {
					rsdcid = rset1.getString(1);
					roomid = rset1.getString(2);
					
					// rsdcnmの取得
					String sql2 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rsdcid);
					pstmt2.setString(2, cmpn_id);
					rset2 = pstmt2.executeQuery();
					if (rset2.next()) {
						rsdcnm = rset2.getString(1);
					}
					
					// roomnu,rentfe,mngfeeの取得
					String sql3 = "SELECT roomnu,rentfe,mngfee FROM room WHERE roomid=? AND cmpnid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, roomid);
					pstmt3.setString(2, cmpn_id);
					rset3 = pstmt3.executeQuery();
					if (rset3.next()) {
						roomnu = rset3.getString(1);
					}
				}
				
				// レスポンス用JSON文字列生成
				String resData =
						"{\"tnntid\":\"" + tnnt_id +
						"\",\"rsdcid\":\"" + rsdcid +
						"\",\"rsdcnm\":\"" + rsdcnm +
						"\",\"roomid\":\"" + roomid +
						"\",\"roomnu\":\"" + roomnu +
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
					if (rset1.next()) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (rset1.next()) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
		}
	}

}

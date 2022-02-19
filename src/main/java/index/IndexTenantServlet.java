package index;

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
 * Servlet implementation class IndexTenantServlet
 */
@WebServlet("/index/tenant")
public class IndexTenantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexTenantServlet() {
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
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// tnntの情報取得
				String sql1 = "SELECT tnntid,rsdcid,roomid,contdt,tnntnm,actvfg FROM tnnt WHERE cmpnid=? ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> tenants = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					// rsdcnmの取得
					String sql2 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(2));
					pstmt2.setString(2, cmpnid);
					rset2 = pstmt2.executeQuery();
					String rsdcnm = null;
					if (rset2.next()) {
						rsdcnm = rset2.getString(1);
					}
					
					// roomnuの取得
					String sql3 = "SELECT roomnu FROM room WHERE roomid=? AND cmpnid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, rset1.getString(3));
					pstmt3.setString(2, cmpnid);
					rset3 = pstmt3.executeQuery();
					String roomnu = null;
					if (rset3.next()) {
						roomnu = rset3.getString(1);
					}
					
					// contdt
					String contdt = "";
					if (!rset1.getString(4).equals("")) {
						String cont_year = rset1.getString(4).substring(0, 4);
						String cont_month = rset1.getString(4).substring(5, 7);
						if (cont_month.substring(0, 2).equals("0")) {cont_month = cont_month.substring(1, 2);}
						String cont_day = rset1.getString(4).substring(8, 10);
						if (cont_day.substring(0, 2).equals("0")) {cont_day = cont_day.substring(1, 2);}
						contdt = cont_year + "年" + cont_month + "月" + cont_day + "日";
					}
					
					Map<String, String> tenant = new HashMap<>();
					tenant.put("tnntid", rset1.getString(1));
					tenant.put("rsdcnm", rsdcnm);
					tenant.put("roomnu", roomnu);
					tenant.put("contdt", contdt);
					tenant.put("tnntnm", rset1.getString(5));
					tenant.put("actvfg", rset1.getString(6));
					tenants.add(tenant);
				}
				
				request.setAttribute("tenants", tenants);
				
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
					if (rset1.next()) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			request.getRequestDispatcher("/WEB-INF/app/index/index_tenant.jsp").forward(request, response);
		}
	}

}

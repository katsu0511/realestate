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
 * Servlet implementation class IndexResidenceServlet
 */
@WebServlet("/index/residence")
public class IndexResidenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexResidenceServlet() {
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
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rsdcの情報取得
				String sql1 = "SELECT rsdcid,landid,rsdcnm,statnm,citynm,strno1,actvfg FROM rsdc WHERE cmpnid=? ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> residences = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					// landnmの情報取得
					String sql2 = "SELECT landnm FROM land WHERE landid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(2));
					rset2 = pstmt2.executeQuery();
					if (rset2.next()) {
						Map<String, String> residence = new HashMap<>();
						residence.put("rsdcid", rset1.getString(1));
						residence.put("landnm", rset2.getString(1));
						residence.put("rsdcnm", rset1.getString(3));
						residence.put("addres", rset1.getString(4) + rset1.getString(5) + rset1.getString(6));
						residence.put("actvfg", rset1.getString(7));
						residences.add(residence);
					}
				}
				
				request.setAttribute("residences", residences);
				
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
					conn.close();
				} catch (SQLException e) {  }
			}
			
			request.getRequestDispatcher("/WEB-INF/app/index/index_residence.jsp").forward(request, response);
		}
	}

}

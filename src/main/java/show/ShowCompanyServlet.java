package show;

import java.io.IOException;
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
 * Servlet implementation class ShowCompanyServlet
 */
@WebServlet("/show/company")
public class ShowCompanyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowCompanyServlet() {
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
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// cmpnの情報取得
				String sql1 = "SELECT * FROM cmpn WHERE cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> cmpn = new ArrayList<String>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 22; i++){
						cmpn.add(rset1.getString(i));
					}
				}
				
				request.setAttribute("cmpn", cmpn);
				
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
			
			request.getRequestDispatcher("/WEB-INF/app/show/show_company.jsp").forward(request, response);
		}
	}

}

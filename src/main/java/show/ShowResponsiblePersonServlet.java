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
 * Servlet implementation class ShowResponsiblePersonServlet
 */
@WebServlet("/show/responsible_person")
public class ShowResponsiblePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowResponsiblePersonServlet() {
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
			boolean data_exists = false;
			
			// rppsidの取得
			String rppsid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rppsの情報取得
				String sql1 = "SELECT * FROM rpps WHERE rppsid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rppsid);
				pstmt1.setString(2, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> rppn = new ArrayList<String>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 11; i++){
						rppn.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("rppn", rppn);
				
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
			
			if (data_exists) {
				request.getRequestDispatcher("/WEB-INF/app/show/show_responsible_person.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/responsible_person");
			}
		}
	}

}

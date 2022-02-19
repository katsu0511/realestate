package home;

import java.io.IOException;
import java.net.URLEncoder;
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

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
			request.getRequestDispatcher("/WEB-INF/app/user/login.jsp").forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/top");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// 文字化け対策
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		// 送信情報の取得
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		// エラーメッセージ
		String errorMessage = null;
		
		// データベース接続管理クラスの変数宣言
		DBManager dbm = new DBManager();
		
		// ログインユーザー情報取得
		UserDTO user = dbm.getLoginUser(userName, password);
			
		if (userName.equals("") || password.equals("")) {
			errorMessage = "ユーザー名とパスワードを入力してください";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("/WEB-INF/app/user/login.jsp").forward(request, response);
		} else if (user != null) {
			// 接続情報
			RealestateDAO db = new RealestateDAO();
			Connection conn = null;
			
			// SQL情報管理
			PreparedStatement pstmt1 = null;
			ResultSet rset1 = null;
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// cmpnidの取得
				String sql1 = "SELECT cmpnid FROM cmpn WHERE usernm=? AND passwd=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, userName);
				pstmt1.setString(2, password);
				rset1 = pstmt1.executeQuery();
				String cmpnid = null;
				
				if (rset1.next()) {
					cmpnid = rset1.getString(1);
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("userName", userName);
				session.setAttribute("password", password);
				session.setAttribute("cmpnid", cmpnid);
				
				userName = URLEncoder.encode(userName, "UTF-8");
				password = URLEncoder.encode(password, "UTF-8");
				cmpnid = URLEncoder.encode(cmpnid, "UTF-8");
				
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
			
			response.sendRedirect(request.getContextPath() + "/top");
		} else {
			errorMessage = "ユーザー名かパスワードが間違っています";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("/WEB-INF/app/user/login.jsp").forward(request, response);
		}
	}

}

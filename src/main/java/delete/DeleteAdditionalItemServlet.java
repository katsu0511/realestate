package delete;

import java.io.IOException;
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
 * Servlet implementation class DeleteAdditionalItemServlet
 */
@WebServlet("/delete/additional_item")
public class DeleteAdditionalItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAdditionalItemServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("resource")
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
//			PreparedStatement pstmt2 = null;
			ResultSet rset1 = null;
//			ResultSet rset2 = null;
			
			// aditidの取得
			String aditid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// aditから削除
				String sql1 = "DELETE FROM adit WHERE aditid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, aditid);
				pstmt1.setString(2, cmpnid);
				pstmt1.executeUpdate();
				
				// adexから削除
//				String sql2 = "DELETE FROM adex WHERE aditid=? AND cmpnid=?";
//				pstmt2 = conn.prepareStatement(sql2);
//				pstmt2.setString(1, aditid);
//				pstmt2.setString(2, cmpnid);
//				pstmt2.executeUpdate();
				
				// 両方削除できているか確認
				pstmt1 = conn.prepareStatement("SELECT * FROM adit WHERE aditid=? AND cmpnid=?");
				pstmt1.setString(1, aditid);
				pstmt1.setString(2, cmpnid);
				rset1 = pstmt1.executeQuery();
				int line = 0;
				while (rset1.next()) {
					line++;
				}
				
//				pstmt2 = conn.prepareStatement("SELECT * FROM adex WHERE aditid=? AND cmpnid=?");
//				pstmt2.setString(1, aditid);
//				pstmt2.setString(2, cmpnid);
//				rset2 = pstmt2.executeQuery();
//				while (rset2.next()) {
//					line++;
//				}
				
				// 削除できていない場合はロールバック
				if (line == 0) {
					conn.commit();
				} else {
					conn.rollback();
				}
				
				conn.setAutoCommit(true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
//				try {
//					pstmt2.close();
//				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			response.sendRedirect(request.getContextPath() + "/index/additional_item");
		}
	}

}

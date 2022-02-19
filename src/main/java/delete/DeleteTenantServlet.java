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
 * Servlet implementation class DeleteTenantServlet
 */
@WebServlet("/delete/tenant")
public class DeleteTenantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteTenantServlet() {
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
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			
			// tnntidの取得
			String tnntid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// roomのcntrctをfalseに変更
				String sql1 = "UPDATE room SET cntrct=? WHERE roomid=("
						    +     "SELECT roomid FROM tnnt WHERE tnntid=?"
						    + ") "
						    + "AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setBoolean(1, false);
				pstmt1.setString(2, tnntid);
				pstmt1.setString(3, cmpnid);
				pstmt1.executeUpdate();
				
				// tnntから削除
				String sql2 = "DELETE FROM tnnt WHERE tnntid=? AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, tnntid);
				pstmt2.setString(2, cmpnid);
				pstmt2.executeUpdate();
				
				// depyから削除
				String sql3 = "DELETE FROM depy WHERE tnntid=? AND cmpnid=?";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, tnntid);
				pstmt3.setString(2, cmpnid);
				pstmt3.executeUpdate();
				
				// 全て削除・変更できているか確認
				pstmt1 = conn.prepareStatement("SELECT * FROM room WHERE cntrct=? AND roomid=("
				 	   +						  "SELECT roomid FROM tnnt WHERE tnntid=?"
					   + 					   ") "
					   + 					   "AND cmpnid=?");
				pstmt1.setBoolean(1, true);
				pstmt1.setString(2, tnntid);
				pstmt1.setString(3, cmpnid);
				rset1 = pstmt1.executeQuery();
				int line = 0;
				while (rset1.next()) {
					line++;
				}
				
				pstmt2 = conn.prepareStatement("SELECT * FROM tnnt WHERE tnntid=? AND cmpnid=?");
				pstmt2.setString(1, tnntid);
				pstmt2.setString(2, cmpnid);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					line++;
				}
				
				pstmt3 = conn.prepareStatement("SELECT * FROM depy WHERE tnntid=? AND cmpnid=?");
				pstmt3.setString(1, tnntid);
				pstmt3.setString(2, cmpnid);
				rset3 = pstmt3.executeQuery();
				while (rset3.next()) {
					line++;
				}
				
				// 削除・変更できていない場合はロールバック
				if (line == 0) {
					conn.commit();
				} else {
					conn.rollback();
				}
				
				// オートコミットをオン
				conn.setAutoCommit(true);
				
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
					conn.close();
				} catch (SQLException e) {  }
			}
			
			response.sendRedirect(request.getContextPath() + "/index/tenant");
		}
	}

}

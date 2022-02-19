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
 * Servlet implementation class DeleteResponsiblePersonServlet
 */
@WebServlet("/delete/responsible_person")
public class DeleteResponsiblePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteResponsiblePersonServlet() {
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
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			
			// rppsidの取得
			String rppsid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// rppsから削除
				String sql1 = "DELETE FROM rpps WHERE rppsid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rppsid);
				pstmt1.setString(2, cmpnid);
				pstmt1.executeUpdate();
				
				// このrppsidを登録しているtnntのrppsidを更新
				String sql2 = "UPDATE tnnt SET rppsid='' WHERE rppsid=? AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rppsid);
				pstmt2.setString(2, cmpnid);
				pstmt2.executeUpdate();
				
				// 全て削除できているか確認
				pstmt1 = conn.prepareStatement("SELECT * FROM rpps WHERE rppsid=? AND cmpnid=?");
				pstmt1.setString(1, rppsid);
				pstmt1.setString(2, cmpnid);
				rset1 = pstmt1.executeQuery();
				int line = 0;
				while (rset1.next()) {
					line++;
				}
				
				pstmt2 = conn.prepareStatement("SELECT * FROM tnnt WHERE rppsid=? AND cmpnid=?");
				pstmt2.setString(1, rppsid);
				pstmt2.setString(2, cmpnid);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					line++;
				}
				
				// 削除できていない場合はロールバック
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
			
			response.sendRedirect(request.getContextPath() + "/index/responsible_person");
		}
	}

}

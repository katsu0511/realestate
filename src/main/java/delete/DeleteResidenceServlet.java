package delete;

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
 * Servlet implementation class DeleteResidenceServlet
 */
@WebServlet("/delete/residence")
public class DeleteResidenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteResidenceServlet() {
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
			PreparedStatement pstmt4 = null;
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			PreparedStatement pstmt7 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			ResultSet rset6 = null;
			ResultSet rset7 = null;
			
			// rsdcidの取得
			String rsdcid = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// roomidを取得
				String sql1 = "SELECT roomid FROM room WHERE cmpnid=? AND rsdcid=? ORDER BY roomid ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				pstmt1.setString(2, rsdcid);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> roomids = new ArrayList<>();
				while (rset1.next()) {
					roomids.add(rset1.getString(1));
				}
				
				// rsdcから削除
				String sql2 = "DELETE FROM rsdc WHERE rsdcid=? AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rsdcid);
				pstmt2.setString(2, cmpnid);
				pstmt2.executeUpdate();
				
				// roomから削除
				String sql3 = "DELETE FROM room WHERE rsdcid=? AND cmpnid=?";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, rsdcid);
				pstmt3.setString(2, cmpnid);
				pstmt3.executeUpdate();
				
				// このrsdcidを登録しているtnntのrsdcid,roomid,contdtを更新
				String sql4 = "UPDATE tnnt SET rsdcid='',roomid='',contdt='' WHERE rsdcid=? AND cmpnid=?";
				pstmt4 = conn.prepareStatement(sql4);
				pstmt4.setString(1, rsdcid);
				pstmt4.setString(2, cmpnid);
				pstmt4.executeUpdate();
				
				// depyから削除
				String sql5 = "DELETE FROM depy WHERE rsdcid=? AND cmpnid=?";
				pstmt5 = conn.prepareStatement(sql5);
				pstmt5.setString(1, rsdcid);
				pstmt5.setString(2, cmpnid);
				pstmt5.executeUpdate();
				
				// adebから削除
				String sql6 = "DELETE FROM adeb WHERE rsdcid=? AND cmpnid=?";
				pstmt6 = conn.prepareStatement(sql6);
				pstmt6.setString(1, rsdcid);
				pstmt6.setString(2, cmpnid);
				pstmt6.executeUpdate();
				
				// adehから削除
				String sql7 = "DELETE FROM adeh WHERE cmpnid=? AND roomid=?";
				pstmt7 = conn.prepareStatement(sql7);
				pstmt7.setString(1, cmpnid);
				for (String roomid : roomids) {
					pstmt7.setString(2, roomid);
					pstmt7.executeUpdate();
				}
				
				// 全て削除できているか確認
				pstmt2 = conn.prepareStatement("SELECT * FROM rsdc WHERE rsdcid=? AND cmpnid=?");
				pstmt2.setString(1, rsdcid);
				pstmt2.setString(2, cmpnid);
				rset2 = pstmt2.executeQuery();
				int line = 0;
				while (rset2.next()) {
					line++;
				}
				
				pstmt3 = conn.prepareStatement("SELECT * FROM room WHERE rsdcid=? AND cmpnid=?");
				pstmt3.setString(1, rsdcid);
				pstmt3.setString(2, cmpnid);
				rset3 = pstmt3.executeQuery();
				while (rset3.next()) {
					line++;
				}
				
				pstmt4 = conn.prepareStatement("SELECT * FROM tnnt WHERE rsdcid=? AND cmpnid=?");
				pstmt4.setString(1, rsdcid);
				pstmt4.setString(2, cmpnid);
				rset4 = pstmt4.executeQuery();
				while (rset4.next()) {
					line++;
				}
				
				pstmt5 = conn.prepareStatement("SELECT * FROM depy WHERE rsdcid=? AND cmpnid=?");
				pstmt5.setString(1, rsdcid);
				pstmt5.setString(2, cmpnid);
				rset5 = pstmt5.executeQuery();
				while (rset5.next()) {
					line++;
				}
				
				pstmt6 = conn.prepareStatement("SELECT * FROM adeb WHERE rsdcid=? AND cmpnid=?");
				pstmt6.setString(1, rsdcid);
				pstmt6.setString(2, cmpnid);
				rset6 = pstmt6.executeQuery();
				while (rset6.next()) {
					line++;
				}
				
				pstmt7 = conn.prepareStatement("SELECT * FROM adeh WHERE cmpnid=? AND roomid=?");
				pstmt7.setString(1, cmpnid);
				for (String roomid : roomids) {
					pstmt7.setString(2, roomid);
					rset7 = pstmt7.executeQuery();
					while (rset7.next()) {
						line++;
					}
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
					pstmt3.close();
				} catch (SQLException e) { }
				
				try {
					pstmt4.close();
				} catch (SQLException e) { }
				
				try {
					pstmt5.close();
				} catch (SQLException e) { }
				
				try {
					pstmt6.close();
				} catch (SQLException e) { }
				
				try {
					pstmt7.close();
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			response.sendRedirect(request.getContextPath() + "/index/residence");
		}
	}

}

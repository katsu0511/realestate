package edit;

import java.io.IOException;
import java.net.URLEncoder;
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
 * Servlet implementation class EditAdditionalItemServlet
 */
@WebServlet("/edit/additional_item")
public class EditAdditionalItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditAdditionalItemServlet() {
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
			
			// SQL
			PreparedStatement pstmt1 = null;
			ResultSet rset1 = null;
			boolean data_exists = false;
			
			// adit_idの取得
			String adit_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// adit情報を取得
				String sql1 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, adit_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> adit = new ArrayList<>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 9; i++){
						adit.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("adit", adit);
				
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
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_additional_item.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/additional_item");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
			
			// SQL
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			int row1 = 0;
			int row2 = 0;
			
			// 送信情報の取得
			String adit_id = request.getParameter("adit_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String item_name = request.getParameter("item_name");
			String item_id = request.getParameter("item_id");
			String max_lng = request.getParameter("max_lng");
			String spc_lng = request.getParameter("spc_lng");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// cmpn_idとitem_nameが同じものが既に登録されているか確認
				String sql1 = "SELECT * FROM adit WHERE aditid != ? AND cmpnid=? AND itemnm=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, adit_id);
				pstmt1.setString(2, cmpn_id);
				pstmt1.setString(3, item_name);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					row1++;
				}
				
				// cmpn_idとitem_idが同じものが既に登録されているか確認
				String sql2 = "SELECT * FROM adit WHERE aditid != ? AND cmpnid=? AND itemid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, adit_id);
				pstmt2.setString(2, cmpn_id);
				pstmt2.setString(3, item_id);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					row2++;
				}
				
				// cmpn_idとitem_nameまたはcmpn_idとitem_idが同じものが既に登録されている場合
				if (row1 > 0 || row2 > 0) {
					String errorMessage = null;
					if (row1 > 0 && row2 > 0) {
						errorMessage = "追加項目表示名と追加項目ID名がすでに登録されています";
					} else if (row1 > 0 && row2 == 0) {
						errorMessage = "追加項目表示名がすでに登録されています";
					} else if (row1 == 0 && row2 > 0) {
						errorMessage = "追加項目ID名がすでに登録されています";
					}
					
					String[] adit = {adit_id,cmpn_id,item_name,item_id,max_lng,spc_lng,comment};
					request.setAttribute("errorMessage", errorMessage);
					request.setAttribute("item_name", item_name);
					request.setAttribute("item_id", item_id);
					request.setAttribute("adit", adit);
				} else {
					// オートコミットをオフ
					conn.setAutoCommit(false);
					
					// aditに保存
					String sql3 = "UPDATE adit SET itemnm=?,itemid=?,maxlng=?,spclng=?,commnt=?,uddttm=current_timestamp WHERE aditid=? AND cmpnid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, item_name);
					pstmt3.setString(2, item_id);
					pstmt3.setString(3, max_lng);
					pstmt3.setString(4, spc_lng);
					pstmt3.setString(5, comment);
					pstmt3.setString(6, adit_id);
					pstmt3.setString(7, cmpn_id);
					int edited1 = pstmt3.executeUpdate();
					
					// 変更できていない場合はロールバック
					if (edited1 == 1) {
						conn.commit();
					} else {
						conn.rollback();
					}
					
					// エンコード
					adit_id = URLEncoder.encode(adit_id, "UTF-8");
					cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
					item_name = URLEncoder.encode(item_name, "UTF-8");
					item_id = URLEncoder.encode(item_id, "UTF-8");
					max_lng = URLEncoder.encode(max_lng, "UTF-8");
					spc_lng = URLEncoder.encode(spc_lng, "UTF-8");
					comment = URLEncoder.encode(comment, "UTF-8");
					
					// オートコミットをオン
					conn.setAutoCommit(true);
				}
				
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
					if (row1 == 0 && row2 == 0) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (row1 == 0 && row2 == 0) {
				String url = request.getContextPath() + "/show/additional_item?id=" + adit_id;
				response.sendRedirect(url);
			} else {
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_additional_item.jsp").forward(request, response);
			}
		}
	}

}

package cite;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class CiteResidenceForAdits
 */
@WebServlet("/cite/residence_for_adits")
public class CiteResidenceForAdits extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CiteResidenceForAdits() {
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
			
			// rsdc_idの取得
			String rsdc_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 物件登録で使われている追加費用項目一覧
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				
				// 物件登録で使われている追加費用項目のaditid,adebvlをadebから取得
				String sql1 = "SELECT aditid,adebvl FROM adeb WHERE cmpnid=? AND rsdcid=? ORDER BY aditid ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsdc_id);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					// aditの情報を取得
					String sql2 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(1));
					pstmt2.setString(2, cmpn_id);
					rset2 = pstmt2.executeQuery();
					
					if (rset2.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset2.getString(1));
						adit.put("itemnm", rset2.getString(3));
						adit.put("itemid", rset2.getString(4));
						adit.put("maxlng", rset2.getString(5));
						adit.put("spclng", rset2.getString(6));
						adit.put("commnt", rset2.getString(7));
						adit.put("adebvl", rset1.getString(2));
						adits.add(adit);
					}
				}
				
				// レスポンス用JSON文字列生成
				String resData =
					"{\"adits\":\"" + adits + "\"}";

				// レスポンス処理
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf8");
				PrintWriter out = response.getWriter();
				out.println(resData);
				
			} catch(Exception err) {
				((HttpServletResponse) request).sendError(HttpServletResponse.SC_BAD_REQUEST);
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
		}
	}

}

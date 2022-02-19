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
 * Servlet implementation class EditResponsiblePersonServlet
 */
@WebServlet("/edit/responsible_person")
public class EditResponsiblePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditResponsiblePersonServlet() {
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
			
			// rpps_idの取得
			String rpps_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rpps情報を取得
				String sql1 = "SELECT * FROM rpps WHERE rppsid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rpps_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> rppn = new ArrayList<>();
				
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
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_responsible_person.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/responsible_person");
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
			
			// 送信情報の取得
			String rppn_id = request.getParameter("rppn_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rppn_name = request.getParameter("rppn_name").replace("　", " ");
			String rppn_kana = request.getParameter("rppn_kana").replace("　", " ");
			String number1 = request.getParameter("number1");
			String number2 = request.getParameter("number2");
			String email = request.getParameter("email");
			String actv_flg = request.getParameter("actv_flg");
			Boolean actvflg = Boolean.valueOf(actv_flg);
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// SQL実行
				String sql1 = "UPDATE rpps SET rppsnm=?,rppsnf=?,phone1=?,phone2=?,emladr=?,actvfg=?,commnt=?,uddttm=current_timestamp WHERE rppsid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rppn_name);
				pstmt1.setString(2, rppn_kana);
				pstmt1.setString(3, number1);
				pstmt1.setString(4, number2);
				pstmt1.setString(5, email);
				pstmt1.setBoolean(6, actvflg);
				pstmt1.setString(7, comment);
				pstmt1.setString(8, rppn_id);
				pstmt1.setString(9, cmpn_id);
				pstmt1.executeUpdate();
				
				// エンコード
				rppn_id = URLEncoder.encode(rppn_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rppn_name = URLEncoder.encode(rppn_name, "UTF-8");
				rppn_kana = URLEncoder.encode(rppn_kana, "UTF-8");
				number1 = URLEncoder.encode(number1, "UTF-8");
				number2 = URLEncoder.encode(number2, "UTF-8");
				email = URLEncoder.encode(email, "UTF-8");
				actv_flg = URLEncoder.encode(actv_flg, "UTF-8");
				comment = URLEncoder.encode(comment, "UTF-8");
				
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
			
			String url = request.getContextPath() + "/show/responsible_person?id=" + rppn_id;
			response.sendRedirect(url);
		}
	}

}

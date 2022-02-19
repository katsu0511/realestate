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
 * Servlet implementation class EditCompanyServlet
 */
@WebServlet("/edit/company")
public class EditCompanyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditCompanyServlet() {
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
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// cmpn情報を取得
				String sql1 = "SELECT * FROM cmpn WHERE cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> cmpn = new ArrayList<>();
				
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
			
			request.getRequestDispatcher("/WEB-INF/app/edit/edit_company.jsp").forward(request, response);
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
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String cmpn_name = request.getParameter("cmpn_name").replace("　", " ");
			String cmpn_kana = request.getParameter("cmpn_kana").replace("　", " ");
			String rpst_name = request.getParameter("rpst_name").replace("　", " ");
			String brnc_name = request.getParameter("brnc_name");
			String password = request.getParameter("password2");
			String zip1 = request.getParameter("zip1");
			String zip2 = request.getParameter("zip2");
			String address1 = request.getParameter("address1");
			String address2 = request.getParameter("address2");
			String address3 = request.getParameter("address3");
			String address4 = request.getParameter("address4");
			String number = request.getParameter("number");
			String fax = request.getParameter("fax");
			String bank = request.getParameter("bank");
			String bank_branch = request.getParameter("bank_branch");
			String account_type = request.getParameter("account_type");
			String account_number = request.getParameter("account_number");
			String account_name = request.getParameter("account_name");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// SQL実行
				String sql = "UPDATE cmpn SET cmpnnm=?,cmpnnf=?,rpstnm=?,brncnm=?,passwd=?,zipcod=?,statnm=?,citynm=?,strno1=?,strno2=?,number=?,faxnum=?,banknm=?,bankbr=?,acntty=?,acntnu=?,acntnm=?,commnt=?,uddttm=current_timestamp WHERE cmpnid=?";
				pstmt1 = conn.prepareStatement(sql);
				pstmt1.setString(1, cmpn_name);
				pstmt1.setString(2, cmpn_kana);
				pstmt1.setString(3, rpst_name);
				pstmt1.setString(4, brnc_name);
				pstmt1.setString(5, password);
				pstmt1.setString(6, zip1 + "-" + zip2);
				pstmt1.setString(7, address1);
				pstmt1.setString(8, address2);
				pstmt1.setString(9, address3);
				pstmt1.setString(10, address4);
				pstmt1.setString(11, number);
				pstmt1.setString(12, fax);
				pstmt1.setString(13, bank);
				pstmt1.setString(14, bank_branch);
				pstmt1.setString(15, account_type);
				pstmt1.setString(16, account_number);
				pstmt1.setString(17, account_name);
				pstmt1.setString(18, comment);
				pstmt1.setString(19, cmpn_id);
				pstmt1.executeUpdate();
				
				// エンコード
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				cmpn_name = URLEncoder.encode(cmpn_name, "UTF-8");
				cmpn_kana = URLEncoder.encode(cmpn_kana, "UTF-8");
				rpst_name = URLEncoder.encode(rpst_name, "UTF-8");
				brnc_name = URLEncoder.encode(brnc_name, "UTF-8");
				password = URLEncoder.encode(password, "UTF-8");
				zip1 = URLEncoder.encode(zip1, "UTF-8");
				zip2 = URLEncoder.encode(zip2, "UTF-8");
				address1 = URLEncoder.encode(address1, "UTF-8");
				address2 = URLEncoder.encode(address2, "UTF-8");
				address3 = URLEncoder.encode(address3, "UTF-8");
				address4 = URLEncoder.encode(address4, "UTF-8");
				number = URLEncoder.encode(number, "UTF-8");
				fax = URLEncoder.encode(fax, "UTF-8");
				bank = URLEncoder.encode(bank, "UTF-8");
				bank_branch = URLEncoder.encode(bank_branch, "UTF-8");
				account_type = URLEncoder.encode(account_type, "UTF-8");
				account_number = URLEncoder.encode(account_number, "UTF-8");
				account_name = URLEncoder.encode(account_name, "UTF-8");
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
			
			response.sendRedirect(request.getContextPath() + "/show/company");
		}
	}

}

package register;

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

import bean.CreateIdBean;
import home.RealestateDAO;

/**
 * Servlet implementation class RegisterCompanyServlet
 */
@WebServlet("/register/company")
public class RegisterCompanyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterCompanyServlet() {
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
			// 文字化け対策
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			// cmpnidを生成
			CreateIdBean cib = new CreateIdBean();
			String cmpnid = cib.getCreatedId("cmpn", "C", "");
				
			// cmpnidをセット
			request.setAttribute("cmpnid", cmpnid);
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_company.jsp").forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/top");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userName") == null && session.getAttribute("password") == null) {
			// 文字化け対策
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			// 接続情報
			RealestateDAO db = new RealestateDAO();
			Connection conn = null;
			
			// SQL
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			ResultSet rset1 = null;
			int row = 0;
			
			// 送信情報の取得
			String cmpn_id = request.getParameter("cmpn_id");
			String cmpn_name = request.getParameter("cmpn_name").replace("　", " ");
			String cmpn_kana = request.getParameter("cmpn_kana").replace("　", " ");
			String rpst_name = request.getParameter("rpst_name").replace("　", " ");
			String brnc_name = request.getParameter("brnc_name");
			String user_name = request.getParameter("user_name");
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
				
				// user_nameが既に登録されているか確認
				String sql1 = "SELECT * FROM cmpn WHERE usernm=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, user_name);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					row++;
				}
				
				if (row > 0) {
					// user_nameが既に登録されている場合
					String errorMessage = "ユーザー名がすでに登録されています";
					String[] cmpn = {cmpn_name,cmpn_kana,rpst_name,brnc_name,user_name,password,zip1,zip2,address1,address2,address3,address4,number,fax,bank,bank_branch,account_type,account_number,account_name,comment};
					request.setAttribute("errorMessage", errorMessage);
					request.setAttribute("cmpnid", cmpn_id);
					request.setAttribute("cmpn", cmpn);
				} else {
					// cmpnに保存
					String sql2 = "INSERT INTO cmpn(cmpnid,cmpnnm,cmpnnf,rpstnm,brncnm,usernm,passwd,zipcod,statnm,citynm,strno1,strno2,number,faxnum,banknm,bankbr,acntty,acntnu,acntnm,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, cmpn_id);
					pstmt2.setString(2, cmpn_name);
					pstmt2.setString(3, cmpn_kana);
					pstmt2.setString(4, rpst_name);
					pstmt2.setString(5, brnc_name);
					pstmt2.setString(6, user_name);
					pstmt2.setString(7, password);
					pstmt2.setString(8, zip1 + "-" + zip2);
					pstmt2.setString(9, address1);
					pstmt2.setString(10, address2);
					pstmt2.setString(11, address3);
					pstmt2.setString(12, address4);
					pstmt2.setString(13, number);
					pstmt2.setString(14, fax);
					pstmt2.setString(15, bank);
					pstmt2.setString(16, bank_branch);
					pstmt2.setString(17, account_type);
					pstmt2.setString(18, account_number);
					pstmt2.setString(19, account_name);
					pstmt2.setString(20, comment);
					pstmt2.executeUpdate();
					
					// エンコード
					cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
					cmpn_name = URLEncoder.encode(cmpn_name, "UTF-8");
					cmpn_kana = URLEncoder.encode(cmpn_kana, "UTF-8");
					rpst_name = URLEncoder.encode(rpst_name, "UTF-8");
					brnc_name = URLEncoder.encode(brnc_name, "UTF-8");
					user_name = URLEncoder.encode(user_name, "UTF-8");
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
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					if (row == 0) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (row == 0) {
				response.sendRedirect(request.getContextPath() + "/login");
			} else if (row > 0) {
				request.getRequestDispatcher("/WEB-INF/app/register/register_company.jsp").forward(request, response);
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/top");
		}
	}

}

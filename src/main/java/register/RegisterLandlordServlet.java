package register;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class RegisterLandlordServlet
 */
@WebServlet("/register/landlord")
public class RegisterLandlordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterLandlordServlet() {
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
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			// landidを生成
			CreateIdBean cib = new CreateIdBean();
			String landid = cib.getCreatedId("land", "L", cmpnid);
			
			// landidをセット
			request.setAttribute("landid", landid);
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_landlord.jsp").forward(request, response);
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
			String land_id = request.getParameter("land_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String land_name = request.getParameter("land_name").replace("　", " ");
			String land_kana = request.getParameter("land_kana").replace("　", " ");
			String zip1 = request.getParameter("zip1");
			String zip2 = request.getParameter("zip2");
			String address1 = request.getParameter("address1");
			String address2 = request.getParameter("address2");
			String address3 = request.getParameter("address3");
			String address4 = request.getParameter("address4");
			String number1 = request.getParameter("number1");
			String number2 = request.getParameter("number2");
			String email = request.getParameter("email");
			String bank = request.getParameter("bank");
			String bank_branch = request.getParameter("bank_branch");
			String account_type = request.getParameter("account_type");
			String account_number = request.getParameter("account_number");
			String account_name = request.getParameter("account_name");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// landに保存
				String sql1 = "INSERT INTO land(landid,cmpnid,landnm,landnf,zipcod,statnm,citynm,strno1,strno2,phone1,phone2,emladr,banknm,bankbr,acntty,acntnu,acntnm,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, land_id);
				pstmt1.setString(2, cmpn_id);
				pstmt1.setString(3, land_name);
				pstmt1.setString(4, land_kana);
				pstmt1.setString(5, zip1 + "-" + zip2);
				pstmt1.setString(6, address1);
				pstmt1.setString(7, address2);
				pstmt1.setString(8, address3);
				pstmt1.setString(9, address4);
				pstmt1.setString(10, number1);
				pstmt1.setString(11, number2);
				pstmt1.setString(12, email);
				pstmt1.setString(13, bank);
				pstmt1.setString(14, bank_branch);
				pstmt1.setString(15, account_type);
				pstmt1.setString(16, account_number);
				pstmt1.setString(17, account_name);
				pstmt1.setString(18, comment);
				pstmt1.executeUpdate();
				
				// エンコード
				land_id = URLEncoder.encode(land_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				land_name = URLEncoder.encode(land_name, "UTF-8");
				land_kana = URLEncoder.encode(land_kana, "UTF-8");
				zip1 = URLEncoder.encode(zip1, "UTF-8");
				zip2 = URLEncoder.encode(zip2, "UTF-8");
				address1 = URLEncoder.encode(address1, "UTF-8");
				address2 = URLEncoder.encode(address2, "UTF-8");
				address3 = URLEncoder.encode(address3, "UTF-8");
				address4 = URLEncoder.encode(address4, "UTF-8");
				number1 = URLEncoder.encode(number1, "UTF-8");
				number2 = URLEncoder.encode(number2, "UTF-8");
				email = URLEncoder.encode(email, "UTF-8");
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
			
			response.sendRedirect(request.getContextPath() + "/index/landlord");
		}
	}

}

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
 * Servlet implementation class RegisterResponsiblePersonServlet
 */
@WebServlet("/register/responsible_person")
public class RegisterResponsiblePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterResponsiblePersonServlet() {
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
			
			// rppsidを生成
			CreateIdBean cib = new CreateIdBean();
			String rppsid = cib.getCreatedId("rpps", "P", cmpnid);
			
			// rppsidをセット
			request.setAttribute("rppsid", rppsid);
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_responsible_person.jsp").forward(request, response);
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
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rppsに保存
				String sql1 = "INSERT INTO rpps(rppsid,cmpnid,rppsnm,rppsnf,phone1,phone2,emladr,commnt) VALUES(?,?,?,?,?,?,?,?)";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rppn_id);
				pstmt1.setString(2, cmpn_id);
				pstmt1.setString(3, rppn_name);
				pstmt1.setString(4, rppn_kana);
				pstmt1.setString(5, number1);
				pstmt1.setString(6, number2);
				pstmt1.setString(7, email);
				pstmt1.setString(8, comment);
				pstmt1.executeUpdate();
				
				// エンコード
				rppn_id = URLEncoder.encode(rppn_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rppn_name = URLEncoder.encode(rppn_name, "UTF-8");
				rppn_kana = URLEncoder.encode(rppn_kana, "UTF-8");
				number1 = URLEncoder.encode(number1, "UTF-8");
				number2 = URLEncoder.encode(number2, "UTF-8");
				email = URLEncoder.encode(email, "UTF-8");
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
			
			response.sendRedirect(request.getContextPath() + "/index/responsible_person");
		}
	}

}

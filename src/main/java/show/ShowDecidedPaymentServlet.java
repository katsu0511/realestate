package show;

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
 * Servlet implementation class ShowDecidedPaymentServlet
 */
@WebServlet("/show/decided_payment")
public class ShowDecidedPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowDecidedPaymentServlet() {
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
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			boolean data_exists = false;
			
			// pymtnuの取得
			String pymtnu = request.getParameter("id");
			
			// cmpnidの取得
			String cmpnid = (String) session.getAttribute("cmpnid");

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// depyの情報取得
				String sql1 = "SELECT * FROM depy WHERE pymtnu=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, pymtnu);
				pstmt1.setString(2, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> depy = new ArrayList<String>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 10; i++){
						depy.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("depy", depy);
				
				// tnntnmの取得
				String sql2 = "SELECT tnntnm FROM tnnt WHERE tnntid=("
						    +     "SELECT tnntid FROM depy WHERE pymtnu=?"
						    + ") "
						    + "AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, pymtnu);
				pstmt2.setString(2, cmpnid);
				rset2 = pstmt2.executeQuery();
				String tnntnm = null;
				
				if (rset2.next()) {
					tnntnm = rset2.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("tnntnm", tnntnm);
				
				// rsdcnmの取得
				String sql3 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=("
						    +     "SELECT rsdcid FROM depy WHERE pymtnu=?"
						    + ") "
						    + "AND cmpnid=?";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, pymtnu);
				pstmt3.setString(2, cmpnid);
				rset3 = pstmt3.executeQuery();
				String rsdcnm = null;
				
				if (rset3.next()) {
					rsdcnm = rset3.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("rsdcnm", rsdcnm);
				
				// roomnuの取得
				String sql4 = "SELECT roomnu FROM room WHERE roomid=("
						    +     "SELECT roomid FROM depy WHERE pymtnu=?"
						    + ") "
						    + "AND cmpnid=?";
				pstmt4 = conn.prepareStatement(sql4);
				pstmt4.setString(1, pymtnu);
				pstmt4.setString(2, cmpnid);
				rset4 = pstmt4.executeQuery();
				String roomnu = null;
				
				if (rset4.next()) {
					roomnu = rset4.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("roomnu", roomnu);
				
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
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (data_exists) {
				request.getRequestDispatcher("/WEB-INF/app/show/show_decided_payment.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/decided_payment");
			}
		}
	}

}

package index;

import java.io.IOException;
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
 * Servlet implementation class IndexDecidedPaymentServlet
 */
@WebServlet("/index/decided_payment")
public class IndexDecidedPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexDecidedPaymentServlet() {
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
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// depyの情報取得
				String sql1 = "SELECT * FROM depy WHERE cmpnid=? ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> payments = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					// tnntnmの取得
					String sql2 = "SELECT tnntnm FROM tnnt WHERE tnntid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(4));
					rset2 = pstmt2.executeQuery();
					String tnntnm = null;
					if (rset2.next()) {
						tnntnm = rset2.getString(1);
					}
					
					// rsdcnmの取得
					String sql3 = "SELECT rsdcnm FROM rsdc WHERE rsdcid=?";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, rset1.getString(5));
					rset3 = pstmt3.executeQuery();
					String rsdcnm = null;
					if (rset3.next()) {
						rsdcnm = rset3.getString(1);
					}
					
					// roomnuの取得
					String sql4 = "SELECT roomnu FROM room WHERE roomid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, rset1.getString(6));
					rset4 = pstmt4.executeQuery();
					String roomnu = null;
					if (rset4.next()) {
						roomnu = rset4.getString(1);
					}
					
					Map<String, String> payment = new HashMap<>();
					payment.put("pymtnu", rset1.getString(1));
					payment.put("paydat", rset1.getString(3));
					payment.put("tnntnm", tnntnm);
					payment.put("rsdcnm", rsdcnm);
					payment.put("roomnu", roomnu);
					payment.put("payamt", rset1.getString(7));
					payment.put("rcvamt", rset1.getString(8));
					payment.put("balanc", rset1.getString(9));
					payments.add(payment);
				}
				
				request.setAttribute("payments", payments);
				
			} catch (SQLException e) {
				e.printStackTrace();
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
					if (rset1.next()) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (rset1.next()) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			request.getRequestDispatcher("/WEB-INF/app/index/index_decided_payment.jsp").forward(request, response);
		}
	}

}

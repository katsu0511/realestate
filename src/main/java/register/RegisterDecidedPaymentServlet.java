package register;

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
 * Servlet implementation class RegisterDecidedPaymentServlet
 */
@WebServlet("/register/decided_payment")
public class RegisterDecidedPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterDecidedPaymentServlet() {
        super();
        // TODO Auto-generated constructor stub
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
			
			// 送信情報の取得
			String pay_nu = request.getParameter("pay_nu");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rsdc_id = request.getParameter("rsdc_id");
			String room_id = request.getParameter("room_id");
			String tnnt_id = request.getParameter("tnnt_id");
			String payamt = request.getParameter("payamt");
			String balance = request.getParameter("balance");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// udpy情報を取得
				String sql1 = "SELECT * FROM udpy WHERE pymtnu=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, pay_nu);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> udpy = new ArrayList<>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 7; i++){
						udpy.add(rset1.getString(i));
					}
				}
				
				// udpyのcmfrmdをtrueに変更
				String sql2 = "UPDATE udpy SET cmfrmd=true WHERE pymtnu=? AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, pay_nu);
				pstmt2.setString(2, cmpn_id);
				int edited1 = pstmt2.executeUpdate();
				
				// depyに保存
				String sql3 = "INSERT INTO depy(pymtnu,cmpnid,paydat,tnntid,rsdcid,roomid,payamt,rcvamt,balanc) VALUES(?,?,?,?,?,?,?,?,?)";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, pay_nu);
				pstmt3.setString(2, cmpn_id);
				pstmt3.setString(3, udpy.get(2));
				pstmt3.setString(4, tnnt_id);
				pstmt3.setString(5, rsdc_id);
				pstmt3.setString(6, room_id);
				pstmt3.setString(7, payamt);
				pstmt3.setString(8, udpy.get(4));
				pstmt3.setString(9, balance);
				int edited2 = pstmt3.executeUpdate();
				
				// 削除できていない場合はロールバック
				if (edited1 == 1 && edited2 == 1) {
					conn.commit();
				} else {
					conn.rollback();
				}
				
				// エンコード
				pay_nu = URLEncoder.encode(pay_nu, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rsdc_id = URLEncoder.encode(rsdc_id, "UTF-8");
				room_id = URLEncoder.encode(room_id, "UTF-8");
				tnnt_id = URLEncoder.encode(tnnt_id, "UTF-8");
				payamt = URLEncoder.encode(payamt, "UTF-8");
				balance = URLEncoder.encode(balance, "UTF-8");
				
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
					conn.close();
				} catch (SQLException e) {  }
			}
			
			response.sendRedirect(request.getContextPath() + "/index/undecided_payment");
		}
	}

}

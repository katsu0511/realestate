package register;

import java.io.IOException;
import java.net.URLEncoder;
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

import bean.CreateIdBean;
import home.RealestateDAO;

/**
 * Servlet implementation class RegisterTenantServlet
 */
@WebServlet("/register/tenant")
public class RegisterTenantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterTenantServlet() {
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
			ArrayList<String> rsdcids = new ArrayList<String>();
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// tnntidを生成
				CreateIdBean cib = new CreateIdBean();
				String tnntid = cib.getCreatedId("tnnt", "T", cmpnid);
				
				// tnntidをセット
				request.setAttribute("tnntid", tnntid);
				
				// 担当者情報を取得
				String sql1 = "SELECT rppsid,rppsnm FROM rpps WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> rppns = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					Map<String, String> rppn = new HashMap<>();
					rppn.put("rppsid", rset1.getString(1));
					rppn.put("rppsnm", rset1.getString(2));
					rppns.add(rppn);
				}
				
				request.setAttribute("rppns", rppns);
				
				// 物件情報を取得
				String sql2 = "SELECT rsdcid FROM rsdc WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, cmpnid);
				rset2 = pstmt2.executeQuery();
				ArrayList<Map<String, String>> rsdcs = new ArrayList<Map<String, String>>();
				
				while (rset2.next()) {
					rsdcids.add(rset2.getString(1));
				}
				
				for (String rsdcid: rsdcids) {
					String sql3 = "SELECT roomid FROM room WHERE cmpnid=? AND rsdcid=? AND cntrct='f' AND actvfg='t'";
					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setString(1, cmpnid);
					pstmt3.setString(2, rsdcid);
					rset3 = pstmt3.executeQuery();
					
					if (rset3.next()) {
						String sql4 = "SELECT rsdcnm FROM rsdc WHERE cmpnid=? AND rsdcid=?";
						pstmt4 = conn.prepareStatement(sql4);
						pstmt4.setString(1, cmpnid);
						pstmt4.setString(2, rsdcid);
						rset4 = pstmt4.executeQuery();
						
						if (rset4.next()) {
							Map<String, String> rsdc = new HashMap<>();
							rsdc.put("rsdcid", rsdcid);
							rsdc.put("rsdcnm", rset4.getString(1));
							rsdcs.add(rsdc);
						}
					}
				}
				
				request.setAttribute("rsdcs", rsdcs);
				
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
					if (rsdcids.size() != 0) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (rsdcids.size() != 0 && rset4.next()) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_tenant.jsp").forward(request, response);
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
			
			// 送信情報の取得
			String tnnt_id = request.getParameter("tnnt_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String rp_prsn = request.getParameter("rp_prsn");
			String cont_rsd = request.getParameter("cont_rsd");
			String cont_room = request.getParameter("cont_room");
			String cont_date = request.getParameter("cont_date");
			String tnnt_name = request.getParameter("tnnt_name").replace("　", " ");
			String tnnt_kana = request.getParameter("tnnt_kana").replace("　", " ");
			String zip1 = request.getParameter("zip1");
			String zip2 = request.getParameter("zip2");
			String address1 = request.getParameter("address1");
			String address2 = request.getParameter("address2");
			String address3 = request.getParameter("address3");
			String address4 = request.getParameter("address4");
			String number1 = request.getParameter("number1");
			String number2 = request.getParameter("number2");
			String email = request.getParameter("email");
			String birthday = request.getParameter("birthday");
			String bank = request.getParameter("bank");
			String bank_branch = request.getParameter("bank_branch");
			String account_type = request.getParameter("account_type");
			String account_number = request.getParameter("account_number");
			String account_name = request.getParameter("account_name");
			String wkplc_name = request.getParameter("wkplc_name").replace("　", " ");
			String wkplc_kana = request.getParameter("wkplc_kana").replace("　", " ");
			String wkplc_zip1 = request.getParameter("wkplc_zip1");
			String wkplc_zip2 = request.getParameter("wkplc_zip2");
			String wkplc_zip = wkplc_zip1.equals("") || wkplc_zip2.equals("") ? "" : wkplc_zip1 + "-" + wkplc_zip2;
			String wkplc_address1 = request.getParameter("wkplc_address1");
			String wkplc_address2 = request.getParameter("wkplc_address2");
			String wkplc_address3 = request.getParameter("wkplc_address3");
			String wkplc_address4 = request.getParameter("wkplc_address4");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// オートコミットをオフ
				conn.setAutoCommit(false);
				
				// tnntに保存
				String sql1 = "INSERT INTO tnnt(tnntid,cmpnid,rppsid,rsdcid,roomid,contdt,tnntnm,tnntnf,zipcod,statnm,citynm,strno1,strno2,phone1,phone2,emladr,brthdy,banknm,bankbr,acntty,acntnu,acntnm,wkpcnm,wkpcnf,wkpzip,wkpsta,wkpcit,wkpst1,wkpst2,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, tnnt_id);
				pstmt1.setString(2, cmpn_id);
				pstmt1.setString(3, rp_prsn);
				pstmt1.setString(4, cont_rsd);
				pstmt1.setString(5, cont_room);
				pstmt1.setString(6, cont_date);
				pstmt1.setString(7, tnnt_name);
				pstmt1.setString(8, tnnt_kana);
				pstmt1.setString(9, zip1 + '-' + zip2);
				pstmt1.setString(10, address1);
				pstmt1.setString(11, address2);
				pstmt1.setString(12, address3);
				pstmt1.setString(13, address4);
				pstmt1.setString(14, number1);
				pstmt1.setString(15, number2);
				pstmt1.setString(16, email);
				pstmt1.setString(17, birthday);
				pstmt1.setString(18, bank);
				pstmt1.setString(19, bank_branch);
				pstmt1.setString(20, account_type);
				pstmt1.setString(21, account_number);
				pstmt1.setString(22, account_name);
				pstmt1.setString(23, wkplc_name);
				pstmt1.setString(24, wkplc_kana);
				pstmt1.setString(25, wkplc_zip);
				pstmt1.setString(26, wkplc_address1);
				pstmt1.setString(27, wkplc_address2);
				pstmt1.setString(28, wkplc_address3);
				pstmt1.setString(29, wkplc_address4);
				pstmt1.setString(30, comment);
				int edited1 = pstmt1.executeUpdate();
				int edited2 = 0;
				
				// 契約中の部屋が選択されている場合
				if (!cont_room.equals("")) {
					// roomのcntrctを変更
					String sql2 = "UPDATE room SET cntrct=? WHERE roomid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setBoolean(1, true);
					pstmt2.setString(2, cont_room);
					pstmt2.setString(3, cmpn_id);
					edited2 = pstmt2.executeUpdate();
				}
				
				// 保存できていない場合はロールバック
				// 契約中の部屋が選択されている場合
				if (!cont_room.equals("")) {
					if (edited1 == 1 && edited2 == 1) {
						conn.commit();
					} else {
						conn.rollback();
					}
				} else {
					if (edited1 == 1) {
						conn.commit();
					} else {
						conn.rollback();
					}
				}
				
				// エンコード
				tnnt_id = URLEncoder.encode(tnnt_id, "UTF-8");
				cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
				rp_prsn = URLEncoder.encode(rp_prsn, "UTF-8");
				cont_rsd = URLEncoder.encode(cont_rsd, "UTF-8");
				cont_room = URLEncoder.encode(cont_room, "UTF-8");
				cont_date = URLEncoder.encode(cont_date, "UTF-8");
				tnnt_name = URLEncoder.encode(tnnt_name, "UTF-8");
				tnnt_kana = URLEncoder.encode(tnnt_kana, "UTF-8");
				zip1 = URLEncoder.encode(zip1, "UTF-8");
				zip2 = URLEncoder.encode(zip2, "UTF-8");
				address1 = URLEncoder.encode(address1, "UTF-8");
				address2 = URLEncoder.encode(address2, "UTF-8");
				address3 = URLEncoder.encode(address3, "UTF-8");
				address4 = URLEncoder.encode(address4, "UTF-8");
				number1 = URLEncoder.encode(number1, "UTF-8");
				number2 = URLEncoder.encode(number2, "UTF-8");
				email = URLEncoder.encode(email, "UTF-8");
				birthday = URLEncoder.encode(birthday, "UTF-8");
				bank = URLEncoder.encode(bank, "UTF-8");
				bank_branch = URLEncoder.encode(bank_branch, "UTF-8");
				account_type = URLEncoder.encode(account_type, "UTF-8");
				account_number = URLEncoder.encode(account_number, "UTF-8");
				account_name = URLEncoder.encode(account_name, "UTF-8");
				wkplc_name = URLEncoder.encode(wkplc_name, "UTF-8");
				wkplc_kana = URLEncoder.encode(wkplc_kana, "UTF-8");
				wkplc_zip1 = URLEncoder.encode(wkplc_zip1, "UTF-8");
				wkplc_zip2 = URLEncoder.encode(wkplc_zip2, "UTF-8");
				wkplc_address1 = URLEncoder.encode(wkplc_address1, "UTF-8");
				wkplc_address2 = URLEncoder.encode(wkplc_address2, "UTF-8");
				wkplc_address3 = URLEncoder.encode(wkplc_address3, "UTF-8");
				wkplc_address4 = URLEncoder.encode(wkplc_address4, "UTF-8");
				comment = URLEncoder.encode(comment, "UTF-8");
				
				// オートコミットをオン
				conn.setAutoCommit(true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt1.close();
				} catch (SQLException e) { }
				
				try {
					if (!cont_room.equals("")) {
						pstmt2.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (request.getParameter("submit").equals("登録終了")) {
				response.sendRedirect(request.getContextPath() + "/index/tenant");
			} else {
				response.sendRedirect(request.getContextPath() + "/register/tenant");
			}
		}
	}

}

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
import bean.CreateIdBean2;
import home.RealestateDAO;

/**
 * Servlet implementation class RegisterResidenceServlet
 */
@WebServlet("/register/residence")
public class RegisterResidenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterResidenceServlet() {
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
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rsdcidを生成
				CreateIdBean cib = new CreateIdBean();
				String rsdcid = cib.getCreatedId("rsdc", "R", cmpnid);
				
				// rsdcidをセット
				request.setAttribute("rsdcid", rsdcid);
				
				// 大家情報を取得
				String sql1 = "SELECT landid,landnm FROM land WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> lands = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					Map<String, String> land = new HashMap<>();
					land.put("landid", rset1.getString(1));
					land.put("landnm", rset1.getString(2));
					lands.add(land);
				}
				
				request.setAttribute("lands", lands);
				
				// 物件追加費用項目情報を取得
				String sql2 = "SELECT * FROM adit WHERE cmpnid=? ORDER BY rgdttm ASC";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, cmpnid);
				rset2 = pstmt2.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				
				while (rset2.next()) {
					Map<String, String> adit = new HashMap<>();
					adit.put("aditid", rset2.getString(1));
					adit.put("itemnm", rset2.getString(3));
					adit.put("itemid", rset2.getString(4));
					adit.put("maxlng", rset2.getString(5));
					adit.put("srclng", rset2.getString(6));
					adit.put("commnt", rset2.getString(7));
					adits.add(adit);
				}
				
				request.setAttribute("adits", adits);
				
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
					conn.close();
				} catch (SQLException e) {  }
			}
			
			request.getRequestDispatcher("/WEB-INF/app/register/register_residence.jsp").forward(request, response);
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
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			PreparedStatement pstmt7 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			int adebRow = 0;
			int row1 = 0;
			int row2 = 0;
			
			// 送信情報の取得
			String rsd_id = request.getParameter("rsd_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String land_id = request.getParameter("land_id");
			String rsd_name = request.getParameter("rsd_name").replace("　", " ");
			String rsd_kana = request.getParameter("rsd_kana").replace("　", " ");
			String zip1 = request.getParameter("zip1");
			String zip2 = request.getParameter("zip2");
			String address1 = request.getParameter("address1");
			String address2 = request.getParameter("address2");
			String address3 = request.getParameter("address3");
			String rent = request.getParameter("rent");
			String mng_fee = request.getParameter("mng_fee");
			String deposit = request.getParameter("deposit");
			String honorarium = request.getParameter("honorarium");
			String brokerage = request.getParameter("brokerage");
			String key_fee = request.getParameter("key_fee");
			String comment = request.getParameter("comment");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rsd_nameが既に登録されているか確認
				String sql1 = "SELECT * FROM rsdc WHERE cmpnid=? AND rsdcnm=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsd_name);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					row1++;
				}
				
				// rsd_kanaが既に登録されているか確認
				String sql2 = "SELECT * FROM rsdc WHERE cmpnid=? AND rsdcnf=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, cmpn_id);
				pstmt2.setString(2, rsd_kana);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					row2++;
				}
				
				// 追加費用項目情報を取得
				String sql3 = "SELECT * FROM adit WHERE cmpnid=? ORDER BY rgdttm ASC";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, cmpn_id);
				rset3 = pstmt3.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				
				while (rset3.next()) {
					Map<String, String> adit = new HashMap<>();
					adit.put("aditid", rset3.getString(1));
					adit.put("itemnm", rset3.getString(3));
					adit.put("itemid", rset3.getString(4));
					adit.put("maxlng", rset3.getString(5));
					adit.put("srclng", rset3.getString(6));
					adit.put("commnt", rset3.getString(7));
					adits.add(adit);
				}
				
				// rsd_nameかrsd_kanaが既に登録されている場合
				if (row1 > 0 || row2 > 0) {
					// 大家情報を取得
					String sql4 = "SELECT landid,landnm FROM land WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, cmpn_id);
					rset4 = pstmt4.executeQuery();
					ArrayList<Map<String, String>> lands = new ArrayList<Map<String, String>>();
					
					while (rset4.next()) {
						Map<String, String> land = new HashMap<>();
						land.put("landid", rset4.getString(1));
						land.put("landnm", rset4.getString(2));
						lands.add(land);
					}
					
					String errorMessage = null;
					if (row1 > 0 && row2 > 0) {
						errorMessage = "物件名と物件名フリガナがすでに登録されています";
					} else if (row1 > 0 && row2 == 0) {
						errorMessage = "物件名がすでに登録されています";
					} else if (row1 == 0 && row2 > 0) {
						errorMessage = "物件名フリガナがすでに登録されています";
					}
					
					String[] rsdc = {land_id,rsd_name,rsd_kana,zip1,zip2,address1,address2,address3,rent,mng_fee,deposit,honorarium,brokerage,key_fee,comment};
					request.setAttribute("errorMessage", errorMessage);
					request.setAttribute("rsdcid", rsd_id);
					request.setAttribute("rsdc", rsdc);
					request.setAttribute("lands", lands);
					request.setAttribute("adits", adits);
				} else {
					// 追加費用項目のitemidを取得し配列に
					ArrayList<String> itemids = new ArrayList<String>();
					for (Map<String, String> adit : adits) {
						itemids.add(adit.get("itemid"));
					}
					
					for (int i = 0; i < itemids.size(); i++) {
						String adebvl = request.getParameter(itemids.get(i));
						
						if (!adebvl.equals("")) {
							adebRow++;
							
							// adebidを生成
							CreateIdBean2 cib2 = new CreateIdBean2();
							String adebid = cib2.getCreatedId("adeb", "adebid", cmpn_id);
							
							// aditidを取得
							String sql5 = "SELECT aditid FROM adit WHERE cmpnid=? AND itemid=?";
							pstmt5 = conn.prepareStatement(sql5);
							pstmt5.setString(1, cmpn_id);
							pstmt5.setString(2, itemids.get(i));
							rset5 = pstmt5.executeQuery();
							String aditid = null;
							
							if (rset5.next()) {
								aditid = rset5.getString(1);
							}
							
							// adebに保存
							String sql6 = "INSERT INTO adeb(adebid,cmpnid,rsdcid,aditid,adebvl) VALUES(?,?,?,?,?)";
							pstmt6 = conn.prepareStatement(sql6);
							pstmt6.setString(1, adebid);
							pstmt6.setString(2, cmpn_id);
							pstmt6.setString(3, rsd_id);
							pstmt6.setString(4, aditid);
							pstmt6.setString(5, adebvl);
							pstmt6.executeUpdate();
						}
					}
					
					// rsdcに保存
					String sql7 = "INSERT INTO rsdc(rsdcid,cmpnid,landid,rsdcnm,rsdcnf,zipcod,statnm,citynm,strno1,rentfe,mngfee,depsit,hnrrum,brkrag,keyfee,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt7 = conn.prepareStatement(sql7);
					pstmt7.setString(1, rsd_id);
					pstmt7.setString(2, cmpn_id);
					pstmt7.setString(3, land_id);
					pstmt7.setString(4, rsd_name);
					pstmt7.setString(5, rsd_kana);
					pstmt7.setString(6, zip1 + "-" + zip2);
					pstmt7.setString(7, address1);
					pstmt7.setString(8, address2);
					pstmt7.setString(9, address3);
					pstmt7.setString(10, rent);
					pstmt7.setString(11, mng_fee);
					pstmt7.setString(12, deposit);
					pstmt7.setString(13, honorarium);
					pstmt7.setString(14, brokerage);
					pstmt7.setString(15, key_fee);
					pstmt7.setString(16, comment);
					pstmt7.executeUpdate();
					
					// エンコード
					rsd_id = URLEncoder.encode(rsd_id, "UTF-8");
					cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
					land_id = URLEncoder.encode(land_id, "UTF-8");
					rsd_name = URLEncoder.encode(rsd_name, "UTF-8");
					rsd_kana = URLEncoder.encode(rsd_kana, "UTF-8");
					zip1 = URLEncoder.encode(zip1, "UTF-8");
					zip2 = URLEncoder.encode(zip2, "UTF-8");
					address1 = URLEncoder.encode(address1, "UTF-8");
					address2 = URLEncoder.encode(address2, "UTF-8");
					address3 = URLEncoder.encode(address3, "UTF-8");
					rent = URLEncoder.encode(rent, "UTF-8");
					mng_fee = URLEncoder.encode(mng_fee, "UTF-8");
					deposit = URLEncoder.encode(deposit, "UTF-8");
					honorarium = URLEncoder.encode(honorarium, "UTF-8");
					brokerage = URLEncoder.encode(brokerage, "UTF-8");
					key_fee = URLEncoder.encode(key_fee, "UTF-8");
					comment = URLEncoder.encode(comment, "UTF-8");
				}
				
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
					if (row1 > 0 || row2 > 0) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0 && row2 == 0 && adebRow > 0) {
						pstmt5.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0 && row2 == 0 && adebRow > 0) {
						pstmt6.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0 && row2 == 0) {
						pstmt7.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (row1 > 0 || row2 > 0) {
				request.getRequestDispatcher("/WEB-INF/app/register/register_residence.jsp").forward(request, response);
			} else if (row1 == 0 && row2 == 0) {
				if (request.getParameter("submit").equals("登録終了")) {
					response.sendRedirect(request.getContextPath() + "/index/residence");
				} else {
					response.sendRedirect(request.getContextPath() + "/register/room");
				}
			}
		}
	}

}

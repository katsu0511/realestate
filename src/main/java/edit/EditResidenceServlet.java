package edit;

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

import bean.CreateIdBean2;
import home.RealestateDAO;

/**
 * Servlet implementation class EditResidenceServlet
 */
@WebServlet("/edit/residence")
public class EditResidenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditResidenceServlet() {
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
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			ResultSet rset6 = null;
			boolean data_exists = false;
			
			// rsdc_idの取得
			String rsdc_id = request.getParameter("id");
			
			// cmpn_idの取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// rsdc情報を取得
				String sql1 = "SELECT * FROM rsdc WHERE rsdcid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rsdc_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<String> residence = new ArrayList<>();
				
				if (rset1.next()) {
					for (int i = 1; i <= 19; i++){
						residence.add(rset1.getString(i));
					}
					data_exists = true;
				}
				
				request.setAttribute("residence", residence);
				
				// landnmを取得
				String sql2 = "SELECT landnm FROM land WHERE landid=("
						    +     "SELECT landid FROM rsdc WHERE rsdcid=?"
						    + ") "
						    + "AND cmpnid=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rsdc_id);
				pstmt2.setString(2, cmpn_id);
				rset2 = pstmt2.executeQuery();
				String landnm = null;
				
				if (rset2.next()) {
					landnm = rset2.getString(1);
				} else {
					data_exists = false;
				}
				
				request.setAttribute("landnm", landnm);
				
				// aditid,adebvlを取得
				String sql3 = "SELECT aditid,adebvl FROM adeb WHERE cmpnid=? AND rsdcid=? ORDER BY adebid ASC";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, cmpn_id);
				pstmt3.setString(2, rsdc_id);
				rset3 = pstmt3.executeQuery();
				ArrayList<Map<String, String>> usedadits = new ArrayList<Map<String, String>>();
				
				while (rset3.next()) {
					// aditの情報取得
					String sql4 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt4 = conn.prepareStatement(sql4);
					pstmt4.setString(1, rset3.getString(1));
					pstmt4.setString(2, cmpn_id);
					rset4 = pstmt4.executeQuery();
					
					if (rset4.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset4.getString(1));
						adit.put("itemnm", rset4.getString(3));
						adit.put("itemid", rset4.getString(4));
						adit.put("maxlng", rset4.getString(5));
						adit.put("srclng", rset4.getString(6));
						adit.put("commnt", rset4.getString(7));
						adit.put("adebvl", rset3.getString(2));
						usedadits.add(adit);
					}
				}
				
				request.setAttribute("usedadits", usedadits);
				
				// adebで使われていないaditidの取得
				String sql5 = "SELECT aditid FROM adit WHERE cmpnid=? ORDER BY aditid ASC";
				pstmt5 = conn.prepareStatement(sql5);
				pstmt5.setString(1, cmpn_id);
				rset5 = pstmt5.executeQuery();
				ArrayList<String> unused_aditids = new ArrayList<String>();
				
				boolean used = false;
				while (rset5.next()) {
					used = false;
					
					for (Map<String, String> usedadit: usedadits) {
						if (rset5.getString(1).equals(usedadit.get("aditid"))) {
							used = true;
						}
					}
					
					if (used == false) {
						unused_aditids.add(rset5.getString(1));
					}
				}
				
				// まだ使われていないaditの情報取得
				ArrayList<Map<String, String>> unused_adits = new ArrayList<Map<String, String>>();
				String sql6 = "SELECT * FROM adit WHERE cmpnid=? AND aditid=?";
				pstmt6 = conn.prepareStatement(sql6);
				pstmt6.setString(1, cmpn_id);
				
				for (String unused_aditid : unused_aditids) {
					pstmt6.setString(2, unused_aditid);
					rset6 = pstmt6.executeQuery();
					
					if (rset6.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset6.getString(1));
						adit.put("itemnm", rset6.getString(3));
						adit.put("itemid", rset6.getString(4));
						adit.put("maxlng", rset6.getString(5));
						adit.put("srclng", rset6.getString(6));
						adit.put("commnt", rset6.getString(7));
						unused_adits.add(adit);
					}
				}
				
				request.setAttribute("unused_adits", unused_adits);
				
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
					if (rset3.next()) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					pstmt5.close();
				} catch (SQLException e) { }
				
				try {
					pstmt6.close();
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (data_exists) {
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_residence.jsp").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/index/residence");
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
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;
			PreparedStatement pstmt5 = null;
			PreparedStatement pstmt6 = null;
			PreparedStatement pstmt7 = null;
			PreparedStatement pstmt8 = null;
			PreparedStatement pstmt9 = null;
			PreparedStatement pstmt10 = null;
			PreparedStatement pstmt11 = null;
			PreparedStatement pstmt12 = null;
			PreparedStatement pstmt13 = null;
			PreparedStatement pstmt14 = null;
			PreparedStatement pstmt15 = null;
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			ResultSet rset5 = null;
			ResultSet rset6 = null;
			ResultSet rset7 = null;
			ResultSet rset10 = null;
			int row1 = 0;
			int row2 = 0;
			boolean committed = false;
			
			// 送信情報の取得
			String rsdc_id = request.getParameter("rsdc_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String land_nm = request.getParameter("land_nm");
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
			String actv_flg = request.getParameter("actv_flg");
			Boolean actvflg = Boolean.valueOf(actv_flg);
			String actv_before_change = request.getParameter("actv_before_change").equals("t") ? "true" : "false";
			String comment = request.getParameter("comment");
			int numberOfRooms = 0;
			int edited1 = 0;
			int edited2 = 0;
			int edited3 = 0;
			int edited4 = 0;
			int edited5 = 0;
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 部屋登録があるか確認（登録されている部屋数を取得）
				String sql1 = "SELECT * FROM room WHERE rsdcid=? AND cmpnid=?";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, rsdc_id);
				pstmt1.setString(2, cmpn_id);
				rset1 = pstmt1.executeQuery();
				while (rset1.next()) {
					numberOfRooms++;
				}
				
				// rsd_nameが既に登録されているか確認
				String sql2 = "SELECT * FROM rsdc WHERE rsdcnm=? AND rsdcid != ?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rsd_name);
				pstmt2.setString(2, rsdc_id);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					row1++;
				}
				
				// rsd_kanaが既に登録されているか確認
				String sql3 = "SELECT * FROM rsdc WHERE rsdcnf=? AND rsdcid != ?";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, rsd_kana);
				pstmt3.setString(2, rsdc_id);
				rset3 = pstmt3.executeQuery();
				while (rset3.next()) {
					row2++;
				}
				
				// adebに登録済のaditの配列
				ArrayList<Map<String, String>> usedadits = new ArrayList<Map<String, String>>();
				// aditid,adebvlを取得
				String sql4 = "SELECT aditid,adebvl FROM adeb WHERE cmpnid=? AND rsdcid=? ORDER BY adebid ASC";
				pstmt4 = conn.prepareStatement(sql4);
				pstmt4.setString(1, cmpn_id);
				pstmt4.setString(2, rsdc_id);
				rset4 = pstmt4.executeQuery();
				while (rset4.next()) {
					// aditの情報取得
					String sql5 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt5 = conn.prepareStatement(sql5);
					pstmt5.setString(1, rset4.getString(1));
					pstmt5.setString(2, cmpn_id);
					rset5 = pstmt5.executeQuery();
					if (rset5.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset5.getString(1));
						adit.put("itemnm", rset5.getString(3));
						adit.put("itemid", rset5.getString(4));
						adit.put("maxlng", rset5.getString(5));
						adit.put("srclng", rset5.getString(6));
						adit.put("commnt", rset5.getString(7));
						adit.put("adebvl", rset4.getString(2));
						usedadits.add(adit);
					}
				}
				
				// adebに未登録のaditidの配列
				ArrayList<String> unused_aditids = new ArrayList<String>();
				// adebで使われていないaditidの取得
				String sql6 = "SELECT aditid FROM adit WHERE cmpnid=? ORDER BY aditid ASC";
				pstmt6 = conn.prepareStatement(sql6);
				pstmt6.setString(1, cmpn_id);
				rset6 = pstmt6.executeQuery();
				boolean used = false;
				while (rset6.next()) {
					used = false;
					for (Map<String, String> usedadit: usedadits) {
						if (rset6.getString(1).equals(usedadit.get("aditid"))) {
							used = true;
						}
					}
					if (used == false) {
						unused_aditids.add(rset6.getString(1));
					}
				}
				
				// まだ使われていないaditの情報取得
				ArrayList<Map<String, String>> unused_adits = new ArrayList<Map<String, String>>();
				String sql7 = "SELECT * FROM adit WHERE cmpnid=? AND aditid=?";
				pstmt7 = conn.prepareStatement(sql7);
				pstmt7.setString(1, cmpn_id);
				for (String unused_aditid : unused_aditids) {
					pstmt7.setString(2, unused_aditid);
					rset7 = pstmt7.executeQuery();
					if (rset7.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset7.getString(1));
						adit.put("itemnm", rset7.getString(3));
						adit.put("itemid", rset7.getString(4));
						adit.put("maxlng", rset7.getString(5));
						adit.put("srclng", rset7.getString(6));
						adit.put("commnt", rset7.getString(7));
						unused_adits.add(adit);
					}
				}
				
				// rsd_nameかrsd_kanaが既に登録されている場合
				if (row1 > 0 || row2 > 0) {
					String errorMessage = null;
					if (row1 > 0 && row2 > 0) {
						errorMessage = "物件名と物件名フリガナがすでに登録されています";
					} else if (row1 > 0 && row2 == 0) {
						errorMessage = "物件名がすでに登録されています";
					} else if (row1 == 0 && row2 > 0) {
						errorMessage = "物件名フリガナがすでに登録されています";
					}
					
					String[] residence = {rsdc_id,"","",rsd_name,rsd_kana,zip1 + "-" + zip2,address1,address2,address3,rent,mng_fee,deposit,honorarium,brokerage,key_fee,actv_flg.substring(0,1),comment};
					request.setAttribute("errorMessage", errorMessage);
					request.setAttribute("landnm", land_nm);
					request.setAttribute("residence", residence);
					request.setAttribute("usedadits", usedadits);
					request.setAttribute("unused_adits", unused_adits);
				} else {
					// オートコミットをオフ
					conn.setAutoCommit(false);
					
					// rsdcに保存
					String sql8 = "UPDATE rsdc SET rsdcnm=?,rsdcnf=?,zipcod=?,statnm=?,citynm=?,strno1=?,rentfe=?,mngfee=?,depsit=?,hnrrum=?,brkrag=?,keyfee=?,actvfg=?,commnt=?,uddttm=current_timestamp WHERE rsdcid=? AND cmpnid=?";
					pstmt8 = conn.prepareStatement(sql8);
					pstmt8.setString(1, rsd_name);
					pstmt8.setString(2, rsd_kana);
					pstmt8.setString(3, zip1 + "-" + zip2);
					pstmt8.setString(4, address1);
					pstmt8.setString(5, address2);
					pstmt8.setString(6, address3);
					pstmt8.setString(7, rent);
					pstmt8.setString(8, mng_fee);
					pstmt8.setString(9, deposit);
					pstmt8.setString(10, honorarium);
					pstmt8.setString(11, brokerage);
					pstmt8.setString(12, key_fee);
					pstmt8.setBoolean(13, actvflg);
					pstmt8.setString(14, comment);
					pstmt8.setString(15, rsdc_id);
					pstmt8.setString(16, cmpn_id);
					edited1 = pstmt8.executeUpdate();
					
					// roomのactvfgを変更
					// 部屋が登録されているかつ、actvfgが変更されている場合
					if (numberOfRooms > 0 && !actv_before_change.equals(actv_flg)) {
						String sql9 = "UPDATE room SET actvfg=? WHERE rsdcid=? AND cmpnid=?";
						pstmt9 = conn.prepareStatement(sql9);
						pstmt9.setBoolean(1, actvflg);
						pstmt9.setString(2, rsdc_id);
						pstmt9.setString(3, cmpn_id);
						pstmt9.executeUpdate();
						edited2 = pstmt9.executeUpdate();
					}
					
					// 変更できていない場合はロールバック
					// 部屋が登録されているかつ、actvfgが変更されている場合
					if (numberOfRooms > 0 && !actv_before_change.equals(actv_flg)) {
						if (edited1 > 0 && edited2 > 0) {
							committed = true;
							conn.commit();
						} else {
							conn.rollback();
						}
					} else {
						if (edited1 > 0) {
							committed = true;
							conn.commit();
						} else {
							conn.rollback();
						}
					}
					
					// オートコミットをオン
					conn.setAutoCommit(true);
					
					if (committed) {
						// room_idを取得
						String sql10 = "SELECT roomid FROM room WHERE cmpnid=? AND rsdcid=? ORDER BY roomid ASC";
						pstmt10 = conn.prepareStatement(sql10);
						pstmt10.setString(1, cmpn_id);
						pstmt10.setString(2, rsdc_id);
						rset10 = pstmt10.executeQuery();
						ArrayList<String> room_ids = new ArrayList<String>();
						while (rset10.next()) {
							room_ids.add(rset10.getString(1));
						}
						
						// adebに登録済のaditのadebvlを取得し、値がある場合はadebを更新、値がない場合はadeb,adehから削除
						for (int i = 0; i < usedadits.size(); i++) {
							String adebvl = request.getParameter(usedadits.get(i).get("itemid"));
							String adit_id = usedadits.get(i).get("aditid");
							
							if (!adebvl.equals("") && adebvl != null) {
								edited3++;
								// adebを更新
								String sql11 = "UPDATE adeb SET adebvl=?,uddttm=current_timestamp WHERE cmpnid=? AND rsdcid=? AND aditid=?";
								pstmt11 = conn.prepareStatement(sql11);
								pstmt11.setString(1, adebvl);
								pstmt11.setString(2, cmpn_id);
								pstmt11.setString(3, rsdc_id);
								pstmt11.setString(4, adit_id);
								pstmt11.executeUpdate();
							} else {
								edited4++;
								// adebから削除
								String sql12 = "DELETE FROM adeb WHERE cmpnid=? AND rsdcid=? AND aditid=?";
								pstmt12 = conn.prepareStatement(sql12);
								pstmt12.setString(1, cmpn_id);
								pstmt12.setString(2, rsdc_id);
								pstmt12.setString(3, adit_id);
								pstmt12.executeUpdate();
								
								// adehから削除
								String sql13 = "DELETE FROM adeh WHERE cmpnid=? AND roomid=? AND aditid=?";
								pstmt13 = conn.prepareStatement(sql13);
								pstmt13.setString(1, cmpn_id);
								pstmt13.setString(3, adit_id);
								for (String room_id : room_ids) {
									pstmt13.setString(2, room_id);
									pstmt13.executeUpdate();
								}
							}
						}
						
						// adebに未登録のaditのadebvlを取得し、値がある場合はadeb,adehに追加
						for (int i = 0; i < unused_adits.size(); i++) {
							String adebvl = request.getParameter(unused_adits.get(i).get("itemid"));
							String adit_id = unused_adits.get(i).get("aditid");
							
							if (!adebvl.equals("") && adebvl != null) {
								edited5++;
								// adebidを生成
								CreateIdBean2 cib2 = new CreateIdBean2();
								String adebid = cib2.getCreatedId("adeb", "adebid", cmpn_id);
								
								// adebに追加
								String sql14 = "INSERT INTO adeb(adebid,cmpnid,rsdcid,aditid,adebvl) VALUES(?,?,?,?,?)";
								pstmt14 = conn.prepareStatement(sql14);
								pstmt14.setString(1, adebid);
								pstmt14.setString(2, cmpn_id);
								pstmt14.setString(3, rsdc_id);
								pstmt14.setString(4, adit_id);
								pstmt14.setString(5, adebvl);
								pstmt14.executeUpdate();
								
								// adehidを生成
								String adehid = cib2.getCreatedId("adeh", "adehid", cmpn_id);
								
								// adehに追加
								String sql15 = "INSERT INTO adeh(adehid,cmpnid,roomid,aditid,adehvl) VALUES(?,?,?,?,?)";
								pstmt15 = conn.prepareStatement(sql15);
								pstmt15.setString(1, adehid);
								pstmt15.setString(2, cmpn_id);
								pstmt15.setString(4, adit_id);
								pstmt15.setString(5, adebvl);
								
								for (String room_id : room_ids) {
									pstmt15.setString(3, room_id);
									pstmt15.executeUpdate();
								}
							}
						}
					}
					
					// エンコード
					rsdc_id = URLEncoder.encode(rsdc_id, "UTF-8");
					cmpn_id = URLEncoder.encode(cmpn_id, "UTF-8");
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
					actv_flg = URLEncoder.encode(actv_flg, "UTF-8");
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
					pstmt4.close();
				} catch (SQLException e) { }
				
				try {
					if (rset4.next()) {
						pstmt5.close();
					}
				} catch (SQLException e) { }
				
				try {
					pstmt6.close();
				} catch (SQLException e) { }
				
				try {
					pstmt7.close();
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0 && row2 == 0) {
						pstmt8.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (row1 == 0 && row2 == 0 && numberOfRooms > 0 && !actv_before_change.equals(actv_flg)) {
						pstmt9.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed) {
						pstmt10.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed && edited3 > 0) {
						pstmt11.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed && edited4 > 0) {
						pstmt12.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed && edited4 > 0) {
						pstmt13.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed && edited5 > 0) {
						pstmt14.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (committed && edited5 > 0) {
						pstmt15.close();
					}
				} catch (SQLException e) { }
				
				try {
					conn.close();
				} catch (SQLException e) {  }
			}
			
			if (row1 == 0 && row2 == 0) {
				String url = request.getContextPath() + "/show/residence?id=" + rsdc_id;
				response.sendRedirect(url);
			} else {
				request.getRequestDispatcher("/WEB-INF/app/edit/edit_residence.jsp").forward(request, response);
			}
		}
	}

}

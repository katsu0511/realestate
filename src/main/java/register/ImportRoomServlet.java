package register;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.CreateIdBean2;
import home.RealestateDAO;

/**
 * Servlet implementation class ImportRoomServlet
 */
@WebServlet("/import/room")
@MultipartConfig(
	maxFileSize=10000000,
	maxRequestSize=10000000,
	fileSizeThreshold=10000000
)
public class ImportRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportRoomServlet() {
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
			ResultSet rset1 = null;
			
			// cmpnidの情報取得
			String cmpnid = (String) session.getAttribute("cmpnid");
			
			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 物件情報を取得
				String sql1 = "SELECT rsdcid,rsdcnm FROM rsdc WHERE cmpnid=? AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> rsdcs = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					Map<String, String> rsdc = new HashMap<>();
					rsdc.put("rsdcid", rset1.getString(1));
					rsdc.put("rsdcnm", rset1.getString(2));
					rsdcs.add(rsdc);
				}
				
				request.setAttribute("rsdcs", rsdcs);
				
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
			
			request.getRequestDispatcher("/WEB-INF/app/register/import_room.jsp").forward(request, response);
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
			ResultSet rset1 = null;
			ResultSet rset2 = null;
			ResultSet rset3 = null;
			ResultSet rset4 = null;
			
			// 送信情報の取得
			String rsd_id = request.getParameter("rsd_id");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			Part room = request.getPart("room");
			BufferedReader br = null;
			boolean firstRow = true;
			int row = 0;
			int su1 = 0;
			int su2 = 0;
			int su3 = 0;
			int edited1 = 0;

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// 物件登録で使われている追加費用項目のaditid,adebvlをadebから取得
				String sql1 = "SELECT aditid,adebvl FROM adeb WHERE cmpnid=? AND rsdcid=? ORDER BY aditid ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpn_id);
				pstmt1.setString(2, rsd_id);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> adits = new ArrayList<Map<String, String>>();
				while (rset1.next()) {
					// aditの情報を取得
					String sql2 = "SELECT * FROM adit WHERE aditid=? AND cmpnid=?";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, rset1.getString(1));
					pstmt2.setString(2, cmpn_id);
					rset2 = pstmt2.executeQuery();
					
					if (rset2.next()) {
						Map<String, String> adit = new HashMap<>();
						adit.put("aditid", rset2.getString(1));
						adit.put("itemnm", rset2.getString(3));
						adit.put("itemid", rset2.getString(4));
						adit.put("maxlng", rset2.getString(5));
						adit.put("spclng", rset2.getString(6));
						adit.put("commnt", rset2.getString(7));
						adit.put("adebvl", rset1.getString(2));
						adits.add(adit);
					}
				}
				
				// csv読み込み
				InputStream is = room.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line;
				
				while ((line = br.readLine()) != null) {
					if (firstRow) {
						firstRow = false;
						continue;
					} else {
						String[] data = line.split(",");
						String comment = data.length == 9 ? data[8] : "";
						row = 0;
						edited1 = 0;
						
						// rsd_idとroom_nuが同じものが既に登録されているか確認
						String sql3 = "SELECT * FROM room WHERE rsdcid=? AND roomnu=? AND cmpnid=?";
						pstmt3 = conn.prepareStatement(sql3);
						pstmt3.setString(1, rsd_id);
						pstmt3.setString(2, data[1].trim());
						pstmt3.setString(3, cmpn_id);
						rset3 = pstmt3.executeQuery();
						while (rset3.next()) {
							row++;
						}
						su1++;
						
						// rsd_idとroom_nuが同じものが既に登録されている場合
						if (row > 0) {
							continue;
						} else {
							// オートコミットをオフ
							conn.setAutoCommit(false);
							
							// roomidを作成
							String sql4 = "SELECT roomid FROM room WHERE rsdcid=? AND cmpnid=? ORDER BY roomid DESC LIMIT 1";
							pstmt4 = conn.prepareStatement(sql4);
							pstmt4.setString(1, rsd_id);
							pstmt4.setString(2, cmpn_id);
							rset4 = pstmt4.executeQuery();
							String last_roomid = null;
							
							if (rset4.next()) {
								last_roomid = rset4.getString(1);
							} else {
								last_roomid = rsd_id.substring(0, 7) + "00000_" + cmpn_id;
							}
							
							String num1 = last_roomid.substring(7,12);
							int num2 = Integer.parseInt(num1);
							String num3 = String.format("%05d", num2 + 1);
							String room_id = rsd_id.substring(0, 7) + num3 + "_" + cmpn_id;
							
							// roomに保存
							String sql5 = "INSERT INTO room(roomid,cmpnid,rsdcid,roomnu,rentfe,mngfee,depsit,hnrrum,brkrag,keyfee,commnt) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
							pstmt5 = conn.prepareStatement(sql5);
							pstmt5.setString(1, room_id);
							pstmt5.setString(2, cmpn_id);
							pstmt5.setString(3, rsd_id);
							pstmt5.setString(4, data[1].trim());
							pstmt5.setString(5, data[2].trim());
							pstmt5.setString(6, data[3].trim());
							pstmt5.setString(7, data[4].trim());
							pstmt5.setString(8, data[5].trim());
							pstmt5.setString(9, data[6].trim());
							pstmt5.setString(10, data[7].trim());
							pstmt5.setString(11, comment.trim());
							edited1 = pstmt5.executeUpdate();
							su2++;
							
							// 保存できていない場合はロールバック
							if (edited1 == 1) {
								conn.commit();
							} else {
								conn.rollback();
							}
							
							// オートコミットをオン
							conn.setAutoCommit(true);
							
							for (int i = 0; i < adits.size(); i++) {
								String aditid = adits.get(i).get("aditid");
								String adehvl = adits.get(i).get("adebvl");
								
								// adehidを生成
								CreateIdBean2 cib2 = new CreateIdBean2();
								String adehid = cib2.getCreatedId("adeh", "adehid", cmpn_id);
								
								// adehに保存
								String sql6 = "INSERT INTO adeh(adehid,cmpnid,roomid,aditid,adehvl) VALUES(?,?,?,?,?)";
								pstmt6 = conn.prepareStatement(sql6);
								pstmt6.setString(1, adehid);
								pstmt6.setString(2, cmpn_id);
								pstmt6.setString(3, room_id);
								pstmt6.setString(4, aditid);
								pstmt6.setString(5, adehvl);
								pstmt6.executeUpdate();
								su3++;
							}
						}
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
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
					if (su1 > 0) {
						pstmt3.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (su2 > 0) {
						pstmt4.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (su2 > 0) {
						pstmt5.close();
					}
				} catch (SQLException e) { }
				
				try {
					if (su3 > 0) {
						pstmt6.close();
					}
				} catch (SQLException e) { }
				
				try {
					br.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			response.sendRedirect(request.getContextPath() + "/index/room");
		}
	}

}

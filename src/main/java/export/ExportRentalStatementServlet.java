package export;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import home.RealestateDAO;

/**
 * Servlet implementation class ExportRentalStatementServlet
 */
@WebServlet("/export/rental_statement")
public class ExportRentalStatementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String mode = "DOWNLOAD";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportRentalStatementServlet() {
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
				
				// tnntの情報取得
				String sql1 = "SELECT tnntid,tnntnm FROM tnnt WHERE cmpnid=? AND rppsid!='' AND rsdcid!='' AND roomid!='' AND actvfg='t' ORDER BY rgdttm ASC";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, cmpnid);
				rset1 = pstmt1.executeQuery();
				ArrayList<Map<String, String>> tenants = new ArrayList<Map<String, String>>();
				
				while (rset1.next()) {
					Map<String, String> tenant = new HashMap<>();
					tenant.put("tnntid", rset1.getString(1));
					tenant.put("tnntnm", rset1.getString(2));
					tenants.add(tenant);
				}
				
				request.setAttribute("tenants", tenants);
				
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
			
			request.getRequestDispatcher("/WEB-INF/app/export/export_rental_statement.jsp").forward(request, response);
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
		}
		else {
			// コンテキストルートディレクトリ取得
			String baseDir = this.getServletContext().getRealPath("/");

			// PDFパラメータ設定
			LedgerRentalStatement ls = new LedgerRentalStatement(baseDir);
			
			// 送信情報の取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String tnnt_id = request.getParameter("tnnt_id");
			String st_date = request.getParameter("st_date");
			String en_date = request.getParameter("en_date");
			String[] data = {cmpn_id,tnnt_id,st_date,en_date};

			// TODO 帳票が追加されたらここで条件分岐してそれぞれの帳票パラメータを設定するよう改造すること
			// 賃貸精算書のパラメータ設定
			setParamToRentalStatementSheet(ls, data);

			// PDFファイル出力
			if ("FILE".equals(mode)) {
				ls.outputPDF();
				response.sendRedirect(request.getContextPath()+"/top");
			}
			// PDFダウンロード
			else if ("DOWNLOAD".equals(mode)) {
				ServletOutputStream out = null;
				try {
					byte[] pdfData = ls.getPDFData();
					response.setContentType("application/pdf");
					response.setContentLength(pdfData.length);
					out = response.getOutputStream();
					out.write(pdfData);
					out.flush();
					out.close();
				}
				catch (Exception e) {
					if (out != null) out.close();
				}
			}
			// 不明
			else {
				response.setStatus(500);
			}
		}
	}
	
	/**
	 * 賃貸精算書のパラメータ設定
	 * <ul>
	 * <li>param.addProperty()メソッドにて必要分の値を設定する。</li>
	 * <li>設定できる項目は、LedgerSheet.getDefaultParameter()を参考のこと。</li>
	 * </ul>
	 * @param ls 帳票出力インスタンス
	 */
	private void setParamToRentalStatementSheet(LedgerRentalStatement ls, String[] data) {
		JsonObject param = ls.getDefaultParameter(LedgerRentalStatement.RENTAL_STATEMENT_SHEET);
		// TODO 実用化する場合はデータベース等から読み込んだデータを設定すること
		
		// 接続情報
		RealestateDAO db = new RealestateDAO();
		Connection conn = null;
		
		// SQL情報管理
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		ResultSet rset1 = null;
		ResultSet rset2 = null;
		ResultSet rset3 = null;
		ResultSet rset4 = null;
		ResultSet rset5 = null;
		ArrayList<String> rest = new ArrayList<String>();

		try {
			// データベース接続情報取得
			conn = db.getConnection();
			
			// 情報管理
			ArrayList<String> cmpn = new ArrayList<String>();
			ArrayList<String> tnnt = new ArrayList<String>();
			ArrayList<String> rpps = new ArrayList<String>();
			ArrayList<String> rsdc = new ArrayList<String>();
			ArrayList<String> room = new ArrayList<String>();
			
			// cmpnの情報取得
			String sql1 = "SELECT * FROM cmpn WHERE cmpnid=?";
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, data[0]);
			rset1 = pstmt1.executeQuery();
			if (rset1.next()) {
				for (int i = 1; i <= 22; i++){
					cmpn.add(rset1.getString(i));
				}
			}
			
			// tnntの情報取得
			String sql2 = "SELECT * FROM tnnt WHERE tnntid=? AND cmpnid=?";
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setString(1, data[1]);
			pstmt2.setString(2, data[0]);
			rset2 = pstmt2.executeQuery();
			if (rset2.next()) {
				for (int i = 1; i <= 28; i++){
					tnnt.add(rset2.getString(i));
				}
			}
			
			// rppsの情報取得
			String sql3 = "SELECT * FROM rpps WHERE rppsid=("
					    +     "SELECT rppsid FROM tnnt WHERE tnntid=?"
					    + ") AND cmpnid=?";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, data[1]);
			pstmt3.setString(2, data[0]);
			rset3 = pstmt3.executeQuery();
			if (rset3.next()) {
				for (int i = 1; i <= 11; i++){
					rpps.add(rset3.getString(i));
				}
			}
			
			// rsdcの情報取得
			String sql4 = "SELECT * FROM rsdc WHERE rsdcid=("
					    +     "SELECT rsdcid FROM tnnt WHERE tnntid=?"
					    + ") AND cmpnid=?";
			pstmt4 = conn.prepareStatement(sql4);
			pstmt4.setString(1, data[1]);
			pstmt4.setString(2, data[0]);
			rset4 = pstmt4.executeQuery();
			if (rset4.next()) {
				for (int i = 1; i <= 19; i++){
					rsdc.add(rset4.getString(i));
				}
			}
			
			// roomの情報取得
			String sql5 = "SELECT * FROM room WHERE roomid=("
					    +     "SELECT roomid FROM tnnt WHERE tnntid=?"
					    + ") AND cmpnid=?";
			pstmt5 = conn.prepareStatement(sql5);
			pstmt5.setString(1, data[1]);
			pstmt5.setString(2, data[0]);
			rset5 = pstmt5.executeQuery();
			if (rset5.next()) {
				for (int i = 1; i <= 15; i++){
					room.add(rset5.getString(i));
				}
			}
			
			String No = "00001";
			rest.add(No);                 // rest.get(0)  連番
			String CHINSHAKUNIN = tnnt.get(6);
			rest.add(CHINSHAKUNIN);       // rest.get(1)  賃借人
			String BUKKENMEI = rsdc.get(3) + " " + room.get(3) + "号室";
			rest.add(BUKKENMEI);          // rest.get(2)  物件名
			String st_year = data[2].substring(0, 4);
			String st_month = data[2].substring(5, 7);
			if (st_month.substring(0, 1).equals("0")) {st_month = st_month.substring(1, 2);}
			String st_day = data[2].substring(8, 10);
			if (st_day.substring(0, 1).equals("0")) {st_day = st_day.substring(1, 2);}
			String KEIYAKUKIKAN_B = st_year + "年" + st_month + "月" + st_day + "日";
			rest.add(KEIYAKUKIKAN_B);     // rest.get(3)  契約期間（開始）
			String en_year = data[3].substring(0, 4);
			String en_month = data[3].substring(5, 7);
			if (en_month.substring(0, 1).equals("0")) {en_month = en_month.substring(1, 2);}
			String en_day = data[3].substring(8, 10);
			if (en_day.substring(0, 1).equals("0")) {en_day = en_day.substring(1, 2);}
			String KEIYAKUKIKAN_E = en_year + "年" + en_month + "月" + en_day + "日";
			rest.add(KEIYAKUKIKAN_E);     // rest.get(4)  契約期間（終了）
			
			int chinryou = Integer.parseInt(room.get(5));
			String CHINRYOU = String.format("%,d", chinryou);
			rest.add(CHINRYOU);           // rest.get(5)  賃料
			int kyouekihi = Integer.parseInt(room.get(6));
			String KYOUEKIHI = String.format("%,d", kyouekihi);
			rest.add(KYOUEKIHI);          // rest.get(6)  共益費
			int shouhizei = Integer.parseInt("1000");
			String SHOUHIZEI = String.format("%,d", shouhizei);
			rest.add(SHOUHIZEI);          // rest.get(7)  消費税
			String KOUSHINRYOU = "賃料の1ヶ月分";
			rest.add(KOUSHINRYOU);		  // rest.get(8)  更新料
			int shikikinn = Integer.parseInt(room.get(7));
			String SHIKIKIN = String.format("%,d", shikikinn);
			rest.add(SHIKIKIN);           // rest.get(9)  敷金
			int reikin = Integer.parseInt(room.get(8));
			String REIKIN = String.format("%,d", reikin);
			rest.add(REIKIN);             // rest.get(10) 礼金
			int shoukyaku = Integer.parseInt("10000");
			String SHOUKYAKU = String.format("%,d", shoukyaku);
			rest.add(SHOUKYAKU);          // rest.get(11) 償却
			
			int rent = Integer.parseInt(room.get(5));
			int depsit = Integer.parseInt(room.get(7));
			int hnrrum = Integer.parseInt(room.get(8));
			int depsitM = 0;
			int hnrrumM = 0;
			if (depsit != 0) {depsitM = depsit / rent;}
			if (hnrrum != 0) {hnrrumM = hnrrum / rent;}
			String SHIKIKIN_M = String.valueOf(depsitM);
			String REIKIN_M = String.valueOf(hnrrumM);
			rest.add(SHIKIKIN_M);         // rest.get(12)  敷金（月）
			rest.add(REIKIN_M);           // rest.get(13)  礼金（月）
			String HIWARI1_M = st_month;
			rest.add(HIWARI1_M);          // rest.get(14)  日割り１（月）
			String HIWARI1_B = st_month + "月" + st_day + "日";
			rest.add(HIWARI1_B);          // rest.get(15)  日割り１（開始日）
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(st_year));
			cal.set(Calendar.MONTH, Integer.parseInt(st_month) - 1);
			int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
			String HIWARI1_E = st_month + "月" + lastDayOfMonth + "日";
			rest.add(HIWARI1_E);          // rest.get(16)  日割り１（終了日）
			int hiwari1_n = lastDayOfMonth - Integer.parseInt(st_day) + 1;
			String HIWARI1_N = String.valueOf(hiwari1_n);
			rest.add(HIWARI1_N);          // rest.get(17)  日割り１（実日数）
			String HIWARI1_H = String.valueOf(lastDayOfMonth);
			rest.add(HIWARI1_H);          // rest.get(18)  日割り１（月日数）
			String HIWARI2_M = String.valueOf(Integer.parseInt(st_month) + 1);
			rest.add(HIWARI2_M);          // rest.get(19)  日割り２（月）
			String HIWARI2_B = HIWARI2_M + "月" + "1日";
			rest.add(HIWARI2_B);           // rest.get(20)  日割り２（開始日）
			cal.set(Calendar.MONTH, Integer.parseInt(HIWARI2_M) - 1);
			int lastDayOfNextMonth = cal.getActualMaximum(Calendar.DATE);
			String HIWARI2_E = HIWARI2_M + "月" + lastDayOfNextMonth + "日";
			rest.add(HIWARI2_E);          // rest.get(21)  日割り２（終了日）
			String HIWARI2_N = String.valueOf(lastDayOfNextMonth);
			rest.add(HIWARI2_N);          // rest.get(22)  日割り２（実日数）
			String HIWARI2_H = HIWARI2_N;
			rest.add(HIWARI2_H);          // rest.get(23)  日割り２（月日数）
			float chinryou1 = (float) chinryou / Float.parseFloat(HIWARI1_H) * Float.parseFloat(HIWARI1_N);
			int chinryou1Int = (int) chinryou1;
			String CHINRYOU1 = String.format("%,d", chinryou1Int);
			rest.add(CHINRYOU1);          // rest.get(24)  賃料１（日割り残額）
			float chinryou2 = (float) chinryou / Float.parseFloat(HIWARI2_H) * Float.parseFloat(HIWARI2_N);
			int chinryou2Int = (int) chinryou2;
			String CHINRYOU2 = String.format("%,d", chinryou2Int);
			rest.add(CHINRYOU2);          // rest.get(25)  賃料２（日割り残額）
			float kyouekiohi1 = (float) kyouekihi / Float.parseFloat(HIWARI1_H) * Float.parseFloat(HIWARI1_N);
			int kyouekiohi1Int = (int) kyouekiohi1;
			String KYOUEKIHI1 = String.format("%,d", kyouekiohi1Int);
			rest.add(KYOUEKIHI1);         // rest.get(26)  共益費１（日割り残額）
			float kyouekiohi2 = (float) kyouekihi / Float.parseFloat(HIWARI2_H) * Float.parseFloat(HIWARI2_N);
			int kyouekiohi2Int = (int) kyouekiohi2;
			String KYOUEKIHI2 = String.format("%,d", kyouekiohi2Int);
			rest.add(KYOUEKIHI2);         // rest.get(27)  共益費２（日割り残額）
			int chuukai_shouhizei = Integer.parseInt("1000");
			String CHUUKAI_SHOUHIZEI = String.format("%,d", chuukai_shouhizei);
			rest.add(CHUUKAI_SHOUHIZEI);  // rest.get(28)  仲介手数料の消費税
			int chuukai_tesuuryou = Integer.parseInt(room.get(9));
			String CHUUKAI_TESUURYOU = String.format("%,d", chuukai_tesuuryou);
			rest.add(CHUUKAI_TESUURYOU);  // rest.get(29)  仲介手数料（消費税込み）
			int shoukei = shikikinn + reikin + chinryou1Int + chinryou2Int + kyouekiohi1Int + kyouekiohi2Int + shouhizei + chuukai_tesuuryou;
			String SHOUKEI = String.format("%,d", shoukei);
			rest.add(SHOUKEI);            // rest.get(30)  小計
			String KASAIHOKEN_SETUMEI = "2年分";
			rest.add(KASAIHOKEN_SETUMEI); // rest.get(31)  火災保険料の説明
			int kasaihoken_ryoukin = Integer.parseInt("10000");
			String KASAIHOKEN_RYOUKIN = String.format("%,d", kasaihoken_ryoukin);
			rest.add(KASAIHOKEN_RYOUKIN); // rest.get(32)  火災保険料
			int goukei = shoukei + kasaihoken_ryoukin;
			String GOUKEI = String.format("%,d", goukei);
			rest.add(GOUKEI);             // rest.get(33)  合計
			int tetsukekin = Integer.parseInt("0");
			String TETSUKEKIN = String.format("%,d", tetsukekin);
			rest.add(TETSUKEKIN);         // rest.get(34)  手付金
			
			String cmpn_nm = cmpn.get(1);
			String FURIKOMISAKI = cmpn.get(14) + "銀行  " + cmpn.get(15) + "  " + cmpn.get(16) + "預金  " + cmpn.get(17) + "  " + cmpn.get(18) + "宛";
			rest.add(FURIKOMISAKI);       // rest.get(35)  振込先
			String JUSYO = "〒" + cmpn.get(7) + " " + cmpn.get(8) + cmpn.get(9) + cmpn.get(10) + " " + cmpn.get(11);
			rest.add(JUSYO);              // rest.get(36)  住所
			String KAISYAMEI = cmpn_nm;
			rest.add(KAISYAMEI);          // rest.get(37)  会社名
			String DENWABANGOU = cmpn.get(13).equals("") ? cmpn.get(12) : cmpn.get(12) + " / FAX  " + cmpn.get(13);
			rest.add(DENWABANGOU);        // rest.get(38)  電話番号
			String TANTOU = rpps.get(2);
			rest.add(TANTOU);             // rest.get(39)  担当
			
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
				pstmt5.close();
			} catch (SQLException e) { }
			
			try {
				conn.close();
			} catch (SQLException e) {  }
		}
		
		param.addProperty("No",rest.get(0));
		param.addProperty("CHINSHAKUNIN",rest.get(1));
		param.addProperty("BUKKENMEI",rest.get(2));
		param.addProperty("KEIYAKUKIKAN_B",rest.get(3));
		param.addProperty("KEIYAKUKIKAN_E",rest.get(4));
		
		param.addProperty("CHINRYOU",rest.get(5));
		param.addProperty("KYOUEKIHI",rest.get(6));
		param.addProperty("SHOUHIZEI",rest.get(7));
		param.addProperty("KOUSHINRYOU",rest.get(8));
		param.addProperty("SHIKIKIN",rest.get(9));
		param.addProperty("REIKIN",rest.get(10));
		param.addProperty("SHOUKYAKU",rest.get(11));
		
		param.addProperty("SHIKIKIN_M",rest.get(12));
		param.addProperty("REIKIN_M",rest.get(13));
		param.addProperty("HIWARI1_M",rest.get(14));
		param.addProperty("HIWARI1_B",rest.get(15));
		param.addProperty("HIWARI1_E",rest.get(16));
		param.addProperty("HIWARI1_N",rest.get(17));
		param.addProperty("HIWARI1_H",rest.get(18));
		param.addProperty("HIWARI2_M",rest.get(19));
		param.addProperty("HIWARI2_B",rest.get(20));
		param.addProperty("HIWARI2_E",rest.get(21));
		param.addProperty("HIWARI2_N",rest.get(22));
		param.addProperty("HIWARI2_H",rest.get(23));
		param.addProperty("CHINRYOU1",rest.get(24));
		param.addProperty("CHINRYOU2",rest.get(25));
		param.addProperty("KYOUEKIHI1",rest.get(26));
		param.addProperty("KYOUEKIHI2",rest.get(27));
		param.addProperty("CHUUKAI_SHOUHIZEI",rest.get(28));
		param.addProperty("CHUUKAI_TESUURYOU",rest.get(29));
		param.addProperty("SHOUKEI",rest.get(30));
		param.addProperty("KASAIHOKEN_SETSUMEI",rest.get(31));
		param.addProperty("KASAIHOKEN_RYOUKIN",rest.get(32));
		param.addProperty("GOUKEI",rest.get(33));
		param.addProperty("TETSUKEKIN",rest.get(34));
		
		param.addProperty("FURIKOMISAKI",rest.get(35));
		param.addProperty("JUSYO",rest.get(36));
		param.addProperty("KAISYAMEI",rest.get(37));
		param.addProperty("DENWABANGOU",rest.get(38));
		param.addProperty("TANTOU",rest.get(39));
		
		ls.setParameter(LedgerRentalStatement.RENTAL_STATEMENT_SHEET,param);
	}

}

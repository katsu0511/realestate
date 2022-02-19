package export;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * Servlet implementation class ExportCancellationStatementServlet
 */
@WebServlet("/export/cancellation_statement")
public class ExportCancellationStatementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String mode = "DOWNLOAD";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportCancellationStatementServlet() {
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
			
			request.getRequestDispatcher("/WEB-INF/app/export/export_cancellation_statement.jsp").forward(request, response);
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
			LedgerCancellationStatement ls = new LedgerCancellationStatement(baseDir);
			
			// 送信情報の取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String tnnt_id = request.getParameter("tnnt_id");
			String cancel_date = request.getParameter("cancel_date");
			String depart_date = request.getParameter("depart_date");
			String state_date = request.getParameter("state_date");
			String expire_date = request.getParameter("expire_date");
			String[] data = {cmpn_id,tnnt_id,cancel_date,depart_date,state_date,expire_date};

			// TODO 帳票が追加されたらここで条件分岐してそれぞれの帳票パラメータを設定するよう改造すること
			// 解約精算書のパラメータ設定
			setParamToCancellationStatementSheet(ls, data);

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
	 * 解約精算書のパラメータ設定
	 * <ul>
	 * <li>param.addProperty()メソッドにて必要分の値を設定する。</li>
	 * <li>設定できる項目は、LedgerSheet.getDefaultParameter()を参考のこと。</li>
	 * </ul>
	 * @param ls 帳票出力インスタンス
	 */
	private void setParamToCancellationStatementSheet(LedgerCancellationStatement ls, String[] data) {
		JsonObject param = ls.getDefaultParameter(LedgerCancellationStatement.CANCELLATION_STATEMENT_SHEET);
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
		ArrayList<String> cast = new ArrayList<String>();

		try {
			// データベース接続情報取得
			conn = db.getConnection();
			
			// 情報管理
			ArrayList<String> cmpn = new ArrayList<String>();
			ArrayList<String> tnnt = new ArrayList<String>();
			ArrayList<String> rsdc = new ArrayList<String>();
			ArrayList<String> room = new ArrayList<String>();
			ArrayList<String> land = new ArrayList<String>();
			
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
				for (int i = 1; i <= 33; i++){
					tnnt.add(rset2.getString(i));
				}
			}
			
			// rsdcの情報取得
			String sql3 = "SELECT * FROM rsdc WHERE rsdcid=("
					    +     "SELECT rsdcid FROM tnnt WHERE tnntid=?"
					    + ") AND cmpnid=?";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, data[1]);
			pstmt3.setString(2, data[0]);
			rset3 = pstmt3.executeQuery();
			if (rset3.next()) {
				for (int i = 1; i <= 19; i++){
					rsdc.add(rset3.getString(i));
				}
			}
			
			// roomの情報取得
			String sql4 = "SELECT * FROM room WHERE roomid=("
					    +     "SELECT roomid FROM tnnt WHERE tnntid=?"
					    + ") AND cmpnid=?";
			pstmt4 = conn.prepareStatement(sql4);
			pstmt4.setString(1, data[1]);
			pstmt4.setString(2, data[0]);
			rset4 = pstmt4.executeQuery();
			if (rset4.next()) {
				for (int i = 1; i <= 15; i++){
					room.add(rset4.getString(i));
				}
			}
			
			// landの情報取得
			String sql5 = "SELECT * FROM land WHERE landid=("
					    +     "SELECT landid FROM rsdc WHERE rsdcid=("
					    +          "SELECT rsdcid FROM tnnt WHERE tnntid=?"
					    +      ")"
					    + ") AND cmpnid=?";
			pstmt5 = conn.prepareStatement(sql5);
			pstmt5.setString(1, data[1]);
			pstmt5.setString(2, data[0]);
			rset5 = pstmt5.executeQuery();
			if (rset5.next()) {
				for (int i = 1; i <= 21; i++){
					land.add(rset5.getString(i));
				}
			}
			
			String KAISHAMEI = cmpn.get(1);
			cast.add(KAISHAMEI);						// cast.get(0)  会社名
			String CHINSHAKUNIN = tnnt.get(6);
			cast.add(CHINSHAKUNIN);						// cast.get(1)  賃借人
			String YUUBINBANGOU = rsdc.get(5);
			cast.add(YUUBINBANGOU);						// cast.get(2)  郵便番号
			String JUSHO = rsdc.get(6) + rsdc.get(7) + rsdc.get(8) + " " + rsdc.get(3) + room.get(3);
			cast.add(JUSHO);							// cast.get(3)  住所
			String CHINTAININ = land.get(2);
			cast.add(CHINTAININ);						// cast.get(4)  賃貸人
			String BUKKENMEI = rsdc.get(3);
			cast.add(BUKKENMEI);						// cast.get(5)  物件名
			String cancel_year = data[2].substring(0, 4);
			String cancel_month = data[2].substring(5, 7);
			if (cancel_month.substring(0, 1).equals("0")) {cancel_month = cancel_month.substring(1, 2);}
			String cancel_day = data[2].substring(8, 10);
			if (cancel_day.substring(0, 1).equals("0")) {cancel_day = cancel_day.substring(1, 2);}
			String KAIYAKUBI = cancel_year + "年" + cancel_month + "月" + cancel_day + "日";
			cast.add(KAIYAKUBI);     					// cast.get(6)  解約日
			String depart_year = data[3].substring(0, 4);
			String depart_month = data[3].substring(5, 7);
			if (depart_month.substring(0, 1).equals("0")) {depart_month = depart_month.substring(1, 2);}
			String depart_day = data[3].substring(8, 10);
			if (depart_day.substring(0, 1).equals("0")) {depart_day = depart_day.substring(1, 2);}
			String TAIKYOBI = depart_year + "年" + depart_month + "月" + depart_day + "日";
			cast.add(TAIKYOBI);     					// cast.get(7)  退去日
			String state_year = data[4].substring(0, 4);
			String state_month = data[4].substring(5, 7);
			if (state_month.substring(0, 1).equals("0")) {state_month = state_month.substring(1, 2);}
			String state_day = data[4].substring(8, 10);
			if (state_day.substring(0, 1).equals("0")) {state_day = state_day.substring(1, 2);}
			String KAIYAKUSEISANBIYOTEIBI = state_year + "年" + state_month + "月" + state_day + "日";
			cast.add(KAIYAKUSEISANBIYOTEIBI);     		// cast.get(8)  解約精算日予定日
			String OSHIHARAISAKIKINYUUKIKANMEI = cmpn.get(14) + "銀行";
			cast.add(OSHIHARAISAKIKINYUUKIKANMEI);		// cast.get(9)  お支払い先金融機関名
			String OSHIHARAISAKIHONSHITENMEI = cmpn.get(15);
			cast.add(OSHIHARAISAKIHONSHITENMEI);		// cast.get(10) お支払い先本支店名
			String OSHIHARAISAKIKOUZABUNRUI = cmpn.get(16);
			cast.add(OSHIHARAISAKIKOUZABUNRUI);			// cast.get(11) お支払い先口座分類
			String OSHIHARAISAKIKOUZABANGOU = cmpn.get(17);
			cast.add(OSHIHARAISAKIKOUZABANGOU);			// cast.get(12) お支払い先口座番号
			String OSHIHARAISAKIKOUZAMEIGI = cmpn.get(18);
			cast.add(OSHIHARAISAKIKOUZAMEIGI);			// cast.get(13) お支払い先口座名義
			String GOHENKINKINYUUKIKANMEI = tnnt.get(17);
			cast.add(GOHENKINKINYUUKIKANMEI);			// cast.get(14) ご返金金融機関名
			String GOHENKINHONSHITENMEI = tnnt.get(18);
			cast.add(GOHENKINHONSHITENMEI);				// cast.get(15) ご返金本支店名
			String GOHENKINKOUZABUNRUI = tnnt.get(19);
			cast.add(GOHENKINKOUZABUNRUI);				// cast.get(16) ご返金口座分類
			String GOHENKINKOUZABANGOU = tnnt.get(20);
			cast.add(GOHENKINKOUZABANGOU);				// cast.get(17) ご返金口座番号
			String GOHENKINKOUZAMEIGI = tnnt.get(21);
			cast.add(GOHENKINKOUZAMEIGI);				// cast.get(18) ご返金口座名義
			String expire_year = data[5].substring(0, 4);
			String expire_month = data[5].substring(5, 7);
			if (expire_month.substring(0, 1).equals("0")) {expire_month = expire_month.substring(1, 2);}
			String expire_day = data[5].substring(8, 10);
			if (expire_day.substring(0, 1).equals("0")) {expire_day = expire_day.substring(1, 2);}
			String GOSEISANKIGEN = expire_year + "年" + expire_month + "月" + expire_day + "日";
			cast.add(GOSEISANKIGEN);					// cast.get(19) ご精算期限
			
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
		
		param.addProperty("KAISHAMEI",cast.get(0));
		param.addProperty("CHINSHAKUNIN",cast.get(1));
		param.addProperty("YUUBINBANGOU",cast.get(2));
		param.addProperty("JUSHO",cast.get(3));
		param.addProperty("CHINTAININ",cast.get(4));
		param.addProperty("BUKKENMEI",cast.get(5));
		param.addProperty("KAIYAKUBI",cast.get(6));
		param.addProperty("TAIKYOBI",cast.get(7));
		param.addProperty("KAIYAKUSEISANBIYOTEIBI",cast.get(8));
		
		for (int i = 1; i < 13; i++) {
			param.addProperty("OAZUKARIKOUMOKU" + i ,"");
			param.addProperty("OAZUKARIKIN" + i ,"");
			param.addProperty("SEIKYUKOUMOKU" + i ,"");
			param.addProperty("SEIKYUGAKU" + i ,"");
		}
		
		param.addProperty("SHOUKEI1","9,999,999");
		param.addProperty("SHOUKEI2","9,999,999");
		
		for (int i = 1; i < 24; i++) {
			param.addProperty("SYUZENKOUMOKU" + i ,"");
			param.addProperty("SYUZENTANKA" + i ,"");
			param.addProperty("SYUZENSUURYOU" + i ,"");
			param.addProperty("SYUZENHIKINGAKU" + i ,"");
		}
		
		param.addProperty("SHOUKEI3","9,999,999");
		param.addProperty("SASHIHIKIHENKINGAKU","9,999,999");
		
		param.addProperty("OSHIHARAISAKIKINYUUKIKANMEI",cast.get(9));
		param.addProperty("OSHIHARAISAKIHONSHITENMEI",cast.get(10));
		param.addProperty("OSHIHARAISAKIKOUZABUNRUI",cast.get(11));
		param.addProperty("OSHIHARAISAKIKOUZABANGOU",cast.get(12));
		param.addProperty("OSHIHARAISAKIKOUZAMEIGI",cast.get(13));
		param.addProperty("GOHENKINKINYUUKIKANMEI",cast.get(14));
		param.addProperty("GOHENKINHONSHITENMEI",cast.get(15));
		param.addProperty("GOHENKINKOUZABUNRUI",cast.get(16));
		param.addProperty("GOHENKINKOUZABANGOU",cast.get(17));
		param.addProperty("GOHENKINKOUZAMEIGI",cast.get(18));
		param.addProperty("GOSEISANKIGEN",cast.get(19));
		
		ls.setParameter(LedgerCancellationStatement.CANCELLATION_STATEMENT_SHEET,param);
	}

}

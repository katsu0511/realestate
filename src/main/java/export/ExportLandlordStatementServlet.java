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
 * Servlet implementation class ExportLandlordStatementServlet
 */
@WebServlet("/export/landlord_statement")
public class ExportLandlordStatementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String mode = "DOWNLOAD";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportLandlordStatementServlet() {
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
				
				// landの情報取得
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
			
			request.getRequestDispatcher("/WEB-INF/app/export/export_landlord_statement.jsp").forward(request, response);
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
			LedgerLandlordStatement ls = new LedgerLandlordStatement(baseDir);
			
			// 送信情報の取得
			String cmpn_id = (String) session.getAttribute("cmpnid");
			String land_id = request.getParameter("land_id");
			String pay_date = request.getParameter("pay_date");
			String[] data = {cmpn_id,land_id,pay_date};

			// TODO 帳票が追加されたらここで条件分岐してそれぞれの帳票パラメータを設定するよう改造すること
			// 大家精算書のパラメータ設定
			setParamToLandlordStatementSheet(ls, data);

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
	 * 大家精算書のパラメータ設定
	 * <ul>
	 * <li>param.addProperty()メソッドにて必要分の値を設定する。</li>
	 * <li>設定できる項目は、LedgerSheet.getDefaultParameter()を参考のこと。</li>
	 * </ul>
	 * @param ls 帳票出力インスタンス
	 */
	private void setParamToLandlordStatementSheet(LedgerLandlordStatement ls, String[] data) {
		JsonObject param = ls.getDefaultParameter(LedgerLandlordStatement.LANDLORD_STATEMENT_SHEET);
		// TODO 実用化する場合はデータベース等から読み込んだデータを設定すること
		
		// 接続情報
		RealestateDAO db = new RealestateDAO();
		Connection conn = null;
		
		// SQL情報管理
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rset1 = null;
		ResultSet rset2 = null;
		ArrayList<String> cast = new ArrayList<String>();

		try {
			// データベース接続情報取得
			conn = db.getConnection();
			
			// 情報管理
			ArrayList<String> cmpn = new ArrayList<String>();
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
			
			// landの情報取得
			String sql2 = "SELECT * FROM land WHERE landid=? AND cmpnid=?";
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setString(1, data[1]);
			pstmt2.setString(2, data[0]);
			rset2 = pstmt2.executeQuery();
			if (rset2.next()) {
				for (int i = 1; i <= 21; i++){
					land.add(rset2.getString(i));
				}
			}
			
			String OOYAYUUBINBANGOU = land.get(4);
			cast.add(OOYAYUUBINBANGOU);							// cast.get(0)  大家郵便番号
			String OOYAJUUSYO = land.get(5) + land.get(6) + land.get(7) + " " + land.get(8);
			cast.add(OOYAJUUSYO);								// cast.get(1)  大家住所
			String OOYAMEI = land.get(2);
			cast.add(OOYAMEI);									// cast.get(2)  大家名
			String KANRIGAISYAMEI = cmpn.get(1);
			cast.add(KANRIGAISYAMEI);							// cast.get(3)  管理会社名
			String DAIHYOUSYAMEI = cmpn.get(3);
			cast.add(DAIHYOUSYAMEI);							// cast.get(4)  代表者名
			String KANRIGAISYAYUUBINBANGOU = cmpn.get(7);
			cast.add(KANRIGAISYAYUUBINBANGOU);					// cast.get(5)  管理会社郵便番号
			String KANRIGAISYAJUUSYO = cmpn.get(8) + cmpn.get(9) + cmpn.get(10) + " " + cmpn.get(11);
			cast.add(KANRIGAISYAJUUSYO);						// cast.get(6)  管理会社住所
			String KANRIGAISYADENWABANGOU = cmpn.get(13).equals("") ? cmpn.get(12) : cmpn.get(12) + " / FAX  " + cmpn.get(13);
			cast.add(KANRIGAISYADENWABANGOU);        			// cast.get(7)  管理会社電話番号
			String pay_year = data[2].substring(0, 4);
			String pay_month = data[2].substring(5, 7);
			if (pay_month.substring(0, 1).equals("0")) {pay_month = pay_month.substring(1, 2);}
			String pay_day = data[2].substring(8, 10);
			if (pay_day.substring(0, 1).equals("0")) {pay_day = pay_day.substring(1, 2);}
			String OSHIHARAIYOTEIBI = pay_year + "年" + pay_month + "月" + pay_day + "日";
			cast.add(OSHIHARAIYOTEIBI);     					// cast.get(8)  お支払い予定日
			String GOUKEIKINGAKU = "";
			cast.add(GOUKEIKINGAKU);							// cast.get(9)  合計金額
			String SONOTASHIHARAIGAKU = "";
			cast.add(SONOTASHIHARAIGAKU);						// cast.get(10) その他支払額
			String KOUJOGAKU = "";
			cast.add(KOUJOGAKU);								// cast.get(11) 控除額
			String SOUKINGAKU = "";
			cast.add(SOUKINGAKU);								// cast.get(12) 送金額
			String FURIKOMISAKIKINYUUKIKAN = land.get(12) + "銀行 " + land.get(13);
			cast.add(FURIKOMISAKIKINYUUKIKAN);					// cast.get(13) 振込先金融機関
			String FURIKOMISAKISYUBETU = land.get(14);
			cast.add(FURIKOMISAKISYUBETU);						// cast.get(14) 振込先種別
			String FURIKOMISAKIKOUZABANGOU = land.get(15);
			cast.add(FURIKOMISAKIKOUZABANGOU);					// cast.get(15) 振込先口座番号
			String SHIHARAIGAKU = "";
			cast.add(SHIHARAIGAKU);								// cast.get(16) 支払額
			String FURIKOMITESUURYOU = "";
			cast.add(FURIKOMITESUURYOU);						// cast.get(17) 振込手数料
			String FURIKOMIGAKU = "";
			cast.add(FURIKOMIGAKU);								// cast.get(18) 振込額
			
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
		
		param.addProperty("OOYAYUUBINBANGOU",cast.get(0));
		param.addProperty("OOYAJUUSYO",cast.get(1));
		param.addProperty("OOYAMEI",cast.get(2));
		param.addProperty("KANRIGAISYAMEI",cast.get(3));
		param.addProperty("DAIHYOUSYAMEI",cast.get(4));
		param.addProperty("KANRIGAISYAYUUBINBANGOU",cast.get(5));
		param.addProperty("KANRIGAISYAJUUSYO",cast.get(6));
		param.addProperty("KANRIGAISYADENWABANGOU",cast.get(7));
		param.addProperty("OSHIHARAIYOTEIBI",cast.get(8));
		param.addProperty("GOUKEIKINGAKU",cast.get(9));
		param.addProperty("SONOTASHIHARAIGAKU",cast.get(10));
		param.addProperty("KOUJOGAKU",cast.get(11));
		param.addProperty("SOUKINGAKU",cast.get(12));
		param.addProperty("FURIKOMISAKIKINYUUKIKAN",cast.get(13));
		param.addProperty("FURIKOMISAKISYUBETU",cast.get(14));
		param.addProperty("FURIKOMISAKIKOUZABANGOU",cast.get(15));
		param.addProperty("SHIHARAIGAKU",cast.get(16));
		param.addProperty("FURIKOMITESUURYOU",cast.get(17));
		param.addProperty("FURIKOMIGAKU",cast.get(18));
		
		for (int i = 1; i < 15; i++) {
			param.addProperty("TAISHONENGETU" + i ,"");
			param.addProperty("NUMBER" + i ,"");
			param.addProperty("SHIMEI" + i ,"");
			param.addProperty("CHINTAIRYOU" + i ,"");
			param.addProperty("KYOUEKIHI" + i ,"");
			param.addProperty("CHUSHARYOU" + i ,"");
			param.addProperty("SONOTACHINRYOU" + i ,"");
			param.addProperty("SHIKIKIN" + i ,"");
			param.addProperty("REIKIN" + i ,"");
			param.addProperty("SONOTAICHIJIKIN" + i ,"");
			param.addProperty("KAIYAKUKOUSHIN" + i ,"");
			param.addProperty("SONOTAKOUJO" + i ,"");
			param.addProperty("GOUKEI" + i ,"");
			param.addProperty("BIKOU" + i ,"");
		}
		
		param.addProperty("CHINTAIRYOUGOUKEI","");
		param.addProperty("KYOUEKIHIGOUKEI","");
		param.addProperty("CHUSHARYOUGOUKEI","");
		param.addProperty("SONOTACHINRYOUGOUKEI","");
		param.addProperty("SHIKIKINGOUKEI","");
		param.addProperty("REIKINGOUKEI","");
		param.addProperty("SONOTAICHIJIKINGOUKEI","");
		param.addProperty("KAIYAKUKOUSHINGOUKEI","");
		param.addProperty("SONOTAKOUJOGOUKEI","");
		param.addProperty("GOUKEIBIKOU","");
		param.addProperty("KOUJOGAKUNAIYOU1","");
		param.addProperty("KOUJOGAKU1","");
		param.addProperty("KOUJOGAKUBIKOU","");
		param.addProperty("KOUJOGAKUGOUKEI","");
		param.addProperty("BUKKENGOUKEI","");
		
		ls.setParameter(LedgerLandlordStatement.LANDLORD_STATEMENT_SHEET,param);
	}

}

package export;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonObject;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * 帳票出力
 * <ul>
 * <li>jrxml形式のXMLファイルをテンプレートとしてPDFを出力する。</li>
 * <li>jrxmlファイルは「TIBCO Jaspersoft Studio」で作成。</li>
 * </ul>
 * 
 * @author B-Stage K.Harada
 */
public class LedgerCancellationStatement {
	// テンプレート定義（テンプレート追加の際はここにテンプレートファイル名を定義すること）
	public static final String CANCELLATION_STATEMENT_SHEET = "CancellationStatement.jrxml";	// 解約精算書テンプレート

	// 設置環境によって変更すること
	private static final String templateDir = "template/";		// テンプレートファイル(jrxml)を格納するディレクトリ
	private static final String outputDir = "output/";			// PDF出力先ディレクトリ

	// クラス内部で使用する変数
	private String baseDir = "";
	private String template = "";					// 帳票テンプレート
	private Map<String,Object> param = null;		// 帳票データ
	
	/**
	 * コンストラクタ
	 */
	public LedgerCancellationStatement() {
	}
	
	/**
	 * ベースディレクトリの指定ありコンストラクタ
	 * 
	 * @param dir
	 */
	public LedgerCancellationStatement(String dir) {
		baseDir = dir;
	}
	
	/**
	 * 帳票のデフォルトパラメータ取得
	 * 
	 * @param sheet 取得したいパラメータの帳票テンプレート
	 * @return 引数で指定した帳票に対応するデフォルトパラメータのJSONデータ
	 */
	public JsonObject getDefaultParameter(String sheet) {
		JsonObject defaultParam = new JsonObject();

		// 現在日付取得
		LocalDateTime nowDate = LocalDateTime.now();
		String yyyymmdd = DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(nowDate);

		// 賃貸精算書テンプレート
		if (CANCELLATION_STATEMENT_SHEET.equals(sheet)) {
			defaultParam.addProperty("Date",yyyymmdd);						// [String] 発行日
			defaultParam.addProperty("KAISHAMEI","");						// [String] 会社名
			defaultParam.addProperty("CHINSHAKUNIN","");					// [String] 賃借人
			defaultParam.addProperty("YUUBINBANGOU","");					// [String] 郵便番号
			defaultParam.addProperty("JUSHO","");							// [String] 住所
			defaultParam.addProperty("CHINTAININ","");						// [String] 賃貸人
			defaultParam.addProperty("BUKKENMEI","");						// [String] 物件名
			defaultParam.addProperty("KAIYAKUBI","");						// [String] 解約日
			defaultParam.addProperty("TAIKYOBI","");						// [String] 退去日
			defaultParam.addProperty("KAIYAKUSEISANBIYOTEIBI","");			// [String] 解約精算日予定日
			defaultParam.addProperty("OAZUKARIKOUMOKU1","");				// [String] お預かり項目1
			defaultParam.addProperty("OAZUKARIKOUMOKU2","");				// [String] お預かり項目2
			defaultParam.addProperty("OAZUKARIKOUMOKU3","");				// [String] お預かり項目3
			defaultParam.addProperty("OAZUKARIKOUMOKU4","");				// [String] お預かり項目4
			defaultParam.addProperty("OAZUKARIKOUMOKU5","");				// [String] お預かり項目5
			defaultParam.addProperty("OAZUKARIKOUMOKU6","");				// [String] お預かり項目6
			defaultParam.addProperty("OAZUKARIKOUMOKU7","");				// [String] お預かり項目7
			defaultParam.addProperty("OAZUKARIKOUMOKU8","");				// [String] お預かり項目8
			defaultParam.addProperty("OAZUKARIKOUMOKU9","");				// [String] お預かり項目9
			defaultParam.addProperty("OAZUKARIKOUMOKU10","");				// [String] お預かり項目10
			defaultParam.addProperty("OAZUKARIKOUMOKU11","");				// [String] お預かり項目11
			defaultParam.addProperty("OAZUKARIKOUMOKU12","");				// [String] お預かり項目12
			defaultParam.addProperty("OAZUKARIKIN1","");					// [String] お預かり金1
			defaultParam.addProperty("OAZUKARIKIN2","");					// [String] お預かり金2
			defaultParam.addProperty("OAZUKARIKIN3","");					// [String] お預かり金3
			defaultParam.addProperty("OAZUKARIKIN4","");					// [String] お預かり金4
			defaultParam.addProperty("OAZUKARIKIN5","");					// [String] お預かり金5
			defaultParam.addProperty("OAZUKARIKIN6","");					// [String] お預かり金6
			defaultParam.addProperty("OAZUKARIKIN7","");					// [String] お預かり金7
			defaultParam.addProperty("OAZUKARIKIN8","");					// [String] お預かり金8
			defaultParam.addProperty("OAZUKARIKIN9","");					// [String] お預かり金9
			defaultParam.addProperty("OAZUKARIKIN10","");					// [String] お預かり金10
			defaultParam.addProperty("OAZUKARIKIN11","");					// [String] お預かり金11
			defaultParam.addProperty("OAZUKARIKIN12","");					// [String] お預かり金12
			defaultParam.addProperty("SHOUKEI1","");						// [String] 小計1
			defaultParam.addProperty("SEIKYUKOUMOKU1","");					// [String] 請求項目1
			defaultParam.addProperty("SEIKYUKOUMOKU2","");					// [String] 請求項目2
			defaultParam.addProperty("SEIKYUKOUMOKU3","");					// [String] 請求項目3
			defaultParam.addProperty("SEIKYUKOUMOKU4","");					// [String] 請求項目4
			defaultParam.addProperty("SEIKYUKOUMOKU5","");					// [String] 請求項目5
			defaultParam.addProperty("SEIKYUKOUMOKU6","");					// [String] 請求項目6
			defaultParam.addProperty("SEIKYUKOUMOKU7","");					// [String] 請求項目7
			defaultParam.addProperty("SEIKYUKOUMOKU8","");					// [String] 請求項目8
			defaultParam.addProperty("SEIKYUKOUMOKU9","");					// [String] 請求項目9
			defaultParam.addProperty("SEIKYUKOUMOKU10","");					// [String] 請求項目10
			defaultParam.addProperty("SEIKYUKOUMOKU11","");					// [String] 請求項目11
			defaultParam.addProperty("SEIKYUKOUMOKU12","");					// [String] 請求項目12
			defaultParam.addProperty("SEIKYUGAKU1","");						// [String] 請求額1
			defaultParam.addProperty("SEIKYUGAKU2","");						// [String] 請求額2
			defaultParam.addProperty("SEIKYUGAKU3","");						// [String] 請求額3
			defaultParam.addProperty("SEIKYUGAKU4","");						// [String] 請求額4
			defaultParam.addProperty("SEIKYUGAKU5","");						// [String] 請求額5
			defaultParam.addProperty("SEIKYUGAKU6","");						// [String] 請求額6
			defaultParam.addProperty("SEIKYUGAKU7","");						// [String] 請求額7
			defaultParam.addProperty("SEIKYUGAKU8","");						// [String] 請求額8
			defaultParam.addProperty("SEIKYUGAKU9","");						// [String] 請求額9
			defaultParam.addProperty("SEIKYUGAKU10","");					// [String] 請求額10
			defaultParam.addProperty("SEIKYUGAKU11","");					// [String] 請求額11
			defaultParam.addProperty("SEIKYUGAKU12","");					// [String] 請求額12
			defaultParam.addProperty("SHOUKEI2","");						// [String] 小計2
			defaultParam.addProperty("SYUZENKOUMOKU1","");					// [String] 修繕項目1
			defaultParam.addProperty("SYUZENKOUMOKU2","");					// [String] 修繕項目2
			defaultParam.addProperty("SYUZENKOUMOKU3","");					// [String] 修繕項目3
			defaultParam.addProperty("SYUZENKOUMOKU4","");					// [String] 修繕項目4
			defaultParam.addProperty("SYUZENKOUMOKU5","");					// [String] 修繕項目5
			defaultParam.addProperty("SYUZENKOUMOKU6","");					// [String] 修繕項目6
			defaultParam.addProperty("SYUZENKOUMOKU7","");					// [String] 修繕項目7
			defaultParam.addProperty("SYUZENKOUMOKU8","");					// [String] 修繕項目8
			defaultParam.addProperty("SYUZENKOUMOKU9","");					// [String] 修繕項目9
			defaultParam.addProperty("SYUZENKOUMOKU10","");					// [String] 修繕項目10
			defaultParam.addProperty("SYUZENKOUMOKU11","");					// [String] 修繕項目11
			defaultParam.addProperty("SYUZENKOUMOKU12","");					// [String] 修繕項目12
			defaultParam.addProperty("SYUZENKOUMOKU13","");					// [String] 修繕項目13
			defaultParam.addProperty("SYUZENKOUMOKU14","");					// [String] 修繕項目14
			defaultParam.addProperty("SYUZENKOUMOKU15","");					// [String] 修繕項目15
			defaultParam.addProperty("SYUZENKOUMOKU16","");					// [String] 修繕項目16
			defaultParam.addProperty("SYUZENKOUMOKU17","");					// [String] 修繕項目17
			defaultParam.addProperty("SYUZENKOUMOKU18","");					// [String] 修繕項目18
			defaultParam.addProperty("SYUZENKOUMOKU19","");					// [String] 修繕項目19
			defaultParam.addProperty("SYUZENKOUMOKU20","");					// [String] 修繕項目20
			defaultParam.addProperty("SYUZENKOUMOKU21","");					// [String] 修繕項目21
			defaultParam.addProperty("SYUZENKOUMOKU22","");					// [String] 修繕項目22
			defaultParam.addProperty("SYUZENKOUMOKU23","");					// [String] 修繕項目23
			defaultParam.addProperty("SYUZENTANKA1","");					// [String] 修繕単価1
			defaultParam.addProperty("SYUZENTANKA2","");					// [String] 修繕単価2
			defaultParam.addProperty("SYUZENTANKA3","");					// [String] 修繕単価3
			defaultParam.addProperty("SYUZENTANKA4","");					// [String] 修繕単価4
			defaultParam.addProperty("SYUZENTANKA5","");					// [String] 修繕単価5
			defaultParam.addProperty("SYUZENTANKA6","");					// [String] 修繕単価6
			defaultParam.addProperty("SYUZENTANKA7","");					// [String] 修繕単価7
			defaultParam.addProperty("SYUZENTANKA8","");					// [String] 修繕単価8
			defaultParam.addProperty("SYUZENTANKA9","");					// [String] 修繕単価9
			defaultParam.addProperty("SYUZENTANKA10","");					// [String] 修繕単価10
			defaultParam.addProperty("SYUZENTANKA11","");					// [String] 修繕単価11
			defaultParam.addProperty("SYUZENTANKA12","");					// [String] 修繕単価12
			defaultParam.addProperty("SYUZENTANKA13","");					// [String] 修繕単価13
			defaultParam.addProperty("SYUZENTANKA14","");					// [String] 修繕単価14
			defaultParam.addProperty("SYUZENTANKA15","");					// [String] 修繕単価15
			defaultParam.addProperty("SYUZENTANKA16","");					// [String] 修繕単価16
			defaultParam.addProperty("SYUZENTANKA17","");					// [String] 修繕単価17
			defaultParam.addProperty("SYUZENTANKA18","");					// [String] 修繕単価18
			defaultParam.addProperty("SYUZENTANKA19","");					// [String] 修繕単価19
			defaultParam.addProperty("SYUZENTANKA20","");					// [String] 修繕単価20
			defaultParam.addProperty("SYUZENTANKA21","");					// [String] 修繕単価21
			defaultParam.addProperty("SYUZENTANKA22","");					// [String] 修繕単価22
			defaultParam.addProperty("SYUZENTANKA23","");					// [String] 修繕単価23
			defaultParam.addProperty("SYUZENSUURYOU1","");					// [String] 修繕数量1
			defaultParam.addProperty("SYUZENSUURYOU2","");					// [String] 修繕数量2
			defaultParam.addProperty("SYUZENSUURYOU3","");					// [String] 修繕数量3
			defaultParam.addProperty("SYUZENSUURYOU4","");					// [String] 修繕数量4
			defaultParam.addProperty("SYUZENSUURYOU5","");					// [String] 修繕数量5
			defaultParam.addProperty("SYUZENSUURYOU6","");					// [String] 修繕数量6
			defaultParam.addProperty("SYUZENSUURYOU7","");					// [String] 修繕数量7
			defaultParam.addProperty("SYUZENSUURYOU8","");					// [String] 修繕数量8
			defaultParam.addProperty("SYUZENSUURYOU9","");					// [String] 修繕数量9
			defaultParam.addProperty("SYUZENSUURYOU10","");					// [String] 修繕数量10
			defaultParam.addProperty("SYUZENSUURYOU11","");					// [String] 修繕数量11
			defaultParam.addProperty("SYUZENSUURYOU12","");					// [String] 修繕数量12
			defaultParam.addProperty("SYUZENSUURYOU13","");					// [String] 修繕数量13
			defaultParam.addProperty("SYUZENSUURYOU14","");					// [String] 修繕数量14
			defaultParam.addProperty("SYUZENSUURYOU15","");					// [String] 修繕数量15
			defaultParam.addProperty("SYUZENSUURYOU16","");					// [String] 修繕数量16
			defaultParam.addProperty("SYUZENSUURYOU17","");					// [String] 修繕数量17
			defaultParam.addProperty("SYUZENSUURYOU18","");					// [String] 修繕数量18
			defaultParam.addProperty("SYUZENSUURYOU19","");					// [String] 修繕数量19
			defaultParam.addProperty("SYUZENSUURYOU20","");					// [String] 修繕数量20
			defaultParam.addProperty("SYUZENSUURYOU21","");					// [String] 修繕数量21
			defaultParam.addProperty("SYUZENSUURYOU22","");					// [String] 修繕数量22
			defaultParam.addProperty("SYUZENSUURYOU23","");					// [String] 修繕数量23
			defaultParam.addProperty("SYUZENHIKINGAKU1","");				// [String] 修繕費金額1
			defaultParam.addProperty("SYUZENHIKINGAKU2","");				// [String] 修繕費金額2
			defaultParam.addProperty("SYUZENHIKINGAKU3","");				// [String] 修繕費金額3
			defaultParam.addProperty("SYUZENHIKINGAKU4","");				// [String] 修繕費金額4
			defaultParam.addProperty("SYUZENHIKINGAKU5","");				// [String] 修繕費金額5
			defaultParam.addProperty("SYUZENHIKINGAKU6","");				// [String] 修繕費金額6
			defaultParam.addProperty("SYUZENHIKINGAKU7","");				// [String] 修繕費金額7
			defaultParam.addProperty("SYUZENHIKINGAKU8","");				// [String] 修繕費金額8
			defaultParam.addProperty("SYUZENHIKINGAKU9","");				// [String] 修繕費金額9
			defaultParam.addProperty("SYUZENHIKINGAKU10","");				// [String] 修繕費金額10
			defaultParam.addProperty("SYUZENHIKINGAKU11","");				// [String] 修繕費金額11
			defaultParam.addProperty("SYUZENHIKINGAKU12","");				// [String] 修繕費金額12
			defaultParam.addProperty("SYUZENHIKINGAKU13","");				// [String] 修繕費金額13
			defaultParam.addProperty("SYUZENHIKINGAKU14","");				// [String] 修繕費金額14
			defaultParam.addProperty("SYUZENHIKINGAKU15","");				// [String] 修繕費金額15
			defaultParam.addProperty("SYUZENHIKINGAKU16","");				// [String] 修繕費金額16
			defaultParam.addProperty("SYUZENHIKINGAKU17","");				// [String] 修繕費金額17
			defaultParam.addProperty("SYUZENHIKINGAKU18","");				// [String] 修繕費金額18
			defaultParam.addProperty("SYUZENHIKINGAKU19","");				// [String] 修繕費金額19
			defaultParam.addProperty("SYUZENHIKINGAKU20","");				// [String] 修繕費金額20
			defaultParam.addProperty("SYUZENHIKINGAKU21","");				// [String] 修繕費金額21
			defaultParam.addProperty("SYUZENHIKINGAKU22","");				// [String] 修繕費金額22
			defaultParam.addProperty("SYUZENHIKINGAKU23","");				// [String] 修繕費金額23
			defaultParam.addProperty("SHOUKEI3","");						// [String] 小計3
			defaultParam.addProperty("SASHIHIKIHENKINGAKU","");				// [String] 差引返金額
			defaultParam.addProperty("OSHIHARAISAKIKINYUUKIKANMEI","");		// [String] お支払い先金融機関名
			defaultParam.addProperty("OSHIHARAISAKIHONSHITENMEI","");		// [String] お支払い先本支店名
			defaultParam.addProperty("OSHIHARAISAKIKOUZABUNRUI","");		// [String] お支払い先口座分類
			defaultParam.addProperty("OSHIHARAISAKIKOUZABANGOU","");		// [String] お支払い先口座番号
			defaultParam.addProperty("OSHIHARAISAKIKOUZAMEIGI","");			// [String] お支払い先口座名義
			defaultParam.addProperty("GOHENKINKINYUUKIKANMEI","");			// [String] ご返金金融機関名
			defaultParam.addProperty("GOHENKINHONSHITENMEI","");			// [String] ご返金本支店名
			defaultParam.addProperty("GOHENKINKOUZABUNRUI","");				// [String] ご返金口座分類
			defaultParam.addProperty("GOHENKINKOUZABANGOU","");				// [String] ご返金口座番号
			defaultParam.addProperty("GOHENKINKOUZAMEIGI","");				// [String] ご返金口座名義
			defaultParam.addProperty("GOSEISANKIGEN","");					// [String] ご精算期限
		}

		return defaultParam;
	}
	
	/**
	 * 帳票処理で使用するディレクトリのベースディレクトリを設定
	 * 
	 * @param dir ベースディレクトリ
	 * @return ベースディレクトリ設定成功時は true を返し、失敗時は false を返す。
	 */
	public boolean setBaseDir(String dir) {
		baseDir = dir;
		return true;
	}
	
	/**
	 * 帳票パラメータ設定
	 * 
	 * @param sheet 帳票テンプレート
	 * @param data sheetで指定したテンプレートに合わせたJSONデータ
	 * @return パラメータ設定成功時は true を返し、失敗時は false を返す。
	 */
	public boolean setParameter(String sheet,JsonObject data) {
		try {
			template = sheet;
			param = new HashMap<String,Object>();
			Iterator<String> keyList = data.keySet().iterator();
			while (keyList.hasNext()) {
				String keyName = keyList.next();
				String value = data.get(keyName).getAsString();
				param.put(keyName,value);
			}
			return true;
		}
		catch(Exception e) {
			//e.printStackTrace();
			template = "";
			param = null;
			return false;
		}
	}
	
	/**
	 * 帳票をPDFファイルとして出力する
	 * 
	 * @return PDFファイルパスを返す。
	 */
	public String outputPDF() {
		try {
			// 時刻取得
			LocalDateTime nowDate = LocalDateTime.now();
			DateTimeFormatter formDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

			// 出力ファイル名のベース部生成
			int pos = template.lastIndexOf(".");
			String baseName = (pos > -1) ? template.substring(0,pos) : template.substring(0);
			baseName += "_"+formDate.format(nowDate);

			// PDFファイル出力
			File jrxmlFile = new File(baseDir+templateDir+template);
			String templateFile = jrxmlFile.getAbsolutePath();
			String outputFile = baseDir+outputDir+baseName+".pdf";
			JasperReport report = JasperCompileManager.compileReport(templateFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,param,new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(jasperPrint,outputFile);
			return outputFile;
		}
		catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 帳票のPDF形式バイトデータを取得する
	 * 
	 * @return 成功時はPDFのバイトデータ、失敗時はnullを返す。
	 */
	public byte[] getPDFData() {
		try {
			// PDFファイル出力
			File jrxmlFile = new File(baseDir+templateDir+template);
			String templateFile = jrxmlFile.getAbsolutePath();
			JasperReport report = JasperCompileManager.compileReport(templateFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,param,new JREmptyDataSource());
			return JasperExportManager.exportReportToPdf(jasperPrint);
		}
		catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
}

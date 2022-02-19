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
public class LedgerRentalStatement {
	// テンプレート定義（テンプレート追加の際はここにテンプレートファイル名を定義すること）
	public static final String RENTAL_STATEMENT_SHEET = "RentalStatement.jrxml";		// 賃貸精算書テンプレート

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
	public LedgerRentalStatement() {
	}
	
	/**
	 * ベースディレクトリの指定ありコンストラクタ
	 * 
	 * @param dir
	 */
	public LedgerRentalStatement(String dir) {
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
		if (RENTAL_STATEMENT_SHEET.equals(sheet)) {
			defaultParam.addProperty("No","");					// [String] 連番
			defaultParam.addProperty("Date",yyyymmdd);			// [String] 発行日
			defaultParam.addProperty("CHINSHAKUNIN","");		// [String] 賃借人
			defaultParam.addProperty("BUKKENMEI","");			// [String] 物件名
			defaultParam.addProperty("KEIYAKUKIKAN_B","");		// [String] 契約期間（開始）
			defaultParam.addProperty("KEIYAKUKIKAN_E","");		// [String] 契約期間（終了）
			defaultParam.addProperty("CHINRYOU","");			// [String] 賃料
			defaultParam.addProperty("KYOUEKIHI","");			// [String] 共益費
			defaultParam.addProperty("SHOUHIZEI","");			// [String] 消費税
			defaultParam.addProperty("KOUSHINRYOU","");			// [String] 更新料
			defaultParam.addProperty("SHIKIKIN","");			// [String] 敷金
			defaultParam.addProperty("REIKIN","");				// [String] 礼金
			defaultParam.addProperty("SHOUKYAKU","");			// [String] 償却
			defaultParam.addProperty("SHIKIKIN_M","");			// [String] 敷金（月）
			defaultParam.addProperty("REIKIN_M","");			// [String] 礼金（月）
			defaultParam.addProperty("HIWARI1_M","");			// [String] 日割り１（月）
			defaultParam.addProperty("HIWARI1_B","");			// [String] 日割り１（開始日）
			defaultParam.addProperty("HIWARI1_E","");			// [String] 日割り１（終了日）
			defaultParam.addProperty("HIWARI1_N","");			// [String] 日割り１（実日数）
			defaultParam.addProperty("HIWARI1_H","");			// [String] 日割り１（月日数）
			defaultParam.addProperty("HIWARI2_M","");			// [String] 日割り２（月）
			defaultParam.addProperty("HIWARI2_B","");			// [String] 日割り２（開始日）
			defaultParam.addProperty("HIWARI2_E","");			// [String] 日割り２（終了日）
			defaultParam.addProperty("HIWARI2_N","");			// [String] 日割り２（実日数）
			defaultParam.addProperty("HIWARI2_H","");			// [String] 日割り２（月日数）
			defaultParam.addProperty("CHINRYOU1","");			// [String] 賃料１（日割り残額）
			defaultParam.addProperty("CHINRYOU2","");			// [String] 賃料２（日割り残額）
			defaultParam.addProperty("KYOUEKIHI1","");			// [String] 共益費１（日割り残額）
			defaultParam.addProperty("KYOUEKIHI2","");			// [String] 共益費２（日割り残額）
			defaultParam.addProperty("CHUUKAI_SHOUHIZEI","");	// [String] 仲介手数料の消費税
			defaultParam.addProperty("CHUUKAI_TESUURYOU","");	// [String] 仲介手数料（消費税込み）
			defaultParam.addProperty("SHOUKEI","");				// [String] 小計
			defaultParam.addProperty("KASAIHOKEN_SETSUMEI","");	// [String] 火災保険料の説明
			defaultParam.addProperty("KASAIHOKEN_RYOUKIN","");	// [String] 火災保険料
			defaultParam.addProperty("GOUKEI","");				// [String] 合計
			defaultParam.addProperty("TETSUKEKIN","");			// [String] 手付金
			defaultParam.addProperty("JURYOUBI","");			// [String] 受領日
			defaultParam.addProperty("SASHIHIKIZANKIN","");		// [String] 差引残金
			defaultParam.addProperty("ZANKINKESSAIBI","");		// [String] 残金決済日
			defaultParam.addProperty("FURIKOMISAKI","");		// [String] 振込先
			defaultParam.addProperty("JUSYO","");			    // [String] 住所
			defaultParam.addProperty("KAISYAMEI","");			// [String] 会社名
			defaultParam.addProperty("DENWABANGOU","");			// [String] 電話番号
			defaultParam.addProperty("TANTOU","");				// [String] 担当
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

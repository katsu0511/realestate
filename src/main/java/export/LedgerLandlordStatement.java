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
public class LedgerLandlordStatement {
	// テンプレート定義（テンプレート追加の際はここにテンプレートファイル名を定義すること）
	public static final String LANDLORD_STATEMENT_SHEET = "LandlordStatement.jrxml";		// 大家精算書テンプレート

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
	public LedgerLandlordStatement() {
	}
	
	/**
	 * ベースディレクトリの指定ありコンストラクタ
	 * 
	 * @param dir
	 */
	public LedgerLandlordStatement(String dir) {
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

		// 大家精算書テンプレート
		if (LANDLORD_STATEMENT_SHEET.equals(sheet)) {
			defaultParam.addProperty("HINICHI",yyyymmdd);					// [String] 発行日
			defaultParam.addProperty("OOYAYUUBINBANGOU","");				// [String] 大家郵便番号
			defaultParam.addProperty("OOYAJUUSYO","");						// [String] 大家住所
			defaultParam.addProperty("OOYAMEI","");							// [String] 大家名
			defaultParam.addProperty("KANRIGAISYAMEI","");					// [String] 管理会社名
			defaultParam.addProperty("DAIHYOUSYAMEI","");					// [String] 代表者名
			defaultParam.addProperty("KANRIGAISYAYUUBINBANGOU","");			// [String] 管理会社郵便番号
			defaultParam.addProperty("KANRIGAISYAJUUSYO","");				// [String] 管理会社住所
			defaultParam.addProperty("KANRIGAISYADENWABANGOU","");			// [String] 管理会社電話番号
			defaultParam.addProperty("OSHIHARAIYOTEIBI","");				// [String] お支払い予定日
			defaultParam.addProperty("GOUKEIKINGAKU","");					// [String] 合計金額
			defaultParam.addProperty("SONOTASHIHARAIGAKU","");				// [String] その他支払額
			defaultParam.addProperty("KOUJOGAKU","");						// [String] 控除額
			defaultParam.addProperty("SOUKINGAKU","");						// [String] 送金額
			defaultParam.addProperty("FURIKOMISAKIKINYUUKIKAN","");			// [String] 振込先金融機関
			defaultParam.addProperty("FURIKOMISAKISYUBETU","");				// [String] 振込先種別
			defaultParam.addProperty("FURIKOMISAKIKOUZABANGOU","");			// [String] 振込先口座番号
			defaultParam.addProperty("SHIHARAIGAKU","");					// [String] 支払額
			defaultParam.addProperty("FURIKOMITESUURYOU","");				// [String] 振込手数料
			defaultParam.addProperty("FURIKOMIGAKU","");					// [String] 振込額
			defaultParam.addProperty("TAISHONENGETU1","");					// [String] 対象年月1
			defaultParam.addProperty("NUMBER1","");							// [String] No1
			defaultParam.addProperty("SHIMEI1","");							// [String] 氏名1
			defaultParam.addProperty("CHINTAIRYOU1","");					// [String] 賃貸料1
			defaultParam.addProperty("KYOUEKIHI1","");						// [String] 共益費1
			defaultParam.addProperty("CHUSHARYOU1","");						// [String] 駐車料1
			defaultParam.addProperty("SONOTACHINRYOU1","");					// [String] その他月々賃料1
			defaultParam.addProperty("SHIKIKIN1","");						// [String] 敷金1
			defaultParam.addProperty("REIKIN1","");							// [String] 礼金1
			defaultParam.addProperty("SONOTAICHIJIKIN1","");				// [String] その他契約一時金1
			defaultParam.addProperty("KAIYAKUKOUSHIN1","");					// [String] 解約費・更新費1
			defaultParam.addProperty("SONOTAKOUJO1","");					// [String] その他支払・控除額1
			defaultParam.addProperty("GOUKEI1","");							// [String] 合計1
			defaultParam.addProperty("BIKOU1","");							// [String] 備考1
			defaultParam.addProperty("TAISHONENGETU2","");					// [String] 対象年月2
			defaultParam.addProperty("NUMBER2","");							// [String] No2
			defaultParam.addProperty("SHIMEI2","");							// [String] 氏名2
			defaultParam.addProperty("CHINTAIRYOU2","");					// [String] 賃貸料2
			defaultParam.addProperty("KYOUEKIHI2","");						// [String] 共益費2
			defaultParam.addProperty("CHUSHARYOU2","");						// [String] 駐車料2
			defaultParam.addProperty("SONOTACHINRYOU2","");					// [String] その他月々賃料2
			defaultParam.addProperty("SHIKIKIN2","");						// [String] 敷金2
			defaultParam.addProperty("REIKIN2","");							// [String] 礼金2
			defaultParam.addProperty("SONOTAICHIJIKIN2","");				// [String] その他契約一時金2
			defaultParam.addProperty("KAIYAKUKOUSHIN2","");					// [String] 解約費・更新費2
			defaultParam.addProperty("SONOTAKOUJO2","");					// [String] その他支払・控除額2
			defaultParam.addProperty("GOUKEI2","");							// [String] 合計2
			defaultParam.addProperty("BIKOU2","");							// [String] 備考2
			defaultParam.addProperty("TAISHONENGETU3","");					// [String] 対象年月3
			defaultParam.addProperty("NUMBER3","");							// [String] No3
			defaultParam.addProperty("SHIMEI3","");							// [String] 氏名3
			defaultParam.addProperty("CHINTAIRYOU3","");					// [String] 賃貸料3
			defaultParam.addProperty("KYOUEKIHI3","");						// [String] 共益費3
			defaultParam.addProperty("CHUSHARYOU3","");						// [String] 駐車料3
			defaultParam.addProperty("SONOTACHINRYOU3","");					// [String] その他月々賃料3
			defaultParam.addProperty("SHIKIKIN3","");						// [String] 敷金3
			defaultParam.addProperty("REIKIN3","");							// [String] 礼金3
			defaultParam.addProperty("SONOTAICHIJIKIN3","");				// [String] その他契約一時金3
			defaultParam.addProperty("KAIYAKUKOUSHIN3","");					// [String] 解約費・更新費3
			defaultParam.addProperty("SONOTAKOUJO3","");					// [String] その他支払・控除額3
			defaultParam.addProperty("GOUKEI3","");							// [String] 合計3
			defaultParam.addProperty("BIKOU3","");							// [String] 備考3
			defaultParam.addProperty("TAISHONENGETU4","");					// [String] 対象年月4
			defaultParam.addProperty("NUMBER4","");							// [String] No4
			defaultParam.addProperty("SHIMEI4","");							// [String] 氏名4
			defaultParam.addProperty("CHINTAIRYOU4","");					// [String] 賃貸料4
			defaultParam.addProperty("KYOUEKIHI4","");						// [String] 共益費4
			defaultParam.addProperty("CHUSHARYOU4","");						// [String] 駐車料4
			defaultParam.addProperty("SONOTACHINRYOU4","");					// [String] その他月々賃料4
			defaultParam.addProperty("SHIKIKIN4","");						// [String] 敷金4
			defaultParam.addProperty("REIKIN4","");							// [String] 礼金4
			defaultParam.addProperty("SONOTAICHIJIKIN4","");				// [String] その他契約一時金4
			defaultParam.addProperty("KAIYAKUKOUSHIN4","");					// [String] 解約費・更新費4
			defaultParam.addProperty("SONOTAKOUJO4","");					// [String] その他支払・控除額4
			defaultParam.addProperty("GOUKEI4","");							// [String] 合計4
			defaultParam.addProperty("BIKOU4","");							// [String] 備考4
			defaultParam.addProperty("TAISHONENGETU5","");					// [String] 対象年月5
			defaultParam.addProperty("NUMBER5","");							// [String] No5
			defaultParam.addProperty("SHIMEI5","");							// [String] 氏名5
			defaultParam.addProperty("CHINTAIRYOU5","");					// [String] 賃貸料5
			defaultParam.addProperty("KYOUEKIHI5","");						// [String] 共益費5
			defaultParam.addProperty("CHUSHARYOU5","");						// [String] 駐車料5
			defaultParam.addProperty("SONOTACHINRYOU5","");					// [String] その他月々賃料5
			defaultParam.addProperty("SHIKIKIN5","");						// [String] 敷金5
			defaultParam.addProperty("REIKIN5","");							// [String] 礼金5
			defaultParam.addProperty("SONOTAICHIJIKIN5","");				// [String] その他契約一時金5
			defaultParam.addProperty("KAIYAKUKOUSHIN5","");					// [String] 解約費・更新費5
			defaultParam.addProperty("SONOTAKOUJO5","");					// [String] その他支払・控除額5
			defaultParam.addProperty("GOUKEI5","");							// [String] 合計5
			defaultParam.addProperty("BIKOU5","");							// [String] 備考5
			defaultParam.addProperty("TAISHONENGETU6","");					// [String] 対象年月6
			defaultParam.addProperty("NUMBER6","");							// [String] No6
			defaultParam.addProperty("SHIMEI6","");							// [String] 氏名6
			defaultParam.addProperty("CHINTAIRYOU6","");					// [String] 賃貸料6
			defaultParam.addProperty("KYOUEKIHI6","");						// [String] 共益費6
			defaultParam.addProperty("CHUSHARYOU6","");						// [String] 駐車料6
			defaultParam.addProperty("SONOTACHINRYOU6","");					// [String] その他月々賃料6
			defaultParam.addProperty("SHIKIKIN6","");						// [String] 敷金6
			defaultParam.addProperty("REIKIN6","");							// [String] 礼金6
			defaultParam.addProperty("SONOTAICHIJIKIN6","");				// [String] その他契約一時金6
			defaultParam.addProperty("KAIYAKUKOUSHIN6","");					// [String] 解約費・更新費6
			defaultParam.addProperty("SONOTAKOUJO6","");					// [String] その他支払・控除額6
			defaultParam.addProperty("GOUKEI6","");							// [String] 合計6
			defaultParam.addProperty("BIKOU6","");							// [String] 備考6
			defaultParam.addProperty("TAISHONENGETU7","");					// [String] 対象年月7
			defaultParam.addProperty("NUMBER7","");							// [String] No7
			defaultParam.addProperty("SHIMEI7","");							// [String] 氏名7
			defaultParam.addProperty("CHINTAIRYOU7","");					// [String] 賃貸料7
			defaultParam.addProperty("KYOUEKIHI7","");						// [String] 共益費7
			defaultParam.addProperty("CHUSHARYOU7","");						// [String] 駐車料7
			defaultParam.addProperty("SONOTACHINRYOU7","");					// [String] その他月々賃料7
			defaultParam.addProperty("SHIKIKIN7","");						// [String] 敷金7
			defaultParam.addProperty("REIKIN7","");							// [String] 礼金7
			defaultParam.addProperty("SONOTAICHIJIKIN7","");				// [String] その他契約一時金7
			defaultParam.addProperty("KAIYAKUKOUSHIN7","");					// [String] 解約費・更新費7
			defaultParam.addProperty("SONOTAKOUJO7","");					// [String] その他支払・控除額7
			defaultParam.addProperty("GOUKEI7","");							// [String] 合計7
			defaultParam.addProperty("BIKOU7","");							// [String] 備考7
			defaultParam.addProperty("TAISHONENGETU8","");					// [String] 対象年月8
			defaultParam.addProperty("NUMBER8","");							// [String] No8
			defaultParam.addProperty("SHIMEI8","");							// [String] 氏名8
			defaultParam.addProperty("CHINTAIRYOU8","");					// [String] 賃貸料8
			defaultParam.addProperty("KYOUEKIHI8","");						// [String] 共益費8
			defaultParam.addProperty("CHUSHARYOU8","");						// [String] 駐車料8
			defaultParam.addProperty("SONOTACHINRYOU8","");					// [String] その他月々賃料8
			defaultParam.addProperty("SHIKIKIN8","");						// [String] 敷金8
			defaultParam.addProperty("REIKIN8","");							// [String] 礼金8
			defaultParam.addProperty("SONOTAICHIJIKIN8","");				// [String] その他契約一時金8
			defaultParam.addProperty("KAIYAKUKOUSHIN8","");					// [String] 解約費・更新費8
			defaultParam.addProperty("SONOTAKOUJO8","");					// [String] その他支払・控除額8
			defaultParam.addProperty("GOUKEI8","");							// [String] 合計8
			defaultParam.addProperty("BIKOU8","");							// [String] 備考8
			defaultParam.addProperty("TAISHONENGETU9","");					// [String] 対象年月9
			defaultParam.addProperty("NUMBER9","");							// [String] No9
			defaultParam.addProperty("SHIMEI9","");							// [String] 氏名9
			defaultParam.addProperty("CHINTAIRYOU9","");					// [String] 賃貸料9
			defaultParam.addProperty("KYOUEKIHI9","");						// [String] 共益費9
			defaultParam.addProperty("CHUSHARYOU9","");						// [String] 駐車料9
			defaultParam.addProperty("SONOTACHINRYOU9","");					// [String] その他月々賃料9
			defaultParam.addProperty("SHIKIKIN9","");						// [String] 敷金9
			defaultParam.addProperty("REIKIN9","");							// [String] 礼金9
			defaultParam.addProperty("SONOTAICHIJIKIN9","");				// [String] その他契約一時金9
			defaultParam.addProperty("KAIYAKUKOUSHIN9","");					// [String] 解約費・更新費9
			defaultParam.addProperty("SONOTAKOUJO9","");					// [String] その他支払・控除額9
			defaultParam.addProperty("GOUKEI9","");							// [String] 合計9
			defaultParam.addProperty("BIKOU9","");							// [String] 備考9
			defaultParam.addProperty("TAISHONENGETU10","");					// [String] 対象年月10
			defaultParam.addProperty("NUMBER10","");						// [String] No10
			defaultParam.addProperty("SHIMEI10","");						// [String] 氏名10
			defaultParam.addProperty("CHINTAIRYOU10","");					// [String] 賃貸料10
			defaultParam.addProperty("KYOUEKIHI10","");						// [String] 共益費10
			defaultParam.addProperty("CHUSHARYOU10","");					// [String] 駐車料10
			defaultParam.addProperty("SONOTACHINRYOU10","");				// [String] その他月々賃料10
			defaultParam.addProperty("SHIKIKIN10","");						// [String] 敷金10
			defaultParam.addProperty("REIKIN10","");						// [String] 礼金10
			defaultParam.addProperty("SONOTAICHIJIKIN10","");				// [String] その他契約一時金10
			defaultParam.addProperty("KAIYAKUKOUSHIN10","");				// [String] 解約費・更新費10
			defaultParam.addProperty("SONOTAKOUJO10","");					// [String] その他支払・控除額10
			defaultParam.addProperty("GOUKEI10","");						// [String] 合計10
			defaultParam.addProperty("BIKOU10","");							// [String] 備考10
			defaultParam.addProperty("TAISHONENGETU11","");					// [String] 対象年月11
			defaultParam.addProperty("NUMBER11","");						// [String] No11
			defaultParam.addProperty("SHIMEI11","");						// [String] 氏名11
			defaultParam.addProperty("CHINTAIRYOU11","");					// [String] 賃貸料11
			defaultParam.addProperty("KYOUEKIHI11","");						// [String] 共益費11
			defaultParam.addProperty("CHUSHARYOU11","");					// [String] 駐車料11
			defaultParam.addProperty("SONOTACHINRYOU11","");				// [String] その他月々賃料11
			defaultParam.addProperty("SHIKIKIN11","");						// [String] 敷金11
			defaultParam.addProperty("REIKIN11","");						// [String] 礼金11
			defaultParam.addProperty("SONOTAICHIJIKIN11","");				// [String] その他契約一時金11
			defaultParam.addProperty("KAIYAKUKOUSHIN11","");				// [String] 解約費・更新費11
			defaultParam.addProperty("SONOTAKOUJO11","");					// [String] その他支払・控除額11
			defaultParam.addProperty("GOUKEI11","");						// [String] 合計11
			defaultParam.addProperty("BIKOU11","");							// [String] 備考11
			defaultParam.addProperty("TAISHONENGETU12","");					// [String] 対象年月12
			defaultParam.addProperty("NUMBER12","");						// [String] No12
			defaultParam.addProperty("SHIMEI12","");						// [String] 氏名12
			defaultParam.addProperty("CHINTAIRYOU12","");					// [String] 賃貸料12
			defaultParam.addProperty("KYOUEKIHI12","");						// [String] 共益費12
			defaultParam.addProperty("CHUSHARYOU12","");					// [String] 駐車料12
			defaultParam.addProperty("SONOTACHINRYOU12","");				// [String] その他月々賃料12
			defaultParam.addProperty("SHIKIKIN12","");						// [String] 敷金12
			defaultParam.addProperty("REIKIN12","");						// [String] 礼金12
			defaultParam.addProperty("SONOTAICHIJIKIN12","");				// [String] その他契約一時金12
			defaultParam.addProperty("KAIYAKUKOUSHIN12","");				// [String] 解約費・更新費12
			defaultParam.addProperty("SONOTAKOUJO12","");					// [String] その他支払・控除額12
			defaultParam.addProperty("GOUKEI12","");						// [String] 合計12
			defaultParam.addProperty("BIKOU12","");							// [String] 備考12
			defaultParam.addProperty("TAISHONENGETU13","");					// [String] 対象年月13
			defaultParam.addProperty("NUMBER13","");						// [String] No13
			defaultParam.addProperty("SHIMEI13","");						// [String] 氏名13
			defaultParam.addProperty("CHINTAIRYOU13","");					// [String] 賃貸料13
			defaultParam.addProperty("KYOUEKIHI13","");						// [String] 共益費13
			defaultParam.addProperty("CHUSHARYOU13","");					// [String] 駐車料13
			defaultParam.addProperty("SONOTACHINRYOU13","");				// [String] その他月々賃料13
			defaultParam.addProperty("SHIKIKIN13","");						// [String] 敷金13
			defaultParam.addProperty("REIKIN13","");						// [String] 礼金13
			defaultParam.addProperty("SONOTAICHIJIKIN13","");				// [String] その他契約一時金13
			defaultParam.addProperty("KAIYAKUKOUSHIN13","");				// [String] 解約費・更新費13
			defaultParam.addProperty("SONOTAKOUJO13","");					// [String] その他支払・控除額13
			defaultParam.addProperty("GOUKEI13","");						// [String] 合計13
			defaultParam.addProperty("BIKOU13","");							// [String] 備考13
			defaultParam.addProperty("TAISHONENGETU14","");					// [String] 対象年月14
			defaultParam.addProperty("NUMBER14","");						// [String] No14
			defaultParam.addProperty("SHIMEI14","");						// [String] 氏名14
			defaultParam.addProperty("CHINTAIRYOU14","");					// [String] 賃貸料14
			defaultParam.addProperty("KYOUEKIHI14","");						// [String] 共益費14
			defaultParam.addProperty("CHUSHARYOU14","");					// [String] 駐車料14
			defaultParam.addProperty("SONOTACHINRYOU14","");				// [String] その他月々賃料14
			defaultParam.addProperty("SHIKIKIN14","");						// [String] 敷金14
			defaultParam.addProperty("REIKIN14","");						// [String] 礼金14
			defaultParam.addProperty("SONOTAICHIJIKIN14","");				// [String] その他契約一時金14
			defaultParam.addProperty("KAIYAKUKOUSHIN14","");				// [String] 解約費・更新費14
			defaultParam.addProperty("SONOTAKOUJO14","");					// [String] その他支払・控除額14
			defaultParam.addProperty("GOUKEI14","");						// [String] 合計14
			defaultParam.addProperty("BIKOU14","");							// [String] 備考14
			defaultParam.addProperty("CHINTAIRYOUGOUKEI","");				// [String] 賃貸料合計
			defaultParam.addProperty("KYOUEKIHIGOUKEI","");					// [String] 共益費合計
			defaultParam.addProperty("CHUSHARYOUGOUKEI","");				// [String] 駐車料合計
			defaultParam.addProperty("SONOTACHINRYOUGOUKEI","");			// [String] その他月々賃料合計
			defaultParam.addProperty("SHIKIKINGOUKEI","");					// [String] 敷金合計
			defaultParam.addProperty("REIKINGOUKEI","");					// [String] 礼金合計
			defaultParam.addProperty("SONOTAICHIJIKINGOUKEI","");			// [String] その他契約一時金合計
			defaultParam.addProperty("KAIYAKUKOUSHINGOUKEI","");			// [String] 解約費・更新費合計
			defaultParam.addProperty("SONOTAKOUJOGOUKEI","");				// [String] その他支払・控除額合計
			defaultParam.addProperty("GOUKEIBIKOU","");						// [String] 合計備考
			defaultParam.addProperty("KOUJOGAKUNAIYOU1","");				// [String] 控除額内容1
			defaultParam.addProperty("KOUJOGAKU1","");						// [String] 控除額1
			defaultParam.addProperty("KOUJOGAKUBIKOU","");					// [String] 控除額備考
			defaultParam.addProperty("KOUJOGAKUGOUKEI","");					// [String] 控除額合計
			defaultParam.addProperty("BUKKENGOUKEI","");					// [String] 物件合計
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

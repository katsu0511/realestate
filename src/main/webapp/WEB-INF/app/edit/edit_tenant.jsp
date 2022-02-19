<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>借り主情報編集</h2>
		<form name="TNNT" action="${pageContext.request.contextPath}/edit/tenant" method="POST">
		
			<input type="hidden" name="room_bfo_change" value="${tenant[4]}">
			<input type="hidden" id="context_path" value="${pageContext.request.contextPath}">
		
			<div class="tr">
				<label for="tnnt_id" class="th">借り主ID:</label>
				<div class="td">
					<input type="text" id="tnnt_id" class="back-gray" name="tnnt_id" value="${tenant[0]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="rp_prsn" class="th">担当者:</label>
				<div class="td">
					<select id="rp_prsn" class="short" name="rp_prsn">
						<option value="">選択してください</option>
						<c:forEach var="rppn" items="${rppns}">
							<option value="${rppn.rppsid}" <c:if test="${rppn.rppsid == tenant[2]}"> selected </c:if>>
								${rppn.rppsnm}
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="tr">
				<label for="cont_rsd" class="th">契約中の物件:</label>
				<div class="td">
					<select id="cont_rsd" class="short" name="cont_rsd">
						<option value="">未契約</option>
						<c:forEach var="rsdc" items="${rsdcs}">
							<option value="${rsdc.rsdcid}" <c:if test="${rsdc.rsdcid == tenant[3]}"> selected </c:if>>
								${rsdc.rsdcnm}
							</option>
						</c:forEach>
					</select>
					<input type="hidden" id="current_rsdc" value="${tenant[3]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="cont_room" class="th">契約中の部屋:</label>
				<div class="td">
					<select id="cont_room" class="short" name="cont_room">
						<option value="">未契約</option>
						<c:forEach var="room" items="${rooms}">
							<option value="${room.roomid}" <c:if test="${room.roomid == tenant[4]}"> selected </c:if>>
								${room.roomnu}号室
							</option>
						</c:forEach>
					</select>
					<input type="hidden" id="current_room" value="${tenant[4]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="cont_date" class="th">契約日:</label>
				<div class="td">
					<input type="date" id="cont_date" name="cont_date" value="${tenant[5]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="tnnt_name" class="th">借り主名:<span>*</span></label>
				<div class="td">
					<input type="text" id="tnnt_name" class="check_target" name="tnnt_name" value="${tenant[6]}" required>
					<p id="tnnt_name_error1" class="error">借り主名を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="tnnt_kana" class="th">借り主名フリガナ:<span>*</span></label>
				<div class="td">
					<input type="text" id="tnnt_kana" class="check_target" name="tnnt_kana" value="${tenant[7]}" pattern="^([ァ-ンヴー|　| ]{1,})+$" required>
					<p>※カタカナ以外はエラーになります</p>
					<p id="tnnt_kana_error1" class="error">借り主名フリガナを入力してください</p>
					<p id="tnnt_kana_error2" class="error">カタカナで入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="zip1" class="th">郵便番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="zip1" class="zip check_target" name="zip1" value="${tenant[8].substring(0,3)}" maxlength="3" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{3}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
					-
					<input type="text" id="zip2" class="zip check_target" name="zip2" value="${tenant[8].substring(4,8)}" maxlength="4" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{4}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
					<p>(例：123-4567)</p>
					<p id="zip1_error1" class="error">郵便番号を入力してください</p>
					<p id="zip1_error2" class="error">郵便番号の桁数が間違っています</p>
					<p id="zip2_error1" class="error">郵便番号を入力してください</p>
					<p id="zip2_error2" class="error">郵便番号の桁数が間違っています</p>
				</div>
			</div>

			<div class="tr">
				<label for="address1" class="th">都道府県:<span>*</span></label>
				<div class="td">
					<select id="address1" class="check_target" name="address1" required>
						<optgroup label="北海道・東北">
							<option value="北海道" <c:if test="${tenant[9] == '北海道'}"> selected </c:if>>北海道</option>
							<option value="青森県" <c:if test="${tenant[9] == '青森県'}"> selected </c:if>>青森県</option>
							<option value="岩手県" <c:if test="${tenant[9] == '岩手県'}"> selected </c:if>>岩手県</option>
							<option value="秋田県" <c:if test="${tenant[9] == '秋田県'}"> selected </c:if>>秋田県</option>
							<option value="宮城県" <c:if test="${tenant[9] == '宮城県'}"> selected </c:if>>宮城県</option>
							<option value="山形県" <c:if test="${tenant[9] == '山形県'}"> selected </c:if>>山形県</option>
							<option value="福島県" <c:if test="${tenant[9] == '福島県'}"> selected </c:if>>福島県</option>
						</optgroup>
						<optgroup label="北信越">
							<option value="新潟県" <c:if test="${tenant[9] == '新潟県'}"> selected </c:if>>新潟県</option>
							<option value="長野県" <c:if test="${tenant[9] == '長野県'}"> selected </c:if>>長野県</option>
							<option value="富山県" <c:if test="${tenant[9] == '富山県'}"> selected </c:if>>富山県</option>
							<option value="石川県" <c:if test="${tenant[9] == '石川県'}"> selected </c:if>>石川県</option>
							<option value="福井県" <c:if test="${tenant[9] == '福井県'}"> selected </c:if>>福井県</option>
						</optgroup>
						<optgroup label="関東">
							<option value="茨城県" <c:if test="${tenant[9] == '茨城県'}"> selected </c:if>>茨城県</option>
							<option value="栃木県" <c:if test="${tenant[9] == '栃木県'}"> selected </c:if>>栃木県</option>
							<option value="群馬県" <c:if test="${tenant[9] == '群馬県'}"> selected </c:if>>群馬県</option>
							<option value="千葉県" <c:if test="${tenant[9] == '千葉県'}"> selected </c:if>>千葉県</option>
							<option value="埼玉県" <c:if test="${tenant[9] == '埼玉県'}"> selected </c:if>>埼玉県</option>
							<option value="東京都" <c:if test="${tenant[9] == '東京都'}"> selected </c:if>>東京都</option>
							<option value="神奈川県" <c:if test="${tenant[9] == '神奈川県'}"> selected </c:if>>神奈川県</option>
						</optgroup>
						<optgroup label="中部">
							<option value="山梨県" <c:if test="${tenant[9] == '山梨県'}"> selected </c:if>>山梨県</option>
							<option value="静岡県" <c:if test="${tenant[9] == '静岡県'}"> selected </c:if>>静岡県</option>
							<option value="岐阜県" <c:if test="${tenant[9] == '岐阜県'}"> selected </c:if>>岐阜県</option>
							<option value="愛知県" <c:if test="${tenant[9] == '愛知県'}"> selected </c:if>>愛知県</option>
							<option value="三重県" <c:if test="${tenant[9] == '三重県'}"> selected </c:if>>三重県</option>
						</optgroup>
						<optgroup label="関西">
							<option value="滋賀県" <c:if test="${tenant[9] == '滋賀県'}"> selected </c:if>>滋賀県</option>
							<option value="奈良県" <c:if test="${tenant[9] == '奈良県'}"> selected </c:if>>奈良県</option>
							<option value="和歌山県" <c:if test="${tenant[9] == '和歌山県'}"> selected </c:if>>和歌山県</option>
							<option value="京都府" <c:if test="${tenant[9] == '京都府'}"> selected </c:if>>京都府</option>
							<option value="大阪府" <c:if test="${tenant[9] == '大阪府'}"> selected </c:if>>大阪府</option>
							<option value="兵庫県" <c:if test="${tenant[9] == '兵庫県'}"> selected </c:if>>兵庫県</option>
						</optgroup>
						<optgroup label="中国">
							<option value="鳥取県" <c:if test="${tenant[9] == '鳥取県'}"> selected </c:if>>鳥取県</option>
							<option value="岡山県" <c:if test="${tenant[9] == '岡山県'}"> selected </c:if>>岡山県</option>
							<option value="島根県" <c:if test="${tenant[9] == '島根県'}"> selected </c:if>>島根県</option>
							<option value="広島県" <c:if test="${tenant[9] == '広島県'}"> selected </c:if>>広島県</option>
							<option value="山口県" <c:if test="${tenant[9] == '山口県'}"> selected </c:if>>山口県</option>
						</optgroup>
						<optgroup label="四国">
							<option value="香川県" <c:if test="${tenant[9] == '香川県'}"> selected </c:if>>香川県</option>
							<option value="徳島県" <c:if test="${tenant[9] == '徳島県'}"> selected </c:if>>徳島県</option>
							<option value="愛媛県" <c:if test="${tenant[9] == '愛媛県'}"> selected </c:if>>愛媛県</option>
							<option value="高知県" <c:if test="${tenant[9] == '高知県'}"> selected </c:if>>高知県</option>
						</optgroup>
						<optgroup label="九州">
							<option value="福岡県" <c:if test="${tenant[9] == '福岡県'}"> selected </c:if>>福岡県</option>
							<option value="佐賀県" <c:if test="${tenant[9] == '佐賀県'}"> selected </c:if>>佐賀県</option>
							<option value="長崎県" <c:if test="${tenant[9] == '長崎県'}"> selected </c:if>>長崎県</option>
							<option value="大分県" <c:if test="${tenant[9] == '大分県'}"> selected </c:if>>大分県</option>
							<option value="熊本県" <c:if test="${tenant[9] == '熊本県'}"> selected </c:if>>熊本県</option>
							<option value="宮崎県" <c:if test="${tenant[9] == '宮崎県'}"> selected </c:if>>宮崎県</option>
							<option value="鹿児島県" <c:if test="${tenant[9] == '鹿児島県'}"> selected </c:if>>鹿児島県</option>
							<option value="沖縄県" <c:if test="${tenant[9] == '沖縄県'}"> selected </c:if>>沖縄県</option>
						</optgroup>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="address2" class="th">市区町村:<span>*</span></label>
				<div class="td">
					<input type="text" id="address2" class="check_target" name="address2" value="${tenant[10]}" required>
					<p id="address2_error1" class="error">市区町村を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address3" class="th">番地1:<span>*</span></label>
				<div class="td">
					<input type="text" id="address3" class="check_target" name="address3" value="${tenant[11]}" required>
					<p>(例：番地、丁目)</p>
					<p id="address3_error1" class="error">番地1を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address4" class="th">番地2（建物名）:</label>
				<div class="td">
					<input type="text" id="address4" name="address4" value="${tenant[12]}">
				</div>
			</div>

			<div class="tr">
				<label for="number1" class="th">電話番号1:<span>*</span></label>
				<div class="td">
					<input type="text" id="number1" class="short check_target" name="number1" value="${tenant[13]}" maxlength="13" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$" required>
					<p>(例：03-1234-5678)</p>
					<p id="number1_error1" class="error">電話番号1を入力してください</p>
					<p id="number1_error2" class="error">電話番号1の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="number2" class="th">電話番号2:</label>
				<div class="td">
					<input type="text" id="number2" class="short check_target" name="number2" value="${tenant[14]}" maxlength="13" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$">
					<p>(例：03-1234-5678)</p>
					<p id="number2_error2" class="error">電話番号2の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="email" class="th">メールアドレス:</label>
				<div class="td">
					<input type="text" id="email" class="check_target" name="email" value="${tenant[15]}" oninput="value = value.replace(/[^a-zA-Z0-9\.@_-]+/i,'');" pattern="^[0-9a-zA-Z]+[\w\.-]+@[\w\.-]+\.\w{2,}$">
			    	<p id="email_error2" class="error">正しいメールアドレスの形式で入力して下さい</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="birthday" class="th">生年月日:</label>
				<div class="td">
					<input type="date" id="birthday" name="birthday" value="${tenant[16]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="bank" class="th">銀行名:</label>
				<div class="td">
					<input type="text" id="bank" class="short" name="bank" value="${tenant[17]}">
					銀行
				</div>
			</div>
			
			<div class="tr">
				<label for="bank_branch" class="th">銀行支店名:</label>
				<div class="td">
					<input type="text" id="bank_branch" class="short" name="bank_branch" value="${tenant[18]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="account_type" class="th">口座区分:</label>
				<div class="td">
					<select id="account_type" class="short" name="account_type">
						<option value="">選択してください</option>
						<option value="普通" <c:if test="${tenant[19] == '普通'}"> selected </c:if>>普通</option>
						<option value="当座" <c:if test="${tenant[19] == '当座'}"> selected </c:if>>当座</option>
						<option value="総合" <c:if test="${tenant[19] == '総合'}"> selected </c:if>>総合</option>
					</select>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_number" class="th">口座番号:</label>
				<div class="td">
					<input type="text" id="account_number" class="short" name="account_number" value="${tenant[20]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{7}">
					<p id="account_number_error2" class="error">口座番号の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_name" class="th">口座名義:</label>
				<div class="td">
					<input type="text" id="account_name" class="short" name="account_name" value="${tenant[21]}">
				</div>
			</div>
			
			<div class="tr">
				<div class="ts" style="width: 100%;">勤務先情報</div>
			</div>

			<div class="tr">
				<label for="wkplc_name" class="th">勤務先名:</label>
				<div class="td">
					<input type="text" id="wkplc_name" name="wkplc_name" value="${tenant[22]}">
				</div>
			</div>

			<div class="tr">
				<label for="wkplc_kana" class="th">勤務先名フリガナ:</label>
				<div class="td">
					<input type="text" id="wkplc_kana" class="check_target" name="wkplc_kana" value="${tenant[23]}" pattern="^([ァ-ンヴー|　| ]{1,})+$">
					<p>※カタカナ以外はエラーになります</p>
					<p id="wkplc_kana_error2" class="error">カタカナで入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="wkplc_zip1" class="th">勤務先郵便番号:</label>
				<div class="td">
					<input type="text" id="wkplc_zip1" class="zip check_target" name="wkplc_zip1" maxlength="3" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{3}" onkeyup="AjaxZip3.zip2addr('wkplc_zip1', 'wkplc_zip2', 'wkplc_address1', 'wkplc_address2');"
						<c:if test="${tenant[24] != ''}">
							value="${tenant[24].substring(0,3)}"
						</c:if>>
					-
					<input type="text" id="wkplc_zip2" class="zip check_target" name="wkplc_zip2" maxlength="4" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{4}" onkeyup="AjaxZip3.zip2addr('wkplc_zip1', 'wkplc_zip2', 'wkplc_address1', 'wkplc_address2');"
						<c:if test="${tenant[24] != ''}">
							value="${tenant[24].substring(4,8)}"
						</c:if>>
					<p>(例：123-4567)</p>
					<p id="wkplc_zip1_error2" class="error">勤務先郵便番号の桁数が間違っています</p>
					<p id="wkplc_zip2_error2" class="error">勤務先郵便番号の桁数が間違っています</p>
				</div>
			</div>

			<div class="tr">
				<label for="wkplc_address1" class="th">勤務先都道府県:</label>
				<div class="td">
					<select id="wkplc_address1" class="check_target" name="wkplc_address1">
						<option value="">選択してください</option>
						<optgroup label="北海道・東北">
							<option value="北海道" <c:if test="${tenant[25] == '北海道'}"> selected </c:if>>北海道</option>
							<option value="青森県" <c:if test="${tenant[25] == '青森県'}"> selected </c:if>>青森県</option>
							<option value="岩手県" <c:if test="${tenant[25] == '岩手県'}"> selected </c:if>>岩手県</option>
							<option value="秋田県" <c:if test="${tenant[25] == '秋田県'}"> selected </c:if>>秋田県</option>
							<option value="宮城県" <c:if test="${tenant[25] == '宮城県'}"> selected </c:if>>宮城県</option>
							<option value="山形県" <c:if test="${tenant[25] == '山形県'}"> selected </c:if>>山形県</option>
							<option value="福島県" <c:if test="${tenant[25] == '福島県'}"> selected </c:if>>福島県</option>
						</optgroup>
						<optgroup label="北信越">
							<option value="新潟県" <c:if test="${tenant[25] == '新潟県'}"> selected </c:if>>新潟県</option>
							<option value="長野県" <c:if test="${tenant[25] == '長野県'}"> selected </c:if>>長野県</option>
							<option value="富山県" <c:if test="${tenant[25] == '富山県'}"> selected </c:if>>富山県</option>
							<option value="石川県" <c:if test="${tenant[25] == '石川県'}"> selected </c:if>>石川県</option>
							<option value="福井県" <c:if test="${tenant[25] == '福井県'}"> selected </c:if>>福井県</option>
						</optgroup>
						<optgroup label="関東">
							<option value="茨城県" <c:if test="${tenant[25] == '茨城県'}"> selected </c:if>>茨城県</option>
							<option value="栃木県" <c:if test="${tenant[25] == '栃木県'}"> selected </c:if>>栃木県</option>
							<option value="群馬県" <c:if test="${tenant[25] == '群馬県'}"> selected </c:if>>群馬県</option>
							<option value="千葉県" <c:if test="${tenant[25] == '千葉県'}"> selected </c:if>>千葉県</option>
							<option value="埼玉県" <c:if test="${tenant[25] == '埼玉県'}"> selected </c:if>>埼玉県</option>
							<option value="東京都" <c:if test="${tenant[25] == '東京都'}"> selected </c:if>>東京都</option>
							<option value="神奈川県" <c:if test="${tenant[25] == '神奈川県'}"> selected </c:if>>神奈川県</option>
						</optgroup>
						<optgroup label="中部">
							<option value="山梨県" <c:if test="${tenant[25] == '山梨県'}"> selected </c:if>>山梨県</option>
							<option value="静岡県" <c:if test="${tenant[25] == '静岡県'}"> selected </c:if>>静岡県</option>
							<option value="岐阜県" <c:if test="${tenant[25] == '岐阜県'}"> selected </c:if>>岐阜県</option>
							<option value="愛知県" <c:if test="${tenant[25] == '愛知県'}"> selected </c:if>>愛知県</option>
							<option value="三重県" <c:if test="${tenant[25] == '三重県'}"> selected </c:if>>三重県</option>
						</optgroup>
						<optgroup label="関西">
							<option value="滋賀県" <c:if test="${tenant[25] == '滋賀県'}"> selected </c:if>>滋賀県</option>
							<option value="奈良県" <c:if test="${tenant[25] == '奈良県'}"> selected </c:if>>奈良県</option>
							<option value="和歌山県" <c:if test="${tenant[25] == '和歌山県'}"> selected </c:if>>和歌山県</option>
							<option value="京都府" <c:if test="${tenant[25] == '京都府'}"> selected </c:if>>京都府</option>
							<option value="大阪府" <c:if test="${tenant[25] == '大阪府'}"> selected </c:if>>大阪府</option>
							<option value="兵庫県" <c:if test="${tenant[25] == '兵庫県'}"> selected </c:if>>兵庫県</option>
						</optgroup>
						<optgroup label="中国">
							<option value="鳥取県" <c:if test="${tenant[25] == '鳥取県'}"> selected </c:if>>鳥取県</option>
							<option value="岡山県" <c:if test="${tenant[25] == '岡山県'}"> selected </c:if>>岡山県</option>
							<option value="島根県" <c:if test="${tenant[25] == '島根県'}"> selected </c:if>>島根県</option>
							<option value="広島県" <c:if test="${tenant[25] == '広島県'}"> selected </c:if>>広島県</option>
							<option value="山口県" <c:if test="${tenant[25] == '山口県'}"> selected </c:if>>山口県</option>
						</optgroup>
						<optgroup label="四国">
							<option value="香川県" <c:if test="${tenant[25] == '香川県'}"> selected </c:if>>香川県</option>
							<option value="徳島県" <c:if test="${tenant[25] == '徳島県'}"> selected </c:if>>徳島県</option>
							<option value="愛媛県" <c:if test="${tenant[25] == '愛媛県'}"> selected </c:if>>愛媛県</option>
							<option value="高知県" <c:if test="${tenant[25] == '高知県'}"> selected </c:if>>高知県</option>
						</optgroup>
						<optgroup label="九州">
							<option value="福岡県" <c:if test="${tenant[25] == '福岡県'}"> selected </c:if>>福岡県</option>
							<option value="佐賀県" <c:if test="${tenant[25] == '佐賀県'}"> selected </c:if>>佐賀県</option>
							<option value="長崎県" <c:if test="${tenant[25] == '長崎県'}"> selected </c:if>>長崎県</option>
							<option value="大分県" <c:if test="${tenant[25] == '大分県'}"> selected </c:if>>大分県</option>
							<option value="熊本県" <c:if test="${tenant[25] == '熊本県'}"> selected </c:if>>熊本県</option>
							<option value="宮崎県" <c:if test="${tenant[25] == '宮崎県'}"> selected </c:if>>宮崎県</option>
							<option value="鹿児島県" <c:if test="${tenant[25] == '鹿児島県'}"> selected </c:if>>鹿児島県</option>
							<option value="沖縄県" <c:if test="${tenant[25] == '沖縄県'}"> selected </c:if>>沖縄県</option>
						</optgroup>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="wkplc_address2" class="th">勤務先市区町村:</label>
				<div class="td">
					<input type="text" id="wkplc_address2" name="wkplc_address2" value="${tenant[26]}">
				</div>
			</div>

			<div class="tr">
				<label for="wkplc_address3" class="th">勤務先番地1:</label>
				<div class="td">
					<input type="text" id="wkplc_address3" name="wkplc_address3" value="${tenant[27]}">
					<p>(例：番地、丁目)</p>
				</div>
			</div>

			<div class="tr">
				<label for="wkplc_address4" class="th">勤務先番地2（建物名）:</label>
				<div class="td">
					<input type="text" id="wkplc_address4" name="wkplc_address4" value="${tenant[28]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="actv_flg" class="th">有効フラグ:</label>
				<div class="td">
					<select id="actv_flg" name="actv_flg">
						<option value="true" <c:if test="${tenant[29] == 't'}"> selected </c:if>>有効</option>
						<option value="false" <c:if test="${tenant[29] == 'f'}"> selected </c:if>>無効</option>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${tenant[30]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="保存">
				<a href="${pageContext.request.contextPath}/show/tenant?id=${tenant[0]}" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const zip1 = $('#zip1');
	const zip2 = $('#zip2');
	const wkplcZip1 = $('#wkplc_zip1');
	const wkplcZip2 = $('#wkplc_zip2');
	const submit = $('#submit');
	zip1.keyup(function(){
		if(zip1.val().length == 3) {
			zip2.focus();
	    }
	});
	wkplcZip1.keyup(function(){
		if(wkplcZip1.val().length == 3) {
			wkplcZip2.focus();
	    }
	});
	submit.click(function() {
		const elmList = document.TNNT.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			var elmErr2 = document.getElementById(elm.id + "_error2");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elmErr2) {
				elmErr2.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
				has_error = true;
			}
			if (elm.validity.patternMismatch) {
				elmErr2.style.display = "block";
				has_error = true;
			}
		}
		if (!has_error) {
			if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
	
	const contRsd = $('#cont_rsd');
	const contRoom = $('#cont_room');
	const currentRsdc = $('#current_rsdc');
	const currentRoom = $('#current_room');
	contRsd.change(function() {
		contRoom.empty();
		contRoom.append("<option value=\"\">未契約</option>");
		contRoom.val('');
		if (contRsd.val() != '') {
			var req = new XMLHttpRequest();
			const contextPath = $('#context_path');
			var url = contextPath.val() + '/cite/room';
			req.open('POST',url);
			req.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
			req.send('id=' + contRsd.val() + '&current_rsdc=' + currentRsdc.val() + '&current_room=' + currentRoom.val());
			req.onreadystatechange = function() {
				if (req.readyState === 4 && req.status === 200) {
					let info = (req.responseText)?JSON.parse(req.responseText):null;
					if (info) {
						if (info.roomnu != '[]') {
							ary = info.roomnu.split(',');
							$.each(ary, function(index, value){
								if (ary.length == 1) {
									var value = value.slice(1).slice(0, -1);
								} else if (ary.length > 1) {
									if (index == 0) {
										var value = value.slice(1);
									} else if (index == ary.length - 1) {
										var value = value.slice(0, -1);
									}	
								}
								var res1 = $.trim(value);
								var res2 = res1.split(' ');
								contRoom.append("<option value=\"" + res2[0] + "\">" + res2[1] +"号室</option>");
							});	
						}
					}
				}
			}
		}
	});
	
	$.fn.autoKana('#tnnt_name', '#tnnt_kana', {katakana:true});
	$.fn.autoKana('#wkplc_name', '#wkplc_kana', {katakana:true});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

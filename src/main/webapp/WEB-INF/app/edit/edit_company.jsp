<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>自社情報登録</h2>
		<form name="CMPN" action="${pageContext.request.contextPath}/edit/company" method="POST">
		
			<div class="tr">
				<label for="cmpn_id" class="th">自社ID:</label>
				<div class="td">
					<input type="text" id="cmpn_id" class="back-gray" name="cmpn_id" value="${cmpn[0]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="cmpn_name" class="th">自社名:<span>*</span></label>
				<div class="td">
					<input type="text" id="cmpn_name" class="check_target" name="cmpn_name" value="${cmpn[1]}" required>
					<p id="cmpn_name_error1" class="error">自社名を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="cmpn_kana" class="th">自社名フリガナ:<span>*</span></label>
				<div class="td">
					<input type="text" id="cmpn_kana" class="check_target" name="cmpn_kana" value="${cmpn[2]}" pattern="^([ァ-ンヴー|　| ]{1,})+$" required>
					<p>※カタカナ以外はエラーになります</p>
					<p id="cmpn_kana_error1" class="error">自社名フリガナを入力してください</p>
					<p id="cmpn_kana_error2" class="error">カタカナで入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="rpst_name" class="th">代表者名:<span>*</span></label>
				<div class="td">
					<input type="text" id="rpst_name" class="check_target" name="rpst_name" value="${cmpn[3]}" required>
					<p id="rpst_name_error1" class="error">代表者名を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="brnc_name" class="th">本支店名:<span>*</span></label>
				<div class="td">
					<input type="text" id="brnc_name" class="check_target" name="brnc_name" value="${cmpn[4]}" required>
					<p id="brnc_name_error1" class="error">本支店名を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="user_name" class="th">ユーザー名:</label>
				<div class="td">
					<input type="text" id="user_name" class="back-gray" name="user_name" value="${cmpn[5]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="password" class="th">パスワード:<span>*</span></label>
				<div class="td">
					<input type="password" id="password" class="check_target" name="password" value="${cmpn[6]}" maxlength="32" pattern="(?=.*\d)(?=.*[a-z]).{8,32}" oninput="value = value.replace(/[^0-9A-Za-z.?/_-]+/i,'');" autocomplete="off" required>
					<p>※半角英数字8文字以上</p>
					<p id="password_error1" class="error">パスワードを入力してください</p>
					<p id="password_error2" class="error">正しいパスワードの形式で入力して下さい</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="password2" class="th">パスワード確認:<span>*</span></label>
				<div class="td">
					<input type="password" id="password2" class="check_target" name="password2" value="${cmpn[6]}" maxlength="32" pattern="(?=.*\d)(?=.*[a-z]).{8,32}" oninput="value = value.replace(/[^0-9A-Za-z.?/_-]+/i,'');" autocomplete="off" required>
					<p id="password2_error1" class="error">パスワードを入力してください</p>
					<p id="password2_error3" class="error">同じパスワードを入力して下さい</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="zip1" class="th">郵便番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="zip1" class="zip check_target" name="zip1" value="${cmpn[7].substring(0,3)}" maxlength="3" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{3}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
					-
					<input type="text" id="zip2" class="zip check_target" name="zip2" value="${cmpn[7].substring(4,8)}" maxlength="4" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{4}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
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
							<option value="北海道" <c:if test="${cmpn[8] == '北海道'}"> selected </c:if>>北海道</option>
							<option value="青森県" <c:if test="${cmpn[8] == '青森県'}"> selected </c:if>>青森県</option>
							<option value="岩手県" <c:if test="${cmpn[8] == '岩手県'}"> selected </c:if>>岩手県</option>
							<option value="秋田県" <c:if test="${cmpn[8] == '秋田県'}"> selected </c:if>>秋田県</option>
							<option value="宮城県" <c:if test="${cmpn[8] == '宮城県'}"> selected </c:if>>宮城県</option>
							<option value="山形県" <c:if test="${cmpn[8] == '山形県'}"> selected </c:if>>山形県</option>
							<option value="福島県" <c:if test="${cmpn[8] == '福島県'}"> selected </c:if>>福島県</option>
						</optgroup>
						<optgroup label="北信越">
							<option value="新潟県" <c:if test="${cmpn[8] == '新潟県'}"> selected </c:if>>新潟県</option>
							<option value="長野県" <c:if test="${cmpn[8] == '長野県'}"> selected </c:if>>長野県</option>
							<option value="富山県" <c:if test="${cmpn[8] == '富山県'}"> selected </c:if>>富山県</option>
							<option value="石川県" <c:if test="${cmpn[8] == '石川県'}"> selected </c:if>>石川県</option>
							<option value="福井県" <c:if test="${cmpn[8] == '福井県'}"> selected </c:if>>福井県</option>
						</optgroup>
						<optgroup label="関東">
							<option value="茨城県" <c:if test="${cmpn[8] == '茨城県'}"> selected </c:if>>茨城県</option>
							<option value="栃木県" <c:if test="${cmpn[8] == '栃木県'}"> selected </c:if>>栃木県</option>
							<option value="群馬県" <c:if test="${cmpn[8] == '群馬県'}"> selected </c:if>>群馬県</option>
							<option value="千葉県" <c:if test="${cmpn[8] == '千葉県'}"> selected </c:if>>千葉県</option>
							<option value="埼玉県" <c:if test="${cmpn[8] == '埼玉県'}"> selected </c:if>>埼玉県</option>
							<option value="東京都" <c:if test="${cmpn[8] == '東京都'}"> selected </c:if>>東京都</option>
							<option value="神奈川県" <c:if test="${cmpn[8] == '神奈川県'}"> selected </c:if>>神奈川県</option>
						</optgroup>
						<optgroup label="中部">
							<option value="山梨県" <c:if test="${cmpn[8] == '山梨県'}"> selected </c:if>>山梨県</option>
							<option value="静岡県" <c:if test="${cmpn[8] == '静岡県'}"> selected </c:if>>静岡県</option>
							<option value="岐阜県" <c:if test="${cmpn[8] == '岐阜県'}"> selected </c:if>>岐阜県</option>
							<option value="愛知県" <c:if test="${cmpn[8] == '愛知県'}"> selected </c:if>>愛知県</option>
							<option value="三重県" <c:if test="${cmpn[8] == '三重県'}"> selected </c:if>>三重県</option>
						</optgroup>
						<optgroup label="関西">
							<option value="滋賀県" <c:if test="${cmpn[8] == '滋賀県'}"> selected </c:if>>滋賀県</option>
							<option value="奈良県" <c:if test="${cmpn[8] == '奈良県'}"> selected </c:if>>奈良県</option>
							<option value="和歌山県" <c:if test="${cmpn[8] == '和歌山県'}"> selected </c:if>>和歌山県</option>
							<option value="京都府" <c:if test="${cmpn[8] == '京都府'}"> selected </c:if>>京都府</option>
							<option value="大阪府" <c:if test="${cmpn[8] == '大阪府'}"> selected </c:if>>大阪府</option>
							<option value="兵庫県" <c:if test="${cmpn[8] == '兵庫県'}"> selected </c:if>>兵庫県</option>
						</optgroup>
						<optgroup label="中国">
							<option value="鳥取県" <c:if test="${cmpn[8] == '鳥取県'}"> selected </c:if>>鳥取県</option>
							<option value="岡山県" <c:if test="${cmpn[8] == '岡山県'}"> selected </c:if>>岡山県</option>
							<option value="島根県" <c:if test="${cmpn[8] == '島根県'}"> selected </c:if>>島根県</option>
							<option value="広島県" <c:if test="${cmpn[8] == '広島県'}"> selected </c:if>>広島県</option>
							<option value="山口県" <c:if test="${cmpn[8] == '山口県'}"> selected </c:if>>山口県</option>
						</optgroup>
						<optgroup label="四国">
							<option value="香川県" <c:if test="${cmpn[8] == '香川県'}"> selected </c:if>>香川県</option>
							<option value="徳島県" <c:if test="${cmpn[8] == '徳島県'}"> selected </c:if>>徳島県</option>
							<option value="愛媛県" <c:if test="${cmpn[8] == '愛媛県'}"> selected </c:if>>愛媛県</option>
							<option value="高知県" <c:if test="${cmpn[8] == '高知県'}"> selected </c:if>>高知県</option>
						</optgroup>
						<optgroup label="九州">
							<option value="福岡県" <c:if test="${cmpn[8] == '福岡県'}"> selected </c:if>>福岡県</option>
							<option value="佐賀県" <c:if test="${cmpn[8] == '佐賀県'}"> selected </c:if>>佐賀県</option>
							<option value="長崎県" <c:if test="${cmpn[8] == '長崎県'}"> selected </c:if>>長崎県</option>
							<option value="大分県" <c:if test="${cmpn[8] == '大分県'}"> selected </c:if>>大分県</option>
							<option value="熊本県" <c:if test="${cmpn[8] == '熊本県'}"> selected </c:if>>熊本県</option>
							<option value="宮崎県" <c:if test="${cmpn[8] == '宮崎県'}"> selected </c:if>>宮崎県</option>
							<option value="鹿児島県" <c:if test="${cmpn[8] == '鹿児島県'}"> selected </c:if>>鹿児島県</option>
							<option value="沖縄県" <c:if test="${cmpn[8] == '沖縄県'}"> selected </c:if>>沖縄県</option>
						</optgroup>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="address2" class="th">市区町村:<span>*</span></label>
				<div class="td">
					<input type="text" id="address2" class="check_target" name="address2" value="${cmpn[9]}" required>
					<p id="address2_error1" class="error">市区町村を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address3" class="th">番地1:<span>*</span></label>
				<div class="td">
					<input type="text" id="address3" class="check_target" name="address3" value="${cmpn[10]}" required>
					<p>(例：番地、丁目)</p>
					<p id="address3_error1" class="error">番地1を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address4" class="th">番地2（建物名）:</label>
				<div class="td">
					<input type="text" id="address4" name="address4" value="${cmpn[11]}">
				</div>
			</div>

			<div class="tr">
				<label for="number" class="th">電話番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="number" class="short check_target" name="number" value="${cmpn[12]}" maxlength="13" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$" required>
					<p>(例：03-1234-5678)</p>
					<p id="number_error1" class="error">電話番号を入力してください</p>
					<p id="number_error2" class="error">電話番号の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="fax" class="th">FAX:</label>
				<div class="td">
					<input type="text" id="fax" class="short check_target" name="fax" value="${cmpn[13]}" maxlength="12" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$">
					<p>(例：03-1234-5678)</p>
					<p id="fax_error2" class="error">FAXの桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="bank" class="th">銀行名:</label>
				<div class="td">
					<input type="text" id="bank" class="short" name="bank" value="${cmpn[14]}">
					銀行
				</div>
			</div>
          
          <div class="tr">
				<label for="bank_branch" class="th">銀行支店名:</label>
				<div class="td">
					<input type="text" id="bank_branch" class="short" name="bank_branch" value="${cmpn[15]}">
				</div>
			</div>
			
			<div class="tr">
				<label for="account_type" class="th">口座区分:</label>
				<div class="td">
					<select id="account_type" class="short" name="account_type">
						<option value="">選択してください</option>
						<option value="普通" <c:if test="${cmpn[16] == '普通'}"> selected </c:if>>普通</option>
						<option value="当座" <c:if test="${cmpn[16] == '当座'}"> selected </c:if>>当座</option>
						<option value="総合" <c:if test="${cmpn[16] == '総合'}"> selected </c:if>>総合</option>
					</select>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_number" class="th">口座番号:</label>
				<div class="td">
					<input type="text" id="account_number" class="short" name="account_number" value="${cmpn[17]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{7}">
					<p id="account_number_error2" class="error">口座番号の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_name" class="th">口座名義:</label>
				<div class="td">
					<input type="text" id="account_name" class="short" name="account_name" value="${cmpn[18]}">
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${cmpn[19]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="保存">
				<a href="${pageContext.request.contextPath}/show/company?id=${cmpn[0]}" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const zip1 = $('#zip1');
	const zip2 = $('#zip2');
	const submit = $('#submit');
	zip1.keyup(function(){
		if(zip1.val().length == 3) {
			zip2.focus();
	    }
	});
	submit.click(function() {
		const elmList = document.CMPN.getElementsByClassName("check_target");
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
			if ($('#password').val() != $('#password2').val()) {
				$('#password2_error3').css('display','block');
			} else if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
	
	$.fn.autoKana('#cmpn_name', '#cmpn_kana', {katakana:true});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

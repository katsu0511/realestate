<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>

<main>
	<div class="container">
		<h2>大家情報登録</h2>
		<form name="LAND" action="${pageContext.request.contextPath}/register/landlord" method="POST">
		
			<div class="tr">
				<label for="land_id" class="th">大家ID:</label>
				<div class="td">
					<input type="text" id="land_id" class="back-gray" name="land_id" value="${landid}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="land_name" class="th">大家名:<span>*</span></label>
				<div class="td">
					<input type="text" id="land_name" class="check_target" name="land_name" required>
					<p id="land_name_error1" class="error">大家名を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="land_kana" class="th">大家名フリガナ:<span>*</span></label>
				<div class="td">
					<input type="text" id="land_kana" class="check_target" name="land_kana" pattern="^([ァ-ンヴー|　| ]{1,})+$" required>
					<p>※カタカナ以外はエラーになります</p>
					<p id="land_kana_error1" class="error">大家名フリガナを入力してください</p>
					<p id="land_kana_error2" class="error">カタカナで入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="zip1" class="th">郵便番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="zip1" class="zip check_target" name="zip1" maxlength="3" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{3}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
					-
					<input type="text" id="zip2" class="zip check_target" name="zip2" maxlength="4" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{4}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
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
						<option value="">選択してください</option>
						<optgroup label="北海道・東北">
							<option value="北海道">北海道</option>
							<option value="青森県">青森県</option>
							<option value="岩手県">岩手県</option>
							<option value="秋田県">秋田県</option>
							<option value="宮城県">宮城県</option>
							<option value="山形県">山形県</option>
							<option value="福島県">福島県</option>
						</optgroup>
						<optgroup label="北信越">
							<option value="新潟県">新潟県</option>
							<option value="長野県">長野県</option>
							<option value="富山県">富山県</option>
							<option value="石川県">石川県</option>
							<option value="福井県">福井県</option>
						</optgroup>
						<optgroup label="関東">
							<option value="茨城県">茨城県</option>
							<option value="栃木県">栃木県</option>
							<option value="群馬県">群馬県</option>
							<option value="千葉県">千葉県</option>
							<option value="埼玉県">埼玉県</option>
							<option value="東京都">東京都</option>
							<option value="神奈川県">神奈川県</option>
						</optgroup>
						<optgroup label="中部">
							<option value="山梨県">山梨県</option>
							<option value="静岡県">静岡県</option>
							<option value="岐阜県">岐阜県</option>
							<option value="愛知県">愛知県</option>
							<option value="三重県">三重県</option>
						</optgroup>
						<optgroup label="関西">
							<option value="滋賀県">滋賀県</option>
							<option value="奈良県">奈良県</option>
							<option value="和歌山県">和歌山県</option>
							<option value="京都府">京都府</option>
							<option value="大阪府">大阪府</option>
							<option value="兵庫県">兵庫県</option>
						</optgroup>
						<optgroup label="中国">
							<option value="鳥取県">鳥取県</option>
							<option value="岡山県">岡山県</option>
							<option value="島根県">島根県</option>
							<option value="広島県">広島県</option>
							<option value="山口県">山口県</option>
						</optgroup>
						<optgroup label="四国">
							<option value="香川県">香川県</option>
							<option value="徳島県">徳島県</option>
							<option value="愛媛県">愛媛県</option>
							<option value="高知県">高知県</option>
						</optgroup>
						<optgroup label="九州">
							<option value="福岡県">福岡県</option>
							<option value="佐賀県">佐賀県</option>
							<option value="長崎県">長崎県</option>
							<option value="大分県">大分県</option>
							<option value="熊本県">熊本県</option>
							<option value="宮崎県">宮崎県</option>
							<option value="鹿児島県">鹿児島県</option>
							<option value="沖縄県">沖縄県</option>
						</optgroup>
					</select>
					<p id="address1_error1" class="error">都道府県を選択してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address2" class="th">市区町村:<span>*</span></label>
				<div class="td">
					<input type="text" id="address2" class="check_target" name="address2" required>
					<p id="address2_error1" class="error">市区町村を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address3" class="th">番地1:<span>*</span></label>
				<div class="td">
					<input type="text" id="address3" class="check_target" name="address3" required>
					<p>(例：番地、丁目)</p>
					<p id="address3_error1" class="error">番地1を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address4" class="th">番地2（建物名）:</label>
				<div class="td">
					<input type="text" id="address4" name="address4">
				</div>
			</div>

			<div class="tr">
				<label for="number1" class="th">電話番号1:<span>*</span></label>
				<div class="td">
					<input type="text" id="number1" class="short check_target" name="number1" maxlength="13" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$" required>
					<p>(例：03-1234-5678)</p>
					<p id="number1_error1" class="error">電話番号1を入力してください</p>
					<p id="number1_error2" class="error">電話番号1の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="number2" class="th">電話番号2:</label>
				<div class="td">
					<input type="text" id="number2" class="short check_target" name="number2" maxlength="13" oninput="value = value.replace(/[^0-9-]+/i,'');" pattern="^0\d{1,3}-\d{2,4}-\d{3,4}$">
					<p>(例：03-1234-5678)</p>
					<p id="number2_error2" class="error">電話番号2の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="email" class="th">メールアドレス:<span>*</span></label>
				<div class="td">
					<input type="text" id="email" class="check_target" name="email" oninput="value = value.replace(/[^a-zA-Z0-9\.@_-]+/i,'');" pattern="^[0-9a-zA-Z]+[\w\.-]+@[\w\.-]+\.\w{2,}$" required>
			    	<p id="email_error1" class="error">メールアドレスを入力してください</p>
			    	<p id="email_error2" class="error">正しいメールアドレスの形式で入力して下さい</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="bank" class="th">銀行名:</label>
				<div class="td">
					<input type="text" id="bank" class="short" name="bank">
					銀行
				</div>
			</div>
			
			<div class="tr">
				<label for="bank_branch" class="th">銀行支店名:</label>
				<div class="td">
					<input type="text" id="bank_branch" class="short" name="bank_branch">
					<p>(例：新宿支店)</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_type" class="th">口座区分:</label>
				<div class="td">
					<select id="account_type" class="short" name="account_type">
						<option value="">選択してください</option>
						<option value="普通">普通</option>
						<option value="当座">当座</option>
						<option value="総合">総合</option>
					</select>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_number" class="th">口座番号:</label>
				<div class="td">
					<input type="text" id="account_number" class="short" name="account_number" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{7}">
					<p id="account_number_error2" class="error">口座番号の桁数が間違っています</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="account_name" class="th">口座名義:</label>
				<div class="td">
					<input type="text" id="account_name" class="short" name="account_name">
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment"></textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="登録">
				<a href="${pageContext.request.contextPath}/index/landlord" class="button">戻る</a>
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
		const elmList = document.LAND.getElementsByClassName("check_target");
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
	
	$.fn.autoKana('#land_name', '#land_kana', {katakana:true});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>物件情報編集</h2>
		<form name="RSDC" action="${pageContext.request.contextPath}/edit/residence" method="POST">
		
			<c:if test="${errorMessage != null && errorMessage != ''}">
				<div class="error-message" style="margin-bottom:20px;">
					${errorMessage}
				</div>
			</c:if>
		
			<div class="tr">
				<label for="rsdc_id" class="th">物件ID:</label>
				<div class="td">
					<input type="text" id="rsdc_id" class="back-gray" name="rsdc_id" value="${residence[0]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="land_nm" class="th">大家:</label>
				<div class="td">
					<input type="text" id="land_nm" class="back-gray" name="land_nm" value="${landnm}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="rsd_name" class="th">物件名:<span>*</span></label>
				<div class="td">
					<input type="text" id="rsd_name" class="check_target" name="rsd_name" value="${residence[3]}" required>
					<p id="rsd_name_error1" class="error">物件名を入力してください</p>
					<c:if test="${errorMessage == '物件名と物件名フリガナがすでに登録されています' || errorMessage == '物件名がすでに登録されています'}">
						<p style="color:red;">この物件名はすでに登録されています</p>
					</c:if>
				</div>
			</div>

			<div class="tr">
				<label for="rsd_kana" class="th">物件名フリガナ:<span>*</span></label>
				<div class="td">
					<input type="text" id="rsd_kana" class="check_target" name="rsd_kana" value="${residence[4]}" pattern="^([ァ-ンヴー|　| ]{1,})+$" required>
					<p>※カタカナ以外はエラーになります</p>
					<p id="rsd_kana_error1" class="error">物件名フリガナを入力してください</p>
					<p id="rsd_kana_error2" class="error">カタカナで入力してください</p>
					<c:if test="${errorMessage == '物件名と物件名フリガナがすでに登録されています' || errorMessage == '物件名フリガナがすでに登録されています'}">
						<p style="color:red;">この物件名フリガナはすでに登録されています</p>
					</c:if>
				</div>
			</div>
			
			<div class="tr">
				<label for="zip1" class="th">郵便番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="zip1" class="zip check_target" name="zip1" value="${residence[5].substring(0,3)}" maxlength="3" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{3}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
					-
					<input type="text" id="zip2" class="zip check_target" name="zip2" value="${residence[5].substring(4,8)}" maxlength="4" oninput="value = value.replace(/[^0-9]+/i,'');" pattern="[0-9]{4}" onkeyup="AjaxZip3.zip2addr('zip1', 'zip2', 'address1', 'address2');" required>
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
							<option value="北海道" <c:if test="${residence[6] == '北海道'}"> selected </c:if>>北海道</option>
							<option value="青森県" <c:if test="${residence[6] == '青森県'}"> selected </c:if>>青森県</option>
							<option value="岩手県" <c:if test="${residence[6] == '岩手県'}"> selected </c:if>>岩手県</option>
							<option value="秋田県" <c:if test="${residence[6] == '秋田県'}"> selected </c:if>>秋田県</option>
							<option value="宮城県" <c:if test="${residence[6] == '宮城県'}"> selected </c:if>>宮城県</option>
							<option value="山形県" <c:if test="${residence[6] == '山形県'}"> selected </c:if>>山形県</option>
							<option value="福島県" <c:if test="${residence[6] == '福島県'}"> selected </c:if>>福島県</option>
						</optgroup>
						<optgroup label="北信越">
							<option value="新潟県" <c:if test="${residence[6] == '新潟県'}"> selected </c:if>>新潟県</option>
							<option value="長野県" <c:if test="${residence[6] == '長野県'}"> selected </c:if>>長野県</option>
							<option value="富山県" <c:if test="${residence[6] == '富山県'}"> selected </c:if>>富山県</option>
							<option value="石川県" <c:if test="${residence[6] == '石川県'}"> selected </c:if>>石川県</option>
							<option value="福井県" <c:if test="${residence[6] == '福井県'}"> selected </c:if>>福井県</option>
						</optgroup>
						<optgroup label="関東">
							<option value="茨城県" <c:if test="${residence[6] == '茨城県'}"> selected </c:if>>茨城県</option>
							<option value="栃木県" <c:if test="${residence[6] == '栃木県'}"> selected </c:if>>栃木県</option>
							<option value="群馬県" <c:if test="${residence[6] == '群馬県'}"> selected </c:if>>群馬県</option>
							<option value="千葉県" <c:if test="${residence[6] == '千葉県'}"> selected </c:if>>千葉県</option>
							<option value="埼玉県" <c:if test="${residence[6] == '埼玉県'}"> selected </c:if>>埼玉県</option>
							<option value="東京都" <c:if test="${residence[6] == '東京都'}"> selected </c:if>>東京都</option>
							<option value="神奈川県" <c:if test="${residence[6] == '神奈川県'}"> selected </c:if>>神奈川県</option>
						</optgroup>
						<optgroup label="中部">
							<option value="山梨県" <c:if test="${residence[6] == '山梨県'}"> selected </c:if>>山梨県</option>
							<option value="静岡県" <c:if test="${residence[6] == '静岡県'}"> selected </c:if>>静岡県</option>
							<option value="岐阜県" <c:if test="${residence[6] == '岐阜県'}"> selected </c:if>>岐阜県</option>
							<option value="愛知県" <c:if test="${residence[6] == '愛知県'}"> selected </c:if>>愛知県</option>
							<option value="三重県" <c:if test="${residence[6] == '三重県'}"> selected </c:if>>三重県</option>
						</optgroup>
						<optgroup label="関西">
							<option value="滋賀県" <c:if test="${residence[6] == '滋賀県'}"> selected </c:if>>滋賀県</option>
							<option value="奈良県" <c:if test="${residence[6] == '奈良県'}"> selected </c:if>>奈良県</option>
							<option value="和歌山県" <c:if test="${residence[6] == '和歌山県'}"> selected </c:if>>和歌山県</option>
							<option value="京都府" <c:if test="${residence[6] == '京都府'}"> selected </c:if>>京都府</option>
							<option value="大阪府" <c:if test="${residence[6] == '大阪府'}"> selected </c:if>>大阪府</option>
							<option value="兵庫県" <c:if test="${residence[6] == '兵庫県'}"> selected </c:if>>兵庫県</option>
						</optgroup>
						<optgroup label="中国">
							<option value="鳥取県" <c:if test="${residence[6] == '鳥取県'}"> selected </c:if>>鳥取県</option>
							<option value="岡山県" <c:if test="${residence[6] == '岡山県'}"> selected </c:if>>岡山県</option>
							<option value="島根県" <c:if test="${residence[6] == '島根県'}"> selected </c:if>>島根県</option>
							<option value="広島県" <c:if test="${residence[6] == '広島県'}"> selected </c:if>>広島県</option>
							<option value="山口県" <c:if test="${residence[6] == '山口県'}"> selected </c:if>>山口県</option>
						</optgroup>
						<optgroup label="四国">
							<option value="香川県" <c:if test="${residence[6] == '香川県'}"> selected </c:if>>香川県</option>
							<option value="徳島県" <c:if test="${residence[6] == '徳島県'}"> selected </c:if>>徳島県</option>
							<option value="愛媛県" <c:if test="${residence[6] == '愛媛県'}"> selected </c:if>>愛媛県</option>
							<option value="高知県" <c:if test="${residence[6] == '高知県'}"> selected </c:if>>高知県</option>
						</optgroup>
						<optgroup label="九州">
							<option value="福岡県" <c:if test="${residence[6] == '福岡県'}"> selected </c:if>>福岡県</option>
							<option value="佐賀県" <c:if test="${residence[6] == '佐賀県'}"> selected </c:if>>佐賀県</option>
							<option value="長崎県" <c:if test="${residence[6] == '長崎県'}"> selected </c:if>>長崎県</option>
							<option value="大分県" <c:if test="${residence[6] == '大分県'}"> selected </c:if>>大分県</option>
							<option value="熊本県" <c:if test="${residence[6] == '熊本県'}"> selected </c:if>>熊本県</option>
							<option value="宮崎県" <c:if test="${residence[6] == '宮崎県'}"> selected </c:if>>宮崎県</option>
							<option value="鹿児島県" <c:if test="${residence[6] == '鹿児島県'}"> selected </c:if>>鹿児島県</option>
							<option value="沖縄県" <c:if test="${residence[6] == '沖縄県'}"> selected </c:if>>沖縄県</option>
						</optgroup>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="address2" class="th">市区町村:<span>*</span></label>
				<div class="td">
					<input type="text" id="address2" class="check_target" name="address2" value="${residence[7]}" required>
					<p id="address2_error1" class="error">市区町村を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="address3" class="th">番地1:<span>*</span></label>
				<div class="td">
					<input type="text" id="address3" class="check_target" name="address3" value="${residence[8]}" required>
					<p>(例：番地、丁目)</p>
					<p id="address3_error1" class="error">番地1を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="rent" class="th">家賃:</label>
				<div class="td">
					<input type="text" id="rent" class="short" name="rent" value="${residence[9]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="mng_fee" class="th">管理費:</label>
				<div class="td">
					<input type="text" id="mng_fee" class="short" name="mng_fee" value="${residence[10]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="deposit" class="th">敷金:</label>
				<div class="td">
					<input type="text" id="deposit" class="short" name="deposit" value="${residence[11]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="honorarium" class="th">礼金:</label>
				<div class="td">
					<input type="text" id="honorarium" class="short" name="honorarium" value="${residence[12]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="brokerage" class="th">仲介料:</label>
				<div class="td">
					<input type="text" id="brokerage" class="short" name="brokerage" value="${residence[13]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="key_fee" class="th">鍵代金:</label>
				<div class="td">
					<input type="text" id="key_fee" class="short" name="key_fee" value="${residence[14]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<c:if test="${!empty usedadits}">
				<c:forEach var="adit" items="${usedadits}">
					<div class="tr">
						<label for="${adit.itemid}" class="th">${adit.itemnm}:</label>
						<div class="td">
							<div class="td_next">
								<input type="text" id="${adit.itemid}" name="${adit.itemid}" value="${adit.adebvl}"
									<c:if test="${empty adit.spclng}">class="short"</c:if>
									<c:if test="${!empty adit.spclng}">class="short check_target" pattern="[0-9]{${adit.spclng}}"</c:if>
									<c:if test="${!empty adit.maxlng}">maxlength="${adit.maxlng}"</c:if>
									oninput="value = value.replace(/[^0-9]+/i,'');"
								>
							</div>
							<c:if test="${!empty adit.commnt}"><p>${adit.commnt}</p></c:if>
							<c:if test="${!empty adit.spclng}"><p id="${adit.itemid}_error2" class="error">${adit.itemnm}の桁数が間違っています</p></c:if>
						</div>
					</div>
				</c:forEach>
			</c:if>
			
			<c:if test="${!empty unused_adits}">
				<c:forEach var="adit" items="${unused_adits}">
					<div id="${adit.aditid}" class="tr" style="display:none;">
						<label for="${adit.itemid}" class="th">${adit.itemnm}:</label>
						<div class="td">
							<div class="td_next">
								<input type="text" id="${adit.itemid}" name="${adit.itemid}"
									<c:if test="${empty adit.spclng}">class="short"</c:if>
									<c:if test="${!empty adit.spclng}">class="short check_target" pattern="[0-9]{${adit.spclng}}"</c:if>
									<c:if test="${!empty adit.maxlng}">maxlength="${adit.maxlng}"</c:if>
									oninput="value = value.replace(/[^0-9]+/i,'');"
								>
								<input type="button" class="delete_input" value="削除">
							</div>
							<c:if test="${!empty adit.commnt}"><p>${adit.commnt}</p></c:if>
							<c:if test="${!empty adit.spclng}"><p id="${adit.itemid}_error2" class="error">${adit.itemnm}の桁数が間違っています</p></c:if>
						</div>
					</div>
				</c:forEach>
			</c:if>
			
			<c:if test="${!empty unused_adits}">
				<div class="new_inputs short">
					<c:forEach var="adit" items="${unused_adits}">
						<div class="new_input">
							<p>${adit.itemnm}</p>
							<input type="button" class="add_input" value="追加">
							<input type="hidden" class="aditid" value="${adit.aditid}">
						</div>
					</c:forEach>
				</div>
			</c:if>
			
			<div class="tr">
				<label for="actv_flg" class="th">有効フラグ:</label>
				<div class="td">
					<select id="actv_flg" name="actv_flg">
						<option value="true" <c:if test="${residence[15] == 't'}"> selected </c:if>>有効</option>
						<option value="false" <c:if test="${residence[15] == 'f'}"> selected </c:if>>無効</option>
					</select>
					<input type="hidden" name="actv_before_change" value="${residence[15]}">
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${residence[16]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="保存">
				<a href="${pageContext.request.contextPath}/show/residence?id=${residence[0]}" class="button">戻る</a>
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
		const elmList = document.RSDC.getElementsByClassName("check_target");
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
	
	$('.add_input').click(function() {
		var aditid = $(this).parent().find('.aditid').val();
		$('#' + aditid).css('display','flex');
	});
	$('.delete_input').click(function() {
		$(this).parent().find('input:text').val('');
		$(this).parent().parent().parent().css('display','none');
	});
	
	$.fn.autoKana('#rsdc_name', '#rsdc_kana', {katakana:true});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

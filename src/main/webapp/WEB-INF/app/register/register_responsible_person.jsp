<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>担当者情報登録</h2>
		<form name="RPPS" action="${pageContext.request.contextPath}/register/responsible_person" method="POST">
		
			<div class="tr">
				<label for="rppn_id" class="th">担当者ID:</label>
				<div class="td">
					<input type="text" id="rppn_id" class="back-gray" name="rppn_id" value="${rppsid}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="rppn_name" class="th">担当者名:<span>*</span></label>
				<div class="td">
					<input type="text" id="rppn_name" class="check_target" name="rppn_name" required>
					<p id="rppn_name_error1" class="error">担当者名を入力してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="rppn_kana" class="th">担当者フリガナ:<span>*</span></label>
				<div class="td">
					<input type="text" id="rppn_kana" class="check_target" name="rppn_kana" pattern="^([ァ-ンヴー|　| ]{1,})+$" required>
					<p>※カタカナ以外はエラーになります</p>
					<p id="rppn_kana_error1" class="error">担当者フリガナを入力してください</p>
					<p id="rppn_kana_error2" class="error">カタカナで入力してください</p>
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
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment"></textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="登録">
				<a href="${pageContext.request.contextPath}/index/responsible_person" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	submit.click(function() {
		const elmList = document.RPPS.getElementsByClassName("check_target");
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
	
	$.fn.autoKana('#rppn_name', '#rppn_kana', {katakana:true});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

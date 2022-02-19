<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>追加項目登録</h2>
		<form name="ADIT" action="${pageContext.request.contextPath}/edit/additional_item" method="POST">
		
			<c:if test="${errorMessage != null && errorMessage != ''}">
				<div class="error-message" style="margin-bottom:20px;">
					${errorMessage}
				</div>
			</c:if>
		
			<div class="tr">
				<label for="adit_id" class="th">追加項目ID:</label>
				<div class="td">
					<input type="text" id="adit_id" class="back-gray" name="adit_id" value="${adit[0]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="item_name" class="th">追加項目表示名:<span>*</span></label>
				<div class="td">
					<input type="text" id="item_name" class="check_target" name="item_name" value="${adit[2]}" required>
					<p id="item_name_error1" class="error">追加項目表示名を入力してください</p>
					<c:if test="${errorMessage == '追加項目表示名と追加項目ID名がすでに登録されています' || errorMessage == '追加項目表示名がすでに登録されています'}">
						<p style="color:red;">この追加項目表示名はすでに登録されています</p>
					</c:if>
				</div>
			</div>
			
			<div class="tr">
				<label for="item_id" class="th">追加項目ID名:<span>*</span></label>
				<div class="td">
					<input type="text" id="item_id" class="check_target" name="item_id" value="${adit[3]}" oninput="value = value.replace(/[^0-9A-Za-z_-]+/i,'');" required>
					<p>※半角で入力してください。</p>
					<p id="item_id_error1" class="error">追加項目ID名を入力してください</p>
					<c:if test="${errorMessage == '追加項目表示名と追加項目ID名がすでに登録されています' || errorMessage == '追加項目ID名がすでに登録されています'}">
						<p style="color:red;">この追加項目ID名はすでに登録されています</p>
					</c:if>
				</div>
			</div>
			
			<div class="tr">
				<label for="max_lng" class="th">最大桁数:</label>
				<div class="td">
					<input type="text" id="max_lng" class="short" name="max_lng" value="${adit[4]}" maxlength="2" oninput="value = value.replace(/[^0-9]+/i,'');">
				</div>
			</div>
			
			<div class="tr">
				<label for="spc_lng" class="th">桁数指定:</label>
				<div class="td">
					<input type="text" id="spc_lng" class="short" name="spc_lng" value="${adit[5]}" maxlength="100" oninput="value = value.replace(/[^0-9,]+/i,'');">
					<p>※複数の場合は半角コンマで区切ってください。（例）10,11</p>
					<p id="spc_lng_error3" class="error">桁数指定の値は最大桁数の値以下に設定してください</p>
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${adit[6]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="保存">
				<a href="${pageContext.request.contextPath}/show/additional_item?id=${adit[0]}" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	submit.click(function() {
		const spcLngError3 = $('#spc_lng_error3');
		spcLngError3.css('display','none');
		const elmList = document.ADIT.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
				has_error = true;
			}
		}
		if (!has_error) {
			const maxLng = parseInt($('#max_lng').val());
			const spcLng = $('#spc_lng');
			const spcLngs = spcLng.val().split(',');
			var max_validate = true;
			for (let i = 0; i < spcLngs.length; i++) {
				var spclng = parseInt(spcLngs[i]);
				if (maxLng < spclng) {
					max_validate = false;
				}
			}
			
			if (!max_validate) {
				alert('桁数指定の値は最大桁数の値以下に設定してください');
				spcLngError3.css('display','block');
				return false;
			} else if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>解約精算書</h2>
		<form name="CAST" action="${pageContext.request.contextPath}/export/cancellation_statement" target="_blank" method="POST">
		
			<div class="tr">
				<label for="tnnt_id" class="th">借り主:<span>*</span></label>
				<div class="td">
					<select id="tnnt_id" class="short check_target" name="tnnt_id" required>
						<option value="">選択してください</option>
						<c:forEach var="tenant" items="${tenants}">
							<option value="${tenant.tnntid}">${tenant.tnntnm}</option>
						</c:forEach>
					</select>
					<p id="tnnt_id_error1" class="error">借り主を選択してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="cancel_date" class="th">解約日:<span>*</span></label>
				<div class="td">
					<input type="date" id="cancel_date" class="check_target" name="cancel_date" required>
					<p id="cancel_date_error1" class="error">解約日を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="depart_date" class="th">退去日:<span>*</span></label>
				<div class="td">
					<input type="date" id="depart_date" class="check_target" name="depart_date" required>
					<p id="depart_date_error1" class="error">退去日を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="state_date" class="th">解約精算日予定日:<span>*</span></label>
				<div class="td">
					<input type="date" id="state_date" class="check_target" name="state_date" required>
					<p id="state_date_error1" class="error">解約精算日予定日を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="expire_date" class="th">精算期限日:<span>*</span></label>
				<div class="td">
					<input type="date" id="expire_date" class="check_target" name="expire_date" required>
					<p id="expire_date_error1" class="error">精算期限日を入力してください</p>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" value="解約精算書発行">
				<a href="${pageContext.request.contextPath}/index/pdf_export" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	submit.click(function() {
		const elmList = document.CAST.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
			}
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
